/* Annot8 (annot8.io) - Licensed under Apache-2.0. */
package io.annot8.pipelines.simple;

import io.annot8.pipelines.elements.Pipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePipeBuilder extends AbstractPipeBuilder {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(SimplePipeBuilder.class);

  @Override
  public Pipe build() {
    return new SimplePipe(
        getResourcesHolder(), getProcessorHolder());
  }
}
