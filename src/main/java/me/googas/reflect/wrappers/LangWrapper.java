package me.googas.reflect.wrappers;

import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import me.googas.reflect.Wrapper;

/**
 * Implementation of wrapper for classes located in the Java package 'java.lang.reflect'
 *
 * @param <T> the wrapped type
 */
class LangWrapper<T> implements Wrapper<T> {

  @Getter final T handle;

  LangWrapper(T handle) {
    this.handle = handle;
  }

  @Override
  @Deprecated
  public @NonNull Optional<T> get() {
    return Optional.ofNullable(this.handle);
  }

  @Override
  @Deprecated
  public @NonNull LangWrapper<T> set(T object) {
    throw new UnsupportedOperationException("References in LangWrappers are final");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    LangWrapper<?> that = (LangWrapper<?>) o;
    return Objects.equals(handle, that.handle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(handle);
  }
}
