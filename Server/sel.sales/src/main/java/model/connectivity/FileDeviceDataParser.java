package model.connectivity;

import java.util.ArrayList;
import java.util.Collection;

public class FileDeviceDataParser extends DeviceDataParser {
	public FileDeviceDataParser() {
		super(new FileDeviceDataFormat());
	}

	@Override
	public IDeviceData[] parseDeviceDatas(String serialisedDeviceDatas) {
		Collection<IDeviceData> cds = new ArrayList<IDeviceData>();
//		String body = this.getSerialisedDeviceDatasBody(serialisedDeviceDatas);
		String body = this.getDataBody(serialisedDeviceDatas, this.getFormat().getStartIndicator(), this.getFormat().getEndIndicator());
		String[] serialisedDatas = body.split(this.getFormat().getDataFieldEnd());
		for (String sd : serialisedDatas) {
			if (sd != null && sd.length() > 0) {
				cds.add(parseDeviceData(sd));
			}
		}
		return cds.toArray(IDeviceData[]::new);
	}

	@Override
	public IDeviceData parseDeviceData(String serialisedDeviceData) {
//		System.out.println("Serialised data: " + serialisedDeviceData);
		String[] fields = serialisedDeviceData.split(this.getFormat().getDataFieldSeparator());
		
		String name = null;
		String address = null;
		boolean isAllowedToConnect = false;
		
		if (fields.length > 0) {
			name = fields[0];
		} else {
			return null;
		}
		
		if (fields.length > 1) {
			address = fields[1];
		}
		
		if (fields.length > 2) {
			isAllowedToConnect = this.parseBoolean(fields[2]);
		}
//		System.out.println("Serialised name: " + fields[0]);
//		System.out.println("Serialised address: " + fields[1]);
//		System.out.println("Serialised boolean: " + fields[2]);
		return new DeviceData(name, address, isAllowedToConnect);
	}
}
