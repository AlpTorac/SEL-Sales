package model.filewriter;

public abstract class OrderTableNumberFile extends FileAccess {
	private final static String defaultName = "orderTableNumber";
	
	public OrderTableNumberFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
