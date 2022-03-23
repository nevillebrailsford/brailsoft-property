package com.brailsoft.property;

import java.util.logging.Logger;

import com.brailsoft.base.AbstractChange;
import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.Failure;
import com.brailsoft.model.MonitoredItem;
import com.brailsoft.model.PropertyMonitor;

public class ReplaceMonitoredChange extends AbstractChange {
	private static final String CLASS_NAME = ReplaceMonitoredChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private MonitoredItem before;
	private MonitoredItem after;

	public ReplaceMonitoredChange(MonitoredItem before, MonitoredItem after) {
		this.before = before;
		this.after = after;
	}

	@Override
	protected void doHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "doHook");
		redoHook();
		LOGGER.exiting(CLASS_NAME, "doHook");
	}

	@Override
	protected void undoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "undoHook");
		PropertyMonitor.instance().replaceItem(before);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void redoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().replaceItem(after);
		LOGGER.exiting(CLASS_NAME, "redoHook");
	}

}
