package com.brailsoft.property;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.ChangeManager;
import com.brailsoft.base.IniFile;
import com.brailsoft.base.Notification;
import com.brailsoft.base.NotificationCentre;
import com.brailsoft.base.NotificationListener;
import com.brailsoft.base.NotificationType;
import com.brailsoft.model.InventoryItem;
import com.brailsoft.model.InventoryItemNotificationType;
import com.brailsoft.model.MonitoredItem;
import com.brailsoft.model.MonitoredItemNotificationType;
import com.brailsoft.model.Property;
import com.brailsoft.model.PropertyNotificationType;
import com.brailsoft.property.gui.AddInventoryDialog;
import com.brailsoft.property.gui.CalendarView;
import com.brailsoft.property.gui.DeleteInventoryDialog;
import com.brailsoft.property.gui.DeleteItemDialog;
import com.brailsoft.property.gui.EventDialog;
import com.brailsoft.property.gui.PreferencesData;
import com.brailsoft.property.gui.PreferencesDialog;
import com.brailsoft.property.gui.PropertyDialog;
import com.brailsoft.property.gui.PropertyTab;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PropertyController implements Initializable, NotificationListener {
	private static final String CLASS_NAME = PropertyController.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private PropertyManagement propertyManager;

	private BooleanProperty propertiesExist = new SimpleBooleanProperty(this, "propertiesExist", false);

	@FXML
	private MenuItem addProperty;

	@FXML
	private MenuItem addItem;

	@FXML
	private MenuItem addInventory;

	@FXML
	private MenuItem deleteProperty;

	@FXML
	private MenuItem deleteItem;

	@FXML
	private MenuItem deleteInventory;

	@FXML
	private TabPane tabPane;

	@FXML
	private MenuItem undo;

	@FXML
	private MenuItem redo;

	@FXML
	private TextField status;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LOGGER.entering(CLASS_NAME, "initialize");
		try {
			StatusMonitor.instance(this);
		} catch (Throwable t) {
			LOGGER.warning("Caught exception: " + t.getMessage());
			LOGGER.exiting(CLASS_NAME, "initialize");
			Platform.exit();
		}
		undo.disableProperty().bind(ChangeManager.instance().undoableProperty().not());
		redo.disableProperty().bind(ChangeManager.instance().redoableProperty().not());
		addItem.disableProperty().bind(propertiesExist.not());
		addInventory.disableProperty().bind(propertiesExist.not());
		deleteProperty.disableProperty().bind(propertiesExist.not());
		deleteItem.disableProperty().bind(propertiesExist.not());
		deleteInventory.disableProperty().bind(propertiesExist.not());
		NotificationCentre.addListener(this);
		LOGGER.exiting(CLASS_NAME, "initialize");
	}

	public void setPropertyManager(PropertyManagement propertyManager) {
		this.propertyManager = propertyManager;
	}

	public void updateStatus(String status) {
		this.status.textProperty().set(status);
	}

	@FXML
	void about(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "about");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Property Management \nVersion 1.0.0\nBuild date: 22/03/2022");
		alert.setTitle("About Property Management");
		alert.setHeaderText("Property Management");
		alert.showAndWait();
		LOGGER.exiting(CLASS_NAME, "about");
	}

	@FXML
	void undo(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "undo");
		ChangeManager.instance().undo();
		LOGGER.exiting(CLASS_NAME, "undo");
	}

	@FXML
	void redo(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "redo");
		ChangeManager.instance().redo();
		LOGGER.exiting(CLASS_NAME, "redo");
	}

	@FXML
	void addProperty(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "addProperty");
		Optional<Property> result = new PropertyDialog().showAndWait();
		if (result.isPresent()) {
			Property property = result.get();
			AddPropertyChange addPropertyChange = new AddPropertyChange(property);
			ChangeManager.instance().execute(addPropertyChange);
		}
		LOGGER.exiting(CLASS_NAME, "addProperty");
	}

	@FXML
	void deleteProperty(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "deleteProperty");
		Property property = getSelectedProperty();
		if (userWantsToDeleteProperty(property) == ButtonType.YES) {
			RemovePropertyChange removePropertyChange = new RemovePropertyChange(property);
			ChangeManager.instance().execute(removePropertyChange);
		}
		LOGGER.exiting(CLASS_NAME, "deleteProperty");
	}

	@FXML
	void addItem(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "addItem");
		Optional<MonitoredItem> result = new EventDialog().showAndWait();
		if (result.isPresent()) {
			MonitoredItem item = result.get();
			Property property = getSelectedProperty();
			item.setOwner(property);
			AddMonitoredChange addMonitoredChange = new AddMonitoredChange(item);
			ChangeManager.instance().execute(addMonitoredChange);
		}
		LOGGER.exiting(CLASS_NAME, "addItem");
	}

	@FXML
	void deleteItem(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "deleteItem");
		Property property = getSelectedProperty();
		Optional<MonitoredItem> result = new DeleteItemDialog(property).showAndWait();
		if (result.isPresent()) {
			MonitoredItem item = result.get();
			RemoveMonitoredChange removeMonitoredChange = new RemoveMonitoredChange(item);
			ChangeManager.instance().execute(removeMonitoredChange);
		}
		LOGGER.exiting(CLASS_NAME, "deleteItem");
	}

	@FXML
	void addInventory(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "addInventory");
		Property property = getSelectedProperty();
		Optional<InventoryItem> result = new AddInventoryDialog(property).showAndWait();
		if (result.isPresent()) {
			InventoryItem item = result.get();
			item.setOwner(property);
			AddInventoryChange addInventoryChange = new AddInventoryChange(item);
			ChangeManager.instance().execute(addInventoryChange);
		}
		LOGGER.exiting(CLASS_NAME, "addInventory");
	}

	@FXML
	void deleteInventory(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "deleteInventory");
		Property property = getSelectedProperty();
		Optional<InventoryItem> result = new DeleteInventoryDialog(property).showAndWait();
		if (result.isPresent()) {
			InventoryItem item = result.get();
			RemoveInventoryChange removeInventoryChange = new RemoveInventoryChange(item);
			ChangeManager.instance().execute(removeInventoryChange);
		}
		LOGGER.exiting(CLASS_NAME, "deleteInventory");
	}

	@FXML
	void exitApplication(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "exitApplication");
		LOGGER.exiting(CLASS_NAME, "exitApplication");
		propertyManager.shutdown();
	}

	@FXML
	void viewAllItems(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "viewAllItems");
		Stage stage = new Stage();
		stage.setTitle("All Items");
		Scene scene;
		try {
			LoadProperty LoadProperty = propertyManager.loadFXML("AllItems");
			scene = new Scene(LoadProperty.getParent());
		} catch (Exception e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			IllegalArgumentException exc = new IllegalArgumentException("PropertyController: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "viewAllItems", exc);
			LOGGER.exiting(CLASS_NAME, "viewAllItems");
			throw exc;
		}
		scene.getStylesheets().add(PropertyController.class.getResource(Constants.CSS_NAME).toExternalForm());
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.show();
		LOGGER.exiting(CLASS_NAME, "viewAllItems");
	}

	@FXML
	void viewOverdueItems(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "viewOverdueItems");
		Stage stage = new Stage();
		stage.setTitle("Overdue Items");
		Scene scene;
		try {
			LoadProperty LoadProperty = propertyManager.loadFXML("OverdueItems");
			scene = new Scene(LoadProperty.getParent());
		} catch (Exception e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			IllegalArgumentException exc = new IllegalArgumentException("PropertyController: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "viewOverdueItems", exc);
			LOGGER.exiting(CLASS_NAME, "viewOverdueItems");
			throw exc;
		}
		scene.getStylesheets().add(PropertyController.class.getResource(Constants.CSS_NAME).toExternalForm());
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.show();
		LOGGER.exiting(CLASS_NAME, "viewOverdueItems");
	}

	@FXML
	void viewNotifiedItems(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "viewNotifiedItems");
		Stage stage = new Stage();
		stage.setTitle("Notified Items");
		Scene scene;
		try {
			LoadProperty LoadProperty = propertyManager.loadFXML("NotifiedItems");
			scene = new Scene(LoadProperty.getParent());
		} catch (Exception e) {
			LOGGER.warning("Caught exception: " + e.getMessage());
			IllegalArgumentException exc = new IllegalArgumentException("PropertyController: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "viewNotifiedItems", exc);
			LOGGER.exiting(CLASS_NAME, "viewNotifiedItems");
			throw exc;
		}
		scene.getStylesheets().add(PropertyController.class.getResource(Constants.CSS_NAME).toExternalForm());
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setScene(scene);
		stage.show();
		LOGGER.exiting(CLASS_NAME, "viewNotifiedItems");
	}

	@FXML
	void printReport() {
		LOGGER.entering(CLASS_NAME, "printReport");
//		PrintReport.printReport((Stage) tabPane.getScene().getWindow());
		LOGGER.exiting(CLASS_NAME, "printReport");
	}

	@FXML
	void printInventory() {
		LOGGER.entering(CLASS_NAME, "");
//		PrintReport.printInventory((Stage) tabPane.getScene().getWindow());
		LOGGER.exiting(CLASS_NAME, "");
	}

	@FXML
	void preferences() {
		LOGGER.entering(CLASS_NAME, "preferences");
		Optional<PreferencesData> result = new PreferencesDialog().showAndWait();
		if (result.isPresent()) {
			Level loggingLevel = result.get().level();
			boolean notification = result.get().emailNotification();
			String emails = result.get().emailList();
			IniFile.store(Constants.LOGGING_LEVEL, loggingLevel.toString());
			IniFile.store(Constants.EMAIL_NOTIFICATION, Boolean.toString(notification));
			if (!emails.trim().isEmpty()) {
				IniFile.store(Constants.EMAIL_LIST, emails);
			}
		}
		LOGGER.entering(CLASS_NAME, "preferences");
	}

	@FXML
	void viewCalendar() {
		LOGGER.entering(CLASS_NAME, "viewCalendar");
		Stage stage = new Stage();
		stage.setTitle("Calendar");
		CalendarView mainRoot = new CalendarView();
		Scene scene = new Scene(mainRoot, 35 + (53 * 14), 125);
		stage.setScene(scene);
		stage.show();

		LOGGER.exiting(CLASS_NAME, "viewCalendar");
	}

	private ButtonType userWantsToDeleteProperty(Property property) {
		Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
		alert.setTitle("Delete Property");
		alert.setHeaderText("Confirm deletion of property");
		alert.setContentText("Are you sure you want to delete page for \n" + property + "?");
		ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
		return result;
	}

	private Property getSelectedProperty() {
		PropertyTab selectedTab = (PropertyTab) tabPane.getSelectionModel().getSelectedItem();
		Property property = selectedTab.getProperty();
		return property;
	}

	private void updatePropertiesExist() {
		propertiesExist.set(!tabPane.getTabs().isEmpty());
	}

	@Override
	public void notify(Notification notification) {
		LOGGER.entering(CLASS_NAME, "notification", notification);
		NotificationType notificationType = notification.notificationType();
		String category = notificationType.category();
		switch (category) {
			case Constants.PROPERTY_CATEGORY -> {
				LOGGER.fine("Property");
				Property property = (Property) notification.subject().orElse(null);
				Platform.runLater(() -> {
					hanldePropertyNotification(notificationType, property);
				});
			}
			case Constants.MONITORED_ITEM_CATEGORY -> {
				LOGGER.fine("MonitoredItem");
				MonitoredItem monitoredItem = (MonitoredItem) notification.subject().orElse(null);
				Platform.runLater(() -> {
					handleMonitoredItemNotification(notificationType, monitoredItem);
				});
			}
			case Constants.INVENTORY_ITEM_CATEGORY -> {
				LOGGER.fine("InventoryItem");
				InventoryItem inventoryItem = (InventoryItem) notification.subject().orElse(null);
				Platform.runLater(() -> {
					handleInventoryItemNotification(notificationType, inventoryItem);
				});
			}
		}
		LOGGER.exiting(CLASS_NAME, "notification", notification);
	}

	private void hanldePropertyNotification(NotificationType notificationType, Property property) {
		LOGGER.entering(CLASS_NAME, "hanldePropertyNotification", new Object[] { notificationType, property });
		if (notificationType == PropertyNotificationType.Add) {
			addProperty(property);
		}
		if (notificationType == PropertyNotificationType.Removed) {
			removeProperty(property);
		}
		if (notificationType == PropertyNotificationType.Failed) {
			System.out.println("Property failed");
		}
		updatePropertiesExist();
		LOGGER.exiting(CLASS_NAME, "hanldePropertyNotification");
	}

	private void handleMonitoredItemNotification(NotificationType notificationType, MonitoredItem monitoredItem) {
		LOGGER.entering(CLASS_NAME, "handleMonitoredItemNotification",
				new Object[] { notificationType, monitoredItem });
		if (notificationType == MonitoredItemNotificationType.Add) {
			addMonitoredItem(monitoredItem);
		}
		if (notificationType == MonitoredItemNotificationType.Changed) {
			changeMonitoredItem(monitoredItem);
		}
		if (notificationType == MonitoredItemNotificationType.Removed) {
			removeMonitoredItem(monitoredItem);
		}
		if (notificationType == MonitoredItemNotificationType.Failed) {
			System.out.println("MonitoredItem failed");
		}
		LOGGER.exiting(CLASS_NAME, "handleMonitoredItemNotification");
	}

	private void handleInventoryItemNotification(NotificationType notificationType, InventoryItem inventoryItem) {
		LOGGER.entering(CLASS_NAME, "handleMonitoredItemNotification",
				new Object[] { notificationType, inventoryItem });
		if (notificationType == InventoryItemNotificationType.Add) {
			addInventoryItem(inventoryItem);
		}
		if (notificationType == InventoryItemNotificationType.Removed) {
			removeInventoryItem(inventoryItem);
		}
		if (notificationType == InventoryItemNotificationType.Failed) {
			System.out.println("InventryItem failed");
		}
		LOGGER.exiting(CLASS_NAME, "handleMonitoredItemNotification");
	}

	private void addProperty(Property property) {
		LOGGER.entering(CLASS_NAME, "addProperty", property);
		PropertyTab tab = new PropertyTab(property);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);
		LOGGER.exiting(CLASS_NAME, "addProperty");
	}

	private void removeProperty(Property property) {
		LOGGER.entering(CLASS_NAME, "removeProperty", property);
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			PropertyTab tab = (PropertyTab) tabPane.getTabs().get(i);
			if (tab.getProperty().equals(property)) {
				tabPane.getTabs().remove(i);
				break;
			}
		}
		LOGGER.exiting(CLASS_NAME, "removeProperty");
	}

	private void addMonitoredItem(MonitoredItem monitoredItem) {
		LOGGER.entering(CLASS_NAME, "addMonitoredItem", monitoredItem);
		Property property = monitoredItem.owner();
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			PropertyTab tab = (PropertyTab) tabPane.getTabs().get(i);
			if (tab.getProperty().equals(property)) {
				tab.addMonitoredItem(monitoredItem);
				break;
			}
		}
		LOGGER.exiting(CLASS_NAME, "addMonitoredItem");
	}

	private void changeMonitoredItem(MonitoredItem monitoredItem) {
		LOGGER.entering(CLASS_NAME, "changeMonitoredItem", monitoredItem);
		Property property = monitoredItem.owner();
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			PropertyTab tab = (PropertyTab) tabPane.getTabs().get(i);
			if (tab.getProperty().equals(property)) {
				tab.changeMonitoredItem(monitoredItem);
				break;
			}
		}
		LOGGER.exiting(CLASS_NAME, "changeMonitoredItem");
	}

	private void removeMonitoredItem(MonitoredItem monitoredItem) {
		LOGGER.entering(CLASS_NAME, "removeMonitoredItem", monitoredItem);
		Property property = monitoredItem.owner();
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			PropertyTab tab = (PropertyTab) tabPane.getTabs().get(i);
			if (tab.getProperty().equals(property)) {
				tab.removeMonitoredItem(monitoredItem);
				break;
			}
		}
		LOGGER.exiting(CLASS_NAME, "removeMonitoredItem");
	}

	private void addInventoryItem(InventoryItem inventoryItem) {
		LOGGER.entering(CLASS_NAME, "addInventoryItem", inventoryItem);
		Property property = inventoryItem.owner();
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			PropertyTab tab = (PropertyTab) tabPane.getTabs().get(i);
			if (tab.getProperty().equals(property)) {
				tab.addInventoryItem(inventoryItem);
				break;
			}
		}
		LOGGER.exiting(CLASS_NAME, "addInventoryItem");
	}

	private void removeInventoryItem(InventoryItem inventoryItem) {
		LOGGER.entering(CLASS_NAME, "removeInventoryItem", inventoryItem);
		LOGGER.exiting(CLASS_NAME, "removeInventoryItem");
	}

}
