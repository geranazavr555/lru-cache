package ru.itmo.sdcourse.cache;

import org.apache.commons.lang3.Validate;
import ru.itmo.sdcourse.cache.impl.IntrusiveLruList;
import ru.itmo.sdcourse.cache.impl.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LruCache<K, V> extends AbstractCache<K, V> {
    private static final int DEFAULT_CAPACITY = 32;

    private final int capacity;
    private final IntrusiveLruList<Pair<K, V>> values;
    private final Map<K, IntrusiveLruList<Pair<K, V>>.Node> keyToNode;

    public LruCache() {
        this(DEFAULT_CAPACITY);
    }

    public LruCache(int capacity) {
        Validate.isTrue(capacity > 0, "Capacity should be greater than 0, but %d provided", capacity);

        this.capacity = capacity;
        this.values = new IntrusiveLruList<>();
        this.keyToNode = new HashMap<>(capacity);
    }

    @Override
    protected int getSize() {
        return values.getSize();
    }

    @Override
    protected int getCapacity() {
        return capacity;
    }

    @Override
    protected Optional<V> doGet(K key) {
        var node = keyToNode.get(key);
        if (node == null)
            return Optional.empty();

        node = values.touch(node);
        return Optional.of(node.getValue().value());
    }

    @Override
    protected boolean doPut(K key, V value) {
        var node = keyToNode.get(key);
        if (node == null)
            node = values.addFront(values.newNode(Pair.of(key, value)));

        keyToNode.put(key, values.touch(node));

        if (values.getSize() > capacity) {
            IntrusiveLruList<Pair<K, V>>.Node removedNode = values.removeBack();
            keyToNode.remove(removedNode.getValue().key());

            return true;
        }

        return false;
    }
}
