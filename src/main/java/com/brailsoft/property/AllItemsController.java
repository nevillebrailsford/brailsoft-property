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

public class AllItemsController implements Initializable {

	@FXML
	VBox content;

	private MonitoredItemsTableView tableView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		tableView = new MonitoredItemsTableView();
		List<Property> properties = PropertyMonitor.instance().properties();
		for (Property property : properties) {
			List<MonitoredItem> allItems = PropertyMonitor.instance().monitoredItemsFor(property);
			for (MonitoredItem item : allItems) {
				tableView.addItem(item);
			}
		}
		content.getChildren().add(tableView);
	}

}
