package me.googas.reflect.wrappers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import lombok.NonNull;
import me.googas.reflect.util.ReflectUtil;

/**
 * This class wraps a {@link Class} to use its methods checking if those can be executed and
 * returning objects without throwing errors.
 *
 * <p>The method {@link Class#forName(String)} would throw a {@link ClassNotFoundException}
 * meanwhile {@link WrappedClass#forName(String)} will just return an empty instance if that is the
 * case most of the methods declared in this class would return empty instances too
 *
 * @param <O> the type of the class object
 */
public final class WrappedClass<O> extends LangWrapper<Class<O>> {

  private WrappedClass(Class<O> clazz) {
    super(clazz);
  }

  private WrappedClass() {
    this(null);
  }

  /**
   * Return the wrapper of the {@link Class} object if {@link Class#forName(String)} matches a class
   * else it would be empty.
   *
   * @see Class#forName(String)
   * @param name the fully qualified name of the class
   * @return the wrapped {@link Class} instance
   */
  @NonNull
  public static WrappedClass<?> forName(@NonNull String name) {
    try {
      return new WrappedClass<>(Class.forName(name));
    } catch (ClassNotFoundException e) {
      return new WrappedClass<>();
    }
  }

  /**
   * Wrap a {@link Class} instance.
   *
   * @param clazz the class to wrap
   * @return the wrapper of {@link Class}
   * @param <T> the type of the class object
   */
  @NonNull
  public static <T> WrappedClass<T> of(Class<T> clazz) {
    return new WrappedClass<>(clazz);
  }

  /**
   * Get the constructor matching the given parameters.
   *
   * @param params the parameters to match the constructor with
   * @return a {@link WrappedConstructor} instance containing the constructor or empty if not found
   */
  @NonNull
  public WrappedConstructor<O> getConstructor(Class<?>... params) {
    Constructor<O> constructor = null;
    for (Constructor<?> referenceConstructor : this.reference.getConstructors()) {
      if (this.compare(referenceConstructor, params)) {
        //noinspection unchecked
        constructor = (Constructor<O>) referenceConstructor;
        break;
      }
    }
    return WrappedConstructor.of(constructor);
  }

  /**
   * Get the field matching the name.
   *
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   */
  @NonNull
  public WrappedField<?> getField(@NonNull String name) {
    Field field = null;
    try {
      field = this.reference.getField(name);
    } catch (NoSuchFieldException e) {
      // Field not found
    }
    return WrappedField.of(field);
  }

  /**
   * Get the field matching the name.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   * @param <T> the type of the object that the field contains
   */
  @NonNull
  public <T> WrappedField<T> getField(@NonNull Class<T> fieldType, @NonNull String name) {
    Field field = null;
    try {
      field = this.reference.getField(name);
    } catch (NoSuchFieldException e) {
      // Field not found
    }
    return WrappedField.of(fieldType, field);
  }

  /**
   * Get a declared field matching the name.
   *
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   */
  @NonNull
  public WrappedField<?> getDeclaredField(@NonNull String name) {
    Field field = null;
    try {
      field = this.reference.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      // Field not found
    }
    return WrappedField.of(field);
  }

  /**
   * Get a declared field matching the name.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name to match the field with
   * @return a {@link WrappedField} instance containing the field or empty if not found
   * @param <T> the type of the object that the field contains
   */
  @NonNull
  public <T> WrappedField<T> getDeclaredField(@NonNull Class<T> fieldType, @NonNull String name) {
    Field field = null;
    try {
      field = this.reference.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      // Field not found
    }
    return WrappedField.of(fieldType, field);
  }

  /**
   * Get a method matching the name and parameter types.
   *
   * @param name the name to match the method with
   * @param params the parameters to match the method with
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public WrappedMethod<?> getMethod(@NonNull String name, Class<?>... params) {
    return this.getMethod(null, name, params);
  }

  /**
   * Get a method matching the name, parameter types and return type.
   *
   * @param returnType the return type to match the method with
   * @param name the name to match the method with
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getMethod(
      Class<T> returnType, @NonNull String name, Class<?>... params) {
    Method method = null;
    for (Method referenceMethod : this.reference.getMethods()) {
      if (this.compareMethods(returnType, name, referenceMethod, params)) {
        method = referenceMethod;
        break;
      }
    }
    return WrappedMethod.of(method, returnType);
  }

  /**
   * Get a method matching the name and parameter types.
   *
   * @param name the name to match the method with
   * @param params the parameters to match the method with
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public WrappedMethod<?> getDeclaredMethod(@NonNull String name, Class<?>... params) {
    return this.getDeclaredMethod(null, name, params);
  }

  /**
   * Get a method matching the name, parameter types and return type.
   *
   * @param returnType the return type to match the method with
   * @param name the name to match the method with
   * @param params the parameters to math the method with
   * @param <T> the type of return
   * @return a {@link WrappedMethod} instance containing the method or empty if not found
   */
  @NonNull
  public <T> WrappedMethod<T> getDeclaredMethod(
      Class<T> returnType, @NonNull String name, Class<?>... params) {
    Method method = null;
    for (Method referenceMethod : this.reference.getDeclaredMethods()) {
      if (this.compareMethods(returnType, name, referenceMethod, params)) {
        method = referenceMethod;
        break;
      }
    }
    return WrappedMethod.of(method, returnType);
  }

