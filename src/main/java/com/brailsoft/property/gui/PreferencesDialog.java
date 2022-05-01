package com.brailsoft.property.gui;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.IniFile;
import com.brailsoft.mail.EmailConfigurer;
import com.brailsoft.property.Constants;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

public class PreferencesDialog extends Dialog<PreferencesData> {
	private static final String CLASS_NAME = PreferencesDialog.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private Label label2 = new Label("Logging Level:");
	private ChoiceBox<String> loggingChoice = new ChoiceBox<>();
	private Button resetButton = new Button("Reset to default");

	private CheckBox emailNotification = new CheckBox("Send notifications by email");
	private Button editEMail = new Button("Edit");

	private Label label3 = new Label("Send notifications to:");
	private TextField emails = new TextField();

	private static final String[] loggingChoices = new String[] { "ALL", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE",
			"FINER", "FINEST", "OFF" };

	private static final String DEFAULT_LEVEL = "WARNING";

	public PreferencesDialog() {
		LOGGER.entering(CLASS_NAME, "init");
		setTitle("Preferences");
		setHeaderText("Complete details below to set preferences.");
		setResizable(true);

		loadChoiceBoxWithItems();
		loggingChoice.getSelectionModel().select(ApplicationConfiguration.applicationDecsriptor().level().toString());
		if (!IniFile.value(Constants.LOGGING_LEVEL).isEmpty()) {
			loggingChoice.getSelectionModel().select(IniFile.value(Constants.LOGGING_LEVEL));
		}
		GridPane grid = new GridPane();
		grid.setHgap(10.0);
		grid.setVgap(10.0);
		grid.add(label2, 1, 2);
		grid.add(loggingChoice, 2, 2);
		grid.add(resetButton, 3, 2);
		grid.add(emailNotification, 1, 3, 3, 1);
		grid.add(label3, 1, 4);
		grid.add(emails, 2, 4);
		grid.add(editEMail, 3, 4);
		getDialogPane().setContent(grid);

		resetButton.setOnAction((event) -> {
			loggingChoice.getSelectionModel().select(DEFAULT_LEVEL);
		});

		emailNotification.setOnAction((event) -> {
			if (!EmailConfigurer.instance().isValidConfiguration()) {
				emailNotification.setSelected(false);
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Email Configuration");
				alert.setHeaderText("Email Configuration is incomplete.");
				alert.setContentText("Email notification cannot be enabled at this time.");
				alert.showAndWait();
			}
		});

		editEMail.setOnAction((event) -> {
			Optional<String> result = new EmailListDialog(IniFile.value("emailList")).showAndWait();
			if (result.isPresent()) {
				emails.setText(result.get());
			}
		});

		emails.setText(""); // preferences.getEMailList());

		ButtonType buttonTypeOk = new ButtonType("Set Preferences", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.NO);
		getDialogPane().getButtonTypes().addAll(buttonTypeOk, buttonTypeCancel);
		emails.disableProperty().bind(emailNotification.selectedProperty().not());
		editEMail.disableProperty().bind(emailNotification.selectedProperty().not());

		emailNotification.setSelected(false); // preferences.isEmailNotification());

		setResultConverter(new Callback<ButtonType, PreferencesData>() {

			@Override
			public PreferencesData call(ButtonType param) {
				LOGGER.entering(CLASS_NAME, "call");
				if (param == buttonTypeOk) {
					PreferencesData data = new PreferencesData();
					data.setLevel(Level.parse(loggingChoice.getSelectionModel().selectedItemProperty().get()));
					if (emailNotification.isSelected() && isEmpty(emails)) {
						data.setEmailNotification(false);
						new Alert(AlertType.INFORMATION, "No recipients entered", ButtonType.OK).showAndWait();
					} else {
						data.setEmailNotification(emailNotification.isSelected());
					}
					data.setEmailList(emails.getText());
					LOGGER.exiting(CLASS_NAME, "call", data);
					return data;
				}
				LOGGER.exiting(CLASS_NAME, "call");
				return null;
			}
		});
		LOGGER.exiting(CLASS_NAME, "init");
	}

	private boolean isEmpty(TextField textField) {
		return textField.textProperty().get().isBlank() || textField.textProperty().get().isEmpty();
	}

	private void loadChoiceBoxWithItems() {
		for (int i = 0; i < loggingChoices.length; i++) {
			loggingChoice.getItems().add(loggingChoices[i]);
		}
	}

}
