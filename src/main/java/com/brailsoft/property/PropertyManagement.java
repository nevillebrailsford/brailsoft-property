package com.brailsoft.property;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Logger;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.IniFile;
import com.brailsoft.base.LogConfigurer;
import com.brailsoft.base.NotificationCentre;
import com.brailsoft.base.NotificationMonitor;
import com.brailsoft.base.ThreadServices;
import com.brailsoft.model.PropertyRead;
import com.brailsoft.property.gui.PreferencesData;
import com.brailsoft.property.gui.PreferencesDialog;
import com.brailsoft.storage.Archiver;
import com.brailsoft.storage.Storage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PropertyManagement extends Application {

	private static final String CLASS_NAME = PropertyManagement.class.getName();
	private static Logger LOGGER;

	private static final String DIR = "dir";
	private static final String NAME = "name";

	private static PropertyController mainController;

	@Override
	public void stop() throws Exception {
		LOGGER.entering(CLASS_NAME, "stop");
		super.stop();
		NotificationCentre.stop();
		ThreadServices.getInstance().executor().shutdown();
		LOGGER.exiting(CLASS_NAME, "stop");
		LogConfigurer.shutdown();
	}

	/**
	 * Load the fxml file and start the gui.
	 */
	@Override
	public void start(Stage primaryStage) {
		LOGGER.entering(CLASS_NAME, "start");
		if (startupTasksSucceeded()) {
			try {
				LoadProperty loadProperty = loadFXML(Constants.FXML_NAME);
				Scene scene = new Scene(loadProperty.getParent());
				scene.getStylesheets().add(getClass().getResource(Constants.CSS_NAME).toExternalForm());
				mainController = loadProperty.getLoader().getController();
				mainController.setPropertyManager(this);
				primaryStage.setScene(scene);
				primaryStage.setTitle(
						Constants.WINDOW_TITLE + ApplicationConfiguration.applicationDecsriptor().applicationName());
				primaryStage.setResizable(false);
				primaryStage.show();
				archiveExistingModel();
				loadExistingModel();
			} catch (Exception e) {
				System.out.println("start caught exception: " + e.getMessage());
				LOGGER.warning("start caught exception: " + e.getMessage());
				LOGGER.exiting(CLASS_NAME, "start");
				Platform.exit();
			}
		} else {
			Platform.exit();
		}
		LOGGER.exiting(CLASS_NAME, "start");
	}

	public void shutdown() {
		LOGGER.entering(CLASS_NAME, "shutdown");
		Platform.exit();
		LOGGER.exiting(CLASS_NAME, "shutdown");
	}

	/**
	 * This is the place where the brailsoft application environment is established.
	 * Normally, this would be done in main, but the method has been moved here to
	 * take advantage of javafx's parameter handling, which can only be done on an
	 * object.
	 */
	@Override
	public void init() throws Exception {
		super.init();
		Parameters parameters = getParameters();
		if (invalidParameters(parameters)) {
			System.out.println("Usage: java -jar jar_name <--name=application name> <--dir=base directory>");
			System.exit(0);
		}
		configureApplication(parameters);
		configureLogging();
		LOGGER = ApplicationConfiguration.logger();
		new NotificationMonitor(System.out);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private boolean startupTasksSucceeded() {
		LOGGER.entering(CLASS_NAME, "startupTasksSucceeded");
		boolean result = handleFirstUse();
		LOGGER.exiting(CLASS_NAME, "startupTasksSucceeded", result);
		return result;
	}

	private boolean handleFirstUse() {
		LOGGER.entering(CLASS_NAME, "handleFirstUse");
		boolean result = true;
		if (IniFile.value(Constants.HAS_BEEN_USED).trim().isEmpty()) {
			PreferencesData preferencesData = makeInitialChoices();
			if (preferencesData == null) {
				result = false;
			} else {
				IniFile.store(Constants.HAS_BEEN_USED, "true");
				IniFile.store(Constants.LOGGING_LEVEL, preferencesData.level().toString());
				IniFile.store(Constants.EMAIL_NOTIFICATION, Boolean.toString(preferencesData.emailNotification()));
				if (!preferencesData.emailList().trim().isEmpty()) {
					IniFile.store(Constants.EMAIL_LIST, preferencesData.emailList());
				}
			}
		}
		LOGGER.exiting(CLASS_NAME, "handleFirstUse", result);
		return result;
	}

	private PreferencesData makeInitialChoices() {
		LOGGER.entering(CLASS_NAME, "makeInitialChoices");
		PreferencesData data = null;
		Optional<PreferencesData> result = new PreferencesDialog().showAndWait();
		if (result.isPresent()) {
			data = result.get();
		}
		LOGGER.exiting(CLASS_NAME, "makeInitialChoices", data);
		return data;
	}

	public LoadProperty loadFXML(String fxml) throws Exception {
		LOGGER.entering(CLASS_NAME, "loadFXML", fxml);
		FXMLLoader loader = new FXMLLoader(PropertyManagement.class.getResource(fxml + ".fxml"));
		Parent root;
		try {
			root = loader.load();
		} catch (Exception e) {
			LOGGER.warning("LoadFXML caught exception: " + e.getMessage());
			LOGGER.throwing(CLASS_NAME, "LoadFXML", e);
			LOGGER.exiting(CLASS_NAME, "loadFXML");
			throw e;
		}
		LoadProperty LoadProperty = new LoadProperty(loader, root);
		LOGGER.exiting(CLASS_NAME, "loadFXML", LoadProperty);
		return LoadProperty;
	}

	private boolean invalidParameters(Parameters parameters) {
		return (tooMany(parameters) || mixedTypeOf(parameters) || wrongNameIn(parameters));
	}

	private boolean tooMany(Parameters parameters) {
		return parameters.getRaw().size() > 2;
	}

	private boolean mixedTypeOf(Parameters parameters) {
		return parameters.getRaw().size() != parameters.getNamed().size();
	}

	private boolean wrongNameIn(Parameters parameters) {
		boolean wrongName = false;
		Iterator<String> it = parameters.getNamed().keySet().iterator();
		while (it.hasNext()) {
			String name = it.next();
			if (!name.equals(NAME) && !name.equals(DIR)) {
				wrongName = true;
			}
		}
		return wrongName;
	}

	private void configureApplication(Parameters parameters) {
		PropertyDescriptor applicationDescriptor = null;
		if (parameters.getNamed().containsKey(NAME)) {
			String applicationName = parameters.getNamed().get(NAME);
			applicationDescriptor = new PropertyDescriptor(applicationName);
		} else {
			applicationDescriptor = new PropertyDescriptor();
		}
		if (parameters.getNamed().containsKey(DIR)) {
			String baseDirectory = parameters.getNamed().get(DIR);
			ApplicationConfiguration.registerApplication(applicationDescriptor, baseDirectory);
		} else {
			ApplicationConfiguration.registerApplication(applicationDescriptor, System.getProperty("user.home"));
		}
	}

	private void configureLogging() {
		LogConfigurer.setUp();
		LogConfigurer.changeLevel(ApplicationConfiguration.applicationDecsriptor().level());
	}

	private void archiveExistingModel() throws Exception {
		LOGGER.entering(CLASS_NAME, "archiveExistingModel");
		File modelDirectory = obtainModelDirectory();
		File dataFile = new File(modelDirectory, Constants.PROPERTY_FILE);
		if (dataFile.exists()) {
			try {
				Archiver.archive(dataFile);
			} catch (Exception e) {
				LOGGER.fine("Caught exception " + e.getMessage());
				LOGGER.throwing(CLASS_NAME, "archiveExistingModel", e);
				LOGGER.exiting(CLASS_NAME, "archiveExistingModel");
				throw e;
			}
		}
		LOGGER.exiting(CLASS_NAME, "archiveExistingModel");
	}

	private void loadExistingModel() {
		LOGGER.entering(CLASS_NAME, "loadExistingModel");
		PropertyRead propertyRead = new PropertyRead();
		File modelDirectory = obtainModelDirectory();
		File dataFile = new File(modelDirectory, Constants.PROPERTY_FILE);
		if (dataFile.exists()) {
			LOGGER.fine("Data file " + dataFile.getAbsolutePath() + " exists");
			propertyRead.setFileName(dataFile.getAbsolutePath());
			Storage storage = new Storage();
			try {
				storage.loadStoredData(propertyRead);
			} catch (IOException e) {
				LOGGER.fine("Caught exception " + e.getMessage());
			}
		} else {
			LOGGER.fine("Data file " + dataFile.getAbsolutePath() + " does not exist");
		}
		LOGGER.exiting(CLASS_NAME, "loadExistingModel");
	}

	private File obtainModelDirectory() {
		LOGGER.entering(CLASS_NAME, "obtainModelDirectory");
		File rootDirectory = ApplicationConfiguration.rootDirectory();
		File applicationDirectory = new File(rootDirectory,
				ApplicationConfiguration.applicationDecsriptor().applicationName());
		File modelDirectory = new File(applicationDirectory, Constants.MODEL);
		if (!modelDirectory.exists()) {
			LOGGER.fine("Model directory " + modelDirectory.getAbsolutePath() + " does not exist");
		} else {
			LOGGER.fine("Model directory " + modelDirectory.getAbsolutePath() + " does exist");
		}
		LOGGER.exiting(CLASS_NAME, "obtainModelDirectory", modelDirectory);
		return modelDirectory;
	}

}