  /**
   * Checks if a method with the given name and parameter types exists in the class.
   *
   * @param returnType the type that the method returns: null for void
   * @param name the name of the method to find
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasMethod(Class<?> returnType, @NonNull String name, Class<?>... params) {
    if (this.reference != null) {
      for (Method method : this.reference.getMethods()) {
        if (this.compareMethods(returnType, name, method, params)) return true;
      }
    }
    return false;
  }

  /**
   * Checks if a declared method with the given name and parameter types exists in the class.
   *
   * @param returnType the type that the method returns: null for void
   * @param name the name of the method to find
   * @param params the parameters of the method to find
   * @return true if the method is found false otherwise
   */
  public boolean hasDeclaredMethod(Class<?> returnType, @NonNull String name, Class<?>... params) {
    if (this.reference != null) {
      for (Method method : this.reference.getDeclaredMethods()) {
        if (this.compareMethods(returnType, name, method, params)) return true;
      }
    }
    return false;
  }

  private boolean compareMethods(
      Class<?> returnType, @NonNull String name, Method method, Class<?>[] params) {
    if (method.getName().equals(name)) {
      Class<?>[] paramTypes = method.getParameterTypes();
      Class<?> methodReturnType = method.getReturnType();
      return ReflectUtil.compareParameters(paramTypes, params)
          && (returnType == null || returnType.isAssignableFrom(method.getReturnType()));
    }
    return false;
  }

  /**
   * Checks if a field with the given name exists in the class.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name of the field to find
   * @return true if the field is found false otherwise
   */
  public boolean hasField(Class<?> fieldType, @NonNull String name) {
    if (this.reference != null) {
      for (Field field : this.reference.getFields()) {
        if (field.getName().equals(name)
            && (fieldType == null || fieldType.isAssignableFrom(field.getType()))) return true;
      }
    }
    return false;
  }

  /**
   * Checks if a declared field with the given name exists in the class.
   *
   * @param fieldType the class of the object that the field contains
   * @param name the name of the field to find
   * @return true if the field is found false otherwise
   */
  public boolean hasDeclaredField(Class<?> fieldType, @NonNull String name) {
    if (this.reference != null) {
      for (Field field : this.reference.getDeclaredFields()) {
        if (field.getName().equals(name)
            && (fieldType == null || fieldType.isAssignableFrom(field.getType()))) return true;
      }
    }
    return false;
  }

  private static List<WrappedField<?>> wrap(@NonNull Field... fields) {
    List<WrappedField<?>> wrappers = new ArrayList<>(fields.length);
    for (Field field : fields) {
      wrappers.add(WrappedField.of(field));
    }
    return wrappers;
  }

  private static List<WrappedMethod<?>> wrap(@NonNull Method... methods) {
    List<WrappedMethod<?>> wrappers = new ArrayList<>(methods.length);
    for (Method method : methods) {
      wrappers.add(WrappedMethod.of(method));
    }
    return wrappers;
  }

  /**
   * Checks if a constructor with the given parameter types exists in the class.
   *
   * @param params the parameters of the constructor to find
   * @return true if the constructor is found false otherwise
   */
  public boolean hasConstructor(Class<?>... params) {
    if (this.reference != null) {
      for (Constructor<?> constructor : this.reference.getConstructors()) {
        if (this.compare(constructor, params)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Compares a constructor to an array of classes. This checks if the constructor has the same
   * parameter types as the array
   *
   * @param constructor the constructor to be compared
   * @param params the array to compare
   * @return true if the parameter types of the constructor matches the classes of the array
   */
  private boolean compare(Constructor<?> constructor, Class<?>[] params) {
    return ReflectUtil.compareParameters(constructor.getParameterTypes(), params);
  }

  /**
   * Get a list of {@link Method} of the class.
   *
   * @return the list of methods
   */
  @NonNull
  public List<WrappedMethod<?>> getMethods() {
    return reference == null ? new ArrayList<>() : WrappedClass.wrap(this.reference.getMethods());
  }

  /**
   * Get a list of {@link Field} of the class.
   *
   * @return the list of fields
   */
  @NonNull
  public List<WrappedField<?>> getFields() {
    return this.reference == null
        ? new ArrayList<>()
        : WrappedClass.wrap(this.reference.getFields());
  }

  /**
   * Get a list of {@link Field} that are declared in the class.
   *
   * @return the list of fields
   */
  @NonNull
  public List<WrappedField<?>> getDeclaredFields() {
    return this.reference == null
        ? new ArrayList<>()
        : WrappedClass.wrap(this.reference.getDeclaredFields());
  }

  /**
   * Get the wrapped class.
   *
   * @return the wrapped class
   */
  @NonNull
  public Class<O> getClazz() {
    return this.reference;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    WrappedClass<?> that = (WrappedClass<?>) o;
    return Objects.equals(reference, that.reference);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reference);
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", WrappedClass.class.getSimpleName() + "[", "]")
        .add("clazz=" + reference)
        .toString();
  }
}
