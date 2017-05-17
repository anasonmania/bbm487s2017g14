package application.controller;

import application.Main;
import application.controller.AnimationFabric;
import application.controller.DBFormatController;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.Timeline;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import javax.imageio.ImageIO;

public class L1AddUser implements Initializable {
	public GridPane paneHeader;
	public GridPane paneContent;
	public GridPane paneBottom;
	public TextField tfUsername;
	public TextField tfSchoolNumber;
	public TextField tfEmail;
	public TextField tfName;
	public TextField tfSurname;
	public TextField tfPhoneNumber;
	public Label warnUsername;
	public Label warnPassword;
	public Label warnSchoolnumber;
	public Label warnEmail;
	public PasswordField pfPassword;
	public DatePicker dpBirthday;
	public Label lbDeslength;
	public Pane imageUser;
	private boolean imageLoaded;
	private Image image;
	private File imageFile;
	public Rectangle rUsername;
	public Rectangle rPassword;
	public Rectangle rSchoolnumber;
	public Rectangle rEmail;
	public Rectangle rName;
	public Rectangle rSurname;
	public Rectangle rBirthdate;
	public Rectangle rPhonenumber;
	private boolean failControl;
	private boolean failUsername;
	private boolean failPassword;
	private boolean failSchoolnumber;
	private boolean failEmail;
	private Timeline tmDown;
	private Timeline tmUp;
	private FadeTransition showUsernameWarning;
	private FadeTransition hideUsernameWarning;
	private FadeTransition showPasswordWarning;
	private FadeTransition hidePasswordWarning;
	private FadeTransition showSchoolnumberWarning;
	private FadeTransition hideSchoolnumberWarning;
	private FadeTransition showEmailWarning;
	private FadeTransition hideEmailWarning;
	private FadeTransition showContent;
	private FadeTransition hideContent;
	private FadeTransition showHeader;
	private FadeTransition hideHeader;
	private FadeTransition showBottom;
	private FadeTransition hideBottom;
	private FadeTransition showSuccessMsg;
	private FadeTransition showSuccessImg;
	private FillTransition usernameToRed;
	private FillTransition usernameToGrey;
	private FillTransition passwordToRed;
	private FillTransition passwordToGrey;
	private FillTransition schoolnumberToRed;
	private FillTransition schoolnumberToGrey;
	private FillTransition emailToRed;
	private FillTransition emailToGrey;
	private Duration opacityFactor = Duration.millis(1000.0D);
	private Pattern pattern;
	private Matcher matcher;
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	int checkDesStart;
	int remainChar;

	public void initialize(URL location, ResourceBundle resources) {
		tfUsername.setPromptText("Username");
		tfSchoolNumber.setPromptText("School Number");
		tfEmail.setPromptText("E-mail");
		tfName.setPromptText("Name");
		tfSurname.setPromptText("Surname");
		tfPhoneNumber.setPromptText("Phone Number");
		pfPassword.setPromptText("Password");
		dpBirthday.setPromptText("Date of birth");
		failControl = false;
		failUsername = false;
		failPassword = false;
		failSchoolnumber = false;
		failEmail = false;
		imageLoaded = false;
		pattern = Pattern
				.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		setPositionAnims();
		setColorAnims();
		setOpacityAnims();
		showHeader.play();
		showContent.play();
		showBottom.play();
	}

