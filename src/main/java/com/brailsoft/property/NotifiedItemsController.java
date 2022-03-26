package com.brailsoft.property;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.brailsoft.model.MonitoredItem;
import com.brailsoft.model.Property;
import com.brailsoft.model.PropertyMonitor;
import com.brailsoft.property.gui.MonitoredItemsTableView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

public class NotifiedItemsController implements Initializable {

	@FXML
	VBox content;

	private MonitoredItemsTableView tableView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tableView = new MonitoredItemsTableView();
		List<Property> notifiedProperties = PropertyMonitor.instance().propertiesWithOverdueNotices();
		for (Property property : notifiedProperties) {
			List<MonitoredItem> allItems = PropertyMonitor.instance().monitoredItemsFor(property);
			for (MonitoredItem item : allItems) {
				if (item.noticeDue()) {
					tableView.addItem(item);
				}
			}
		}
		content.getChildren().add(tableView);
	}

}
