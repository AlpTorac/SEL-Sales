package model.connectivity;

public class FileDeviceDataSerialiser extends DeviceDataSerialiser {
	public FileDeviceDataSerialiser() {
		super(new FileDeviceDataFormat());
	}
	
	@Override
	public String serialiseDeviceDatas(IDeviceData[] cds) {
		String result = "";
		result += this.getFormat().getStartIndicator();
		for (int i = 0; i < cds.length; i++) {
			result += this.serialiseDeviceData(cds[i]);
		}
		result += this.getFormat().getEndIndicator();
		return result;
	}

	@Override
	public String serialiseDeviceData(IDeviceData cd) {
		String result = "";
		if (cd.getDeviceName() != null) {
			result += cd.getDeviceName() + this.getFormat().getDataFieldSeparator();
		}
		if (cd.getDeviceAddress() != null) {
			result += cd.getDeviceAddress() + this.getFormat().getDataFieldSeparator();
		}
		result += this.serialiseBoolean(cd.getIsAllowedToConnect());
		result += this.getFormat().getDataFieldEnd();
		return result;
	}
}
