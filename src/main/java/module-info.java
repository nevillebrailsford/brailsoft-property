module brailsoft.property {

	requires transitive brailsoft.base;
	requires brailsoft.storage;
	requires transitive brailsoft.model;
	requires brailsoft.mail;

	requires transitive java.logging;
	requires java.prefs;

	requires transitive javafx.graphics;
	requires javafx.controls;
	requires transitive javafx.fxml;

	opens com.brailsoft.property to javafx.fxml;

	exports com.brailsoft.property;
}