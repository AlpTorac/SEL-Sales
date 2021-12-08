package model.filewriter;

public class StandardFileAccess extends FileAccess {
	private String defaultFileName;
	
	public StandardFileAccess(String folderAddress, String fileName) {
		super(folderAddress);
		this.defaultFileName = fileName;
		this.setAddress(folderAddress);
	}
	
	public StandardFileAccess(String address) {
		super(address);
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
