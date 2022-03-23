package com.brailsoft.property.gui;

import com.brailsoft.model.InventoryItem;

import javafx.scene.control.TableView;

public class InventoryItemsTableBase extends TableView<InventoryItem> {
	public void addItem(InventoryItem item) {
		int positionToInsert = -1;
		for (int i = getItems().size() - 1; i >= 0; i--) {
			InventoryItem listItem = getItems().get(i);
			if (item.compareTo(listItem) < 0) {
				positionToInsert = i;
				break;
			}
		}
		if (positionToInsert == -1) {
			getItems().add(item);
		} else {
			getItems().add(positionToInsert, item);
		}
	}

	public void replaceItem(InventoryItem item) {
		for (int i = 0; i < getItems().size(); i++) {
			if (getItems().get(i).equals(item)) {
				getItems().set(i, item);
				break;
			}
		}
	}

	public void removeItem(InventoryItem item) {
		for (int i = 0; i < getItems().size(); i++) {
			if (getItems().get(i).equals(item)) {
				getItems().remove(i);
				break;
			}
		}
	}
}
