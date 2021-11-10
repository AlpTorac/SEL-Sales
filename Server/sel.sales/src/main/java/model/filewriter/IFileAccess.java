package model.filewriter;

import java.io.Closeable;

public interface IFileAccess extends Closeable {
	boolean deleteFile();
	boolean writeToFile(String stringToWrite);
	String readFile();
	String getFileName();
	String getDefaultFileName();
	String getFilePath();
	String getExtension();
	boolean isAddressValid();
	boolean fileExists();
	boolean fileEmpty();
	boolean remakeFile();
	void close();
	void setAddress(String address);
}
