package application.model;

import java.time.LocalDate;
import javafx.scene.image.Image;

public class User {
	private String username;
	private String email;
	private String name;
	private String surname;
	private String phoneNumber;
	private LocalDate birthDate;
	private int userId;
	private int schoolNumber;
	private int islibrarian;
	private Image profilePic;

	public User(String username, String email, String name, String surname, String phoneNumber, LocalDate birthDate,
			int userId, int schoolNumber, Image profilePic, int islibrarian) {
		this.username = username;
		this.email = email;
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.birthDate = birthDate;
		this.userId = userId;
		this.schoolNumber = schoolNumber;
		this.profilePic = profilePic;
		this.islibrarian = islibrarian;
	}

	public void logout() {
		this.username = "";
		this.email = "";
		this.name = "";
		this.surname = "";
		this.phoneNumber = "";
		this.birthDate = null;
		this.userId = 0;
		this.schoolNumber = 0;
		this.islibrarian = 0;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getSchoolNumber() {
		return this.schoolNumber;
	}

	public void setSchoolNumber(int schoolNumber) {
		this.schoolNumber = schoolNumber;
	}

	public int getIslibrarian() {
		return this.islibrarian;
	}

	public void setIslibrarian(int islibrarian) {
		this.islibrarian = islibrarian;
	}

	public Image getProfilePic() {
		return this.profilePic;
	}

	public void setProfilePic(Image profilePic) {
		this.profilePic = profilePic;
	}
}
