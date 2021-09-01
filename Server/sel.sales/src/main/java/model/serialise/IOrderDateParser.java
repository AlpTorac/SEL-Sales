package model.serialise;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface IOrderDateParser {
	default LocalDateTime parseDate(String date) {
		return LocalDateTime.parse(date, this.getFormatter());
	}
	DateTimeFormatter getFormatter();
}
