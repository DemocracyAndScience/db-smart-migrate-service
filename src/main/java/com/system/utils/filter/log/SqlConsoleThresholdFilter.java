package com.system.utils.filter.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
/**
 * Log自定义
 * @author noah
 *
 */
public class SqlConsoleThresholdFilter extends Filter<ILoggingEvent> {

	Level level;

	@Override
	public FilterReply decide(ILoggingEvent event) {
		if (!isStarted()) {
			return FilterReply.NEUTRAL;
		}

		if (event.getLevel().isGreaterOrEqual(level)) {
			return FilterReply.NEUTRAL;
		} else {
			
		 	String message = event.getLoggerName();
		 	boolean contains = message.contains("DBSourceSyncDao");
		 	if(contains) {
		 		return FilterReply.NEUTRAL;
			}
			return FilterReply.DENY;
		}
	}

	public void setLevel(String level) {
		this.level = Level.toLevel(level);
	}

	public void start() {
		if (this.level != null) {
			super.start();
		}
	}

}
