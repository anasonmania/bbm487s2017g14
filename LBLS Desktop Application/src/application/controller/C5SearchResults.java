package application.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.Main;
import application.model.Book;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class C5SearchResults implements Initializable {
	public GridPane paneHeader;
	public GridPane paneContent;
	public GridPane paneBottom;
	public TextField tfSearch;
	public Rectangle rSearch;
	public Rectangle rBookimg;
	public Label lbBookname;
	public Label lbAuthor;
	public Label lbPublisher;

	public TableView<Book> tableBook;
	private ObservableList<Book> bookList;
	private FadeTransition showHeader;
	private FadeTransition hideHeader;
	private FadeTransition showContent;
	private FadeTransition hideContent;
	private FadeTransition showBottom;
	private FadeTransition hideBottom;
	private Duration opacityFactor = Duration.millis(1000.0d);
	int checkDesStart;
	int remainChar;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setOpacityAnims();

		try {
			getBooks();
			createTable();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		showHeader.play();
		showContent.play();
		showBottom.play();
		tableBook.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue)->{
			if(tableBook.getSelectionModel().getSelectedItem()!=null){
				Book selectedItem = tableBook.getSelectionModel().getSelectedItem();
				lbBookname.setText(selectedItem.getName());
				lbAuthor.setText(selectedItem.getAuthor());
				lbPublisher.setText(selectedItem.getPublisher());
				if(selectedItem.getImage()!=null){
					rBookimg.setFill(new ImagePattern(selectedItem.getImage()));
				}
				else{
					rBookimg.setFill(new ImagePattern(Main.defaultBook));
				}
			}
		});

		tableBook.getSelectionModel().selectFirst();
	}

	public void toBookDetail(ActionEvent event) throws SQLException, IOException {
		Main.selectedBook = (Book) tableBook.getSelectionModel().getSelectedItem();
		Parent newParent = (Parent) FXMLLoader.load(getClass().getResource("../view/C1_2BookDetail.fxml"));
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
		Parent newParent = (Parent) FXMLLoader.load(getClass().getResource("../view/HomePageCustomer.fxml"));
		Scene newScreen = new Scene(newParent);
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		appStage.setScene(newScreen);
		appStage.show();
	}

	public void setOpacityAnims() {
		showHeader = AnimationFabric.createOpacityAnim(paneHeader, 0.0d, 1.0d,
				opacityFactor.divide(2.0d));
		showContent = AnimationFabric.createOpacityAnim(paneContent, 0.0d, 1.0d, opacityFactor);
		showBottom = AnimationFabric.createOpacityAnim(paneBottom, 0.0d, 1.0d,
				opacityFactor.multiply(2.0d));
		hideHeader = AnimationFabric.createOpacityAnim(paneHeader, 1.0d, 0.0d,
				opacityFactor.multiply(2.0d));
		hideContent = AnimationFabric.createOpacityAnim(paneContent, 1.0d, 0.0d, opacityFactor);
		hideBottom = AnimationFabric.createOpacityAnim(paneBottom, 1.0d, 0.0d,
				opacityFactor.divide(2.0d));
	}

	public void lineSearch() {
		rSearch.setOpacity(1.0d);
	}

	private void getBooks() throws SQLException, IOException {
		bookList = FXCollections.observableArrayList();
		PreparedStatement idsQuery = Main.con.prepareStatement("SELECT * FROM book ");
		ResultSet result = idsQuery.executeQuery();

		while (result.next()) {
			int idbook = result.getInt("id");
			int isbn = result.getInt("isbn");
			int owner = result.getInt("owner");
			String name = result.getString("name");
			String author = result.getString("author");
			String publisher = result.getString("publisher");
			String description = result.getString("description");
			Boolean isAvailable = result.getBoolean("isavailable");


			byte[] imgBuf = result.getBytes("image");
			WritableImage bookImg;
			if (imgBuf != null) {
				ByteArrayInputStream in = new ByteArrayInputStream(imgBuf);
				BufferedImage bufferedImage = ImageIO.read(in);
				bookImg = SwingFXUtils.toFXImage(bufferedImage, (WritableImage) null);
			} else {
				bookImg = null;
			}
			String nameC, authorC, publisherC;
			nameC = name.toLowerCase();
			authorC = author.toLowerCase();
			publisherC = publisher.toLowerCase();
			if(nameC.indexOf(Main.userInput)!=-1 || authorC.indexOf(Main.userInput)!=-1
					|| publisherC.indexOf(Main.userInput)!=-1 )
			bookList.add(new Book(name, author, publisher, description, idbook, isbn, isAvailable, bookImg
			,owner));
		}

	}

	@SuppressWarnings("unchecked")
	private void createTable() {
		TableColumn<Book, String> nameCol = new TableColumn<Book, String>("Name");
		nameCol.setMinWidth(144.0d);
		nameCol.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
		TableColumn<Book, String> authorCol = new TableColumn<Book, String>("Author");
		authorCol.setMinWidth(144.0d);
		authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
		TableColumn<Book, String> publisherCol = new TableColumn<Book, String>("Publisher");
		publisherCol.setMinWidth(144.0d);
		publisherCol.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
		tableBook.setItems(bookList);
		tableBook.getColumns().clear();
		tableBook.getColumns().addAll(nameCol, authorCol, publisherCol);
	}
}
