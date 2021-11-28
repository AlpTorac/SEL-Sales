package model.filewriter;

public abstract class OrderStatusFile extends FileAccess {
	private final static String defaultName = "orderStatus";
	
	public OrderStatusFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
