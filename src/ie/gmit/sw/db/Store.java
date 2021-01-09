package ie.gmit.sw.db;

import java.util.Collection;
import java.util.function.Predicate;

/**
 * Denotes an object that is responsible for managing persistable objects.
 *
 * @param <T> The type of object that is persisted by the implementing class.
 */
public interface Store<T> {

    /**
     * Called to initialise the object store.
     */
    void init();

    /**
     * Called to shut down the object store.
     */
    void shutDown();

    /**
     * Save an object to the object store.
     * @param t the object to save.
     */
    void add(T t);

    /**
     * Save a {@link Collection} of objects to the object store.
     * @param ts the objects to add.
     */
    void addAll(Collection<? extends T> ts);

    /**
     * Clear all objects from the object store.
     */
    void clear();

    /**
     * Gets a {@link Collection} of all the objects that meet the given predicate.
     * @param predicate A statement that if true, will result in an object being included in the returned collection.
     * @return A collection of objects that satisfy the predicate.
     */
    Collection<T> filter(Predicate<? super T> predicate);
}
