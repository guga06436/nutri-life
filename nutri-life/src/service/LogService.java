package service;

public interface LogService {
	void logException(Exception e);
	void logDebug(String message);
}