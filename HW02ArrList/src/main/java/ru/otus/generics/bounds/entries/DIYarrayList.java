package ru.otus.generics.bounds.entries;

import java.util.*;
import java.util.function.Consumer;

public class DIYarrayList<T> implements List <T> {

    private static final Object[] EMPTY_ELEMENTDATA = {};
    private Object[] elementData ;
    private int size;


    public DIYarrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Illegal Capacity: "+
                    initialCapacity);
        }
    }

    /**
     * ДЗ:
     * addAll(Collection<? super T> c, T... elements)
     * static <T> void copy(List<? super T> dest, List<? extends T> src)
     * static <T> void sort(List<T> list, Comparator<? super T> c)
     *
     * java.util.Collections
     * Constructs an empty list with an initial capacity of ten.
     *
     */
    public DIYarrayList() {
        this.elementData =  EMPTY_ELEMENTDATA;
        size=0;

    }

    @Override
    public boolean add(T t) {
    add(size, t);
        return true;
    }

    @Override
    public void add(int index, T element) {
    int s;
        if(elementData.length == (s = size ))
            elementData = Arrays.copyOf(elementData ,size+10); //size*2

        System.arraycopy(elementData, index,
                elementData, index + 1,
                s - index);
        elementData[index] = element;
        size = s + 1;
    }

    @Override
    public T get(int index) {

        Objects.checkIndex(index, size);
        return (T) elementData[index];
    }

//    static <T> T elementAt(Object[] es, int index) {
//        return (T) es[index];
//    }

    @Override
    public T set(int index, T element) {
        Objects.checkIndex(index, size);
        T oldValue = (T) elementData[index];
        elementData[index] = element;
        return oldValue;
    }

    @Override
    public int size() {
     return size;
    }
    @Override
    public boolean isEmpty() {
        return  size==0;
    }

    @Override
    public boolean contains(Object o) {
        throw new  UnsupportedOperationException();
    }

    public void sort(Comparator<? super T> c) {

        Arrays.sort((T[]) elementData, 0, size, c);
    }

    @Override
    public Iterator<T> iterator() {
        return new  DIYItr();
    }

    private class DIYItr implements Iterator<T> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such

        // prevent creating a synthetic constructor
        DIYItr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public T next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = DIYarrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (T) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                DIYarrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;

            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super T> action) {
            Objects.requireNonNull(action);
            final int size = DIYarrayList.this.size;
            int i = cursor;
            if (i < size) {
                final Object[] es = elementData;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size ; i++)
                    action.accept(DIYarrayList.this.get(i));
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
            }
        }
    }

    @Override
    public ListIterator<T> listIterator() {return new DYIListItr(0);    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new  UnsupportedOperationException();
    }

    private class DYIListItr extends DIYItr implements ListIterator<T> {
        DYIListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public T previous() {

            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = DIYarrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (T) elementData[lastRet = i];
        }

        public void set(T e) {
            if (lastRet < 0)
                throw new IllegalStateException();

            try {
                DIYarrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(T e) {

            try {
                int i = cursor;
                DIYarrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public Object[] toArray() {
        throw new  UnsupportedOperationException();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new  UnsupportedOperationException();
    }


    @Override
    public boolean remove(Object o) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new  UnsupportedOperationException();
    }



    @Override
    public boolean removeAll(Collection<?> c) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new  UnsupportedOperationException();
    }


    @Override
    public T remove(int index) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new  UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new  UnsupportedOperationException();
    }


}
