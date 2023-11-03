package service.iterators;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ArrayListIterator<T> implements Iterator<T> {
    private ArrayList<T> arrayList;
    private int index;

    public ArrayListIterator(ArrayList<T> arrayList) {
        this.arrayList = arrayList;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < arrayList.size();
    }

    @Override
    public T next() {
        if (hasNext()) {
            T element = arrayList.get(index);
            index++;
            return element;
        }
        throw new NoSuchElementException("No more elements in the ArrayList");
    }
}