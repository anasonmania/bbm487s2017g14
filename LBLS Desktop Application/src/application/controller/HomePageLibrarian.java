package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomePageLibrarian implements Initializable {

	public TextField tfSearch;
	public GridPane lnSearch;
	public Label lbName, lbSchoolidentity;
	public Button btnGrid1, btnGrid2, btnGrid3, btnGrid4;
	public GridPane paneButtons, paneSearch, paneHeader;

	private FadeTransition showHeader;
	private FadeTransition hideHeader;
	private FadeTransition showButtons;
	private FadeTransition hideButtons;
	private FadeTransition showSearch;
	private FadeTransition hideSearch;

	private Duration opacityFactor = Duration.millis(1000.0d);



	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		lnSearch.setStyle("-fx-opacity: 0.4");
		tfSearch.setPromptText("Search books");
		lbName.setText(Main.currentUser.getName() + " " + Main.currentUser.getSurname());
		lbSchoolidentity.setText(Integer.toString(Main.currentUser.getSchoolNumber()));


		setOpacityAnims();
		showButtons.play();
		showSearch.play();
		showHeader.play();

	}

	public void logout(ActionEvent event) throws IOException{
		Main.currentUser.logout();

		hideSearch.play();
		hideButtons.play();
		hideHeader.play();

		Parent newParent = FXMLLoader.load(getClass().getResource("../view/Login.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();


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

	public void lineSearch() {
		lnSearch.setStyle("-fx-opacity: 1.0;");
	}

	public void setOpacityAnims(){


		showButtons = new FadeTransition(opacityFactor.multiply(2), paneButtons);
		showButtons.setFromValue(0);
		showButtons.setToValue(1);

		hideButtons = new FadeTransition(opacityFactor.divide(2), paneButtons);
		hideButtons.setFromValue(1);
		hideButtons.setToValue(0);

		showSearch = new FadeTransition(opacityFactor, paneSearch);
		showSearch.setFromValue(0);
		showSearch.setToValue(1);

		hideSearch = new FadeTransition(opacityFactor, paneSearch);
		hideSearch.setFromValue(1);
		hideSearch.setToValue(0);

		showHeader = new FadeTransition(opacityFactor, paneHeader);
		showHeader.setFromValue(0);
		showHeader.setToValue(1);

		hideHeader = new FadeTransition(opacityFactor, paneHeader);
		hideHeader.setFromValue(1);
		hideHeader.setToValue(0);

	}

}
