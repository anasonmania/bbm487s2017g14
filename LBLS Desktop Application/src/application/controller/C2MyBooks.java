package application.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import application.Main;
import application.model.Borrow;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class C2MyBooks implements Initializable {
	public GridPane paneHeader;
	public GridPane paneContent;
	public GridPane paneBottom;
	private LinkedList<Borrow> userBorrows;

	public TextField tfSearch;
	public Rectangle rSearch;
	public Rectangle rBookimg, rBookimg2, rBookimg3, line1, line2;
	public GridPane gpCard, gpCard2, gpCard3, gpCards;
	public Label lbBookname, lbBookname2, lbBookname3, lbAuthor, lbAuthor2, lbAuthor3, lbPublisher, lbPublisher2,
			lbPublisher3, lbReturndate, lbReturndate2, lbReturndate3, lbSuccess, lbSuccess2, lbSuccess3;
	public Button btnReturn, btnReturn2, btnReturn3, btnPay, btnPay2, btnPay3;

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

		lbSuccess.setVisible(false);
		lbSuccess2.setVisible(false);
		lbSuccess3.setVisible(false);

		PreparedStatement myBooksQuery;
		ResultSet borrowResults;
		userBorrows = new LinkedList<Borrow>();
		int bookid, owner;
		String bookName, bookAuthor, bookPublisher;
		LocalDate borrowDate, returnDate;
		Image image=null;
		try {
			myBooksQuery = Main.con.prepareStatement(
					"SELECT book.id, book.name, book.author, book.publisher, book.image, borrow.returndate, borrow.borrowdate, borrow.owner "
					+ "FROM borrow INNER JOIN book "
					+ "ON borrow.idbook = book.id");
			borrowResults = myBooksQuery.executeQuery();
			while(borrowResults.next()){
				bookid = borrowResults.getInt("book.id");
				owner = borrowResults.getInt("borrow.owner");
				bookName = borrowResults.getString("book.name");
				bookAuthor = borrowResults.getString("book.author");
				bookPublisher = borrowResults.getString("book.publisher");
				borrowDate = DBFormatController.dateToJava(borrowResults.getDate("borrow.borrowdate"));
				returnDate = DBFormatController.dateToJava(borrowResults.getDate("borrow.returndate"));

				byte[] imgBuf = borrowResults.getBytes("book.image");

				if (imgBuf != null) {

					try {	ByteArrayInputStream in = new ByteArrayInputStream(imgBuf);
						BufferedImage bufferedImage;
						bufferedImage = ImageIO.read(in);
						image = SwingFXUtils.toFXImage(bufferedImage, (WritableImage) null);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					image = null;
				}

				if(Main.currentUser.getUserId() == owner){
					userBorrows.add(new Borrow(bookid, owner, bookName, bookAuthor, bookPublisher, borrowDate, returnDate, image));
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			setGUI();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showHeader.play();
		showContent.play();
		showBottom.play();

	}


	public void setGUI() throws SQLException {
		LocalDate today = LocalDate.now();
		PreparedStatement countQuery = Main.con.prepareStatement(
				"SELECT COUNT(owner) " + "AS borrowcount FROM borrow WHERE owner = " + Main.currentUser.getUserId());
		ResultSet countResult = countQuery.executeQuery();
		int borrowCount = 0;
		while (countResult.next()) {
			borrowCount = countResult.getInt("borrowcount");
		}
		if (borrowCount == 1) {
			gpCard2.setOpacity(0);
			btnReturn2.setDisable(true);
			gpCard3.setOpacity(0);
			btnReturn3.setDisable(true);
			line1.setOpacity(0);
			line2.setOpacity(0);

			if(userBorrows.get(0).getImage()!=null){
				rBookimg.setFill(new ImagePattern(userBorrows.get(0).getImage()));
			}else{
				rBookimg.setFill(new ImagePattern(Main.defaultBook));
			}
			lbBookname.setText(userBorrows.get(0).getBookName());
			lbAuthor.setText(userBorrows.get(0).getBookAuthor());
			lbPublisher.setText(userBorrows.get(0).getBookPublisher());

			int day1 = Period.between(today, userBorrows.get(0).getReturnDate()).getDays();
			if(day1>0){
				btnPay.setDisable(true);
				btnPay.setVisible(false);
				lbReturndate.setText(userBorrows.get(0).getReturnDate().format(Main.dtf));
			}
			else{
				btnReturn.setDisable(true);
				btnReturn.setVisible(false);
				lbReturndate.setText("Expired " + day1*-1 + " days");
				btnPay.setText("PAY " + day1*3*-1 + " TL");
			}

		} else if (borrowCount == 2) {
			gpCard3.setOpacity(0);
			btnReturn3.setDisable(true);
			line2.setOpacity(0);

			if(userBorrows.get(0).getImage()!=null){
				rBookimg.setFill(new ImagePattern(userBorrows.get(0).getImage()));
			}else{
				rBookimg.setFill(new ImagePattern(Main.defaultBook));
			}
			lbBookname.setText(userBorrows.get(0).getBookName());
			lbAuthor.setText(userBorrows.get(0).getBookAuthor());
			lbPublisher.setText(userBorrows.get(0).getBookPublisher());

			int day1 = Period.between(today, userBorrows.get(0).getReturnDate()).getDays();
			if(day1>0){
				btnPay.setDisable(true);
				btnPay.setVisible(false);
				lbReturndate.setText(userBorrows.get(0).getReturnDate().format(Main.dtf));
			}
			else{
				btnReturn.setDisable(true);
				btnReturn.setVisible(false);
				lbReturndate.setText("Expired " + day1*-1 + " days");
				btnPay.setText("PAY " + day1*3*-1 + " TL");
			}

			//card2
			if(userBorrows.get(1).getImage()!=null){
				rBookimg2.setFill(new ImagePattern(userBorrows.get(1).getImage()));
			}else{
				rBookimg2.setFill(new ImagePattern(Main.defaultBook));
			}
			lbBookname2.setText(userBorrows.get(1).getBookName());
			lbAuthor2.setText(userBorrows.get(1).getBookAuthor());
			lbPublisher2.setText(userBorrows.get(1).getBookPublisher());

			int day2 = Period.between(today, userBorrows.get(1).getReturnDate()).getDays();
			if(day2>0){
				btnPay2.setDisable(true);
				btnPay2.setVisible(false);
				lbReturndate2.setText(userBorrows.get(1).getReturnDate().format(Main.dtf));
			}
			else{
				btnReturn2.setDisable(true);
				btnReturn2.setVisible(false);
				lbReturndate2.setText("Expired " + day2*-1 + " days");
				btnPay2.setText("PAY " + day2*3*-1 + " TL");
			}

		} else if (borrowCount == 3) {

			if(userBorrows.get(0).getImage()!=null){
				rBookimg.setFill(new ImagePattern(userBorrows.get(0).getImage()));
			}else{
				rBookimg.setFill(new ImagePattern(Main.defaultBook));
			}
			lbBookname.setText(userBorrows.get(0).getBookName());
			lbAuthor.setText(userBorrows.get(0).getBookAuthor());
			lbPublisher.setText(userBorrows.get(0).getBookPublisher());

			int day1 = Period.between(today, userBorrows.get(0).getReturnDate()).getDays();
			if(day1>0){
				btnPay.setDisable(true);
				btnPay.setVisible(false);
				lbReturndate.setText(userBorrows.get(0).getReturnDate().format(Main.dtf));
			}
			else{
				btnReturn.setDisable(true);
				btnReturn.setVisible(false);
				lbReturndate.setText("Expired " + day1*-1 + " days");
				btnPay.setText("PAY " + day1*3*-1 + " TL");
			}

			//card2
			if(userBorrows.get(1).getImage()!=null){
				rBookimg2.setFill(new ImagePattern(userBorrows.get(1).getImage()));
			}else{
				rBookimg2.setFill(new ImagePattern(Main.defaultBook));
			}
			lbBookname2.setText(userBorrows.get(1).getBookName());
			lbAuthor2.setText(userBorrows.get(1).getBookAuthor());
			lbPublisher2.setText(userBorrows.get(1).getBookPublisher());

			int day2 = Period.between(today, userBorrows.get(1).getReturnDate()).getDays();
			if(day2>0){
				btnPay2.setDisable(true);
				btnPay2.setVisible(false);
				lbReturndate2.setText(userBorrows.get(1).getReturnDate().format(Main.dtf));
			}
			else{
				btnReturn2.setDisable(true);
				btnReturn2.setVisible(false);
				lbReturndate2.setText("Expired " + day2*-1 + " days");
				btnPay2.setText("PAY " + day2*3*-1 + " TL");
			}

			//card3
			if(userBorrows.get(2).getImage()!=null){
				rBookimg3.setFill(new ImagePattern(userBorrows.get(2).getImage()));
			}else{
				rBookimg3.setFill(new ImagePattern(Main.defaultBook));
			}
			lbBookname3.setText(userBorrows.get(2).getBookName());
			lbAuthor3.setText(userBorrows.get(2).getBookAuthor());
			lbPublisher3.setText(userBorrows.get(2).getBookPublisher());

			int day3 = Period.between(today, userBorrows.get(2).getReturnDate()).getDays();
			if(day3>0){
				btnPay3.setDisable(true);
				btnPay3.setVisible(false);
				lbReturndate3.setText(userBorrows.get(2).getReturnDate().format(Main.dtf));
			}
			else{
				btnReturn3.setDisable(true);
				btnReturn3.setVisible(false);
				lbReturndate3.setText("Expired " + day3*-1 + " days");
				btnPay3.setText("PAY " + day3*3*-1 + " TL");
			}

		}
		else{
			gpCards.setVisible(false);
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
		Parent newParent = (Parent) FXMLLoader.load(getClass().getResource("../view/HomePageCustomer.fxml"));
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

	public void lineSearch() {
		rSearch.setOpacity(1.0d);
	}

	public void handleReturn(ActionEvent event) throws SQLException{

		PreparedStatement returnQuery = Main.con.prepareStatement("DELETE from borrow WHERE idbook = ?");

		PreparedStatement updateBook = Main.con.prepareStatement(
				"UPDATE book SET owner = ?, isavailable = ? WHERE id = ? ");
		updateBook.setInt(1, -1);
		updateBook.setBoolean(2, true);
		if(event.getSource()==btnReturn){
			returnQuery.setInt(1, userBorrows.get(0).getBookid());
			updateBook.setInt(3, userBorrows.get(0).getBookid());
			gpCard.setVisible(false);
			lbSuccess.setVisible(true);
		}
		else if(event.getSource()==btnReturn2){
			returnQuery.setInt(1, userBorrows.get(1).getBookid());

			updateBook.setInt(3, userBorrows.get(1).getBookid());
			gpCard2.setVisible(false);
			lbSuccess2.setVisible(true);
		}
		else if(event.getSource()==btnReturn3){
			returnQuery.setInt(1, userBorrows.get(2).getBookid());

			updateBook.setInt(3, userBorrows.get(2).getBookid());
			gpCard3.setVisible(false);
			lbSuccess3.setVisible(true);
		}

		returnQuery.execute();
		updateBook.executeUpdate();

	}

	public void handlePay(ActionEvent event){
		if(event.getSource()==btnPay){
			btnPay.setDisable(true);
			btnPay.setVisible(false);
			btnReturn.setVisible(true);
			btnReturn.setDisable(false);
		}
		else if(event.getSource()==btnPay2){
			btnPay2.setDisable(true);
			btnPay2.setVisible(false);
			btnReturn2.setVisible(true);
			btnReturn2.setDisable(false);
		}
		else if(event.getSource()==btnPay3){
			btnPay3.setDisable(true);
			btnPay3.setVisible(false);
			btnReturn3.setVisible(true);
			btnReturn3.setDisable(false);
		}
	}
}
