package com.brailsoft.property;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoadProperty {
	private FXMLLoader loader;
	private Parent parent;

	public LoadProperty(FXMLLoader loader, Parent parent) {
		this.loader = loader;
		this.parent = parent;
	}

	public FXMLLoader getLoader() {
		return loader;
	}

	public Parent getParent() {
		return parent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LoadProperty[");
		builder.append(loader.toString()).append(", ");
		builder.append(parent.toString()).append("]");
		return builder.toString();
	}

}
