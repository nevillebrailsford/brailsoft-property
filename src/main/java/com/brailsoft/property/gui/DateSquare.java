package com.brailsoft.property.gui;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DateSquare extends Rectangle {
	public static final double SIZE = 10.0;
	public static final double SPACING = SIZE + 4.0;;

	private double red = 150;
	private double green = 220;
	private double blue = 145;

	public DateSquare(int X, int Y, int notifiedCount, int overdueCount) {
		this.setHeight(SIZE);
		this.setWidth(SIZE);
		this.setX(X);
		this.setY(Y);

		if (overdueCount > 0) {
			red = 250;
			green = 145 - (20 * overdueCount < 8 ? overdueCount : 7);
			blue = 145 - (20 * overdueCount < 8 ? overdueCount : 7);
		} else if (notifiedCount > 0) {
			red = 250;
			green = 180 - (5 * notifiedCount < 21 ? notifiedCount : 20);
			blue = 96 - (20 * notifiedCount < 5 ? notifiedCount : 4);
		}
		setFill(Color.color(red / 256, green / 256, blue / 256));
	}
}
