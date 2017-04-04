package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HomePageLibrarian implements Initializable {

	public TextField tfSearch;
	public GridPane lnSearch;



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lnSearch.setStyle("-fx-opacity: 0.4");
		tfSearch.setPromptText("Search books");

	}

	public void logout(ActionEvent event) throws IOException{
		Parent newParent = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(newScreen);
		appStage.show();
	}

	public void lineSearch() {
		lnSearch.setStyle("-fx-opacity: 1.0;");
	}

}
