package model;

import java.time.LocalDateTime;

public interface IDateSettings {
	default String serialiseDateFromNow() {
		return this.serialiseDate(LocalDateTime.now());
	}
	String serialiseDate(LocalDateTime date);
	LocalDateTime parseDate(String date);
	String getTimeInDay(LocalDateTime date);
	String getDateInYear(LocalDateTime date);
}
