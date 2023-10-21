package persistence;

import persistence.db.exception.InfraException;

public interface Factory<T> {
	T getPersistence() throws InfraException;
}
