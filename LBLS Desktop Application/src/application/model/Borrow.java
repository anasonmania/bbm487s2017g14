package application.model;

import java.time.LocalDate;

import javafx.scene.image.Image;

public class Borrow {

	private int bookid, owner;
	private String bookName, bookAuthor, bookPublisher;
	private LocalDate borrowDate, returnDate;
	private Image image;

	public Borrow(int bookid, int owner, String bookName, String bookAuthor, String bookPublisher, LocalDate borrowDate,
			LocalDate returnDate, Image image) {
		super();
		this.bookid = bookid;
		this.owner = owner;
		this.bookName = bookName;
		this.bookAuthor = bookAuthor;
		this.bookPublisher = bookPublisher;
		this.borrowDate = borrowDate;
		this.returnDate = returnDate;
		this.image = image;
	}

	public int getBookid() {
		return bookid;
	}

	public int getOwner() {
		return owner;
	}

	public String getBookName() {
		return bookName;
	}

	public String getBookAuthor() {
		return bookAuthor;
	}

	public String getBookPublisher() {
		return bookPublisher;
	}

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public Image getImage(){
		return image;
	}




}
