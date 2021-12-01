package archive.fields;

public class DeviceDataFileInfo extends FieldInfo {
	DeviceDataFileInfo() {
		super();
		this.addFieldSeparator("");
		this.addField(DeviceDataFileFieldNames.DEVICE_DATA_ARRAY, FieldClass.DEVICE_DATA_ARRAY);
		this.addFieldSeparator("");
	}
}
