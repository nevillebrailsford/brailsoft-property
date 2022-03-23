package com.brailsoft.property;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class TestFX extends Application {

	@Override
	public void start(Stage primaryStage) {
		System.out.println("start");
		Platform.exit();
	}

	public static void main(String[] args) {
		System.out.println("main");
		launch(args);
		System.out.println("returned");
	}
}
