package com.brailsoft.property.gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.brailsoft.model.PropertyMonitor;
import com.brailsoft.property.Constants;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

public class YearView extends Pane {
	public static final int NUMBER_OF_COLUMNS = 53;

	private static final DateTimeFormatter toolTipFormatter = DateTimeFormatter
			.ofPattern(Constants.dateFormatForCalendarView);
	private static final int GAP = 4;

	public YearView() {
		super();
		setPrefWidth(NUMBER_OF_COLUMNS * DateSquare.SPACING);
		int xpos = GAP;
		int ypos = GAP;
		LocalDate date = LocalDate.now();
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < 7; j++) {
				getChildren().add(createDataSquare(xpos, ypos, date));
				ypos += DateSquare.SPACING;
				date = date.plusDays(1);
			}
			ypos = GAP;
			xpos += DateSquare.SPACING;
		}
	}

	private DateSquare createDataSquare(int xpos, int ypos, LocalDate date) {
		int numberOfOverdue = PropertyMonitor.instance().overdueItemsFor(date).size();
		int numberOfNotified = PropertyMonitor.instance().notifiedItemsFor(date).size();
		String toolTip = date.format(toolTipFormatter);
		if (numberOfOverdue > 0) {
			toolTip += " - " + numberOfOverdue
					+ (numberOfOverdue == 1 ? " item due for completion" : " items due for completion");
		} else if (numberOfNotified > 0) {
			toolTip += " - " + numberOfNotified
					+ (numberOfNotified == 1 ? " item will be due soon" : " items will be due soon");
		}
		Tooltip t = new Tooltip(toolTip);
		DateSquare rect = new DateSquare(xpos, ypos, numberOfNotified, numberOfOverdue);
		Tooltip.install(rect, t);
		return rect;
	}
}
