package ch.makery.address;

import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import ch.makery.address.model.Person;
import ch.makery.address.model.PersonListWrapper;
import ch.makery.address.view.BirthdayStatisticsController;
import ch.makery.address.view.PersonEditDialogController;
import ch.makery.address.view.PersonOverviewController;
import ch.makery.address.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {
	 private Stage primaryStage;
	 private BorderPane rootLayout;
	@Override
	public void start(Stage primaryStage) {
		 this.primaryStage = primaryStage;
	        this.primaryStage.setTitle("AddressApp");
	        this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png"));
	        initRootLayout();
	        showPersonOverview();
	}

	public void initRootLayout() {
		// TODO Auto-generated method stub
         try {
        	 FXMLLoader loader = new FXMLLoader();
             loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			 Scene scene = new Scene(rootLayout);
	            primaryStage.setScene(scene);
	            RootLayoutController controller = loader.getController();
	            controller.setMainApp(this);
	            primaryStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         File file = getPersonFilePath();
         if (file != null) {
             loadPersonDataFromFile(file);
         }
	}


	public void showPersonOverview() {
		// TODO Auto-generated method stub
          try {
        	  FXMLLoader loader = new FXMLLoader();
              loader.setLocation(MainApp.class.getResource("view/PersonOverview.fxml"));
			AnchorPane personOverview = (AnchorPane) loader.load();
			 rootLayout.setCenter(personOverview);
			   PersonOverviewController controller = loader.getController();
		        controller.setMainApp(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 public Stage getPrimaryStage() {
	        return primaryStage;
	    }

	public static void main(String[] args) {
		launch(args);
	}
	  private ObservableList<Person> personData = FXCollections.observableArrayList();
	  public MainApp() {
	        // Add some sample data
	        personData.add(new Person("Hans", "Muster"));
	        personData.add(new Person("Ruth", "Mueller"));
	        personData.add(new Person("Heinz", "Kurz"));
	        personData.add(new Person("Cornelia", "Meier"));
	        personData.add(new Person("Werner", "Meyer"));
	        personData.add(new Person("Lydia", "Kunz"));
	        personData.add(new Person("Anna", "Best"));
	        personData.add(new Person("Stefan", "Meier"));
	        personData.add(new Person("Martin", "Mueller"));
	    }
	  public ObservableList<Person> getPersonData() {
	        return personData;
	    }
	  public boolean showPersonEditDialog(Person person) {
		    try {
		        // Load the fxml file and create a new stage for the popup dialog.
		        FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(MainApp.class.getResource("view/PersonEditDialog.fxml"));
		        AnchorPane page = (AnchorPane) loader.load();

		        // Create the dialog Stage.
		        Stage dialogStage = new Stage();
		        dialogStage.setTitle("Edit Person");
		        dialogStage.initModality(Modality.WINDOW_MODAL);
		        dialogStage.initOwner(primaryStage);
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);

		        // Set the person into the controller.
		        PersonEditDialogController controller = loader.getController();
		        controller.setDialogStage(dialogStage);
		        controller.setPerson(person);

		        // Show the dialog and wait until the user closes it
		        dialogStage.showAndWait();

		        return controller.isOkClicked();
		    } catch (IOException e) {
		        e.printStackTrace();
		        return false;
		    }
	  }
	  public File getPersonFilePath() {
		    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		    String filePath = prefs.get("filePath", null);
		    if (filePath != null) {
		        return new File(filePath);
		    } else {
		        return null;
		    }
		}
	  public void setPersonFilePath(File file) {
		    Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
		    if (file != null) {
		        prefs.put("filePath", file.getPath());

		        // Update the stage title.
		        primaryStage.setTitle("AddressApp - " + file.getName());
		    } else {
		        prefs.remove("filePath");

		        // Update the stage title.
		        primaryStage.setTitle("AddressApp");
		    }
	  }
	  public void loadPersonDataFromFile(File file) {
		    try {
		        JAXBContext context = JAXBContext
		                .newInstance(PersonListWrapper.class);
		        Unmarshaller um = context.createUnmarshaller();

		        // Reading XML from the file and unmarshalling.
		        PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

		        personData.clear();
		        personData.addAll(wrapper.getPersons());

		        // Save the file path to the registry.
		        setPersonFilePath(file);

		    } catch (Exception e) { // catches ANY exception
		    	Alert alert = new Alert(AlertType.WARNING);     
	        	alert.setTitle("Warning");     
	        	alert.setHeaderText("No person to delete");     
	        	alert.setContentText("Please select a row");     
	        	alert.showAndWait();
		    }
	  }
	  public void savePersonDataToFile(File file) {
		    try {
		        JAXBContext context = JAXBContext
		        		.newInstance(PersonListWrapper.class);
		        Marshaller m = context.createMarshaller();
		        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		        // Wrapping our person data.
		        PersonListWrapper wrapper = new PersonListWrapper();
		        wrapper.setPersons(personData);

		        // Marshalling and saving XML to the file.
		        m.marshal(wrapper, file);

		        // Save the file path to the registry.
		        setPersonFilePath(file);
		    } catch (Exception e) { // catches ANY exception
		    	Alert alert = new Alert(AlertType.WARNING);     
	        	alert.setTitle("Warning");     
	        	alert.setHeaderText("No person to delete");     
	        	alert.setContentText("Please select a row");     
	        	alert.showAndWait();
		    }
	  }
	  public void showBirthdayStatistics() {
		    try {
		        // Load the fxml file and create a new stage for the popup.
		        FXMLLoader loader = new FXMLLoader();
		        loader.setLocation(MainApp.class.getResource("view/BirthdayStatistics.fxml"));
		        AnchorPane page = (AnchorPane) loader.load();
		        Stage dialogStage = new Stage();
		        dialogStage.setTitle("Birthday Statistics");
		        dialogStage.initModality(Modality.WINDOW_MODAL);
		        dialogStage.initOwner(primaryStage);
		        Scene scene = new Scene(page);
		        dialogStage.setScene(scene);

		        // Set the persons into the controller.
		        BirthdayStatisticsController controller = loader.getController();
		        controller.setPersonData(personData);

		        dialogStage.show();

		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	  }
}
