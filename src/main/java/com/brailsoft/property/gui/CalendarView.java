package com.brailsoft.property.gui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CalendarView extends HBox {

	public CalendarView() {
		WeekDaysColumn weekDays = new WeekDaysColumn();
		MonthRow monthHBox = new MonthRow();
		YearView pane = new YearView();
		VBox root = new VBox();
		root.getChildren().addAll(monthHBox, pane);
		getChildren().addAll(weekDays, root);

	}
}
