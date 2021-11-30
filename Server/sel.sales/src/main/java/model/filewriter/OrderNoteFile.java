package model.filewriter;

public abstract class OrderNoteFile extends FileAccess {
	private final static String defaultName = "orderNote";
	
	public OrderNoteFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
