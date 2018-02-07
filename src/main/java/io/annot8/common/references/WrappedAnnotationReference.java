package io.annot8.common.references;

import io.annot8.core.annotations.Annotation;
import io.annot8.core.references.AnnotationReference;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO: ...Before you use this you need to understand that annoations change, so this is not going
 * to provide you with the most upto date annoation. Nor will it work if your annotation is deleted
 * from the store.
 */
public class WrappedAnnotationReference implements AnnotationReference {

  private final Annotation annotation;

  public WrappedAnnotationReference(Annotation annotation) {
    assert annotation != null;
    this.annotation = annotation;
  }

  @Override
  public String getContent() {
    return annotation.getContentName();
  }

  @Override
  public String getAnnotationId() {
    return annotation.getId();
  }

  @Override
  public Optional<Annotation> toAnnotation() {
    return Optional.of(annotation);
  }


  @Override
  public boolean equals(Object o) {
    return sameReference(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(annotation.getContentName(), annotation.getId());
  }
}