package com.brailsoft.property.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class EmailListDialog extends Dialog<String> {

	private ListView<String> listView = new ListView<>();
	private StringConverter<String> converter = new EMailValidator();
	private ObservableList<String> users;

	public EmailListDialog(String list) {
		setTitle("Update list of recipients");
		setHeaderText("Enter email addresses to whom a notification should be sent");
		setResizable(true);

		GridPane grid = new GridPane();
		grid.setHgap(10.0);
		grid.setVgap(10.0);
		List<String> userList = new ArrayList<>();
		StringTokenizer st = new StringTokenizer(list, ",");
		int tokens = st.countTokens();
		while (st.hasMoreTokens()) {
			userList.add(st.nextToken());
		}
		for (int i = tokens; i < 10; i++) {
			userList.add("");
		}
		users = FXCollections.observableArrayList(userList);
		listView.setItems(users);
		listView.setPrefWidth(600);
		listView.setPrefHeight(250);
		listView.setEditable(true);
		listView.setCellFactory(param -> new TextFieldListCell<>(converter));
		grid.add(listView, 0, 0);

		getDialogPane().setContent(grid);

		ButtonType buttonTypeOk = new ButtonType("Finish", ButtonData.OK_DONE);
		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

		setResultConverter(new Callback<ButtonType, String>() {

			@Override
			public String call(ButtonType param) {
				if (param == buttonTypeOk) {
					StringBuilder builder = new StringBuilder();
					String comma = "";
					for (String p : users) {
						if (!p.isEmpty()) {
							builder.append(comma).append(p);
							comma = ",";
						}
					}
					if (builder.isEmpty()) {
						return " ";
					} else {
						return builder.toString();
					}
				}
				return null;
			}
		});

	}

	private class EMailValidator extends StringConverter<String> {

		@Override
		public String toString(String object) {
			return object;
		}

		@Override
		public String fromString(String string) {
			if (string.matches(".*@.*")) {
				return string;
			} else {
				return "";
			}
		}

	}
}
