package model.filewriter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class FileAccess implements IFileAccess {
	private static String defaultFileName = "file";
	private File activeFile;
	private String folderAddress;
	
	
	public FileAccess(String address) {
		this.folderAddress = address;
		this.activeFile = new File(address+this.getDefaultFileName()+this.getExtension());
		if (!this.activeFile.exists()) {
			this.activeFile = this.createFile(this.getDefaultFileName());
			this.setActiveFile(activeFile);
		}
	}
	public String getDefaultFileName() {
		return defaultFileName;
	}
	protected File createFile(String name) {
		File f = new File(this.getFolderAddress() + File.separator + name + this.getExtension());
		return f;
	}
	protected String getExtension() {
		return ".txt";
	}
	public boolean writeToFile(String stringToWrite) {
		RandomAccessFile w = this.getFileAccessObject();
		try {
			w.seek(w.length());
			w.writeBytes(stringToWrite);
			w.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	protected RandomAccessFile getFileAccessObject() {
		return this.getFileAccessObjectConstructor(this.getActiveFile());
	}
	protected RandomAccessFile getFileAccessObjectConstructor(File f) {
		RandomAccessFile w = null;
		try {
			w = new RandomAccessFile(this.getActiveFile(), "rw");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return w;
	}
	
	public void setFolderAddress(String address) {
		this.folderAddress = address;
	}

	public String getFolderAddress() {
		return this.folderAddress;
	}

	public void setActiveFile(File file) {
		this.activeFile = file;
	}

	public File getActiveFile() {
		return this.activeFile;
	}
}
