package me.googas.reflect.modifiers;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import me.googas.reflect.wrappers.WrappedClass;
import me.googas.reflect.wrappers.WrappedField;
import me.googas.reflect.wrappers.WrappedMethod;

/** This modifier allows to change collections values in fields. */
@SuppressWarnings("rawtypes")
public abstract class CollectionModifier implements Modifier {

  @NonNull private static final WrappedClass<Collection> CLAZZ = WrappedClass.of(Collection.class);

  @NonNull private static final WrappedClass<List> LIST_CLAZZ = WrappedClass.of(List.class);

  @NonNull
  private static final WrappedMethod<Boolean> ADD =
      CollectionModifier.CLAZZ.getMethod(boolean.class, "add", Object.class);

  @NonNull
  private static final WrappedMethod<?> ADD_INDEX =
      CollectionModifier.LIST_CLAZZ.getMethod("add", int.class, Object.class);

  @NonNull
  private static final WrappedMethod<Boolean> ADD_ALL =
      CollectionModifier.CLAZZ.getMethod(boolean.class, "addAll", Collection.class);

  /**
   * Allows to use {@link Collection#addAll(Collection)}.
   *
   * @param collection the collection to be added
   * @return the modifier to add all
   */
  @NonNull
  public static CollectionModifier addAll(@NonNull Collection<?> collection) {
    return new AddAll(collection);
  }

  /**
   * Allows to use {@link Collection#add(Object)}.
   *
   * @param obj the object to be added
   * @return the modifier to add the object
   */
  @NonNull
  public static CollectionModifier add(@NonNull Object obj) {
    return CollectionModifier.add(-1, obj);
  }

  /**
   * Allows to use {@link List#add(int, Object)}.
   *
   * @param index the index to add the object
   * @param obj the object to add
   * @return the modifier to add the object in an index
   */
  @NonNull
  public static CollectionModifier add(int index, @NonNull Object obj) {
    return new Add(index, obj);
  }

  @Override
  public boolean modify(@NonNull WrappedField<?> field, @NonNull Object reference)
      throws IllegalAccessException, InvocationTargetException {
    Object raw = field.getRaw(reference);
    if (raw instanceof Collection) {
      return this.modify(field, reference, (Collection) raw);
    }
    return false;
  }

  /**
   * Modify a field.
   *
   * @param field the field to be modified
   * @param reference the reference in which the field will be changed
   * @param collection the collection value from the field
   * @return true if the field was changed successfully
   * @throws InvocationTargetException if the modification fails
   * @throws IllegalAccessException if this Field object is enforcing Java language access control
   *     and the underlying field is either inaccessible or final.
   */
  public abstract boolean modify(
      @NonNull WrappedField<?> field, @NonNull Object reference, @NonNull Collection<?> collection)
      throws IllegalAccessException, InvocationTargetException;

  private static class Add extends CollectionModifier {

    private final int index;
    @NonNull private final Object toAdd;

    private Add(int index, @NonNull Object toAdd) {
      this.toAdd = toAdd;
      this.index = index;
    }

    @Override
    public boolean modify(
        @NonNull WrappedField<?> field,
        @NonNull Object reference,
        @NonNull Collection<?> collection)
        throws IllegalAccessException, InvocationTargetException {
      if (index > -1 && collection instanceof List) {
        CollectionModifier.ADD_INDEX.invoke(collection, index, toAdd);
        return true;
      } else {
        return CollectionModifier.ADD.prepare(collection, toAdd);
      }
    }
  }

  private static class AddAll extends CollectionModifier {

    @NonNull private final Collection<?> collection;

    private AddAll(@NonNull Collection<?> collection) {
      this.collection = collection;
    }

    @Override
    public boolean modify(
        @NonNull WrappedField<?> field,
        @NonNull Object reference,
        @NonNull Collection<?> collection)
        throws IllegalAccessException, InvocationTargetException {
      return CollectionModifier.ADD_ALL.prepare(collection, this.collection);
    }
  }
}
