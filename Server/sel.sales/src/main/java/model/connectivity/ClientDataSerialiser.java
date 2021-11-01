package model.connectivity;

public abstract class ClientDataSerialiser implements IClientDataSerialiser {

	private IClientDataFormat format;
	
	ClientDataSerialiser(IClientDataFormat format) {
		this.format = format;
	}
	
	@Override
	public IClientDataFormat getFormat() {
		return this.format;
	}

}
