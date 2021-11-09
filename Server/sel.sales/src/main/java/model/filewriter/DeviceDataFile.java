package model.filewriter;

public abstract class DeviceDataFile extends FileAccess {
	private final static String defaultName = "knownDevices";
	
	public DeviceDataFile(String address) {
		super(address);
	}
	public String getDefaultFileName() {
		return defaultName;
	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
}
