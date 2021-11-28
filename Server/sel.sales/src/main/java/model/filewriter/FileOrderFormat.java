package model.filewriter;

import java.time.format.DateTimeFormatter;

import model.order.serialise.IOrderFormat;

public class FileOrderFormat implements IOrderFormat {
	private String orderStart = "";
	private String orderEnd = System.lineSeparator();
	private String orderItemDataFieldSeperator = ",";
	private String orderItemDataEnd = ";";
	private String orderDataFieldSeperator = "#";
	private String orderDataFieldEnd = ":";
	// private String dateFormat = "yyyyMMdd"+this.getOrderFormat().getOrderDataFieldSeperator()+"HHmmss";
	private String dateFormat = "yyyyMMddHHmmssSSS";
	
	@Override
	public DateTimeFormatter getDateFormatter() {
		return DateTimeFormatter.ofPattern(dateFormat);
	}

	@Override
	public String getOrderItemDataFieldSeperator() {
		return this.orderItemDataFieldSeperator;
	}

	@Override
	public String getOrderItemDataFieldEnd() {
		return this.orderItemDataEnd;
	}

	@Override
	public String getOrderAttributeFieldSeperator() {
		return this.orderDataFieldSeperator;
	}

	@Override
	public String getOrderAttributeFieldEnd() {
		return this.orderDataFieldEnd;
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
