package com.brailsoft.property.gui;

import com.brailsoft.model.Address;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class AddressHBox extends HBox {
	private Address address;

	public AddressHBox(Address address) {
		super();
		this.address = new Address(address);
		this.setSpacing(5);
		Pane pane = new Pane();
		HBox.setHgrow(pane, Priority.ALWAYS);
		getChildren().add(pane);
		Label addressLabel = new Label();
		addressLabel.textProperty().bind(address.fulladdressProperty());
		addressLabel.getStyleClass().add("address-label");
		getChildren().add(addressLabel);
		pane = new Pane();
		HBox.setHgrow(pane, Priority.ALWAYS);
		getChildren().add(pane);
	}

	public Address getAddress() {
		return new Address(address);
	}
}
