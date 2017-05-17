package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class HomePageCustomer implements Initializable {

	public TextField tfSearch;
	public GridPane lnSearch;
	public Label lbName, lbSchoolidentity;



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lnSearch.setStyle("-fx-opacity: 0.4");
		tfSearch.setPromptText("Search books");
		lbName.setText(Main.currentUser.getName() + " " + Main.currentUser.getSurname());
		lbSchoolidentity.setText(Integer.toString(Main.currentUser.getSchoolNumber()));

	}

	public void logout(ActionEvent event) throws IOException{
		Main.currentUser.logout();
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
