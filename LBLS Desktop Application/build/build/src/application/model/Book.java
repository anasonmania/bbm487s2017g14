package application.model;

import javafx.scene.image.Image;

public class Book {
	private String name;
	private String author;
	private String publisher;
	private String description;
	private int idbook;
	private int isbn;
	private boolean isAvailable;
	private Image image;

	public Book(String name, String author, String publisher, String description, int idbook, int isbn,
			boolean isAvailable, Image image) {
		this.name = name;
		this.author = author;
		this.publisher = publisher;
		this.description = description;
		this.idbook = idbook;
		this.isbn = isbn;
		this.isAvailable = isAvailable;
		this.image = image;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return this.publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIdbook() {
		return this.idbook;
	}

	public void setIdbook(int idbook) {
		this.idbook = idbook;
	}

	public int getIsbn() {
		return this.isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public boolean isAvailable() {
		return this.isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}