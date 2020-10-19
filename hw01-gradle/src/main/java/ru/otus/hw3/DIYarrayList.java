package ru.otus.hw3;

import java.util.*;

public class DIYarrayList<T> implements List<T> {

    private Object[] items;
    private int size;

    public DIYarrayList(int size) {
        this.size = size;
        items = new Object[]{};
    }

    public DIYarrayList() {
        this(0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) return false;
        if (size == 0) return false;
        for (int i = 0; i < size; i++) {
            if (o.equals(items[i])) return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        Object[] buf = new Object[size];
        if (size > 0) System.arraycopy(items, 0, buf, 0, size);
        return buf;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (t == null) throw new IllegalArgumentException();
        Object[] buf = null;
        if (size > 0) {
            buf = new Object[size];
            System.arraycopy(items, 0, buf, 0, size);
        }
        size++;
        items = new Object[size];
        if (buf != null) {
            System.arraycopy(buf, 0, items, 0, size - 1);
        }
        items[size - 1] = t;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        if (index < 0 || index > size - 1) throw new IndexOutOfBoundsException();
        return (T) items[index];
    }

    @Override
    public T set(int index, T element) {
        if (element == null) throw new IllegalArgumentException();
        if (index < 0 || index > size - 1) throw new IndexOutOfBoundsException();
        T oldElement = (T) items[index];
        items[index] = element;
        return oldElement;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<>() {

            int currentPosition;
            int lastPosition = -1;

            @Override
            public boolean hasNext() {
                return currentPosition != size;
            }

            @Override
            public T next() {
                if (currentPosition > size) throw new NoSuchElementException();
                lastPosition = currentPosition;
                T t = (T) items[currentPosition];
                currentPosition++;
                return t;
            }

            @Override
            public boolean hasPrevious() {
                return currentPosition != 0;
            }

            @Override
            public T previous() {
                currentPosition--;
                lastPosition = currentPosition;
                if (currentPosition < 0) throw new NoSuchElementException();
                return (T) items[currentPosition];
            }

            @Override
            public int nextIndex() {
                return currentPosition;
            }

            @Override
            public int previousIndex() {
                return currentPosition - 1;
            }

            @Override
            public void remove() {
                if (lastPosition < 0) throw new IllegalStateException();
                int newSize;
                if ((newSize = size - 1) > lastPosition) {
                    System.arraycopy(items, lastPosition + 1, items, lastPosition, newSize - lastPosition);
                }
                items[size = newSize] = null;
                currentPosition = lastPosition;
                lastPosition = -1;
            }

            @Override
            public void set(T t) {
                if (lastPosition < 0) throw new IllegalStateException();
                DIYarrayList.this.set(lastPosition, t);
            }

            @Override
            public void add(T t) {
                DIYarrayList.this.add(t);
                currentPosition++;
                lastPosition = -1;
            }
        };
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[\n");
        for (int i = 0; i < size; i++) builder.append(" ").append(items[i].toString()).append("\n");
        builder.append("]");
        return builder.toString();
    }
}
