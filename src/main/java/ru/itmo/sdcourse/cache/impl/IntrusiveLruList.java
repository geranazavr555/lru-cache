package ru.itmo.sdcourse.cache.impl;

import org.apache.commons.lang3.Validate;

import java.util.NoSuchElementException;

/**
 * Implementation detail of {@link ru.itmo.sdcourse.cache.LruCache}.
 *
 * A list which allows to operate its nodes explicitly.
 *
 * @param <T> type of elements
 */
public class IntrusiveLruList<T> {
    private final Node head;
    private final Node tail;
    private int size;

    public IntrusiveLruList() {
        head = new Node(null);
        tail = new Node(null);

        head.next = tail;
        tail.prev = head;

        size = 0;
    }

    /**
     * Removes node from the list.
     *
     * @throws NullPointerException if node is null
     * @throws IllegalArgumentException if node does not belong to the list
     * @throws NoSuchElementException if performing on an empty list
     *
     * @param node -- node from the list
     * @return removed <code>node</code>.
     */
    public Node unlink(Node node) {
        checkNode(node);
        checkNotEmpty();

        size--;
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = null;
        node.prev = null;
        return node;
    }

    /**
     * Adds a new node to the list after the specified node.
     *
     * @throws IllegalArgumentException if <code>node</code> or <code>newNode</code> does not belong to the list.
     * @throws IllegalArgumentException if <code>newNode</code> is already linked.
     *
     * @param node -- node after which a <code>newNode</code> will be added
     * @param newNode -- node that will be added
     * @return added node
     */
    public Node linkNext(Node node, Node newNode) {
        checkNode(node);
        checkNode(newNode);
        checkNewNode(newNode);

        size++;
        node.next.prev = newNode;
        newNode.next = node.next;
        node.next = newNode;
        newNode.prev = node;
        return newNode;
    }

    /**
     * Adds node to the front of the list.
     *
     * @throws IllegalArgumentException if <code>node</code> or <code>newNode</code> does not belong to the list.
     * @throws IllegalArgumentException if <code>newNode</code> is already linked.
     *
     * @param node -- node to add
     * @return added node.
     */
    public Node addFront(Node node) {
        return linkNext(head, node);
    }

    /**
     * Removes the last (the least recently touched) node from the list.
     *
     * @throws NoSuchElementException if performing on an empty list
     *
     * @return the removed node.
     */
    public Node removeBack() {
        return unlink(tail.prev);
    }

    /**
     * Moves the node to the front of the list. Performing in O(1) time complexity.
     *
     * @throws NullPointerException if node is null
     * @throws IllegalArgumentException if node does not belong to the list
     * @throws NoSuchElementException if performing on an empty list
     *
     * @param node -- node to be moved
     * @return moved node.
     */
    public Node touch(Node node) {
        return addFront(unlink(node));
    }

    /**
     * @return the size of the list.
     */
    public int getSize() {
        return size;
    }

    /**
     * Generates a new node associated with the list. Does not link it into the list.
     *
     * @param value -- the value to be stored in the node.
     * @return newly generated node.
     */
    public Node newNode(T value) {
        return new Node(value);
    }

    private void checkNode(Node node) {
        Validate.notNull(node, "Node should not be null");
        Validate.isTrue(node.getList() == this,
                "IntrusiveLruList instances can operate only nodes generated by it");
    }

    private void checkNewNode(Node node) {
        Validate.isTrue(!node.isLinked(), "Node is already linked");
    }

    private void checkNotEmpty() {
        if (getSize() == 0)
            throw new NoSuchElementException("Can't perform operation on an empty list");
    }

    public class Node {
        private Node next;
        private Node prev;
        private final T value;

        private Node(T value) {
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public IntrusiveLruList<T> getList() {
            return IntrusiveLruList.this;
        }

        private boolean isLinked() {
            return next != null && prev != null;
        }
    }
}
