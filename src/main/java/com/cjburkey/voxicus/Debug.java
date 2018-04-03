package com.cjburkey.voxicus;

import org.apache.logging.log4j.Logger;

public class Debug {
	
	private static Logger logger;
	
	public static final void init(Logger log) {
		if (logger == null) {
			logger = log;
		}
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> error(e));
	}
	
	public static final void log(Object msg) {
		logger.info(sanitize(msg));
	}
	
	public static final void log(Object msg, Object... param) {
		logger.info(sanitize(msg), param);
	}
	
	public static final void warn(Object msg) {
		logger.error(sanitize(msg));
	}
	
	public static final void error(Object msg) {
		error(msg, new Object[] { });
	}
	
	public static final void error(Object msg, Object... param) {
		if (Throwable.class.isAssignableFrom(msg.getClass())) {
			error((Throwable) msg, true);
			return;
		}
		logger.error(sanitize(msg), param);
	}
	
	public static final void error(Throwable t, boolean exit) {
		logger.error("An error occurred: " + sanitize(t.getMessage()));
		t.printStackTrace();
		if (exit) {
			System.exit(-1);
		}
	}
	
	private static final String sanitize(Object msg) {
		return (msg == null) ? "null" : msg.toString();
	}
	
}