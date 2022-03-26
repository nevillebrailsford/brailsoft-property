package com.brailsoft.property.gui;

import com.brailsoft.model.MonitoredItem;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class MonitoredItemsTableView extends MonitoredItemsTableBase {

	TableColumn<MonitoredItem, String> postcode = new TableColumn<>("Property");
	TableColumn<MonitoredItem, String> monitoredItem = new TableColumn<>("Item");
	TableColumn<MonitoredItem, String> description = new TableColumn<>("Description");
	TableColumn<MonitoredItem, String> dateOf = new TableColumn<>("Date of");
	TableColumn<MonitoredItem, String> lastAction = new TableColumn<>("Last Action");
	TableColumn<MonitoredItem, String> nextNotice = new TableColumn<>("Next Notification");
	TableColumn<MonitoredItem, String> nextAction = new TableColumn<>("Next Action");

	public MonitoredItemsTableView() {
		super();
		postcode.setCellValueFactory(cellData -> cellData.getValue().owner().address().postCode().valueProperty());
		description.setCellValueFactory(new PropertyValueFactory<>("description"));
		lastAction.setCellValueFactory(new PropertyValueFactory<>("lastAction"));
		nextNotice.setCellValueFactory(new PropertyValueFactory<>("nextNotice"));
		nextAction.setCellValueFactory(new PropertyValueFactory<>("nextAction"));

		postcode.setMinWidth(40);
		monitoredItem.setMinWidth(730);
		description.setMinWidth(230);
		dateOf.setMinWidth(430);
		lastAction.setMinWidth(150);
		nextNotice.setMinWidth(150);
		nextAction.setMinWidth(150);
		dateOf.getColumns().add(lastAction);
		dateOf.getColumns().add(nextNotice);
		dateOf.getColumns().add(nextAction);
		monitoredItem.getColumns().add(description);
		monitoredItem.getColumns().add(dateOf);
		getColumns().add(postcode);
		getColumns().add(monitoredItem);
	}

}
