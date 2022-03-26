package com.brailsoft.property;

import java.util.logging.Logger;

import com.brailsoft.base.AbstractChange;
import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.Failure;
import com.brailsoft.model.MonitoredItem;
import com.brailsoft.model.PropertyMonitor;

public class AddMonitoredChange extends AbstractChange {
	private static final String CLASS_NAME = AddMonitoredChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private MonitoredItem monitoredItem;

	public AddMonitoredChange(MonitoredItem monitoredItem) {
		this.monitoredItem = monitoredItem;
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
		PropertyMonitor.instance().removeItem(monitoredItem);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void redoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().addItem(monitoredItem);
		LOGGER.exiting(CLASS_NAME, "reHook");
	}

}
