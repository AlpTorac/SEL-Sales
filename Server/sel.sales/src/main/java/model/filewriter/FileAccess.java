package model.filewriter;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class FileAccess implements IFileAccess {
	private final static String defaultName = "file";
	private final static String extension = ".txt";
	private File activeFile;
	private String address;
	private RandomAccessFile raf;
	
	public FileAccess(String address) {
		this.address = address;
		this.adaptActiveFile();
	}
//	protected boolean isFileFilled(File f) {
//		return f != null && f.exists() && f.length() > 0;
//	}
	public static String getDefaultFileNameForClass() {
		return defaultName;
	}
	@Override
	public boolean fileExists() {
		return this.activeFile != null && this.activeFile.exists();
	}
	@Override
	public boolean isAddressValid() {
		if (this.getAddress() == null) {
			return false;
		}
		return new File(this.getAddress()).exists();
	}
	@Override
	public String getDefaultFileName() {
		return defaultName;
	}
	
	@Override
	public String getFilePath() {
		File f = this.getActiveFile();
		if (f == null) {
			return null;
		}
		return f.getPath();
	}
	@Override
	public boolean deleteFile() {
		if (this.activeFile != null && this.activeFile.exists()) {
			this.closeRAF();
			boolean isDeleted = this.activeFile.delete();
			this.activeFile = null;
			return isDeleted;
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
		return this.getAddress()+File.separator+this.getDefaultFileName()+this.getExtension();
	}
	protected boolean isActiveFileAddressValid() {
		return new File(this.getEffectiveAddress()).exists();
	}
	protected String getEffectiveAddress() {
		if (this.getAddress() == null) {
			return this.buildActiveFilePath();
		}
		File f = new File(this.getAddress());
		if (!f.isDirectory()) {
			return f.getPath();
		} else {
			return this.buildActiveFilePath();
		}
	}
	@Override
	public boolean remakeFile() {
		this.deleteFile();
		this.adaptActiveFile();
		return this.isActiveFileAddressValid();
	}
	protected boolean adaptActiveFile() {
		if (this.isAddressValid()) {
			this.setActiveFile(this.loadFile());
			return this.isActiveFileAddressValid();
		}
		return false;
//		else {
//			this.activeFile = new File(this.buildActiveFilePath());	
//		}
//		this.setActiveFile(activeFile);
	}
	
	protected File loadFile() {
		if (this.isAddressValid()) {
			File f = new File(this.getEffectiveAddress());
			if (f.length() <= 0) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
		if (w != null && this.isAddressValid()) {
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
		if (w != null && this.isAddressValid()) {
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
	
	public void setAddress(String address) {
		this.address = address;
		this.adaptActiveFile();
	}

	public String getAddress() {
		return this.address;
	}

	protected void setActiveFile(File file) {
		this.activeFile = file;
	}

	protected File getActiveFile() {
		if (this.activeFile == null) {
			this.activeFile = this.loadFile();
		}
		return this.activeFile;
	}
	@Override
	public void close() {
		this.closeRAF();
	}
	
}
