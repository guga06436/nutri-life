package persistence;

public interface Persistence<T> {
	boolean insert(T object);
	T retrieve(T object);
	boolean update(T object);
	T delete(T object);
}
