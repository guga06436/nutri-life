package service.iterators;

import java.util.List;
import java.util.NoSuchElementException;

public class ListIterator<T> implements Iterator<T> {
    private List<T> list;
    private int index;

    public ListIterator(List<T> list) {
        this.list = list;
        this.index = 0;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public T next() {
        if (hasNext()) {
            T element = list.get(index);
            index++;
            return element;
        }
        throw new NoSuchElementException("No more elements in the ArrayList");
    }
}