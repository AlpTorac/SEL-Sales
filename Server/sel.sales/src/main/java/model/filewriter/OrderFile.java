package model.filewriter;

public abstract class OrderFile extends FileAccess {
	private final static String defaultName = "orders";
	
	public OrderFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
