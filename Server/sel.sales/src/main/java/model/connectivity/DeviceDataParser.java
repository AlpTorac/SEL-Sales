package model.connectivity;

public abstract class DeviceDataParser implements IDeviceDataParser {

	private IDeviceDataFormat format;
	
	protected DeviceDataParser(IDeviceDataFormat format) {
		this.format = format;
	}
	
	@Override
	public IDeviceDataFormat getFormat() {
		return this.format;
	}

}
