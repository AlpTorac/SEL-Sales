package model.connectivity;

public abstract class ClientDataParser implements IClientDataParser {

	private IClientDataFormat format;
	
	ClientDataParser(IClientDataFormat format) {
		this.format = format;
	}
	
	@Override
	public IClientDataFormat getFormat() {
		return this.format;
	}

}
