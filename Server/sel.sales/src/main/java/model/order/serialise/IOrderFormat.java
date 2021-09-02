package model.order.serialise;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface IOrderFormat {
	default LocalDateTime parseDate(String date) {
		return LocalDateTime.parse(date, this.getDateFormatter());
	}
	default String formatDate(LocalDateTime date) {
		return this.getDateFormatter().format(date);
	}
	
	DateTimeFormatter getDateFormatter();
	
	String getOrderItemDataFieldSeperator();
	String getOrderItemDataNewLine();
	String getOrderDataFieldSeperator();
	String getOrderDataFieldEnd();
}
