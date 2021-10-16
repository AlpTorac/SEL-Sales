package model.filewriter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public abstract class FileAccess implements IFileAccess {
	private File activeFile;
	private String folderAddress;
	
	
	public FileAccess(String address) {
		this.folderAddress = address;
		this.adaptActiveFile();
	}
	protected void adaptActiveFile() {
		if (this.activeFile == null || !this.activeFile.exists()) {
			this.activeFile = this.createFile(this.getDefaultFileName());
		} else {
			this.activeFile = new File(this.getFolderAddress()+File.separator+this.getDefaultFileName()+this.getExtension());	
		}
		this.setActiveFile(activeFile);
	}
	protected abstract String getDefaultFileName();
	
	protected File createFile(String name) {
		File f = new File(this.getFolderAddress() + File.separator + name + this.getExtension());
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public String readFile() {
		String result = "";
		RandomAccessFile w = this.getFileAccessObject();
		Byte read = 0;
		try {
			while ((read = w.readByte()) > -1) {
				result += new String(new byte[] {read});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
		this.adaptActiveFile();
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
