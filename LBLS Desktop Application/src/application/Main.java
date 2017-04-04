package application;



import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.view.*;
import application.model.DbProperties;
import application.model.User;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;


public class Main extends Application {


	private static Stage pStage;
	public static Connection con;
	public static User currentUser;
	@Override
	public void start(Stage primaryStage) {
		try {
			GridPane root = (GridPane)FXMLLoader.load(getClass().getResource("view/Login.fxml"));
			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("HomePage.fxml"));
			Scene scene = new Scene(root,1366,768);
			//Scene signUpSc = new Scene(signUp, 1366, 768);
			/*scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());*/
			primaryStage.setScene(scene);
			pStage = primaryStage;
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://" + DbProperties.host + ":" + DbProperties.port + "/" + DbProperties.dbname, DbProperties.username, DbProperties.password);

		PreparedStatement statement = con.prepareStatement("SELECT password FROM user WHERE username = 'anasonmania' ");
		ResultSet result = statement.executeQuery();

		launch(args);
	}
}