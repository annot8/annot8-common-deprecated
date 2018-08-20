package io.annot8.common.implementations.annotations;

import io.annot8.core.annotations.Group;
import io.annot8.core.references.AnnotationReference;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Abstract implementation of Group, providing correct implementations
 * of equals, hashCode and toString.
 *
 * Two groups are taken to be equal if the following properties are
 * all equal. The actual implementation of the group is seen to be
 * irrelevant and not checked.
 * <ul>
 *   <li>id</li>
 *   <li>type</li>
 *   <li>properties</li>
 *   <li>annotationReferences</li>
 * </ul>
 */
public abstract class AbstractGroup implements Group{

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null) {
      return false;
    }

    if (!(other instanceof Group)) {
      return false;
    }

    Group g = (Group) other;

    //First check "easy properties" so we can fail fast
    if(!(Objects.equals(getId(), g.getId())
        && Objects.equals(getType(), g.getType())
        && Objects.equals(getProperties(), g.getProperties())))
      return false;

    //Now check references, which is expensive
    if(!getReferences().keySet().equals(g.getReferences().keySet()))
      return false;

    for(String key : getReferences().keySet()){
      Iterator<AnnotationReference> ourIter = getReferences().getOrDefault(key, Stream.empty()).sorted().iterator();
      Iterator<AnnotationReference> otherIter = g.getReferences().getOrDefault(key, Stream.empty()).sorted().iterator();

      while(ourIter.hasNext()){
        if(!otherIter.hasNext())
          return false;

        if(!ourIter.next().equals(otherIter.next()))
          return false;
      }
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getType(), getProperties(), getReferences());
  }

  @Override
  public String toString() {
    return this.getClass().getName() + " [id=" + getId() + ", type=" + getType() + ", properties="
        + getProperties() + ", annotations=" + getAnnotations() + "]";
  }
}