package ru.itmo.sdcourse.cache;

import java.util.Optional;

/**
 * @param <K> Type of the keys.
 * @param <V> Type of the values.
 */
public interface Cache<K, V> {
    Optional<V> get(K key);

    void put(K key, V value);
}
