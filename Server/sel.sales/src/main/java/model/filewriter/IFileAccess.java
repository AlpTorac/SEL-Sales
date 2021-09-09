package model.filewriter;

public interface IFileAccess {
	boolean writeToFile(String stringToWrite);
	String getDefaultFileName();
}
