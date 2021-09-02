package model.order.serialise;

import java.time.format.DateTimeFormatter;

public class OrderFormat implements IOrderFormat {
	private String orderItemDataFieldSeperator = ",";
	private String orderItemDataNewLine = ";";
	private String orderDataFieldSeperator = "-";
	private String orderDataFieldEnd = ":";
	
	private String dateFormat = "yyyyMMddHHmmssSSS";
	
	@Override
	public DateTimeFormatter getDateFormatter() {
		return DateTimeFormatter.ofPattern(this.dateFormat);
	}

	@Override
	public String getOrderItemDataFieldSeperator() {
		return this.orderItemDataFieldSeperator;
	}

	@Override
	public String getOrderItemDataNewLine() {
		return this.orderItemDataNewLine;
	}

	@Override
	public String getOrderDataFieldSeperator() {
		return this.orderDataFieldSeperator;
	}

	@Override
	public String getOrderDataFieldEnd() {
		return this.orderDataFieldEnd;
	}
}
