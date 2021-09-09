package model.filewriter;

public class StandardOrderFileWriter extends OrderFileWriter {
	public StandardOrderFileWriter(String address) {
		super(address, new FileOrderSerialiser());
	}
}