	public void addNewUser(ActionEvent event) throws SQLException, NoSuchAlgorithmException, IOException {
		if (!checkInputs()) {
			HashSet<Integer> ids = new HashSet<Integer>();
			PreparedStatement idsQuery = Main.con.prepareStatement("SELECT iduserinfo FROM user ");
			ResultSet result = idsQuery.executeQuery();

			while (result.next()) {
				ids.add(Integer.valueOf(Integer.parseInt(result.getString(1))));
			}

			int iduserinfo;
			int schoolnumber;
			String username;
			String password;
			String email;
			String name;
			String surname;
			String phonenumber;
			LocalDate birthdate;
			PreparedStatement statement;
			Random generator;
			if (imageLoaded) {
				FileInputStream insUser = new FileInputStream(imageFile.getPath());
				statement = Main.con.prepareStatement(
						"INSERT INTO user (`iduserinfo`, `username`, `password`, `schoolnumber`, `email`, `name`, `surname`, `birthdate`, `phonenumber`, `profilepic`, `islibrarian`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
				generator = new Random();

				for (iduserinfo = generator.nextInt(99999999) + 1; ids
						.contains(Integer.valueOf(iduserinfo)); iduserinfo = generator.nextInt(99999999) + 1) {
					;
				}

				username = tfUsername.getText();
				password = DBFormatController.passToDatabase(pfPassword.getText());
				if (!tfSchoolNumber.getText().equals("")) {
					schoolnumber = Integer.parseInt(tfSchoolNumber.getText());
					statement.setInt(4, schoolnumber);
				}

				email = tfEmail.getText();
				name = tfName.getText();
				surname = tfSurname.getText();
				phonenumber = tfPhoneNumber.getText();
				birthdate = (LocalDate) dpBirthday.getValue();
				System.out.println(birthdate);
				statement.setInt(1, iduserinfo);
				statement.setString(2, username);
				statement.setString(3, password);
				statement.setString(5, email);
				statement.setString(6, name);
				statement.setString(7, surname);
				statement.setDate(8, DBFormatController.dateToDatabase(birthdate));
				statement.setString(9, phonenumber);
				statement.setBlob(10, insUser);
				statement.setInt(11, 0);
				statement.execute();
			} else {
				statement = Main.con.prepareStatement(
						"INSERT INTO user (`iduserinfo`, `username`, `password`, `schoolnumber`, `email`, `name`, `surname`, `birthdate`, `phonenumber`, `islibrarian`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
				generator = new Random();

				for (iduserinfo = generator.nextInt(99999999) + 1; ids
						.contains(Integer.valueOf(iduserinfo)); iduserinfo = generator.nextInt(99999999) + 1) {
					;
				}

				username = tfUsername.getText();
				password = DBFormatController.passToDatabase(pfPassword.getText());
				if (!tfSchoolNumber.getText().equals("")) {
					schoolnumber = Integer.parseInt(tfSchoolNumber.getText());
					statement.setInt(4, schoolnumber);
				}

				email = tfEmail.getText();
				name = tfName.getText();
				surname = tfSurname.getText();
				phonenumber = tfPhoneNumber.getText();
				birthdate = (LocalDate) dpBirthday.getValue();
				statement.setInt(1, iduserinfo);
				statement.setString(2, username);
				statement.setString(3, password);
				statement.setString(5, email);
				statement.setString(6, name);
				statement.setString(7, surname);
				statement.setDate(8, DBFormatController.dateToDatabase(birthdate));
				statement.setString(9, phonenumber);
				statement.setInt(10, 0);
				statement.execute();
			}

			BorderPane borrowPane = (BorderPane) FXMLLoader.load(this.getClass().getResource("../view/DialogSuccess.fxml"));
			Scene borrowDialog = new Scene(borrowPane, 560, 240);
			Stage borrowStage = new Stage();
			borrowStage.setScene(borrowDialog);
			borrowStage.initStyle(StageStyle.UNDECORATED);
			borrowStage.initModality(Modality.APPLICATION_MODAL);
			borrowStage.showAndWait();
		}

	}

	public void setOpacityAnims() {
		showHeader = AnimationFabric.createOpacityAnim(paneHeader, 0.0D, 1.0D,
				opacityFactor.divide(2.0D));
		showContent = AnimationFabric.createOpacityAnim(paneContent, 0.0D, 1.0D, opacityFactor);
		showBottom = AnimationFabric.createOpacityAnim(paneBottom, 0.0D, 1.0D,
				opacityFactor.multiply(2.0D));
		hideHeader = AnimationFabric.createOpacityAnim(paneHeader, 1.0D, 0.0D,
				opacityFactor.multiply(2.0D));
		hideContent = AnimationFabric.createOpacityAnim(paneContent, 1.0D, 0.0D, opacityFactor);
		hideBottom = AnimationFabric.createOpacityAnim(paneBottom, 1.0D, 0.0D,
				opacityFactor.divide(2.0D));
		showUsernameWarning = AnimationFabric.createOpacityAnim(warnUsername, 0.0D, 1.0D, opacityFactor);
		hideUsernameWarning = AnimationFabric.createOpacityAnim(warnUsername, 1.0D, 0.0D, opacityFactor);
		showPasswordWarning = AnimationFabric.createOpacityAnim(warnPassword, 0.0D, 1.0D, opacityFactor);
		hidePasswordWarning = AnimationFabric.createOpacityAnim(warnPassword, 1.0D, 0.0D, opacityFactor);
		showSchoolnumberWarning = AnimationFabric.createOpacityAnim(warnSchoolnumber, 0.0D, 1.0D,
				opacityFactor);
		hideSchoolnumberWarning = AnimationFabric.createOpacityAnim(warnSchoolnumber, 1.0D, 0.0D,
				opacityFactor);
		showEmailWarning = AnimationFabric.createOpacityAnim(warnEmail, 0.0D, 1.0D, opacityFactor);
		hideEmailWarning = AnimationFabric.createOpacityAnim(warnEmail, 1.0D, 0.0D, opacityFactor);
	}

	public void selectImage() {
		LinkedList<String> extensions = new LinkedList<String>();
		extensions.add("*.jpg");
		extensions.add("*.png");
		extensions.add("*.jpeg");
		extensions.add("*.gif");
		System.out.println("imagefield clicked");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(Main.usersDir);
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
			imageUser.setBackground(bgBook);
			imageLoaded = true;
			imageUser.setOpacity(1.0D);
		} catch (IOException arg5) {
			System.out.println("There is a problem with file");
		}

	}

	private boolean checkInputs() throws SQLException {
		HashSet<String> users = new HashSet<String>();
		PreparedStatement usersQuery = Main.con.prepareStatement("SELECT username FROM user ");
		ResultSet result = usersQuery.executeQuery();

		while (result.next()) {
			users.add(result.getString(1));
		}

		if (tfUsername.getText().equals("")) {
			warnUsername.setText("Please enter a username");
			usernameToRed.play();
			showUsernameWarning.play();
			failUsername = true;
			failControl = true;
		} else if (users.contains(tfUsername.getText())) {
			warnUsername.setText("This username is already taken");
			usernameToRed.play();
			showUsernameWarning.play();
			failUsername = true;
			failControl = true;
		}

		if (pfPassword.getText().equals("")) {
			warnPassword.setText("Please enter a password");
			passwordToRed.play();
			showPasswordWarning.play();
			failPassword = true;
			failControl = true;
		}

		HashSet<Integer> schoolNums = new HashSet<Integer>();
		PreparedStatement schoolNumsQuery = Main.con.prepareStatement("SELECT schoolnumber FROM user ");
		result = schoolNumsQuery.executeQuery();

		while (result.next()) {
			schoolNums.add(Integer.valueOf(Integer.parseInt(result.getString(1))));
		}

		if (tfSchoolNumber.getText().equals("")) {
			warnSchoolnumber.setText("Please enter a schoolnumber");
			schoolnumberToRed.play();
			showSchoolnumberWarning.play();
			failSchoolnumber = true;
			failControl = true;
		} else if (schoolNums.contains(Integer.valueOf(Integer.parseInt(tfSchoolNumber.getText())))) {
			warnSchoolnumber.setText("This schoolnumber is already defined for an account");
			schoolnumberToRed.play();
			showSchoolnumberWarning.play();
			failSchoolnumber = true;
			failControl = true;
		}

		HashSet<String> emails = new HashSet<String>();
		PreparedStatement emailsQuery = Main.con.prepareStatement("SELECT email FROM user ");
		result = emailsQuery.executeQuery();

		while (result.next()) {
			emails.add(result.getString(1));
		}

		if (tfEmail.getText().equals("")) {
			warnEmail.setText("Please enter an e-mail adress");
			emailToRed.play();
			showEmailWarning.play();
			failEmail = true;
			failControl = true;
		} else {
			matcher = pattern.matcher(tfEmail.getText());
			if (!matcher.matches()) {
				warnEmail.setText("E-mail is not valid formatted");
				emailToRed.play();
				showEmailWarning.play();
				failEmail = true;
				failControl = true;
			} else if (emails.contains(tfEmail.getText())) {
				warnEmail.setText("This e-mail is already defined for an account");
				emailToRed.play();
				showEmailWarning.play();
				failEmail = true;
				failControl = true;
			}
		}

		Iterator<Integer> arg8 = schoolNums.iterator();

		while (arg8.hasNext()) {
			Integer x = (Integer) arg8.next();
			System.out.println(x);
		}

		if (failControl) {
			tmDown.play();
		}

		return failControl;
	}

	public void incImagefield() {
		imageUser.setOpacity(1.0D);
	}

	public void decImagefield() {
		if (!imageLoaded) {
			imageUser.setOpacity(0.4D);
		}

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
                    Thread.sleep((long) opacityFactor.toMillis()/2);
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

	public void setPositionAnims() {
		tmDown = new Timeline();
		tmUp = new Timeline();
		LinkedList<Node> formElements = new LinkedList<Node>();
		formElements.add(rUsername);
		formElements.add(tfUsername);
		formElements.add(rPassword);
		formElements.add(pfPassword);
		formElements.add(rSchoolnumber);
		formElements.add(tfSchoolNumber);
		formElements.add(rEmail);
		formElements.add(tfEmail);
		formElements.add(rName);
		formElements.add(tfName);
		formElements.add(rSurname);
		formElements.add(tfSurname);
		formElements.add(rBirthdate);
		formElements.add(dpBirthday);
		formElements.add(rPhonenumber);
		formElements.add(tfPhoneNumber);
		tmDown = AnimationFabric.createPositionAnim(formElements, -10, 0);
		tmUp = AnimationFabric.createPositionAnim(formElements, 0, -10);
	}

	public void setColorAnims() {
		usernameToRed = new FillTransition(Duration.millis(300.0D), rUsername, Color.valueOf("#727986"),
				Color.valueOf("#d41923"));
		usernameToGrey = new FillTransition(Duration.millis(300.0D), rUsername, Color.valueOf("#d41923"),
				Color.valueOf("#727986"));
		passwordToRed = new FillTransition(Duration.millis(300.0D), rPassword, Color.valueOf("#727986"),
				Color.valueOf("#d41923"));
		passwordToGrey = new FillTransition(Duration.millis(300.0D), rPassword, Color.valueOf("#d41923"),
				Color.valueOf("#727986"));
		schoolnumberToRed = new FillTransition(Duration.millis(300.0D), rSchoolnumber,
				Color.valueOf("#727986"), Color.valueOf("#d41923"));
		schoolnumberToGrey = new FillTransition(Duration.millis(300.0D), rSchoolnumber,
				Color.valueOf("#d41923"), Color.valueOf("#727986"));
		emailToRed = new FillTransition(Duration.millis(300.0D), rEmail, Color.valueOf("#727986"),
				Color.valueOf("#d41923"));
		emailToGrey = new FillTransition(Duration.millis(300.0D), rEmail, Color.valueOf("#d41923"),
				Color.valueOf("#727986"));
	}

	public void rollbackAnims() {
		tmUp.play();
		if (failUsername) {
			usernameToGrey.play();
			hideUsernameWarning.play();
			failUsername = false;
		}

		if (failPassword) {
			passwordToGrey.play();
			hidePasswordWarning.play();
			failPassword = false;
		}

		if (failSchoolnumber) {
			schoolnumberToGrey.play();
			hideSchoolnumberWarning.play();
			failSchoolnumber = false;
		}

		if (failEmail) {
			emailToGrey.play();
			hideEmailWarning.play();
			failEmail = false;
		}

		failControl = false;
	}

	public void lineUsername() {
		rUsername.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}

	public void linePassword() {
		rPassword.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}

	public void lineSchoolNumber() {
		rSchoolnumber.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}

	public void lineEmail() {
		rEmail.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}

	public void lineName() {
		rName.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}

	public void lineSurname() {
		rSurname.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}

	public void lineBirthday() {
		rBirthdate.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}

	public void linePhoneNumber() {
		rPhonenumber.setOpacity(1.0D);
		if (failControl) {
			rollbackAnims();
		}

	}
}