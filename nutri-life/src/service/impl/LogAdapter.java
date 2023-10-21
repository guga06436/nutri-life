package service.impl;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import service.LogService;

public class LogAdapter implements LogService{
	private static final Logger log = LogManager.getLogger(LogAdapter.class);
	
	public LogAdapter() {
		
	}
	
	@Override
	public void logException(Exception e) {
		log.log(Level.ERROR, e.getMessage());
	}

	@Override
	public void logDebug(String message) {
		log.log(Level.INFO, message);
	}
}