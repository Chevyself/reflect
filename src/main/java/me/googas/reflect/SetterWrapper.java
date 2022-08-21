package me.googas.reflect;

/**
 * Wraps an element and allows it to be set.
 *
 * @param <T> the type of the object to be wrapped
 */
public interface SetterWrapper<T> extends Wrapper<T> {

  /**
   * Set the wrapped object.
   *
   * @param object the new wrapped object
   */
  void setHandle(T object);
}
