package application.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;

import application.Main;
import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Duration;

@SuppressWarnings("restriction")
public class L4_2EditBook implements Initializable {
	public GridPane paneHeader;
	public GridPane paneContent;
	public GridPane paneBottom;
	public TextField tfName;
	public TextField tfAuthor;
	public TextField tfPublisher;
	public TextField tfIsbn;
	public Label lbDeslength;
	public TextArea taDescription;
	private boolean imageLoaded;
	private Image image;
	public Button remove;
	private File imageFile;
	public ToggleButton btnAvailable;
	public Rectangle rIsbn;
	public Rectangle rName;
	public Rectangle rAuthor;
	public Rectangle rPublisher;
	public ImageView ivBookimg;
	private FadeTransition showHeader;
	private FadeTransition hideHeader;
	private FadeTransition showContent;
	private FadeTransition hideContent;
	private FadeTransition showBottom;
	private FadeTransition hideBottom;
	private Duration opacityFactor = Duration.millis(1000.0d);
	int checkDesStart;
	int remainChar;

	public void initialize(URL location, ResourceBundle resources) {

		taDescription.setOpacity(0.4d);
		taDescription.setTextFormatter(
				new TextFormatter<String>(change -> change.getControlNewText().length() <= 500 ? change : null));

		tfName.setText(Main.selectedBook.getName());
		tfAuthor.setText(Main.selectedBook.getAuthor());
		tfPublisher.setText(Main.selectedBook.getPublisher());
		taDescription.setText(Main.selectedBook.getDescription());
		tfIsbn.setText(Integer.toString(Main.selectedBook.getIsbn()));
		btnAvailable.setSelected(Main.selectedBook.isAvailable());

		if (Main.selectedBook.getImage() != null) {
			ivBookimg.setImage(Main.selectedBook.getImage());
			remove.setDisable(false);
			remove.setVisible(true);
		} else {
			remove.setDisable(true);
			remove.setVisible(false);
			ivBookimg.setImage(Main.defaultBook);
		}

		checkDesStart = 0;
		setOpacityAnims();
		showHeader.play();
		showContent.play();
		showBottom.play();
		imageLoaded = false;
	}

	public void removePic(ActionEvent event) throws SQLException {

		ivBookimg.setImage(Main.defaultBook);
		remove.setDisable(true);
		remove.setOpacity(0);
		imageLoaded = false;

		PreparedStatement statement = Main.con.prepareStatement("UPDATE book SET image = NULL WHERE id = ? ");
		statement.setInt(1, Main.selectedBook.getid());
		statement.executeUpdate();

	}

	public void addNewBook(ActionEvent event) throws SQLException, IOException {

		String name = tfName.getText();
		String author = tfAuthor.getText();
		String publisher = tfPublisher.getText();
		String description = taDescription.getText();

		description = description.replace("\n", "");

		boolean isAvailable;
		if (btnAvailable.isSelected()) {
			isAvailable = true;
		} else {
			isAvailable = false;
		}

		int isbn;
		PreparedStatement statement;
		if (imageLoaded) {
			FileInputStream insBook = new FileInputStream(imageFile.getPath());
			statement = Main.con.prepareStatement(
					"UPDATE book  SET isbn = ?, name = ?, author = ?, publisher = ?,  description = ?, isavailable = ?, "
					+ "image = ?, owner = ? "
					+ "WHERE id = ? ");
			if (!tfIsbn.getText().equals("")) {
				isbn = Integer.parseInt(tfIsbn.getText());
				statement.setInt(1, isbn);
			}

			statement.setString(2, name);
			statement.setString(3, author);
			statement.setString(4, publisher);
			statement.setString(5, description);
			statement.setBoolean(6, isAvailable);
			statement.setBlob(7, insBook);
			statement.setInt(8, -1);
			statement.setInt(9, Main.selectedBook.getid());
			statement.executeUpdate();
		} else {
			statement = Main.con.prepareStatement(
					"UPDATE book SET isbn = ?, name = ?, author = ?, publisher = ?,  description = ?, isavailable = ?, "
					+ " owner = ? "
					+ "WHERE id = ? "
					);
			if (!tfIsbn.getText().equals("")) {
				isbn = Integer.parseInt(tfIsbn.getText());
				statement.setInt(1, isbn);
			}

			statement.setString(2, name);
			statement.setString(3, author);
			statement.setString(4, publisher);
			statement.setString(5, description);
			statement.setBoolean(6, isAvailable);
			statement.setInt(7, -1);
			statement.setInt(8, Main.selectedBook.getid());
			statement.executeUpdate();
			statement.executeUpdate();
			System.out.println("nonphoto");
		}

		BorderPane borrowPane = (BorderPane) FXMLLoader.load(this.getClass().getResource("../view/DialogSuccess.fxml"));
		Scene borrowDialog = new Scene(borrowPane, 560, 240);
		Stage borrowStage = new Stage();
		borrowStage.setScene(borrowDialog);
		borrowStage.initStyle(StageStyle.UNDECORATED);
		borrowStage.initModality(Modality.APPLICATION_MODAL);
		borrowStage.showAndWait();

	}

