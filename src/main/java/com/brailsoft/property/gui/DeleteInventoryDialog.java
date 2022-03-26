package com.brailsoft.property.gui;

import com.brailsoft.model.InventoryItem;
import com.brailsoft.model.Property;
import com.brailsoft.model.PropertyMonitor;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class DeleteInventoryDialog extends Dialog<InventoryItem> {
	Label label = new Label("Items:");
	ChoiceBox<InventoryItem> choiceBox = new ChoiceBox<>();

	public DeleteInventoryDialog(Property property) {
		setTitle("Delete An Item");
		setHeaderText("Select the item to delete.");
		setResizable(true);

		GridPane grid = new GridPane();
		grid.setHgap(10.0);
		grid.setVgap(10.0);
		grid.add(label, 1, 1);
		grid.add(choiceBox, 2, 1);

		choiceBox.getItems().addAll(PropertyMonitor.instance().inventoryItemsFor(property));

		getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("Delete Item", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.NO);
		getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);

		setResultConverter(new Callback<ButtonType, InventoryItem>() {

			@Override
			public InventoryItem call(ButtonType param) {
				if (param == buttonTypeOk) {
					return choiceBox.getSelectionModel().getSelectedItem();
				}
				return null;
			}
		});
	}
}
