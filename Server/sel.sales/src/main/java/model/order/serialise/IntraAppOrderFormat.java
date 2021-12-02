package model.order.serialise;

import java.time.format.DateTimeFormatter;

public class IntraAppOrderFormat implements IOrderFormat {
	private String orderStart = "";
	private String orderEnd = System.lineSeparator();
	private String orderItemDataFieldSeperator = ",";
	private String orderItemDataEnd = ";";
	private String orderAttributeFieldSeperator = "#";
	private String orderAttributeFieldEnd = ":";
	
	private String dateFormat = "yyyyMMddHHmmssSSS";
	
	@Override
	public DateTimeFormatter getDateFormatter() {
		return DateTimeFormatter.ofPattern(this.dateFormat);
	}

	@Override
	public String getOrderItemFieldSeperator() {
		return this.orderItemDataFieldSeperator;
	}

	@Override
	public String getOrderItemFieldEnd() {
		return this.orderItemDataEnd;
	}

	@Override
	public String getOrderAttributeFieldSeperator() {
		return this.orderAttributeFieldSeperator;
	}

	@Override
	public String getOrderAttributeFieldEnd() {
		return this.orderAttributeFieldEnd;
	}
	
	@Override
	public String getOrderStart() {
		return this.orderStart;
	}

	@Override
	public String getOrderEnd() {
		return this.orderEnd;
	}
}
