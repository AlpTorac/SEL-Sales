package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateSettings implements IDateSettings {
	private String dateInYearSeperator = "/";
	private String timeInDaySeperator = ":";
	private String dateInYearPattern = "dd"+dateInYearSeperator+"MM"+dateInYearSeperator+"yyyy";
	private String timeInDayPattern = "HH"+timeInDaySeperator+"mm"+timeInDaySeperator+"ss"+timeInDaySeperator+"SSS";
	private DateTimeFormatter dateInYearFormat = DateTimeFormatter.ofPattern(dateInYearPattern);
	private DateTimeFormatter timeInDayFormat = DateTimeFormatter.ofPattern(timeInDayPattern);
	
	public DateSettings() {
		
	}
	
	public LocalDateTime parseDate(String date) {
		return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(dateInYearPattern+timeInDayPattern));
	}
	
	public String getTimeInDay(LocalDateTime date) {
		return timeInDayFormat.format(date);
	}
	
	public String getDateInYear(LocalDateTime date) {
		return dateInYearFormat.format(date);
	}
}
