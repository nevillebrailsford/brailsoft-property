package com.brailsoft.property.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.brailsoft.model.MonitoredItem;
import com.brailsoft.model.Period;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class EventDialog extends Dialog<MonitoredItem> {

	private BooleanExpression invalidInput;

	private TextField description = new TextField();
	private ChoiceBox<Period> actionPeriod = new ChoiceBox<>();
	private TextField howMany = new TextField();
	private DatePicker lastAction = new DatePicker();
	private ChoiceBox<Period> noticePeriod = new ChoiceBox<>();
	private TextField noticeHowMany = new TextField();

	public EventDialog() {
		lastAction.setConverter(createDateConverter());

		setTitle("Add an Event");
		setHeaderText("Enter the details below to add a new event to be monitored.");
		setResizable(true);

		Label label1 = new Label("Description:");
		Label label2 = new Label("Period:");
		Label label3 = new Label("How many:");
		Label label4 = new Label("Date last actioned:");
		Label label5 = new Label("Notice period:");
		Label label6 = new Label("How many:");
		actionPeriod.getItems().add(Period.YEARLY);
		actionPeriod.getItems().add(Period.MONTHLY);
		actionPeriod.getItems().add(Period.WEEKLY);
		noticePeriod.getItems().add(Period.YEARLY);
		noticePeriod.getItems().add(Period.MONTHLY);
		noticePeriod.getItems().add(Period.WEEKLY);

		GridPane grid = new GridPane();
		grid.setHgap(10.0);
		grid.setVgap(10.0);
		grid.add(label1, 1, 1);
		grid.add(description, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(actionPeriod, 2, 2);
		grid.add(label3, 1, 3);
		grid.add(howMany, 2, 3);
		grid.add(label4, 1, 4);
		grid.add(lastAction, 2, 4);
		grid.add(label5, 1, 5);
		grid.add(noticePeriod, 2, 5);
		grid.add(label6, 1, 6);
		grid.add(noticeHowMany, 2, 6);
		getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("Add Event", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.NO);
		getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
		getDialogPane().lookupButton(buttonTypeOk).disableProperty().bind(invalidInputProperty());

		lastAction.setValue(LocalDate.now());
		actionPeriod.setValue(Period.YEARLY);
		noticePeriod.setValue(Period.WEEKLY);

		setResultConverter(new Callback<ButtonType, MonitoredItem>() {

			@Override
			public MonitoredItem call(ButtonType param) {
				if (param == buttonTypeOk) {
					String desc = description.getText();
					Period per = actionPeriod.getValue();
					String howM = howMany.getText();
					LocalDate date = lastAction.getValue();
					Period evper = noticePeriod.getValue();
					String evhowM = noticeHowMany.getText();
					return new MonitoredItem(desc, per, Integer.parseInt(howM), date, Integer.parseInt(evhowM), evper);
				}
				return null;
			}
		});
	}

	private BooleanExpression invalidInputProperty() {
		if (invalidInput == null) {
			invalidInput = Bindings.createBooleanBinding(
					() -> isEmpty(description) || !isNumeric(howMany) || !isNumeric(noticeHowMany)
							|| !hasValue(actionPeriod) || !hasValue(noticePeriod) || !hasValue(lastAction),
					description.textProperty(), howMany.textProperty(), noticeHowMany.textProperty(),
					actionPeriod.valueProperty(), noticePeriod.valueProperty(), lastAction.valueProperty());
		}
		return invalidInput;
	}

	private boolean isEmpty(TextField textField) {
		return textField.textProperty().get().isBlank() || textField.textProperty().get().isEmpty();
	}

	private boolean isNumeric(TextField textField) {
		return textField.textProperty().get().matches("[0-9]*");
	}

	private boolean hasValue(ChoiceBox<Period> choiceBox) {
		return choiceBox.valueProperty().get() != null;
	}

	private boolean hasValue(DatePicker datePicker) {
		return datePicker.valueProperty().get() != null;
	}

	private StringConverter<LocalDate> createDateConverter() {
		String dateFormatForUI = "dd/MM/uuuu";
		StringConverter<LocalDate> dateConverter = new StringConverter<LocalDate>() {
			private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormatForUI);

			@Override
			public String toString(LocalDate localDate) {
				if (localDate == null) {
					return "";
				}
				return formatter.format(localDate);
			}

			@Override
			public LocalDate fromString(String dateString) {
				if (dateString == null || dateString.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(dateString, formatter);
			}
		};

		return dateConverter;
	}

}
