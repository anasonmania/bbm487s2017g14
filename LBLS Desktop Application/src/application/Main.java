package application;

import application.model.Book;
import application.model.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
	private static Stage pStage;
	public static Connection con;
	public static User currentUser;
	public static User selectedUser;
	public static Book selectedBook;

	public void start(Stage primaryStage) {
		try {
			GridPane e = (GridPane) FXMLLoader.load(this.getClass().getResource("view/Login.fxml"));
			Scene scene = new Scene(e, 1366.0D, 768.0D);
			primaryStage.setScene(scene);
			pStage = primaryStage;
			primaryStage.show();
		} catch (Exception arg3) {
			arg3.printStackTrace();
		}

	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "admin");
		launch(args);
	}
}