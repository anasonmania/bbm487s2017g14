package application;

import application.model.Book;
import application.model.User;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {
	public  Stage pStage;
	public static Connection con;
	public static User currentUser;
	public static User selectedUser;
	public static Book selectedBook;
	public static Image defaultPP;
	public static Image defaultBook;
	public static ImagePattern patDefaultPP;
	public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
	public static String userInput;
	public static File booksDir = new File("c:/books");
	public static File usersDir = new File("c:/libusers");
	public void start(Stage primaryStage) {
		try {
			GridPane e = (GridPane) FXMLLoader.load(this.getClass().getResource("view/Login.fxml"));
			Scene scene = new Scene(e, 1366, 768);
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			pStage = primaryStage;
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		File file = new File("D:/LBLS New/LBLS Desktop Application/resources/images/pp_default.png");
		defaultPP = new Image (file.toURI().toString());
		File file2 = new File("D:/LBLS New/LBLS Desktop Application/resources/images/book_default.png");
		defaultBook = new Image (file2.toURI().toString());
		patDefaultPP = new ImagePattern(defaultPP);
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb?autoReconnect=true&useSSL=false", "root", "admin");
		launch(args);
	}
}