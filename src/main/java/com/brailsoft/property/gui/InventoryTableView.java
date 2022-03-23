package com.brailsoft.property.gui;

import com.brailsoft.model.InventoryItem;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryTableView extends InventoryItemsTableBase {
	TableColumn<InventoryItem, String> description = new TableColumn<>("Description");
	TableColumn<InventoryItem, String> manufacturer = new TableColumn<>("Manufacturer");
	TableColumn<InventoryItem, String> model = new TableColumn<>("Model");
	TableColumn<InventoryItem, String> serialnumber = new TableColumn<>("Serial Number");
	TableColumn<InventoryItem, String> supplier = new TableColumn<>("Supplier");
	TableColumn<InventoryItem, String> purchasedate = new TableColumn<>("Purchase Date");

	public InventoryTableView() {
		super();
		description.setCellValueFactory(new PropertyValueFactory<>("description"));
		manufacturer.setCellValueFactory(new PropertyValueFactory<>("manufacturer"));
		model.setCellValueFactory(new PropertyValueFactory<>("model"));
		serialnumber.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
		supplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));
		purchasedate.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));

		description.setMinWidth(395);
		manufacturer.setMinWidth(95);
		model.setMinWidth(95);
		serialnumber.setMinWidth(95);
		supplier.setMinWidth(95);
		purchasedate.setMinWidth(95);

		getColumns().add(description);
		getColumns().add(manufacturer);
		getColumns().add(model);
		getColumns().add(serialnumber);
		getColumns().add(supplier);
		getColumns().add(purchasedate);
	}
}
