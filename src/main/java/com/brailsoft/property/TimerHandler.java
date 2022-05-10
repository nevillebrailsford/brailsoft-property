package com.brailsoft.property;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.IniFile;
import com.brailsoft.base.Notification;
import com.brailsoft.base.NotificationCentre;
import com.brailsoft.base.NotificationListener;
import com.brailsoft.base.NotificationType;
import com.brailsoft.base.ThreadServices;
import com.brailsoft.mail.EmailSender;
import com.brailsoft.model.MonitoredItem;
import com.brailsoft.model.Property;
import com.brailsoft.model.PropertyMonitor;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TimerHandler implements NotificationListener {
	private static final String CLASS_NAME = TimerHandler.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();
	private static TimerHandler instance = null;

	public synchronized static void listen() {
		LOGGER.entering(CLASS_NAME, "listen");
		if (instance == null) {
			instance = new TimerHandler();
			NotificationCentre.addListener(instance);
		}
		LOGGER.exiting(CLASS_NAME, "listen");
	}

	@Override
	public void notify(Notification notification) {
		LOGGER.entering(CLASS_NAME, "notify", notification);
		NotificationType notificationType = notification.notificationType();
		String category = notificationType.category();
		switch (category) {
			case Constants.TIMER_CATEGORY -> {
				LOGGER.fine("Timer");
				Platform.runLater(() -> {
					handleTimerNotification();
				});
			}

		}
		LOGGER.exiting(CLASS_NAME, "notify");
	}

	private void handleTimerNotification() {
		LOGGER.entering(CLASS_NAME, "handleTimerNotification", "timer popped at " + LocalDateTime.now());
		LocalDateTime now = LocalDateTime.now();
		String lastTime = IniFile.value(Constants.LAST_TIME);
		if (lastTime == null || lastTime.isEmpty()) {
			lastTime = now.toString();
			performTimedActions(now);
		}
		LocalDateTime previous = LocalDateTime.parse(lastTime);
		if (previous.plusDays(1).isBefore(now)) {
			performTimedActions(now);
		}
		LOGGER.exiting(CLASS_NAME, "handleTimerNotification");
	}

	private void performTimedActions(LocalDateTime now) {
		LOGGER.entering(CLASS_NAME, "performTimedActions", now);
		lookForMonitoredItems();
		sendEmailIfRequired();
		updateLastTime(now);
		updateStatus("Timed actions completed");
		LOGGER.exiting(CLASS_NAME, "performTimedActions");
	}

	private void lookForMonitoredItems() {
		LOGGER.entering(CLASS_NAME, "lookForMonitoredItems");
		if (PropertyMonitor.instance().propertiesWithOverdueItems().size() > 0) {
			displayOverdueItems();
		}
		if (PropertyMonitor.instance().propertiesWithOverdueNotices().size() > 0) {
			displayOverdueNotices();
		}
		LOGGER.exiting(CLASS_NAME, "lookForMonitoredItems");
	}

	private void sendEmailIfRequired() {
		LOGGER.entering(CLASS_NAME, "sendEmailIfRequired");
		if (Boolean.valueOf(IniFile.value(Constants.EMAIL_NOTIFICATION)).booleanValue()) {
			LOGGER.fine("Email notification is enabled");
			LocalDate lastSent;
			if (IniFile.value(Constants.DATE_OF_LAST_EMAIL).trim().isEmpty()) {
				lastSent = LocalDate.now().minusDays(1);
			} else {
				lastSent = LocalDate.parse(IniFile.value(Constants.DATE_OF_LAST_EMAIL));
			}
			List<Property> overdueProperties = PropertyMonitor.instance().propertiesWithOverdueItems();
			List<Property> notifiedProperties = PropertyMonitor.instance().propertiesWithOverdueNotices();
			if (overdueProperties.size() + notifiedProperties.size() > 0) {
				LOGGER.fine("Properties found");
				sendEmail();
			} else {
				LOGGER.fine("No properties found");
			}
			updateDateOfLastEmailCheck();
			LOGGER.fine("lastSent = " + lastSent.toString());
		} else {
			LOGGER.fine("Email notification is not enabled");
		}
		LOGGER.exiting(CLASS_NAME, "sendEmailIfRequired");
	}

	private void sendEmail() {
		LOGGER.entering(CLASS_NAME, "sendEmail");
		List<MonitoredItem> overdueItems = PropertyMonitor.instance().overdueItemsFor(LocalDate.now());
		List<MonitoredItem> notifiedItems = PropertyMonitor.instance().notifiedItemsFor(LocalDate.now());
		String message = composeMessage(overdueItems, notifiedItems);
		updateStatus("Preparing to send email");
		EmailSender worker = new EmailSender(message);
		ThreadServices.getInstance().executor().execute(worker);
		updateItemDate(overdueItems, notifiedItems);
		updateStatus("Request to send email complete");
		LOGGER.exiting(CLASS_NAME, "sendEmail");
	}

	private void updateItemDate(List<MonitoredItem> overdueItems, List<MonitoredItem> notifiedItems) {
		LOGGER.entering(CLASS_NAME, "updateItemDate");
		for (MonitoredItem i : overdueItems) {
			i.setEmailSentOn(LocalDate.now());
		}
		for (MonitoredItem i : notifiedItems) {
			i.setEmailSentOn(LocalDate.now());
		}
		LOGGER.exiting(CLASS_NAME, "updateItemDate");
	}

	private String composeMessage(List<MonitoredItem> overdueItems, List<MonitoredItem> notifiedItems) {
		LOGGER.entering(CLASS_NAME, "composeMessage");
		StringBuffer message = new StringBuffer();
		message.append("The following items need attention:\n\n");
		if (notifiedItems.size() > 0) {
			message.append("The following items are due soon:\n");
			for (MonitoredItem item : notifiedItems) {
				message.append(item.owner().toString()).append(" - ");
				message.append(item.toString());
				message.append("\n");
			}
			message.append("\n");
		}
		if (overdueItems.size() > 0) {
			message.append("The following items are overdue:\n");
			for (MonitoredItem item : overdueItems) {
				message.append(item.owner().toString()).append(" ");
				message.append(item.toString());
				message.append("\n");
			}
			message.append("\n");
		}
		LOGGER.exiting(CLASS_NAME, "composeMessage");
		return message.toString();
	}

	private void updateDateOfLastEmailCheck() {
		LOGGER.entering(CLASS_NAME, "updateDateOfLastEmailCheck");
		IniFile.store(Constants.DATE_OF_LAST_EMAIL, LocalDate.now().toString());
		LOGGER.exiting(CLASS_NAME, "updateDateOfLastEmailCheck");
	}

	private void updateLastTime(LocalDateTime now) {
		LOGGER.entering(CLASS_NAME, "", now);
		IniFile.store(Constants.LAST_TIME, now.toString());
		LOGGER.exiting(CLASS_NAME, "");
	}

	private void updateStatus(String message) {
		LOGGER.entering(CLASS_NAME, "updateStatus");
		StatusMonitor.instance(null).update(message);
		LOGGER.exiting(CLASS_NAME, "updateStatus");
	}

	private void displayOverdueItems() {
		LOGGER.entering(CLASS_NAME, "displayOverdueItems");
		final Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Overdue Items");
		alert.setHeaderText("Overdue items have been found");
		StringBuilder context = new StringBuilder();
		context.append("The Following properties have overdue items").append("\n");
		for (Property property : PropertyMonitor.instance().propertiesWithOverdueItems()) {
			context.append(property.address().toString()).append("\n");
		}
		alert.setContentText(context.toString());
		Platform.runLater(() -> {
			alert.showAndWait();
		});
		LOGGER.exiting(CLASS_NAME, "displayOverdueItems");
	}

	private void displayOverdueNotices() {
		LOGGER.entering(CLASS_NAME, "displayOverdueNotices");
		final Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Notified Items");
		alert.setHeaderText("Notified items have been found");
		StringBuilder context = new StringBuilder();
		context.append("The Following properties have overdue items").append("\n");
		for (Property property : PropertyMonitor.instance().propertiesWithOverdueNotices()) {
			context.append(property.address().toString()).append("\n");
		}
		alert.setContentText(context.toString());
		Platform.runLater(() -> {
			alert.showAndWait();
		});
		LOGGER.exiting(CLASS_NAME, "displayOverdueNotices");
	}

}
