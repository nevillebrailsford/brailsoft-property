package com.brailsoft.property;

import com.brailsoft.base.NotificationType;

public enum TimerNotificationType implements NotificationType {
	Ticked("ticked");

	private String type;

	TimerNotificationType(String type) {
		this.type = type;
	}

	public String type() {
		return type;
	}

	@Override
	public String category() {
		return Constants.TIMER_CATEGORY;
	}

}
