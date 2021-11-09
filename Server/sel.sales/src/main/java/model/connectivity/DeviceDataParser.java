package model.connectivity;

public abstract class DeviceDataParser implements IDeviceDataParser {

	private IDeviceDataFormat format;
	
	DeviceDataParser(IDeviceDataFormat format) {
		this.format = format;
	}
	
	@Override
	public IDeviceDataFormat getFormat() {
		return this.format;
	}

}
