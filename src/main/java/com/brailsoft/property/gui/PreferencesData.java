package com.brailsoft.property.gui;

import java.util.logging.Level;

public class PreferencesData {
	private Level level = null;
	private boolean emailNotification = false;
	private String emailList = "";

	public PreferencesData() {
	}

	public Level level() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public boolean emailNotification() {
		return emailNotification;
	}

	public void setEmailNotification(boolean emailNotification) {
		this.emailNotification = emailNotification;
	}

	public String emailList() {
		return emailList;
	}

	public void setEmailList(String emailList) {
		this.emailList = emailList;
	}

	@Override
	public String toString() {
		return "level=" + level + (emailNotification ? ", emailList=" + emailList : ", no notification");
	}

}
