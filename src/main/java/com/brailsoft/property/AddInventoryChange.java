package com.brailsoft.property;

import java.util.logging.Logger;

import com.brailsoft.base.AbstractChange;
import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.Failure;
import com.brailsoft.model.InventoryItem;
import com.brailsoft.model.PropertyMonitor;

public class AddInventoryChange extends AbstractChange {
	private static final String CLASS_NAME = AddInventoryChange.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private InventoryItem inventoryItem;

	public AddInventoryChange(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
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
		PropertyMonitor.instance().removeItem(inventoryItem);
		LOGGER.exiting(CLASS_NAME, "undoHook");
	}

	@Override
	protected void redoHook() throws Failure {
		LOGGER.entering(CLASS_NAME, "redoHook");
		PropertyMonitor.instance().addItem(inventoryItem);
		LOGGER.exiting(CLASS_NAME, "redoHook");
	}

}
