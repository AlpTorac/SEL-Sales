package model.connectivity;

public interface IDeviceDataFormat {
	String getStartIndicator();
	String getDataFieldSeparator();
	String getDataFieldEnd();
	String getEndIndicator();
}
