package com.brailsoft.property.gui;

import java.time.LocalDate;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MonthRow extends HBox {

	public MonthRow() {
		super();
		setAlignment(Pos.TOP_CENTER);
		setSpacing(40);
		setPrefWidth(YearView.NUMBER_OF_COLUMNS * DateSquare.SPACING);
		LocalDate monthDate = LocalDate.now();
		for (int i = 0; i < 12; i++) {
			String month = monthDate.getMonth().name();
			getChildren().add(new Label(month.substring(0, 1) + month.substring(1, 3).toLowerCase()));
			monthDate = monthDate.plusMonths(1);
		}

	}
}
