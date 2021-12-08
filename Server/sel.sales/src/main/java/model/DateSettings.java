package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateSettings {
	private String dateInYearSeperator = "/";
	private String timeInDaySeperator = ":";
	private String dateInYearPattern = "dd"+dateInYearSeperator+"MM"+dateInYearSeperator+"yyyy";
	private String timeInDayPattern = "HH"+timeInDaySeperator+"mm"+timeInDaySeperator+"ss"+timeInDaySeperator+"SSS";
	
	private DateTimeFormatter fullFormatterWithSeparators = DateTimeFormatter.ofPattern(dateInYearPattern+timeInDayPattern);
	private DateTimeFormatter fullFormatterWithoutSeparators = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
	
	private DateTimeFormatter dateInYearFormat = DateTimeFormatter.ofPattern(dateInYearPattern);
	private DateTimeFormatter timeInDayFormat = DateTimeFormatter.ofPattern(timeInDayPattern);
	
	private DateTimeFormatter exportFileNameFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
	
	public DateSettings() {
		
	}
	
	public String serialiseDateForExportFileName() {
		return this.exportFileNameFormat.format(LocalDateTime.now());
	}
	
	public String serialiseDateFromNow() {
		return this.serialiseDateWithoutSeparators(LocalDateTime.now());
	}
	
	public LocalDateTime parseDateWithoutSeparators(String date) {
		return LocalDateTime.parse(date, this.fullFormatterWithoutSeparators);
	}
	
	public String getTimeInDay(LocalDateTime date) {
		return timeInDayFormat.format(date);
	}
	
	public String getDateInYear(LocalDateTime date) {
		return dateInYearFormat.format(date);
	}

	public String serialiseDateWithoutSeparators(LocalDateTime date) {
		return this.fullFormatterWithoutSeparators.format(date);
	}

	public LocalDateTime parseDateWithSeparators(String string) {
		return LocalDateTime.parse(string, this.fullFormatterWithSeparators);
	}
}
