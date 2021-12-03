package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateSettings {
	private String dateInYearSeperator = "/";
	private String timeInDaySeperator = ":";
	private String dateInYearPattern = "dd"+dateInYearSeperator+"MM"+dateInYearSeperator+"yyyy";
	private String timeInDayPattern = "HH"+timeInDaySeperator+"mm"+timeInDaySeperator+"ss"+timeInDaySeperator+"SSS";
	
	private DateTimeFormatter fullFormatter = DateTimeFormatter.ofPattern(dateInYearPattern+timeInDayPattern);
	private DateTimeFormatter dateInYearFormat = DateTimeFormatter.ofPattern(dateInYearPattern);
	private DateTimeFormatter timeInDayFormat = DateTimeFormatter.ofPattern(timeInDayPattern);
	
	public DateSettings() {
		
	}
	
	public String serialiseDateFromNow() {
		return this.serialiseDate(LocalDateTime.now());
	}
	
	public LocalDateTime parseDate(String date) {
		return LocalDateTime.parse(date, this.fullFormatter);
	}
	
	public String getTimeInDay(LocalDateTime date) {
		return timeInDayFormat.format(date);
	}
	
	public String getDateInYear(LocalDateTime date) {
		return dateInYearFormat.format(date);
	}

	public String serialiseDate(LocalDateTime date) {
		return this.fullFormatter.format(date);
	}
}
