package ru.itmo.sdcourse.cache;

import org.apache.commons.lang3.Validate;

import java.util.Optional;

public abstract class AbstractCache<K, V> implements Cache<K, V> {
    @Override
    public final Optional<V> get(K key) {
        int size = getSize();
        int capacity = getCapacity();
        assert 0 <= size && size <= capacity;

        Optional<V> result = doGet(key);

        // Here I really want to check that result is not null, so suppress
        // IDEA's warning about comparison Optional with null.
        // noinspection OptionalAssignedToNull
        assert result != null;
        assert getCapacity() == capacity;
        assert getSize() == size;

        return result;
    }

    @Override
    public final void put(K key, V value) {
        int oldSize = getSize();
        int capacity = getCapacity();
        assert 0 <= oldSize && oldSize <= capacity;

        Validate.notNull(value, "Value should not be null");

        boolean isKeyKicked = doPut(key, value);
        if (isKeyKicked)
            assert oldSize == getSize();
        else
            assert oldSize + 1 == getSize();

        assert getCapacity() == capacity;
        assert 0 <= getSize() && getSize() <= capacity;
    }

    protected abstract int getSize();

    protected abstract int getCapacity();

    protected abstract Optional<V> doGet(K key);

    /**
     * Puts element in the cache
     * @param key key to be put
     * @param value value to be put
     * @return <code>true</code> iff least recently used element has been kicked off from the cache
     */
    protected abstract boolean doPut(K key, V value);
}
