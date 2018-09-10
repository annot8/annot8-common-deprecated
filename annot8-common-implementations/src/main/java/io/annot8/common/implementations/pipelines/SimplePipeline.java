package io.annot8.common.implementations.pipelines;

import io.annot8.core.components.Annot8Component;
import io.annot8.core.components.Processor;
import io.annot8.core.components.Resource;
import io.annot8.core.components.Source;
import io.annot8.core.components.responses.ProcessorResponse;
import io.annot8.core.components.responses.ProcessorResponse.Status;
import io.annot8.core.components.responses.SourceResponse;
import io.annot8.core.data.Item;
import io.annot8.core.data.ItemFactory;
import io.annot8.core.exceptions.Annot8Exception;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePipeline implements Pipeline {

  private static final Logger LOGGER = LoggerFactory.getLogger(SimplePipeline.class);

  private String id;

  private final ItemFactory itemFactory;
  private final ItemQueue itemQueue;
  private final Map<String, Resource> resources;
  private final List<Source> sources;
  private final List<Processor> processors;

  public SimplePipeline(ItemFactory itemFactory, ItemQueue itemQueue,
      Map<String, Resource> resources,
      List<Source> sources, List<Processor> processors) {
    this.id = UUID.randomUUID().toString();
    this.itemFactory = itemFactory;
    this.itemQueue = itemQueue;
    this.resources = resources;
    this.sources = sources;
    this.processors = processors;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void run() {
    for (final Source source : sources) {
      process(source);
    }
  }

  private void process(final Source source) {
    SourceResponse.Status status;
    do {
      final SourceResponse response = source.read(itemFactory);
      status = response.getStatus();

      processItemQueue();

    } while (status == SourceResponse.Status.OK || status == SourceResponse.Status.EMPTY);

    close();

  }

  private void processItemQueue() {

    while (itemQueue.hasItems()) {
      Item item = itemQueue.next();

      processItem(item);
    }

  }

  private void processItem(final Item item) {

    for (final Processor processor : processors) {
      try {
        final ProcessorResponse response = processor.process(item);

        final Status status = response.getStatus();
        if (status == Status.OK) {
          if (item.isDiscarded()) {
            LOGGER.warn("Item discarded, stopping processing");
            return;
          }

        } else if (status == Status.PROCESSOR_ERROR) {
          LOGGER.error("Pipeline problem, exiting");
          System.exit(1);
        } else if (status == Status.ITEM_ERROR) {
          LOGGER.error("Item problem, skipping rest of pipeline");
          return;
        }

      } catch (final Annot8Exception e) {
        LOGGER.error("Failed to process data item with processor {}",processor.getClass().getName(),e);
      }
    }
  }

  @Override
  public void close() {
    sources.forEach(Annot8Component::close);
    processors.forEach(Annot8Component::close);
    resources.values().forEach(Annot8Component::close);
  }
}