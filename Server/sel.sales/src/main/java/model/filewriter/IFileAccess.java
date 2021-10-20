package model.filewriter;

public interface IFileAccess {
	boolean deleteFile();
	boolean writeToFile(String stringToWrite);
	String readFile();
	String getDefaultFileName();
	String getFilePath();
	String getExtension();
	boolean isFolderAddressValid();
	boolean fileExists();
	boolean remakeFile();
}