	public void logout(ActionEvent event) throws IOException {
		Main.currentUser.logout();
		hideHeader.play();
		hideContent.play();
		hideBottom.play();
		Parent newParent = (Parent) FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
		final Scene newScreen = new Scene(newParent);
		final Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		Task<Void> sleeper = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					Thread.sleep((long) opacityFactor.toMillis() / 2);
				} catch (InterruptedException e) {
				}
				return null;
			}
		};
		sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				appStage.setScene(newScreen);
				appStage.show();
			}
		});
		new Thread(sleeper).start();
	}

	public void back(ActionEvent event) throws IOException {
		Parent newParent = (Parent) FXMLLoader.load(getClass().getResource("../view/HomePageLibrarian.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(newScreen);
		appStage.show();
	}

	public void setOpacityAnims() {
		showHeader = AnimationFabric.createOpacityAnim(paneHeader, 0.0d, 1.0d, opacityFactor.divide(2.0d));
		showContent = AnimationFabric.createOpacityAnim(paneContent, 0.0d, 1.0d, opacityFactor);
		showBottom = AnimationFabric.createOpacityAnim(paneBottom, 0.0d, 1.0d, opacityFactor.multiply(2.0d));
		hideHeader = AnimationFabric.createOpacityAnim(paneHeader, 1.0d, 0.0d, opacityFactor.multiply(2.0d));
		hideContent = AnimationFabric.createOpacityAnim(paneContent, 1.0d, 0.0d, opacityFactor);
		hideBottom = AnimationFabric.createOpacityAnim(paneBottom, 1.0d, 0.0d, opacityFactor.divide(2.0d));
	}

	public void handleAvailable(ActionEvent event) {
		if (!btnAvailable.isSelected()) {
			btnAvailable.setText("Not Available");
		} else {
			btnAvailable.setText("Available");
		}

		System.out.println(btnAvailable.isSelected());
	}

	public void selectImage() {
		LinkedList<String> extensions = new LinkedList<String>();
		extensions.add("*.jpg");
		extensions.add("*.png");
		extensions.add("*.jpeg");
		extensions.add("*.gif");
		System.out.println("imagefield clicked");
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", extensions));
		imageFile = fileChooser.showOpenDialog((Window) null);
		if (imageFile != null) {
			System.out.println(imageFile.getName());
		}

		try {
			BufferedImage ex = ImageIO.read(imageFile);
			System.out.println(ex.getWidth() + " " + ex.getHeight());
			image = SwingFXUtils.toFXImage(ex, (WritableImage) null);
			BackgroundImage bgImgBook = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
					BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
			Background bgBook = new Background(new BackgroundImage[] { bgImgBook });
			ivBookimg.setImage(image);
			imageLoaded = true;

		} catch (IOException arg5) {
			System.out.println("There is a problem with file");
		}

	}

	public void incImagefield() {
	}

	public void decImagefield() {

	}

	public void handleDescription() {
		taDescription.setOpacity(1.0d);

		FontMetrics fontMetrics = Toolkit.getToolkit().getFontLoader().getFontMetrics(taDescription.getFont());
		double inputWidth = 0.0d;
		if (checkDesStart < taDescription.getText().length()) {
			inputWidth = (double) fontMetrics
					.computeStringWidth(taDescription.getText(checkDesStart, taDescription.getText().length()));
		}

		if (inputWidth >= 580.0d) {
			taDescription.appendText("\n");
			checkDesStart = taDescription.getText().length() - 1;
		}

		remainChar = 500 - taDescription.getText().length();
		lbDeslength.setText("(" + remainChar + ")");
	}

	public void lineIsbn() {
		rIsbn.setOpacity(1.0d);
	}

	public void linePublisher() {
		rPublisher.setOpacity(1.0d);
	}

	public void lineName() {
		rName.setOpacity(1.0d);
	}

	public void lineAuthor() {
		rAuthor.setOpacity(1.0d);
	}
}