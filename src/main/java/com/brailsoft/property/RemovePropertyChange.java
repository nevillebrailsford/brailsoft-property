package com.brailsoft.property;

import java.util.logging.Logger;

import com.brailsoft.base.AbstractChange;
import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.Failure;
import com.brailsoft.model.Property;
import com.brailsoft.model.PropertyMonitor;

public class RemovePropertyChange extends AbstractChange {
	private static final String CLASS_NAME = RemovePropertyChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private Property property;

	public RemovePropertyChange(Property property) {
		this.property = property;
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
		PropertyMonitor.instance().addProperty(property);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void redoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().removeProperty(property);
		LOGGER.exiting(CLASS_NAME, "redoHook");
	}

}
