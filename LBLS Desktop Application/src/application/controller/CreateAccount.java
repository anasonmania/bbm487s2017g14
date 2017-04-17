package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import application.model.DbProperties;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CreateAccount implements Initializable {

	/* gui elements */
	public TextField tfUsername;
	public TextField tfSchoolNumber;
	public TextField tfEmail;
	public TextField tfName;
	public TextField tfSurname;
	public TextField tfPhoneNumber;
	public PasswordField pfPassword;
	public DatePicker dpBirthday;

	public Label warnUsername, warnPassword, warnSchoolnumber, warnEmail;

	public Rectangle rUsername, rPassword, rSchoolnumber, rEmail, rName, rSurname, rBirthdate, rPhonenumber;

	/* animation controllers */
	private boolean failControl;
	private boolean failUsername;
	private boolean failPassword;
	private boolean failSchoolnumber;
	private boolean failEmail;

	/* animation variables */
	private Timeline tmDown;
	private Timeline tmUp;

	private FillTransition usernameToRed;
	private FillTransition usernameToGrey;
	private FillTransition passwordToRed;
	private FillTransition passwordToGrey;
	private FillTransition schoolnumberToRed;
	private FillTransition schoolnumberToGrey;
	private FillTransition emailToRed;
	private FillTransition emailToGrey;

	private FadeTransition showUsernameWarning;
	private FadeTransition hideUsernameWarning;
	private FadeTransition showPasswordWarning;
	private FadeTransition hidePasswordWarning;
	private FadeTransition showSchoolnumberWarning;
	private FadeTransition hideSchoolnumberWarning;
	private FadeTransition showEmailWarning;
	private FadeTransition hideEmailWarning;

	/* regex for email checking */
	private Pattern pattern;
	private Matcher matcher;
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	@Override
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

		setPositionAnims();
		setColorAnims();
		setOpacityAnims();

		pattern = Pattern.compile(EMAIL_PATTERN);

	}

	public void createAccount(ActionEvent event) throws SQLException {

		int iduserinfo, schoolnumber;
		String username, password, email, name, surname, phonenumber;
		LocalDate birthdate;

		/* Generate id, compare with database table ids */
		HashSet<Integer> ids = new HashSet<Integer>();
		PreparedStatement idsQuery = Main.con.prepareStatement("SELECT iduserinfo FROM user ");
		ResultSet result = idsQuery.executeQuery();
		while (result.next()) {
			ids.add(Integer.parseInt(result.getString(1)));
		}

		if (!checkInputs()) {
			PreparedStatement statement = Main.con
					.prepareStatement("INSERT INTO user (`iduserinfo`, `username`, `password`, `schoolnumber`, "
							+ "`email`, `name`, `surname`, `birthdate`, `phonenumber`, `islibrarian`) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");

			Random generator = new Random();
			iduserinfo = generator.nextInt(99999999) + 1;
			while (ids.contains(iduserinfo)) {
				iduserinfo = generator.nextInt(99999999) + 1;
			}

			username = tfUsername.getText();
			password = pfPassword.getText();
			if (!tfSchoolNumber.getText().equals("")) {
				schoolnumber = Integer.parseInt(tfSchoolNumber.getText());
				statement.setInt(4, schoolnumber);
			}
			email = tfEmail.getText();
			name = tfName.getText();
			surname = tfSurname.getText();
			phonenumber = tfPhoneNumber.getText();
			birthdate = dpBirthday.getValue();

			System.out.println(birthdate);

			statement.setInt(1, iduserinfo);
			statement.setString(2, username);
			statement.setString(3, password);
			statement.setString(5, email);
			statement.setString(6, name);
			statement.setString(7, surname);
			statement.setDate(8, null);
			statement.setString(9, phonenumber);
			statement.setInt(10, 0);
			statement.execute();
		}
	}

	private boolean checkInputs() throws SQLException {
		/* Check username */
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
		} else {

			if (users.contains(tfUsername.getText())) {
				warnUsername.setText("This username is already taken");
				usernameToRed.play();
				showUsernameWarning.play();
				failUsername = true;
				failControl = true;
			}
		}

		/* Check password */
		if (pfPassword.getText().equals("")) {
			warnPassword.setText("Please enter a password");
			passwordToRed.play();
			showPasswordWarning.play();
			failPassword = true;
			failControl = true;
		}

		/* Check schoolnumber */
		HashSet<Integer> schoolNums = new HashSet<Integer>();
		PreparedStatement schoolNumsQuery = Main.con.prepareStatement("SELECT schoolnumber FROM user ");
		result = schoolNumsQuery.executeQuery();
		while (result.next()) {
			schoolNums.add(Integer.parseInt(result.getString(1)));
		}

		if (tfSchoolNumber.getText().equals("")) {
			warnSchoolnumber.setText("Please enter a schoolnumber");
			schoolnumberToRed.play();
			showSchoolnumberWarning.play();
			failSchoolnumber = true;
			failControl = true;
		} else {
			if (schoolNums.contains(Integer.parseInt(tfSchoolNumber.getText()))) {
				warnSchoolnumber.setText("This schoolnumber is already defined for an account");
				schoolnumberToRed.play();
				showSchoolnumberWarning.play();
				failSchoolnumber = true;
				failControl = true;
			}
		}

		/* Check email */

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

		for (Integer x : schoolNums)
			System.out.println(x);

		if (failControl)
			tmDown.play();

		return failControl;
	}

	public void back(ActionEvent event) throws IOException {
		Parent newParent = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(newScreen);
		appStage.show();
	}

	public void lineUsername() {
		rUsername.setOpacity(1);
		if (failControl)
			rollbackAnims();

	}

	public void linePassword() {
		rPassword.setOpacity(1);
		if (failControl)
			rollbackAnims();
	}

	public void lineSchoolNumber() {
		rSchoolnumber.setOpacity(1);
		if (failControl)
			rollbackAnims();
	}

	public void lineEmail() {
		rEmail.setOpacity(1);
		if (failControl)
			rollbackAnims();
	}

	public void lineName() {
		rName.setOpacity(1);
		if (failControl)
			rollbackAnims();
	}

	public void lineSurname() {
		rSurname.setOpacity(1);
		if (failControl)
			rollbackAnims();

	}

	public void lineBirthday() {
		rBirthdate.setOpacity(1);
		if (failControl)
			rollbackAnims();
	}

	public void linePhoneNumber() {
		rPhonenumber.setOpacity(1);
		if (failControl)
			rollbackAnims();
	}

	public void setPositionAnims() {

		tmDown = new Timeline();
		tmUp = new Timeline();

		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rUsername.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rUsername.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfUsername.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfUsername.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rPassword.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rPassword.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(pfPassword.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(pfPassword.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rSchoolnumber.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rSchoolnumber.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(tfSchoolNumber.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfSchoolNumber.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rEmail.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rEmail.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfEmail.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfEmail.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rName.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rName.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfName.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfName.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rSurname.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rSurname.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfSurname.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfSurname.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rBirthdate.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rBirthdate.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(dpBirthday.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(dpBirthday.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rPhonenumber.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rPhonenumber.translateYProperty(), 0)));
		tmDown.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfPhoneNumber.translateYProperty(), -10)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfPhoneNumber.translateYProperty(), 0)));

		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rUsername.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rUsername.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfUsername.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfUsername.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rPassword.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rPassword.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(pfPassword.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(pfPassword.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rSchoolnumber.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rSchoolnumber.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfSchoolNumber.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfSchoolNumber.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rEmail.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rEmail.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfEmail.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfEmail.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rName.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rName.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfName.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfName.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rSurname.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rSurname.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfSurname.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfSurname.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rBirthdate.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rBirthdate.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(dpBirthday.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(dpBirthday.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(rPhonenumber.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(rPhonenumber.translateYProperty(), -10)));
		tmUp.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(tfPhoneNumber.translateYProperty(), 0)),
				new KeyFrame(Duration.millis(100.0d), new KeyValue(tfPhoneNumber.translateYProperty(), -10)));

	}

	public void setColorAnims() {

		/* Prepare color animations */
		usernameToRed = new FillTransition(Duration.millis(300.0d), rUsername, Color.valueOf("#727986"),
				Color.valueOf("#d41923"));
		usernameToGrey = new FillTransition(Duration.millis(300.0d), rUsername, Color.valueOf("#d41923"),
				Color.valueOf("#727986"));

		passwordToRed = new FillTransition(Duration.millis(300.0d), rPassword, Color.valueOf("#727986"),
				Color.valueOf("#d41923"));
		passwordToGrey = new FillTransition(Duration.millis(300.0d), rPassword, Color.valueOf("#d41923"),
				Color.valueOf("#727986"));

		schoolnumberToRed = new FillTransition(Duration.millis(300.0d), rSchoolnumber, Color.valueOf("#727986"),
				Color.valueOf("#d41923"));
		schoolnumberToGrey = new FillTransition(Duration.millis(300.0d), rSchoolnumber, Color.valueOf("#d41923"),
				Color.valueOf("#727986"));

		emailToRed = new FillTransition(Duration.millis(300.0d), rEmail, Color.valueOf("#727986"),
				Color.valueOf("#d41923"));
		emailToGrey = new FillTransition(Duration.millis(300.0d), rEmail, Color.valueOf("#d41923"),
				Color.valueOf("#727986"));

	}

	public void setOpacityAnims() {

		/* Prepare warning text animations */
		showUsernameWarning = new FadeTransition(Duration.millis(300.0d), warnUsername);
		showUsernameWarning.setFromValue(0);
		showUsernameWarning.setToValue(1);

		hideUsernameWarning = new FadeTransition(Duration.millis(300.0d), warnUsername);
		hideUsernameWarning.setFromValue(1);
		hideUsernameWarning.setToValue(0);

		showPasswordWarning = new FadeTransition(Duration.millis(300.0d), warnPassword);
		showPasswordWarning.setFromValue(0);
		showPasswordWarning.setToValue(1);

		hidePasswordWarning = new FadeTransition(Duration.millis(300.0d), warnPassword);
		hidePasswordWarning.setFromValue(1);
		hidePasswordWarning.setToValue(0);

		showSchoolnumberWarning = new FadeTransition(Duration.millis(300.0d), warnSchoolnumber);
		showSchoolnumberWarning.setFromValue(0);
		showSchoolnumberWarning.setToValue(1);

		hideSchoolnumberWarning = new FadeTransition(Duration.millis(300.0d), warnSchoolnumber);
		hideSchoolnumberWarning.setFromValue(1);
		hideSchoolnumberWarning.setToValue(0);

		showEmailWarning = new FadeTransition(Duration.millis(300.0d), warnEmail);
		showEmailWarning.setFromValue(0);
		showEmailWarning.setToValue(1);

		hideEmailWarning = new FadeTransition(Duration.millis(300.0d), warnEmail);
		hideEmailWarning.setFromValue(1);
		hideEmailWarning.setToValue(0);

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

}
