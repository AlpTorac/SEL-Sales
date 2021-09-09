package model.filewriter;

import model.order.IOrderData;

public abstract class OrderFileWriter extends FileAccess {
	private FileOrderSerialiser serialiser;
	
	public OrderFileWriter(String address, FileOrderSerialiser serialiser) {
		super(address);
		this.serialiser = serialiser;
	}
	
	protected FileOrderSerialiser getSerialiser() {
		return this.serialiser;
	}
	public boolean writeOrderData(IOrderData d) {
		return this.writeToFile(this.getSerialiser().serialiseOrderData(d));
	}
}
