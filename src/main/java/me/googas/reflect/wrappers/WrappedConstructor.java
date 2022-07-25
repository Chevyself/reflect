package me.googas.reflect.wrappers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import lombok.NonNull;

/** This class wraps a {@link Constructor} to prepare it and create instances of a class. */
public final class WrappedConstructor<T> extends LangWrapper<Constructor<T>> {

  private WrappedConstructor(Constructor<T> reference) {
    super(reference);
  }

  private WrappedConstructor() {
    this(null);
  }

  /**
   * Wrap a {@link Constructor} instance.
   *
   * @param constructor the constructor to wrap
   * @return the wrapper of constructor
   * @param <T> the type that the constructor initializes
   */
  @NonNull
  public static <T> WrappedConstructor<T> of(Constructor<T> constructor) {
    if (constructor != null) constructor.setAccessible(true);
    return new WrappedConstructor<>(constructor);
  }

  /**
   * Invoke the constructor to create a new instance of an object.
   *
   * @param args the required arguments to prepare the constructor
   * @return the object created using the constructor
   * @throws IllegalAccessException if this Constructor object is enforcing Java language access
   *     control and the underlying constructor is inaccessible.
   * @throws InstantiationException if the class that declares the underlying constructor represents
   *     an abstract class.
   * @throws InvocationTargetException if the underlying constructor throws an exception.
   */
  public T invoke(Object... args)
      throws InvocationTargetException, InstantiationException, IllegalAccessException {
    T other = null;
    if (this.reference != null) {
      other = this.reference.newInstance(args);
    }
    return other;
  }

  /**
   * Get the wrapped constructor.
   *
   * @return the wrapped constructor
   */
  public Constructor<T> getConstructor() {
    return reference;
  }
}
