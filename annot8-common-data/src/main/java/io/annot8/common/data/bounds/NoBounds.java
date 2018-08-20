package io.annot8.common.data.bounds;

import io.annot8.core.bounds.Bounds;
import io.annot8.core.data.Content;
import java.util.Optional;

/**
 * Implementation of Bounds indicating that an annotation does not have any bounds (i.e. it is
 * metadata).
 *
 * This class is a singleton, and should be accessed via getInstance()
 */
public final class NoBounds implements Bounds {

  private static final NoBounds INSTANCE = new NoBounds();

  private NoBounds() {
    //Empty constructor
  }

  /**
   * Return the singleton instance of NoBounds
   */
  public static NoBounds getInstance() {
    return INSTANCE;
  }

  @Override
  public String toString() {
    return "NoBounds";
  }

  @Override
  public <D, C extends Content<D>, R> Optional<R> getData(C content, Class<R> requiredClass) {
    return Optional.empty();
  }

  @Override
  public <D, C extends Content<D>> boolean isValid(C content) {
    return true;
  }
}