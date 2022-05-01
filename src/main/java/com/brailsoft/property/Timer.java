package com.brailsoft.property;

import com.brailsoft.base.Notification;
import com.brailsoft.base.NotificationCentre;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

public class Timer {
	private static Timer instance = null;
	private Timeline timeLine;

	public synchronized static Timer instance() {
		if (instance == null) {
			instance = new Timer();
		}
		return instance;
	}

	private Timer() {
	}

	public void start() {
		timeLine = new Timeline(new KeyFrame(Duration.minutes(1), (event) -> {
			tellListeners(event);
		}));
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.setDelay(Duration.minutes(1));
		timeLine.play();
	}

	public void stop() {
		timeLine.stop();
	};

	private void tellListeners(ActionEvent event) {
		Notification notification = new Notification(TimerNotificationType.Ticked, this, "timer");
		NotificationCentre.broadcast(notification);
	}
}
