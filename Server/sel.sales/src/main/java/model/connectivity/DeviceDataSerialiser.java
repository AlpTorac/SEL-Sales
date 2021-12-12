package model.connectivity;

public abstract class DeviceDataSerialiser implements IDeviceDataSerialiser {

	private IDeviceDataFormat format;
	
	protected DeviceDataSerialiser(IDeviceDataFormat format) {
		this.format = format;
	}
	
	@Override
	public IDeviceDataFormat getFormat() {
		return this.format;
	}

}
