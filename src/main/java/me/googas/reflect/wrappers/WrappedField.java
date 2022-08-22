package me.googas.reflect.wrappers;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;
import me.googas.reflect.modifiers.Modifier;

/** This class wraps a {@link Field} to set or get the declaration. */
public final class WrappedField<O> extends LangWrapper<Field> {

  private final Class<O> fieldType;

  private WrappedField(Field reference, Class<O> fieldType) {
    super(reference);
    this.fieldType = fieldType;
  }

  WrappedField() {
    this(null, null);
  }

  /**
   * Wrap a {@link Field} instance.
   *
   * @param field the field to wrap
   * @return the wrapper of the field
   */
  @NonNull
  public static WrappedField<?> of(Field field) {
    if (field != null) field.setAccessible(true);
    return new WrappedField<>(field, null);
  }

  /**
   * Wrap a {@link Field} instance.
   *
   * @param fieldType the class of the object that the field contains
   * @param field the field to wrap
   * @param <T> the type of the object in the field
   * @return the wrapped field
   */
  @NonNull
  public static <T> WrappedField<T> of(Class<T> fieldType, Field field) {
    if (field != null) field.setAccessible(true);
    return new WrappedField<>(field, fieldType);
  }

  /**
   * Get the value that is stored in the field for the parameter object.
   *
   * @param instance the object to get the value of the field from
   * @return the object from the field
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is inaccessible.
   */
  public Object provide(@NonNull Object instance) throws IllegalAccessException {
    Object other = null;
    if (this.wrapped != null) {
      other = this.wrapped.get(instance);
    }
    return other;
  }

  /**
   * Get the value that is stored in the field for the parameter object and cast it to the field
   * type.
   *
   * @param instance the object to get the value of the field from
   * @return the object from the field
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  public O get(Object instance) throws IllegalAccessException {
    O other = null;
    if (this.wrapped != null && this.fieldType != null) {
      other = this.fieldType.cast(this.provide(instance));
    }
    return other;
  }

  /**
   * Set the value of the field in an object.
   *
   * @param object the object to set the value of the field to
   * @param value the new value to set on the field
   * @return whether the value has been set successfully
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  @NonNull
  public boolean set(@NonNull Object object, Object value) throws IllegalAccessException {
    boolean set = false;
    if (this.wrapped != null) {
      this.wrapped.set(object, value);
      set = true;
    }
    return set;
  }

  /**
   * Set the value of the field using a modifier.
   *
   * @param object the object to set the value of the field to
   * @param modifier the modifier which will change the value of the field
   * @return true if the value has been changed
   * @throws InvocationTargetException if the modification fails
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  public boolean set(@NonNull Object object, @NonNull Modifier modifier)
      throws InvocationTargetException, IllegalAccessException {
    boolean set = false;
    if (this.wrapped != null) {
      set = modifier.modify(this, object);
    }
    return set;
  }

  /**
   * Get the instance of wrapped {@link Field}.
   *
   * @return the wrapped field if present else null
   */
  public Field getField() {
    return wrapped;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedField.class.getSimpleName() + "[", "]")
        .add("field=" + wrapped)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedField<?> that = (WrappedField<?>) o;
    return Objects.equals(wrapped, that.wrapped);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wrapped);
  }
}
