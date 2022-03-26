package com.brailsoft.property;

import java.util.logging.Logger;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.Notification;
import com.brailsoft.base.NotificationCentre;
import com.brailsoft.base.NotificationListener;
import com.brailsoft.base.NotificationType;
import com.brailsoft.storage.LoadState;
import com.brailsoft.storage.StorageNotificationType;

import javafx.application.Platform;

public class StatusMonitor implements NotificationListener {
	private static final String CLASS_NAME = StatusMonitor.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private static StatusMonitor instance = null;
	private PropertyController controller;
	private String message;

	public synchronized static StatusMonitor instance(PropertyController controller) {
		LOGGER.entering(CLASS_NAME, "instance", controller);
		if (instance == null) {
			if (controller == null) {
				throw new IllegalArgumentException("StatusMonitor: controller must be specified");
			}
			instance = new StatusMonitor();
			instance.controller = controller;
			NotificationCentre.addListener(instance);
		}
		LOGGER.exiting(CLASS_NAME, "instance", instance);
		return instance;
	}

	public void update(String status) {
		if (controller != null) {
			Platform.runLater(() -> {
				controller.updateStatus(status);
			});
		}
	}

	public static void reset() {
		instance = null;
	}

	@Override
	public void notify(Notification notification) {
		LOGGER.entering(CLASS_NAME, "notify", notification);
		NotificationType notificationType = notification.notificationType();
		switch (notificationType.category()) {
			case "storage" -> {
				if (notificationType == StorageNotificationType.Store) {
					LOGGER.exiting(CLASS_NAME, "notify");
					return;
				} else {
					LoadState state = (LoadState) notification.subject().get();
					message = "Loading of data " + state;
				}
			}
			case "property" -> {
				message = notification.toString();
			}
		}
		if (controller != null) {
			if (controller != null) {
				Platform.runLater(() -> {
					controller.updateStatus(message);
				});
			}
		}
		LOGGER.exiting(CLASS_NAME, "notify");
	}
}
