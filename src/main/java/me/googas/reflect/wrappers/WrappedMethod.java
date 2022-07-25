package me.googas.reflect.wrappers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import lombok.NonNull;
import me.googas.reflect.util.ReflectUtil;

/** This class wraps a {@link Method} to prepare. */
public final class WrappedMethod<T> extends LangWrapper<Method> {

  private final Class<T> returnType;

  /**
   * Wrap a method.
   *
   * @param reference the method to be wrapped
   * @param returnType the return type of the method
   */
  private WrappedMethod(Method reference, Class<T> returnType) {
    super(reference);
    this.returnType = returnType;
  }

  private WrappedMethod() {
    this(null, null);
  }

  /**
   * Wrap a {@link Method} instance.
   *
   * @param method the method to wrap
   * @param <T> the type that the method returns
   * @return the wrapper of the method
   */
  @NonNull
  public static <T> WrappedMethod<T> of(Method method) {
    if (method != null) method.setAccessible(true);
    return new WrappedMethod<>(method, null);
  }

  /**
   * Wrap a {@link Method} instance which returns a type.
   *
   * @param method the method to wrap
   * @param returnType the type that the method returns
   * @param <T> the type that the method returns
   * @return the wrap of the method
   */
  @NonNull
  public static <T> WrappedMethod<T> of(Method method, Class<T> returnType) {
    if (method != null) method.setAccessible(true);
    return new WrappedMethod<>(
        method,
        returnType == null
            ? null
            : returnType.isPrimitive() ? ReflectUtil.getBoxing(returnType) : returnType);
  }

  /**
   * Invoke the method and cast the object with the return type of the method.
   *
   * @param object the instance of the object to prepare the method if the method is static it may
   *     be null
   * @param params the parameters to prepare the method
   * @return the object which the method returns
   * @throws ClassCastException in case the return type does not match {@link #returnType}
   * @throws IllegalAccessException if this Method object is enforcing Java language access control
   *     and the underlying method is inaccessible.
   * @throws InvocationTargetException if the underlying method throws an exception.
   */
  public T prepare(Object object, Object... params)
      throws InvocationTargetException, IllegalAccessException {
    T obj = null;
    if (this.reference != null) {
      Object invoke = this.reference.invoke(object, params);
      if (invoke != null && returnType != null) {
        obj = returnType.cast(invoke);
      }
    }
    return obj;
  }

  /**
   * Invoke the method.
   *
   * @param object the instance of the object to prepare the method if the method is static it may
   *     be null
   * @param params the parameters to prepare the method
   * @return the object which the method returns
   * @throws IllegalAccessException if this Method object is enforcing Java language access control
   *     and the underlying method is inaccessible.
   * @throws InvocationTargetException if the underlying method throws an exception.
   */
  public Object invoke(Object object, Object... params)
      throws InvocationTargetException, IllegalAccessException {
    if (this.reference != null) {
      return this.reference.invoke(object, params);
    }
    return null;
  }

  /**
   * The type that the method returns when {@link #prepare(Object, Object...)}
   *
   * @return an {@link Optional} instance holding the return type
   */
  @NonNull
  public Optional<Class<?>> getReturnType() {
    return Optional.ofNullable(this.returnType);
  }

  /**
   * Get the wrapped method.
   *
   * @return the wrapped method instance
   */
  public Method getMethod() {
    return this.reference;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedMethod.class.getSimpleName() + "[", "]")
        .add("reference=" + reference)
        .add("returnType=" + returnType)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    WrappedMethod<?> that = (WrappedMethod<?>) o;
    return Objects.equals(returnType, that.returnType) && super.equals(o);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), returnType) + super.hashCode();
  }
}
