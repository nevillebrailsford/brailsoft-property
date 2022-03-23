package com.brailsoft.property.gui;

import com.brailsoft.model.MonitoredItem;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class ItemTableView extends MonitoredItemsTableBase {

	private static final String OVERDUE_NOTIFICATION = "orange";
	private static final String OVERDUE_ACTION = "red";

	TableColumn<MonitoredItem, String> monitoredItem = new TableColumn<>("Item");
	TableColumn<MonitoredItem, String> description = new TableColumn<>("Description");
	TableColumn<MonitoredItem, String> dateOf = new TableColumn<>("Date of");
	TableColumn<MonitoredItem, String> lastAction = new TableColumn<>("Last Action");
	TableColumn<MonitoredItem, String> nextNotice = new TableColumn<>("Next Notification");
	TableColumn<MonitoredItem, String> nextAction = new TableColumn<>("Next Action");

	public ItemTableView() {
		super();
		description.setCellValueFactory(new PropertyValueFactory<>("description"));
		lastAction.setCellValueFactory(new PropertyValueFactory<>("lastAction"));

		nextNotice.setCellValueFactory(new PropertyValueFactory<>("nextNotice"));
		nextNotice.setCellFactory(new Callback<TableColumn<MonitoredItem, String>, TableCell<MonitoredItem, String>>() {
			@Override
			public TableCell<MonitoredItem, String> call(TableColumn<MonitoredItem, String> param) {
				return new ActionTableCell(OVERDUE_NOTIFICATION);
			}
		});

		nextAction.setCellValueFactory(new PropertyValueFactory<>("nextAction"));
		nextAction.setCellFactory(new Callback<TableColumn<MonitoredItem, String>, TableCell<MonitoredItem, String>>() {
			@Override
			public TableCell<MonitoredItem, String> call(TableColumn<MonitoredItem, String> param) {
				return new ActionTableCell(OVERDUE_ACTION);
			}
		});

		monitoredItem.setMinWidth(900);
		description.setMinWidth(450);
		dateOf.setMinWidth(450);
		lastAction.setMinWidth(150);
		nextNotice.setMinWidth(150);
		nextAction.setMinWidth(150);
		dateOf.getColumns().add(lastAction);
		dateOf.getColumns().add(nextNotice);
		dateOf.getColumns().add(nextAction);
		monitoredItem.getColumns().add(description);
		monitoredItem.getColumns().add(dateOf);
		getColumns().add(monitoredItem);
	}

}
