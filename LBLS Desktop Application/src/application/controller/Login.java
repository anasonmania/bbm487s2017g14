package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.Main;
import application.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Login {

	public Button createAccount;
	public Button login;
	public TextField tfUsername;
	public PasswordField pfPassword;
	public GridPane lnUsername, lnPassword;
	public Boolean userLogin;

	public void handleLogin(ActionEvent event) throws SQLException, IOException {
		String inputUsername, inputUserpass;
		inputUsername = tfUsername.getText();
		inputUserpass = pfPassword.getText();
		PreparedStatement statement = Main.con
				.prepareStatement("SELECT password FROM user WHERE username = '" + inputUsername + "' ");

		ResultSet result = statement.executeQuery();
		String temp = null;

		while (result.next()) {
			temp = result.getString(1);
		}

		if (temp != null) {
			if (temp.equals(inputUserpass)) {
				statement = Main.con
						.prepareStatement("SELECT * from user WHERE username = '" + inputUsername + "' ");
				result = statement.executeQuery();
				while(result.next()){
					Main.currentUser = new User(result.getString("username"), result.getString("email"), result.getString("name"),
							result.getString("surname"), result.getString("phonenumber"), result.getDate("birthdate"),
							result.getInt("iduserinfo"), result.getInt("schoolnumber"), result.getInt("islibrarian"));

				}

				int islabrarian = Main.currentUser.getIsLabrarian();

				if(islabrarian > 0){
					Parent newParent;
					newParent = FXMLLoader.load(getClass().getResource("../view/HomePageLibrarian.fxml"));
					Scene newScreen = new Scene(newParent);
					Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					appStage.setScene(newScreen);
					appStage.show();
				}
				else{
					Parent newParent;
					newParent = FXMLLoader.load(getClass().getResource("../view/HomePageCustomer.fxml"));
					Scene newScreen = new Scene(newParent);
					Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					appStage.setScene(newScreen);
					appStage.show();
				}


				System.out.println(Main.currentUser.getBirthDate());
				System.out.println(Main.currentUser.getIsLabrarian());
			}
		}

		System.out.println("login");
	}

	public void handleCreateAccount(ActionEvent event) throws IOException {

		Parent newParent = FXMLLoader.load(getClass().getResource("../view/CreateAccount.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(newScreen);
		appStage.show();

	}

	public void lineUsername() {
		lnUsername.setStyle("-fx-opacity: 1.0;");
	}

	public void linePassword() {
		lnPassword.setStyle("-fx-opacity: 1.0;");
	}

}