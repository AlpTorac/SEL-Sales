package view;

import java.time.LocalDateTime;

public interface IDateSettings {
	LocalDateTime parseDate(String date);
	String getTimeInDay(LocalDateTime date);
	String getDateInYear(LocalDateTime date);
}
