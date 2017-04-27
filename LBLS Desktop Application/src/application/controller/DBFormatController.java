package application.controller;

import java.sql.Date;
import java.time.LocalDate;

public class DBFormatController {
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
}