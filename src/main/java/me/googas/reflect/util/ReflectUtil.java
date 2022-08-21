package me.googas.reflect.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

/** Static utilities for java reflection. */
public final class ReflectUtil {

  public static final Map<Class<?>, Class<?>> boxing = new HashMap<>();

  static {
    ReflectUtil.boxing.put(boolean.class, Boolean.class);
    ReflectUtil.boxing.put(byte.class, Byte.class);
    ReflectUtil.boxing.put(short.class, Short.class);
    ReflectUtil.boxing.put(char.class, Character.class);
    ReflectUtil.boxing.put(int.class, Integer.class);
    ReflectUtil.boxing.put(long.class, Long.class);
    ReflectUtil.boxing.put(float.class, Float.class);
    ReflectUtil.boxing.put(double.class, Double.class);
  }

  /**
   * Check whether an array of annotations has certain annotation.
   *
   * @param annotations the array of annotations to check if it has an annotation
   * @param clazz the class of annotation to match
   * @return true if the array has the annotation
   */
  public static boolean hasAnnotation(
      @NonNull Annotation[] annotations, @NonNull Class<? extends Annotation> clazz) {
    for (Annotation annotation : annotations) {
      if (annotation.annotationType() == clazz) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get the annotation inside an array.
   *
   * @param annotations the array of annotations to get the annotation from
   * @param clazz the class of the annotation to get
   * @param <T> the type of the annotation to get
   * @return the annotation if found
   * @throws IllegalArgumentException if the array does not contain the annotation use {@link
   *     #hasAnnotation(Method, Class)} to avoid this
   */
  @NonNull
  public static <T extends Annotation> T getAnnotation(
      @NonNull Annotation[] annotations, @NonNull Class<T> clazz) {
    for (Annotation annotation : annotations) {
      if (clazz.isAssignableFrom(annotation.annotationType())) {
        return clazz.cast(annotation);
      }
    }
    throw new IllegalArgumentException(
        Arrays.toString(annotations) + " does not contain the annotation " + clazz);
  }

  /**
   * Check whether a method has certain annotation.
   *
   * @param method the method to check if it has an annotation
   * @param clazz the class of annotation to match
   * @return true if the method has the annotation
   */
  public static boolean hasAnnotation(
      @NonNull Method method, @NonNull Class<? extends Annotation> clazz) {
    return ReflectUtil.hasAnnotation(method.getAnnotations(), clazz);
  }

  /**
   * Compare two arrays of {@link Class}. This will check that the classes are in the same indexes
   * for both arrays
   *
   * @param paramTypes the first array
   * @param params the second array
   * @return true if both arrays have the same elements
   */
  public static boolean compareParameters(Class<?>[] paramTypes, Class<?>[] params) {
    if (paramTypes == null || params == null) return true;
    if (paramTypes.length != params.length) return false;
    for (int i = 0; i < paramTypes.length; i++) {
      if (params[i].isAssignableFrom(paramTypes[i])) return false;
    }
    return true;
  }

  /**
   * Get the class that boxes a primitive class.
   *
   * <p>EJ: For the primitive class 'long' its box will be {@link Long}
   *
   * @param primitive the primitive class to box
   * @param <T> the type of the primitive
   * @return the boxing class
   */
  @NonNull
  @SuppressWarnings("unchecked")
  public static <T> Class<T> getBoxing(@NonNull Class<T> primitive) {
    return (Class<T>) ReflectUtil.boxing.get(primitive);
  }
}
