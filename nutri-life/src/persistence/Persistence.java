package persistence;

import java.util.List;

import persistence.db.exception.InfraException;

public interface Persistence<T> {
	boolean insert(T object) throws InfraException;
	T retrieve(T object) throws InfraException;
	boolean update(T object, int id) throws InfraException;
	boolean delete(T object) throws InfraException;
	List<T> listAll() throws InfraException;
	int retrieveId(T object) throws InfraException;
	T retrieveById(int id) throws InfraException;
}
