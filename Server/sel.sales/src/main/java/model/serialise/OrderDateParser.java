package model.serialise;

import java.time.format.DateTimeFormatter;

public class OrderDateParser implements IOrderDateParser {
	private String dateFormat = "yyyyMMddHHmmssSSS";

	@Override
	public DateTimeFormatter getFormatter() {
		return DateTimeFormatter.ofPattern(this.dateFormat);
	}

}
