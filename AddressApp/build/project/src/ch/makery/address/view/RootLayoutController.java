package ch.makery.address.view;

import java.io.File;

import ch.makery.address.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class RootLayoutController {
	   private MainApp mainApp;
	   public void setMainApp(MainApp mainApp) {
	        this.mainApp = mainApp;
	    }
	   @FXML
	    private void handleNew() {
	        mainApp.getPersonData().clear();
	        mainApp.setPersonFilePath(null);
	    }
	   @FXML
	    private void handleOpen() {
	        FileChooser fileChooser = new FileChooser();

	        // Set extension filter
	        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
	                "XML files (*.xml)", "*.xml");
	        fileChooser.getExtensionFilters().add(extFilter);
	        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

	        if (file != null) {
	            mainApp.loadPersonDataFromFile(file);
	        }
	   }
	   
	   @FXML
	    private void handleSave() {
	        File personFile = mainApp.getPersonFilePath();
	        if (personFile != null) {
	            mainApp.savePersonDataToFile(personFile);
	        } else {
	            handleSaveAs();
	        }  
	   }
	   @FXML
	private void handleSaveAs() {
		// TODO Auto-generated method stub
		FileChooser fileChooser = new FileChooser();
		  FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				  "XML files (*.xml)", "*.xml");
		  fileChooser.getExtensionFilters().add(extFilter);
	      File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
	      if (file != null) {
	            // Make sure it has the correct extension
	            if (!file.getPath().endsWith(".xml")) {
	                file = new File(file.getPath() + ".xml");
	            }
	            mainApp.savePersonDataToFile(file);
	        }
	}
	  @FXML
	    private void handleAbout() {
		Alert alert = new Alert(AlertType.WARNING);     
      	alert.setTitle("Warning");     
      	alert.setHeaderText("No person to delete");     
      	alert.setContentText("Please select a row");     
      	alert.showAndWait();
	    }
	  @FXML
	    private void handleExit() {
	        System.exit(0);
	    }
	  @FXML
	  private void handleShowBirthdayStatistics() {
	    mainApp.showBirthdayStatistics();
	  }
	  
	  
}
