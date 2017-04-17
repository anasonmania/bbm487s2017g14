package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import application.Main;
import application.model.User;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Login {

	public Button createAccount;
	public Button login;
	public TextField tfUsername;
	public PasswordField pfPassword;
	public Rectangle rUsername, rPassword;
	public Label lbUsername, lbPassword, lbWelcome, lbUserdata;
	public Boolean loginControl;
	public Boolean failControl = false;
	public Pane paneLogo;

	public GridPane paneMain;
	public HBox boxFailed;

	private Duration opacityFactor = Duration.millis(300.0d);

	private FillTransition usernameToRed;
	private FillTransition usernameToGrey;
	private FillTransition passwordToRed;
	private FillTransition passwordToGrey;

	private Timeline failExpand;
	private Timeline failCollapse;

	private FadeTransition showFail;
	private FadeTransition hideFail;
	private FadeTransition showWelcome;
	private FadeTransition hideWelcome;
	private FadeTransition showUserdata;
	private FadeTransition hideUserdata;

	private FadeTransition hideUsernametf;
	private FadeTransition showUsernamelb;
	private FadeTransition hideUsernamelb;
	private FadeTransition showUsernamer;
	private FadeTransition hideUsernamer;
	private FadeTransition hidePasswordpf;
	private FadeTransition showPasswordlb;
	private FadeTransition hidePasswordlb;
	private FadeTransition showPasswordr;
	private FadeTransition hidePasswordr;
	private FadeTransition hideLogin;
	private FadeTransition hideCreateaccount;
	private FadeTransition hideLogo;

	public void handleLogin(ActionEvent event) throws SQLException, IOException {
		loginControl = false;
		setSizeAnims();
		setOpacityAnims();
		setColorAnims();

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
				loginControl = true;
				statement = Main.con.prepareStatement("SELECT * from user WHERE username = '" + inputUsername + "' ");
				result = statement.executeQuery();
				while (result.next()) {
					Main.currentUser = new User(result.getString("username"), result.getString("email"),
							result.getString("name"), result.getString("surname"), result.getString("phonenumber"),
							result.getDate("birthdate"), result.getInt("iduserinfo"), result.getInt("schoolnumber"),
							result.getInt("islibrarian"));
					System.out.println(result.getString("username"));
					System.out.println(Main.currentUser.getSurname());

				}

				int islabrarian = Main.currentUser.getIsLabrarian();

				lbUserdata.setText(Main.currentUser.getName() + " " + Main.currentUser.getSurname());
				if (islabrarian > 0) {
					Parent newParent;
					hideUsernametf.play();
					hideUsernamelb.play();
					hideUsernamer.play();
					hidePasswordpf.play();
					hidePasswordlb.play();
					hidePasswordr.play();
					hideLogin.play();
					hideCreateaccount.play();
					hideLogo.play();
					showWelcome.play();
					showUserdata.play();
					newParent = FXMLLoader.load(getClass().getResource("../view/HomePageLibrarian.fxml"));
					Scene newScreen = new Scene(newParent);

					Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					Task<Void> sleeper = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							try {
								Thread.sleep((long) opacityFactor.toMillis() * 3);
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
				} else {
					Parent newParent;
					hideUsernametf.play();
					hideUsernamelb.play();
					hideUsernamer.play();
					hidePasswordpf.play();
					hidePasswordlb.play();
					hidePasswordr.play();
					hideLogin.play();
					hideCreateaccount.play();
					hideLogo.play();
					showWelcome.play();
					showUserdata.play();
					newParent = FXMLLoader.load(getClass().getResource("../view/HomePageCustomer.fxml"));
					Scene newScreen = new Scene(newParent);
					Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					Task<Void> sleeper = new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							try {
								Thread.sleep((long) opacityFactor.toMillis() * 3);
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

				System.out.println(Main.currentUser.getBirthDate());
				System.out.println(Main.currentUser.getIsLabrarian());
			}
		}

		if (!loginControl) {
			failExpand.play();
			showFail.play();
			usernameToRed.play();
			passwordToRed.play();
			failControl = true;
		}
	}

	public void handleCreateAccount(ActionEvent event) throws IOException {

		Parent newParent = FXMLLoader.load(getClass().getResource("../view/CreateAccount.fxml"));
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

	}

	public void setSizeAnims() {
		failExpand = new Timeline();
		failExpand.getKeyFrames().addAll(new KeyFrame(Duration.ZERO, new KeyValue(boxFailed.prefHeightProperty(), 0)),
				new KeyFrame(Duration.millis(200.0d), new KeyValue(boxFailed.prefHeightProperty(), 36)));

		failCollapse = new Timeline();
		failCollapse.getKeyFrames().addAll(
				new KeyFrame(Duration.ZERO, new KeyValue(boxFailed.prefHeightProperty(), 36)),
				new KeyFrame(Duration.millis(200.0d), new KeyValue(boxFailed.prefHeightProperty(), 0)));
	}

	public void setOpacityAnims() {
		showFail = new FadeTransition(opacityFactor, boxFailed);
		showFail.setFromValue(0);
		showFail.setToValue(1);

		hideFail = new FadeTransition(opacityFactor, boxFailed);
		hideFail.setFromValue(1);
		hideFail.setToValue(0);

		hideUsernametf = new FadeTransition(opacityFactor, tfUsername);
		hideUsernametf.setFromValue(1);
		hideUsernametf.setToValue(0);

		showUsernamelb = new FadeTransition(opacityFactor, lbUsername);
		showUsernamelb.setFromValue(0);
		showUsernamelb.setToValue(1);

		hideUsernamelb = new FadeTransition(opacityFactor, lbUsername);
		hideUsernamelb.setFromValue(1);
		hideUsernamelb.setToValue(0);

		showUsernamer = new FadeTransition(opacityFactor, rUsername);
		showUsernamer.setFromValue(0);
		showUsernamer.setToValue(1);

		hideUsernamer = new FadeTransition(opacityFactor, rUsername);
		hideUsernamer.setFromValue(1);
		hideUsernamer.setToValue(0);

		hidePasswordpf = new FadeTransition(opacityFactor, pfPassword);
		hidePasswordpf.setFromValue(1);
		hidePasswordpf.setToValue(0);

		showPasswordlb = new FadeTransition(opacityFactor, lbPassword);
		showPasswordlb.setFromValue(0);
		showPasswordlb.setToValue(1);

		hidePasswordlb = new FadeTransition(opacityFactor, lbPassword);
		hidePasswordlb.setFromValue(1);
		hidePasswordlb.setToValue(0);

		showPasswordr = new FadeTransition(opacityFactor, rPassword);
		showPasswordr.setFromValue(0);
		showPasswordr.setToValue(1);

		hidePasswordr = new FadeTransition(opacityFactor, rPassword);
		hidePasswordr.setFromValue(1);
		hidePasswordr.setToValue(0);

		showWelcome = new FadeTransition(opacityFactor, lbWelcome);
		showWelcome.setFromValue(0);
		showWelcome.setToValue(1);

		showUserdata = new FadeTransition(opacityFactor, lbUserdata);
		showUserdata.setFromValue(0);
		showUserdata.setToValue(1);

		hideLogin = new FadeTransition(opacityFactor, login);
		hideLogin.setFromValue(1);
		hideLogin.setToValue(0);

		hideCreateaccount = new FadeTransition(opacityFactor, createAccount);
		hideCreateaccount.setFromValue(1);
		hideCreateaccount.setToValue(0);

		hideLogo = new FadeTransition(opacityFactor, paneLogo);
		hideLogo.setFromValue(1);
		hideLogo.setToValue(0);
	}

	public void rollbackAnims() {

		failCollapse.play();
		hideFail.play();
		usernameToGrey.play();
		passwordToGrey.play();

		failControl = false;

	}

}
