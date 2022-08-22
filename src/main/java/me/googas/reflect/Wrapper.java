package me.googas.reflect;

import java.util.Optional;
import lombok.NonNull;

/**
 * Wraps an element.
 *
 * @param <T> the type of element to be wrapped
 */
public interface Wrapper<T> {

  /**
   * Wrap an element.
   *
   * @param object the object to be wrapped
   * @return the wrapped object
   * @param <T> the type of the wrapped object
   */
  static <T> Wrapper<T> wrap(T object) {
    return new AbstractWrapper<>(object);
  }

  /**
   * Get the wrapped object.
   *
   * @deprecated use {@link #getWrapped()}
   * @return a {@link Optional} containing the nullable wrapped object
   */
  @NonNull
  default Optional<T> get() {
    throw new UnsupportedOperationException("Deprecated");
  }

  /**
   * Get the wrapped object.
   *
   * @return the wrapped objet
   */
  T getWrapped();

  /**
   * Check if {@link #getWrapped()} is not null.
   *
   * @return true if handle is not null
   */
  default boolean isPresent() {
    return this.getWrapped() != null;
  }

  /**
   * Set the wrapped object.
   *
   * @deprecated Use {@link SetterWrapper}
   * @param object the new wrapped object
   * @return this same instance
   */
  default Wrapper<T> set(T object) {
    throw new UnsupportedOperationException("Deprecated");
  }
}
