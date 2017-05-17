package application.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.imageio.ImageIO;

import application.Main;
import application.model.User;
import javafx.animation.FadeTransition;
import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
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
	public Rectangle rUsername;
	public Rectangle rPassword;
	public Label lbUsername;
	public Label lbPassword;
	public Label lbWelcome;
	public Label lbUserdata;
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
	private FadeTransition showUserdata;
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

	public void handleLogin(ActionEvent event) throws SQLException, IOException, NoSuchAlgorithmException {
		loginControl = false;
		setSizeAnims();
		setOpacityAnims();
		setColorAnims();
		String inputUsername = tfUsername.getText();
		String inputUserpass = DBFormatController.passToDatabase(pfPassword.getText());
		PreparedStatement statement = Main.con
				.prepareStatement("SELECT password FROM user WHERE username = \'" + inputUsername + "\' ");
		ResultSet result = statement.executeQuery();

		String temp;
		for (temp = null; result.next(); temp = result.getString(1)) {
			;
		}

		if (temp != null && temp.equals(inputUserpass)) {
			loginControl = true;
			statement = Main.con.prepareStatement("SELECT * from user WHERE username = \'" + inputUsername + "\' ");
			result = statement.executeQuery();
			WritableImage profilePic;
			while (result.next()) {
				byte[] imgBuf = result.getBytes("profilepic");

				if (imgBuf != null) {
					ByteArrayInputStream in = new ByteArrayInputStream(imgBuf);
					BufferedImage bufferedImage = ImageIO.read(in);

					profilePic = SwingFXUtils.toFXImage(bufferedImage, (WritableImage) null);
				} else {
					profilePic = null;
				}

				if (result.getDate("birthdate") == null) {

					Main.currentUser = new User(result.getString("username"), result.getString("password"),
							result.getString("email"), result.getString("name"), result.getString("surname"),
							result.getString("phonenumber"), (LocalDate) null, result.getInt("iduserinfo"),
							result.getInt("schoolnumber"), (Image) profilePic, result.getInt("islibrarian"));
				} else {
					Main.currentUser = new User(result.getString("username"), result.getString("password"),
							result.getString("email"), result.getString("name"), result.getString("surname"),
							result.getString("phonenumber"), DBFormatController.dateToJava(result.getDate("birthdate")),
							result.getInt("iduserinfo"), result.getInt("schoolnumber"), (Image) profilePic,
							result.getInt("islibrarian"));
				}

			}

			int islabrarian = Main.currentUser.getIslibrarian();
			lbUserdata.setText(Main.currentUser.getName() + " " + Main.currentUser.getSurname());
			Parent newParent;
			final Scene newScreen;
			final Stage appStage;
			if (islabrarian > 0) {
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
				newParent = (Parent) FXMLLoader.load(this.getClass().getResource("../view/HomePageLibrarian.fxml"));
				newScreen = new Scene(newParent);
				appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
				newParent = (Parent) FXMLLoader.load(this.getClass().getResource("../view/HomePageCustomer.fxml"));
				newScreen = new Scene(newParent);
				appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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

		}

		if (!loginControl.booleanValue()) {
			failExpand.play();
			showFail.play();
			usernameToRed.play();
			passwordToRed.play();
			failControl = true;
		}

	}

	public void handleCreateAccount(ActionEvent event) throws IOException {
		Parent newParent = (Parent) FXMLLoader.load(this.getClass().getResource("../view/CreateAccount.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(newScreen);
		appStage.show();
	}

	public void lineUsername() {
		rUsername.setOpacity(1.0d);
		if (failControl.booleanValue()) {
			rollbackAnims();
		}

	}

	public void linePassword() {
		rPassword.setOpacity(1.0d);
		if (failControl.booleanValue()) {
			rollbackAnims();
		}

	}

	public void setColorAnims() {
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
		failExpand.getKeyFrames().addAll(new KeyFrame[] {
				new KeyFrame(Duration.ZERO,
						new KeyValue[] { new KeyValue(boxFailed.prefHeightProperty(), Integer.valueOf(0)) }),
				new KeyFrame(Duration.millis(200.0d),
						new KeyValue[] { new KeyValue(boxFailed.prefHeightProperty(), Integer.valueOf(36)) }) });
		failCollapse = new Timeline();
		failCollapse.getKeyFrames()
				.addAll(new KeyFrame[] {
						new KeyFrame(Duration.ZERO,
								new KeyValue[] { new KeyValue(boxFailed.prefHeightProperty(), Integer.valueOf(36)) }),
						new KeyFrame(Duration.millis(200.0d),
								new KeyValue[] { new KeyValue(boxFailed.prefHeightProperty(), Integer.valueOf(0)) }) });
	}

	public void setOpacityAnims() {
		showFail = new FadeTransition(opacityFactor, boxFailed);
		showFail.setFromValue(0.0d);
		showFail.setToValue(1.0d);
		hideFail = new FadeTransition(opacityFactor, boxFailed);
		hideFail.setFromValue(1.0d);
		hideFail.setToValue(0.0d);
		hideUsernametf = new FadeTransition(opacityFactor, tfUsername);
		hideUsernametf.setFromValue(1.0d);
		hideUsernametf.setToValue(0.0d);
		showUsernamelb = new FadeTransition(opacityFactor, lbUsername);
		showUsernamelb.setFromValue(0.0d);
		showUsernamelb.setToValue(1.0d);
		hideUsernamelb = new FadeTransition(opacityFactor, lbUsername);
		hideUsernamelb.setFromValue(1.0d);
		hideUsernamelb.setToValue(0.0d);
		showUsernamer = new FadeTransition(opacityFactor, rUsername);
		showUsernamer.setFromValue(0.0d);
		showUsernamer.setToValue(1.0d);
		hideUsernamer = new FadeTransition(opacityFactor, rUsername);
		hideUsernamer.setFromValue(1.0d);
		hideUsernamer.setToValue(0.0d);
		hidePasswordpf = new FadeTransition(opacityFactor, pfPassword);
		hidePasswordpf.setFromValue(1.0d);
		hidePasswordpf.setToValue(0.0d);
		showPasswordlb = new FadeTransition(opacityFactor, lbPassword);
		showPasswordlb.setFromValue(0.0d);
		showPasswordlb.setToValue(1.0d);
		hidePasswordlb = new FadeTransition(opacityFactor, lbPassword);
		hidePasswordlb.setFromValue(1.0d);
		hidePasswordlb.setToValue(0.0d);
		showPasswordr = new FadeTransition(opacityFactor, rPassword);
		showPasswordr.setFromValue(0.0d);
		showPasswordr.setToValue(1.0d);
		hidePasswordr = new FadeTransition(opacityFactor, rPassword);
		hidePasswordr.setFromValue(1.0d);
		hidePasswordr.setToValue(0.0d);
		showWelcome = new FadeTransition(opacityFactor, lbWelcome);
		showWelcome.setFromValue(0.0d);
		showWelcome.setToValue(1.0d);
		showUserdata = new FadeTransition(opacityFactor, lbUserdata);
		showUserdata.setFromValue(0.0d);
		showUserdata.setToValue(1.0d);
		hideLogin = new FadeTransition(opacityFactor, login);
		hideLogin.setFromValue(1.0d);
		hideLogin.setToValue(0.0d);
		hideCreateaccount = new FadeTransition(opacityFactor, createAccount);
		hideCreateaccount.setFromValue(1.0d);
		hideCreateaccount.setToValue(0.0d);
		hideLogo = new FadeTransition(opacityFactor, paneLogo);
		hideLogo.setFromValue(1.0d);
		hideLogo.setToValue(0.0d);
	}

	public void rollbackAnims() {
		failCollapse.play();
		hideFail.play();
		usernameToGrey.play();
		passwordToGrey.play();
		failControl = false;
	}
}
