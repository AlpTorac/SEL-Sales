package model.filewriter;

public interface IFileAccess {
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
}
