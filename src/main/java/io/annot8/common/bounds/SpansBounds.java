package io.annot8.common.bounds;

import io.annot8.core.bounds.Bounds;
import io.annot8.core.data.Content;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of Bounds for multiple spans.
 *
 * Example usage might be the output of a document summariser, were you want to identify important
 * sentences in the document as a single annotation.
 */
public class SpansBounds implements Bounds {

  private final Collection<SpanBounds> spans;

  /**
   * New bounds from collections (no nulls allowed)
   */
  public SpansBounds(Collection<SpanBounds> spans) {
    assert spans != null;
    assert !spans.contains(null);
    this.spans = spans;
  }

  /**
   * New bounds from array (no nulls allowed)
   */
  public SpansBounds(SpanBounds... spans) {
    this(Arrays.stream(spans));
  }

  /**
   * New bounds from stream (no nulls allowed)
   */
  public SpansBounds(Stream<SpanBounds> spans) {
    this(spans.collect(Collectors.toList()));
  }

  /**
   * Get all spans
   */
  public Stream<SpanBounds> getSpans() {
    return spans.stream();
  }

  /**
   * Get the number of spans
   */
  public int getSize() {
    return spans.size();
  }

  @Override
  public String toString() {
    return this.getClass().getName() + " [size=" + getSize() + "]";
  }

  @Override
  public int hashCode() {
    return Objects.hash(spans);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SpansBounds)) {
      return false;
    }

    SpansBounds sb = (SpansBounds) o;

    return Objects.equals(spans, sb.spans);
  }

  @Override
  public <D, C extends Content<D>, R> Optional<R> getData(C content, Class<R> requiredClass) {
    if (requiredClass.equals(Spans.class)) {

      List<String> list = getSpans().map(sb -> sb.getData(content, String.class))
          .filter(Optional::isPresent)
          .map(Optional::get)
          .filter(s -> !s.isEmpty())
          .collect(Collectors.toList());

      return Optional.of((R) new Spans(list));
    }

    return Optional.empty();
  }

  @Override
  public <D, C extends Content<D>> boolean isValid(C content) {

    D data = content.getData();

    if (data.getClass().equals(String.class)) {
      return getSpans().anyMatch(sb -> sb.isValid(content));
    }

    return false;
  }

  /**
   * String spans returned from getData
   */
  public static class Spans {

    private final List<String> list;

    private Spans(List<String> spans) {
      assert spans != null;
      this.list = spans;
    }

    /**
     * Number of spans
     */
    public int getSize() {
      return list.size();
    }

    /**
     * Get the stream of span strings
     */
    public Stream<String> getSpans() {
      return list.stream();
    }
  }
}