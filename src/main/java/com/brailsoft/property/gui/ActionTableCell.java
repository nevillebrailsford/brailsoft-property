package com.brailsoft.property.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.brailsoft.model.MonitoredItem;

import javafx.scene.control.TableCell;

public class ActionTableCell extends TableCell<MonitoredItem, String> {
	private static final String dateFormatForUI = "dd/MM/uuuu";

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatForUI);
	private String backgroundColor;

	public ActionTableCell(String backgroundColor) {
		super();
		this.backgroundColor = backgroundColor;
	}

	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		setGraphic(null);
		if (!empty) {
			setText(item);
			LocalDate givendate = LocalDate.parse(item, formatter);
			LocalDate currentdate = LocalDate.now();
			if (currentdate.isAfter(givendate)) {
				setStyle("-fx-background-color: " + backgroundColor + ";");
			}
		} else {
			setText(null);
			setStyle(null);
		}
	}
}
