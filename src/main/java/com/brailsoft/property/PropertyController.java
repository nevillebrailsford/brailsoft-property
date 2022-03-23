package com.brailsoft.property;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.ChangeManager;
import com.brailsoft.base.Notification;
import com.brailsoft.base.NotificationCentre;
import com.brailsoft.base.NotificationListener;
import com.brailsoft.base.NotificationType;
import com.brailsoft.model.Property;
import com.brailsoft.model.PropertyNotificationType;
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
import javafx.stage.Stage;

public class PropertyController implements Initializable, NotificationListener {
	private static final String CLASS_NAME = PropertyController.class.getName();
	private static final Logger LOGGER = ApplicationConfiguration.logger();

	private PropertyManagement propertyManager;
//	private ApplicationPreferences applicationPreferences = ApplicationPreferences.getInstance();
//	private File rootDirectory = new File(applicationPreferences.getDirectory());
//	private LocalStorage localStorage = LocalStorage.getInstance(rootDirectory);
//	private PropertyMonitor propertyMonitor = PropertyMonitor.getInstance();

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

//	private ListChangeListener<? super Property> listener = new ListChangeListener<>() {
//		@Override
//		public void onChanged(Change<? extends Property> change) {
//			while (change.next()) {
//				if (change.wasAdded()) {
//					for (Property p : change.getAddedSubList()) {
//						PropertyTab tab = new PropertyTab(p);
//						tabPane.getTabs().add(tab);
//						tabPane.getSelectionModel().select(tab);
//					}
//				}
//				if (change.wasRemoved()) {
//					change.getRemoved().stream().forEach(p -> {
//						for (int i = 0; i < tabPane.getTabs().size(); i++) {
//							PropertyTab tab = (PropertyTab) tabPane.getTabs().get(i);
//							if (tab.getProperty().equals(p)) {
//								tabPane.getTabs().remove(i);
//								break;
//							}
//						}
//					});
//				}
//			}
//			updatePropertiesExist();
//		}
//	};

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		LOGGER.entering(CLASS_NAME, "initialize");
//		try {
//			StatusMonitor.getInstance(this);
//		} catch (Throwable t) {
//			LOGGER.warning("Caught exception: " + t.getMessage());
//			LOGGER.exiting(CLASS_NAME, "initialize");
//			Platform.exit();
//		}
//		propertyMonitor.addListener(listener);
//		propertyMonitor.startTimer();
//		try {
//			localStorage.loadStoredData();
//		} catch (IOException e) {
//			LOGGER.warning("Caught exception: " + e.getMessage());
//			if (e.getMessage().startsWith("LocalStorage: archiveFile") && e.getMessage().endsWith("not found")) {
//				LOGGER.fine("Ignoring exception");
//			} else {
//				LOGGER.fine("Unknown exception");
//				LOGGER.exiting(CLASS_NAME, "initialize");
//				Platform.exit();
//			}
//		}
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
//		Optional<MonitoredItem> result = new EventDialog().showAndWait();
//		if (result.isPresent()) {
//			MonitoredItem item = result.get();
//			Property property = getSelectedProperty();
//			item.setOwner(property);
//			AddMonitoredChange addMonitoredChange = new AddMonitoredChange(item);
//			ChangeManager.getInstance().execute(addMonitoredChange);
//		}
		LOGGER.exiting(CLASS_NAME, "addItem");
	}

	@FXML
	void deleteItem(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "deleteItem");
		Property property = getSelectedProperty();
//		Optional<MonitoredItem> result = new DeleteItemDialog(property).showAndWait();
//		if (result.isPresent()) {
//			MonitoredItem item = result.get();
//			RemoveMonitoredChange removeMonitoredChange = new RemoveMonitoredChange(item);
//			ChangeManager.getInstance().execute(removeMonitoredChange);
//		}
		LOGGER.exiting(CLASS_NAME, "deleteItem");
	}

	@FXML
	void addInventory(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "addInventory");
		Property property = getSelectedProperty();
//		Optional<InventoryItem> result = new AddInventoryDialog(property).showAndWait();
//		if (result.isPresent()) {
//			InventoryItem item = result.get();
//			item.setOwner(property);
//			AddInventoryChange addInventoryChange = new AddInventoryChange(item);
//			ChangeManager.getInstance().execute(addInventoryChange);
//		}
		LOGGER.exiting(CLASS_NAME, "addInventory");
	}

	@FXML
	void deleteInventory(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "deleteInventory");
		Property property = getSelectedProperty();
//		Optional<InventoryItem> result = new DeleteInventoryDialog(property).showAndWait();
//		if (result.isPresent()) {
//			InventoryItem item = result.get();
//			RemoveInventoryChange removeInventoryChange = new RemoveInventoryChange(item);
//			ChangeManager.getInstance().execute(removeInventoryChange);
//		}
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
//		try {
//			LoadProperty LoadProperty = PropertyManager.loadFXML("AllItems");
//			scene = new Scene(LoadProperty.getParent());
//		} catch (Exception e) {
//			LOGGER.warning("Caught exception: " + e.getMessage());
//			IllegalArgumentException exc = new IllegalArgumentException("PropertyController: " + e.getMessage());
//			LOGGER.throwing(CLASS_NAME, "viewAllItems", exc);
//			LOGGER.exiting(CLASS_NAME, "viewAllItems");
//			throw exc;
//		}
//		scene.getStylesheets().add(PropertyManager.class.getResource("PropertyManager.css").toExternalForm());
//		stage.initModality(Modality.APPLICATION_MODAL);
//		stage.setScene(scene);
//		stage.show();
		LOGGER.exiting(CLASS_NAME, "viewAllItems");
	}

	@FXML
	void viewOverdueItems(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "viewOverdueItems");
		Stage stage = new Stage();
		stage.setTitle("Overdue Items");
		Scene scene;
