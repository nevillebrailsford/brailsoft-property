package com.brailsoft.property.gui;

import java.time.LocalDate;

import com.brailsoft.model.InventoryItem;
import com.brailsoft.model.Property;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class AddInventoryDialog extends Dialog<InventoryItem> {
	private BooleanExpression invalidInput;

	private TextField description = new TextField();
	private TextField manufacturer = new TextField();
	private TextField model = new TextField();
	private TextField serialnumber = new TextField();
	private TextField supplier = new TextField();
	private DatePicker purchasedate = new DatePicker();

	public AddInventoryDialog(Property property) {
		setTitle("Add to inventory");
		setHeaderText("Enter the details below to add a new item to the inventory.");
		setResizable(true);

		Label label1 = new Label("Description*:");
		Label label2 = new Label("Manufacturer:");
		Label label3 = new Label("Model:");
		Label label4 = new Label("Serial Number:");
		Label label5 = new Label("Supplier:");
		Label label6 = new Label("Date Purchased:");
		Label label7 = new Label("* description must be specified");

		GridPane grid = new GridPane();
		grid.setHgap(10.0);
		grid.setVgap(10.0);
		grid.add(label1, 1, 1);
		grid.add(description, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(manufacturer, 2, 2);
		grid.add(label3, 1, 3);
		grid.add(model, 2, 3);
		grid.add(label4, 1, 4);
		grid.add(serialnumber, 2, 4);
		grid.add(label5, 1, 5);
		grid.add(supplier, 2, 5);
		grid.add(label6, 1, 6);
		grid.add(purchasedate, 2, 6);
		grid.add(label7, 1, 7, 2, 1);

		getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("Add Item", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.NO);
		getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
		getDialogPane().lookupButton(buttonTypeOk).disableProperty().bind(invalidInputProperty());

		setResultConverter(new Callback<ButtonType, InventoryItem>() {

			@Override
			public InventoryItem call(ButtonType param) {
				if (param == buttonTypeOk) {
					String desc = description.getText();
					String manu = manufacturer.getText();
					String mod = model.getText();
					String ser = serialnumber.getText();
					String sup = supplier.getText();
					LocalDate date = purchasedate.getValue();
					return new InventoryItem(desc, manu, mod, ser, sup, date);
				}
				return null;
			}
		});
	}

	private BooleanExpression invalidInputProperty() {
		if (invalidInput == null) {
			invalidInput = Bindings.createBooleanBinding(() -> isEmpty(description), description.textProperty());
		}
		return invalidInput;
	}

	private boolean isEmpty(TextField textField) {
		return textField.textProperty().get().isBlank() || textField.textProperty().get().isEmpty();
	}

}
