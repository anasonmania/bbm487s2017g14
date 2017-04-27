package application.controller;

import application.Main;
import application.controller.AnimationFabric;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomePageLibrarian implements Initializable {
	public TextField tfSearch;
	public GridPane lnSearch;
	public Label lbName;
	public Label lbSchoolidentity;
	public Button btnGrid1;
	public Button btnGrid2;
	public Button btnGrid3;
	public Button btnGrid4;
	public GridPane paneButtons;
	public GridPane paneSearch;
	public GridPane paneHeader;
	public Rectangle rSearch;
	private FadeTransition showHeader;
	private FadeTransition hideHeader;
	private FadeTransition showButtons;
	private FadeTransition hideButtons;
	private FadeTransition showSearch;
	private FadeTransition hideSearch;
	private Duration opacityFactor = Duration.millis(1000.0D);

	public void initialize(URL location, ResourceBundle resources) {
		this.rSearch.setOpacity(0.4D);
		this.tfSearch.setPromptText("Search books");
		this.lbName.setText(Main.currentUser.getName() + " " + Main.currentUser.getSurname());
		this.lbSchoolidentity.setText(Integer.toString(Main.currentUser.getSchoolNumber()));
		this.setOpacityAnims();
		this.paneHeader.setOpacity(1.0D);
		this.showButtons.play();
		this.showSearch.play();
	}

	public void addUser(ActionEvent event) throws IOException {
		this.hideSearch.play();
		this.hideButtons.play();
		Parent newParent = (Parent) FXMLLoader.load(this.getClass().getResource("../view/L1AddUser.fxml"));
		final Scene newScreen = new Scene(newParent);
		final Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep((long) opacityFactor.toMillis()/4);
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

	public void editUser(ActionEvent event) throws IOException {
		this.hideSearch.play();
		this.hideButtons.play();
		Parent newParent = (Parent) FXMLLoader.load(this.getClass().getResource("../view/L2EditUser.fxml"));
		final Scene newScreen = new Scene(newParent);
		final Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep((long) opacityFactor.toMillis()/4);
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

	public void addBook(ActionEvent event) throws IOException {
		this.hideSearch.play();
		this.hideButtons.play();
		System.out.println("add book clicked");
		Parent newParent = (Parent) FXMLLoader.load(this.getClass().getResource("../view/L3AddBook.fxml"));
		final Scene newScreen = new Scene(newParent);
		final Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep((long) opacityFactor.toMillis()/4);
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

	public void logout(ActionEvent event) throws IOException {
		Main.currentUser.logout();
		this.hideSearch.play();
		this.hideButtons.play();
		this.hideHeader.play();
		Parent newParent = (Parent) FXMLLoader.load(this.getClass().getResource("../view/Login.fxml"));
		final Scene newScreen = new Scene(newParent);
		final Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

		Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep((long) opacityFactor.toMillis()/4);
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

	public void lineSearch() {
		this.rSearch.setOpacity(1.0D);
	}

	public void setOpacityAnims() {
		this.showButtons = new FadeTransition(this.opacityFactor.multiply(2.0D), this.paneButtons);
		this.showButtons.setFromValue(0.0D);
		this.showButtons.setToValue(1.0D);
		this.hideButtons = new FadeTransition(this.opacityFactor.divide(2.0D), this.paneButtons);
		this.hideButtons.setFromValue(1.0D);
		this.hideButtons.setToValue(0.0D);
		this.showSearch = new FadeTransition(this.opacityFactor, this.paneSearch);
		this.showSearch.setFromValue(0.0D);
		this.showSearch.setToValue(1.0D);
		this.hideSearch = new FadeTransition(this.opacityFactor, this.paneSearch);
		this.hideSearch.setFromValue(1.0D);
		this.hideSearch.setToValue(0.0D);
		this.showHeader = AnimationFabric.createOpacityAnim(this.paneHeader, 0.0D, 1.0D, this.opacityFactor);
		this.hideHeader = AnimationFabric.createOpacityAnim(this.paneHeader, 1.0D, 0.0D, this.opacityFactor);
	}
}