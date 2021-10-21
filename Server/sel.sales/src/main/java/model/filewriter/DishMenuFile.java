package model.filewriter;

public abstract class DishMenuFile extends FileAccess {
	private final static String defaultName = "menu";

	public DishMenuFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
