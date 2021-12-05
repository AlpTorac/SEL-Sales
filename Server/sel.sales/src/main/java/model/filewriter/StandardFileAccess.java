package model.filewriter;

public class StandardFileAccess extends FileAccess {
	private final String defaultFileName;
	
	public StandardFileAccess(String address, String fileName) {
		super(address);
		this.defaultFileName = fileName;
		this.setAddress(address);
	}

	@Override
	public String getDefaultFileName() {
		if (this.defaultFileName == null) {
			return super.getDefaultFileName();
		}
		return this.defaultFileName;
	}
}
