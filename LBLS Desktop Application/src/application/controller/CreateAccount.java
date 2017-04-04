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
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CreateAccount implements Initializable{

	public TextField tfUsername;
	public TextField tfSchoolNumber;
	public TextField tfEmail;
	public TextField tfName;
	public TextField tfSurname;
	public TextField tfPhoneNumber;
	public PasswordField pfPassword;
	public DatePicker dpBirthday;

	public GridPane lnUsername, lnPassword, lnSchoolNumber, lnEmail, lnName, lnSurname, lnPhoneNumber, lnBirthday;




	@Override
	public void initialize (URL location, ResourceBundle resources){
		tfUsername.setPromptText("Username");
		tfSchoolNumber.setPromptText("School Number");
		tfEmail.setPromptText("E-mail");
		tfName.setPromptText("Name");
		tfSurname.setPromptText("Surname");
		tfPhoneNumber.setPromptText("Phone Number");
		pfPassword.setPromptText("Password");
		dpBirthday.setPromptText("Date of birth");
	}

	public void createAccount(){

	}

	public void back(ActionEvent event) throws IOException{
		Parent newParent = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(newScreen);
		appStage.show();
	}

	public void lineUsername(){
		lnUsername.setStyle("-fx-opacity: 1.0;");
	}
	public void linePassword(){
		lnPassword.setStyle("-fx-opacity: 1.0;");
	}
	public void lineSchoolNumber(){
		lnSchoolNumber.setStyle("-fx-opacity: 1.0;");
	}
	public void lineEmail(){
		lnEmail.setStyle("-fx-opacity: 1.0;");
	}
	public void lineName(){
		lnName.setStyle("-fx-opacity: 1.0;");
	}
	public void lineSurname(){
		lnSurname.setStyle("-fx-opacity: 1.0;");
	}
	public void lineBirthday(){
		lnBirthday.setStyle("-fx-opacity: 1.0;");
	}
	public void linePhoneNumber(){
		lnPhoneNumber.setStyle("-fx-opacity: 1.0;");
	}



}
