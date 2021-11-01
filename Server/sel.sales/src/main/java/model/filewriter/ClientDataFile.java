package model.filewriter;

public abstract class ClientDataFile extends FileAccess {
	private final static String defaultName = "knownclients";
	
	public ClientDataFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
