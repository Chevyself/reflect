package me.googas.reflect;

import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Implementation for {@link SetterWrapper}. Create it using {@link Wrapper#wrap(Object)}
 *
 * @param <T> the type of the wrapped object
 */
public class AbstractWrapper<T> implements SetterWrapper<T> {

  @Getter @Setter private T handle;

  /**
   * Wrap an object.
   *
   * @param handle the object to be wrapped
   */
  public AbstractWrapper(T handle) {
    this.handle = handle;
  }

  @Override
  public @NonNull Optional<T> get() {
    throw new UnsupportedOperationException("Deprecated");
  }

  @Override
  public Wrapper<T> set(T object) {
    throw new UnsupportedOperationException("Deprecated");
  }
}
