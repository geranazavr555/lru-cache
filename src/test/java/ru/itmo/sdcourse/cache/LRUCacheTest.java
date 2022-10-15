package ru.itmo.sdcourse.cache;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class LRUCacheTest {
    @Test
    public void simple() {
        Cache<Integer, Integer> cache = new LruCache<>();
        cache.put(1, 2);
        Optional<Integer> value = cache.get(1);
        assertTrue(value.isPresent());
        assertEquals(2, (int) value.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalCapacityZero() {
        new LruCache<>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalCapacityNegative() {
        new LruCache<>(-1);
    }

    @Test
    public void minCapacity() {
        Cache<Integer, Integer> cache = new LruCache<>(1);

        cache.put(1, 2);
        Optional<Integer> value = cache.get(1);
        assertTrue(value.isPresent());
        assertEquals(2, (int) value.get());

        cache.put(2, 3);
        value = cache.get(2);
        assertTrue(value.isPresent());
        assertEquals(3, (int) value.get());

        assertFalse(cache.get(5).isPresent());
    }

    @Test
    public void lruKickOut() {
        Cache<String, String> cache = new LruCache<>(5);

        cache.put("a", "A");
        cache.put("b", "B");
        cache.put("c", "C");
        cache.put("d", "D");
        cache.put("e", "E");

        assertFalse(cache.get("f").isPresent());

        Optional<String> value = cache.get("c");

        assertTrue(value.isPresent());
        assertEquals("C", value.get());

        cache.put("f", "F");

        assertFalse(cache.get("a").isPresent());
    }
}