//		try {
//			LoadProperty LoadProperty = PropertyManager.loadFXML("OverdueItems");
//			scene = new Scene(LoadProperty.getParent());
//		} catch (Exception e) {
//			LOGGER.warning("Caught exception: " + e.getMessage());
//			IllegalArgumentException exc = new IllegalArgumentException("PropertyController: " + e.getMessage());
//			LOGGER.throwing(CLASS_NAME, "viewOverdueItems", exc);
//			LOGGER.exiting(CLASS_NAME, "viewOverdueItems");
//			throw exc;
//		}
//		scene.getStylesheets().add(PropertyManager.class.getResource("PropertyManager.css").toExternalForm());
//		stage.initModality(Modality.APPLICATION_MODAL);
//		stage.setScene(scene);
//		stage.show();
		LOGGER.exiting(CLASS_NAME, "viewOverdueItems");
	}

	@FXML
	void viewNotifiedItems(ActionEvent event) {
		LOGGER.entering(CLASS_NAME, "viewNotifiedItems");
		Stage stage = new Stage();
		stage.setTitle("Notified Items");
		Scene scene;
//		try {
//			LoadProperty LoadProperty = PropertyManager.loadFXML("NotifiedItems");
//			scene = new Scene(LoadProperty.getParent());
//		} catch (Exception e) {
//			LOGGER.warning("Caught exception: " + e.getMessage());
//			IllegalArgumentException exc = new IllegalArgumentException("PropertyController: " + e.getMessage());
//			LOGGER.throwing(CLASS_NAME, "viewNotifiedItems", exc);
//			LOGGER.exiting(CLASS_NAME, "viewNotifiedItems");
//			throw exc;
//		}
//		scene.getStylesheets().add(PropertyManager.class.getResource("PropertyManager.css").toExternalForm());
//		stage.initModality(Modality.APPLICATION_MODAL);
//		stage.setScene(scene);
//		stage.show();
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
//		Optional<PreferencesData> result = new PreferencesDialog().showAndWait();
//		if (result.isPresent()) {
//			String newDirectory = result.get().getDirectory();
//			Level loggingLevel = result.get().getLevel();
//			boolean notification = result.get().getEmailNotification();
//			String emails = result.get().getEmailList();
//			try {
//				if (!applicationPreferences.getLevel().equals(loggingLevel)) {
//					applicationPreferences.setLevel(loggingLevel);
//					PropertyManagerLogConfigurer.changeLevel(loggingLevel);
//				}
//				if (!applicationPreferences.getDirectory().equals(newDirectory)) {
//					applicationPreferences.setDirectory(newDirectory);
//					removeTabsFromView();
//					resetModelToEmpty();
//					LocalStorage.getInstance(new File(applicationPreferences.getDirectory())).loadStoredData();
//				}
//				applicationPreferences.setEmailNotification(notification);
//				applicationPreferences.setEMailList(emails);
//			} catch (BackingStoreException e) {
//				LOGGER.warning("Caught exception: " + e.getMessage());
//			} catch (IOException e) {
//				LOGGER.warning("Caught exception: " + e.getMessage());
//			}
//		}
		LOGGER.entering(CLASS_NAME, "preferences");
	}

	@FXML
	void viewCalendar() {
		LOGGER.entering(CLASS_NAME, "viewCalendar");
		Stage stage = new Stage();
		stage.setTitle("Calendar");
//		CalendarView mainRoot = new CalendarView();
//		Scene scene = new Scene(mainRoot, 35 + (53 * 14), 125);
//		stage.setScene(scene);
//		stage.show();

		LOGGER.exiting(CLASS_NAME, "viewCalendar");
	}

	private void resetModelToEmpty() {
		LOGGER.entering(CLASS_NAME, "resetModelToEmpty");
//		PropertyMonitor.getInstance().removeListener(listener);
//		PropertyMonitor.getInstance().clear();
//		PropertyMonitor.getInstance().addListener(listener);
		LOGGER.exiting(CLASS_NAME, "resetModelToEmpty");
	}

	private void removeTabsFromView() {
		LOGGER.entering(CLASS_NAME, "removeTabsFromView");
//		for (Tab t : tabPane.getTabs()) {
//			PropertyTab pt = (PropertyTab) t;
//			PropertyMonitor.getInstance().removeListener(pt.getItemListener(), pt.getProperty());
//			PropertyMonitor.getInstance().removeInventoryListener(pt.getInventoryListener(), pt.getProperty());
//		}
		tabPane.getTabs().clear();
		LOGGER.exiting(CLASS_NAME, "removeTabsFromView");
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
		NotificationType notificationType = notification.notificationType();

		if (notificationType.category().equals("property")) {
			Platform.runLater(() -> {
				System.out.println("got " + notification.notificationType().category());
				if (notificationType == PropertyNotificationType.Add) {
					addProperty(notification);
				}
				if (notificationType == PropertyNotificationType.Removed) {
					removeProperty(notification);
				}
				updatePropertiesExist();
			});
		}
	}

	private void addProperty(Notification notification) {
		Property p = (Property) notification.subject().get();
		PropertyTab tab = new PropertyTab(p);
		tabPane.getTabs().add(tab);
		tabPane.getSelectionModel().select(tab);
	}

	private void removeProperty(Notification notification) {
		Property p = (Property) notification.subject().get();
		for (int i = 0; i < tabPane.getTabs().size(); i++) {
			PropertyTab tab = (PropertyTab) tabPane.getTabs().get(i);
			if (tab.getProperty().equals(p)) {
				tabPane.getTabs().remove(i);
				break;
			}
		}

	}
}
