package persistence;

import java.util.List;

import persistence.db.exception.InfraException;

public interface Persistence<T> {
	boolean insert(T object) throws InfraException;
	T retrieve(T object) throws InfraException;
	boolean update(T object) throws InfraException;
	T delete(T object) throws InfraException;
	List<T> listAll() throws InfraException;
}
