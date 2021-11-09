package model.connectivity;

public abstract class DeviceDataFormat implements IDeviceDataFormat {
	
	/**
	 * Start of the serialised Device data (such as: start of the file)
	 */
	private String startIndicator;
	private String dataFieldSeparator;
	private String dataFieldEnd;
	/**
	 * End of the serialised Device data (such as: end of the file)
	 */
	private String endIndicator;
	
	DeviceDataFormat(String startIndicator, String dataFieldSeparator, String dataFieldEnd, String endIndicator) {
		this.startIndicator = startIndicator;
		this.dataFieldSeparator = dataFieldSeparator;
		this.dataFieldEnd = dataFieldEnd;
		this.endIndicator = endIndicator;
	}
	
	@Override
	public String getDataFieldSeparator() {
		return this.dataFieldSeparator;
	}
	@Override
	public String getDataFieldEnd() {
		return this.dataFieldEnd;
	}
	@Override
	public String getStartIndicator() {
		return this.startIndicator;
	}
	@Override
	public String getEndIndicator() {
		return this.endIndicator;
	}
}
