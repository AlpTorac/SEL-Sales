package model.connectivity;

import model.util.ISerialiser;

public interface IDeviceDataSerialiser extends ISerialiser {
	String serialiseDeviceDatas(IDeviceData[] cds);
	String serialiseDeviceData(IDeviceData cd);
	IDeviceDataFormat getFormat();
}
