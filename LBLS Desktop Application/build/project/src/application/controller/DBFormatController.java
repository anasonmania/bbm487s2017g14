package application.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.time.LocalDate;

public abstract class DBFormatController {
	public static Date dateToDatabase(LocalDate entityValue) {
		if(entityValue == null)
			return null;
		else
			return Date.valueOf(entityValue);
	}

	public static LocalDate dateToJava(Date databaseValue) {
		if(databaseValue==null)
			return null;
		else
			return databaseValue.toLocalDate();
	}

	public static String passToDatabase(String password) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(password.getBytes());
		byte[] b = md.digest();
		StringBuffer sb = new StringBuffer();
		for(byte b1: b){
			sb.append(Integer.toHexString(b1 & 0xff).toString());
		}
		return sb.toString();
	}

//	public static Image imageToJava(){
//
//	}
//
//	public static Byte[] imageToDatabase(){
//
//	}

}