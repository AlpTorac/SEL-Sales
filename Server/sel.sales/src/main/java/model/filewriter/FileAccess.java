package model.filewriter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class FileAccess implements IFileAccess {
	private final static String extension = ".txt";
	private File activeFile;
	private String folderAddress;
	private RandomAccessFile raf;
	
	public FileAccess(String address) {
		this.folderAddress = address;
//		this.adaptActiveFile();
	}
	@Override
	public String getFilePath() {
		return this.getActiveFile().getPath();
	}
	@Override
	public boolean deleteFile() {
		if (this.activeFile != null && this.activeFile.exists()) {
			this.closeRAF();
			return this.activeFile.delete();
		}
		return false;
	}
	protected boolean closeRAF() {
		if (this.raf != null) {
			try {
				this.raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.raf = null;
		}
		return this.raf == null;
	}
	protected String buildActiveFilePath() {
		return this.getFolderAddress()+File.separator+this.getDefaultFileName()+this.getExtension();
	}
	protected void remakeFile() {
		this.deleteFile();
		this.adaptActiveFile();
	}
	protected void adaptActiveFile() {
		if (this.getFolderAddress() != null && new File(this.getFolderAddress()).exists()) {
			this.setActiveFile(this.createFile());
		}
//		else {
//			this.activeFile = new File(this.buildActiveFilePath());	
//		}
//		this.setActiveFile(activeFile);
	}
	public abstract String getDefaultFileName();
	
	protected File createFile() {
		if (this.getFolderAddress() != null && new File(this.getFolderAddress()).exists()) {
			File f = new File(this.buildActiveFilePath());
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return f;
		} else {
			return null;
		}
	}
	@Override
	public String getExtension() {
		return extension;
	}
	public static String getExtensionForClass() {
		return extension;
	}
	public boolean writeToFile(String stringToWrite) {
		RandomAccessFile w = this.getFileAccessObject();
		if (w != null && new File(this.getFolderAddress()).exists()) {
			try {
				w.seek(w.length());
				w.writeBytes(stringToWrite);
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	public String readFile() {
		String result = "";
		RandomAccessFile w = this.getFileAccessObject();
		if (w != null && new File(this.getFolderAddress()).exists()) {
			Byte read = 0;
			try {
				while (w.getFilePointer() <= w.length() - 1 && (read = w.readByte()) > -1) {
					result += new String(new byte[] {read});
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	protected RandomAccessFile getFileAccessObject() {
		this.closeRAF();
		this.raf = null;
		this.raf = this.constructFileAccessObject(this.getActiveFile());
		return this.raf;
	}
	protected RandomAccessFile constructFileAccessObject(File f) {
		if (f != null && f.exists()) {
			try {
				return new RandomAccessFile(f, "rw");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void setFolderAddress(String address) {
		this.folderAddress = address;
		this.adaptActiveFile();
	}

	public String getFolderAddress() {
		return this.folderAddress;
	}

	protected void setActiveFile(File file) {
		this.activeFile = file;
	}

	protected File getActiveFile() {
		if (this.activeFile == null) {
			this.activeFile = this.createFile();
		}
		return this.activeFile;
	}
}
