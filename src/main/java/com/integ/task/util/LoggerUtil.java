package com.integ.task.util;

import org.springframework.stereotype.Component;

@Component
public class LoggerUtil {

	public static final String LOG_MSG_PREFIX = "In %s.%s:%d - ";

	public String getLogLocation(String className, StackTraceElement[] stackTraceElements) {
		StackTraceElement stackTraceElement = stackTraceElements[2];
		if ("invoke".equals(stackTraceElement.getMethodName())) {
			stackTraceElement = stackTraceElements[1];
		}
		return String.format(LOG_MSG_PREFIX, className, stackTraceElement.getMethodName(),
				stackTraceElement.getLineNumber());
	}
}
