package com.brailsoft.property.gui;

import com.brailsoft.model.Address;
import com.brailsoft.model.PostCode;
import com.brailsoft.model.Property;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class PropertyDialog extends Dialog<Property> {

	private BooleanExpression invalidInput;

	private Label label1 = new Label("Street address:");
	private Label label2 = new Label("Town:");
	private Label label3 = new Label("County:");
	private Label label4 = new Label("Post Code:");
	private TextField street = new TextField();
	private TextField town = new TextField();
	private TextField county = new TextField();
	private TextField postcode = new TextField();

	public PropertyDialog() {
		setTitle("Add a Property");
		setHeaderText("Enter the details below to add a new property.");
		setResizable(true);

		GridPane grid = new GridPane();
		grid.setHgap(10.0);
		grid.setVgap(10.0);
		grid.add(label1, 1, 1);
		grid.add(street, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(town, 2, 2);
		grid.add(label3, 1, 3);
		grid.add(county, 2, 3);
		grid.add(label4, 1, 5);
		grid.add(postcode, 2, 5);
		getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("Add Property", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.NO);
		getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
		getDialogPane().lookupButton(buttonTypeOk).disableProperty().bind(invalidInputProperty());

		setResultConverter(new Callback<ButtonType, Property>() {

			@Override
			public Property call(ButtonType param) {
				if (param == buttonTypeOk) {
					String line1 = street.getText();
					String line2 = town.getText();
					String line3 = county.getText();
					String pc = postcode.getText();
					PostCode thePostCode = new PostCode(pc.toUpperCase());
					String[] linesOfAddress = new String[] { line1, line2, line3 };
					Address address = new Address(thePostCode, linesOfAddress);
					return new Property(address);
				}
				return null;
			}
		});

	}

	private BooleanExpression invalidInputProperty() {
		if (invalidInput == null) {
			invalidInput = Bindings.createBooleanBinding(
					() -> isEmpty(street) || isEmpty(town) || isEmpty(county) || !isValidPostCode(),
					street.textProperty(), town.textProperty(), county.textProperty(), postcode.textProperty());
		}
		return invalidInput;
	}

	private boolean isEmpty(TextField textField) {
		return textField.textProperty().get().isBlank() || textField.textProperty().get().isEmpty();
	}

	private boolean isValidPostCode() {
		return postcode.textProperty().get().toUpperCase().matches(PostCode.postCodeRegularExpression);
	}
}
