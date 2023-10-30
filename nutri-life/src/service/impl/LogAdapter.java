package service.impl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import service.LogService;

public class LogAdapter implements LogService{
	private static LogAdapter logAdapter = null;
	private static final Logger exceptionLog = Logger.getLogger("exceptionLog");
	private static final Logger infoLog = Logger.getLogger("infoLog");	
	
	private LogAdapter() {
		
	}
	
	public static LogAdapter getInstance() {
		if(logAdapter == null) {
			logAdapter = new LogAdapter();
		}
		
		return logAdapter;
	}
	
	@Override
	public void logException(Exception e) {
		exceptionLog.log(Level.ERROR, e.getMessage());
	}

	@Override
	public void logDebug(String message) {
		infoLog.log(Level.INFO, message);
	}
}