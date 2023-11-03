package service.iterators;

public interface Iterator<T> {
    boolean hasNext();
    T next();
    int getIndex();
}