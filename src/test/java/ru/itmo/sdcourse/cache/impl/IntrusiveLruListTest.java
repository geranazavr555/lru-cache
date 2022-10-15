package ru.itmo.sdcourse.cache.impl;

import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class IntrusiveLruListTest {
    @Test
    public void testNewNode() {
        IntrusiveLruList<Integer> list = new IntrusiveLruList<>();
        IntrusiveLruList<Integer>.Node node = list.newNode(1);
        assertNotNull(node.getValue());
        assertEquals(1, (int) node.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNodeFromAnotherList() {
        IntrusiveLruList<Integer> list1 = new IntrusiveLruList<>();
        IntrusiveLruList<Integer>.Node nodeList1 = list1.newNode(1);
        new IntrusiveLruList<Integer>().addFront(nodeList1);
    }

    @Test
    public void testSize() {
        IntrusiveLruList<Integer> list = new IntrusiveLruList<>();
        assertEquals(0, list.getSize());

        IntrusiveLruList<Integer>.Node node = list.newNode(5);
        assertEquals(0, list.getSize());

        list.addFront(node);
        assertEquals(1, list.getSize());

        list.removeBack();
        assertEquals(0, list.getSize());

        final int n = 1000;
        for (int i = 0; i < n; i++) {
            assertEquals(i, list.getSize());
            list.addFront(list.newNode(i));
            assertEquals(i + 1, list.getSize());
        }

        for (int i = n - 1; i >= 0; i--) {
            assertEquals(i + 1, list.getSize());
            list.removeBack();
            assertEquals(i, list.getSize());
        }
    }

    @Test
    public void simple() {
        IntrusiveLruList<String> list = new IntrusiveLruList<>();
        list.addFront(list.newNode("A"));
        list.addFront(list.newNode("B"));
        IntrusiveLruList<String>.Node c = list.addFront(list.newNode("C"));
        list.addFront(list.newNode("D"));

        assertEquals(4, list.getSize());

        IntrusiveLruList<String>.Node node = list.removeBack();
        assertEquals("A", node.getValue());

        list.touch(c);
        node = list.removeBack();
        assertEquals("B", node.getValue());
        node = list.removeBack();
        assertEquals("D", node.getValue());
        assertEquals(1, list.getSize());

        node = list.removeBack();
        assertEquals("C", node.getValue());
        assertEquals(0, list.getSize());
    }

    @Test(expected = NoSuchElementException.class)
    public void removeFromEmpty() {
        IntrusiveLruList<String> list = new IntrusiveLruList<>();
        list.removeBack();
    }

    @Test(expected = IllegalArgumentException.class)
    public void multipleAddSameNode() {
        IntrusiveLruList<Integer> list = new IntrusiveLruList<>();
        IntrusiveLruList<Integer>.Node node = list.newNode(5);
        list.addFront(node);
        list.addFront(node);
    }
}
