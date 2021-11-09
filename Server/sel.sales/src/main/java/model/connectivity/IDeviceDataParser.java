package model.connectivity;

import model.util.IParser;

public interface IDeviceDataParser extends IParser {
	IDeviceData[] parseDeviceDatas(String serialisedDeviceDatas);
	IDeviceData parseDeviceData(String serialisedDeviceData);
	IDeviceDataFormat getFormat();
	default String getSerialisedDeviceDatasBody(String serialisedDeviceDatas) {
		int begin = 0;
		int end = serialisedDeviceDatas.length();
		if (serialisedDeviceDatas.startsWith(this.getFormat().getStartIndicator())) {
			begin += this.getFormat().getStartIndicator().length();
		}
		if (serialisedDeviceDatas.endsWith(this.getFormat().getEndIndicator())) {
			end -= this.getFormat().getEndIndicator().length();
		}
		return serialisedDeviceDatas.substring(begin, end);
	}
}
