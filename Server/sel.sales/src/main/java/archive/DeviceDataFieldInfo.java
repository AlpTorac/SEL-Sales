package archive;

public class DeviceDataFieldInfo extends FieldInfo {
	DeviceDataFieldInfo() {
		super();
		this.addFieldSeparator("");
		this.addField(DeviceDataFieldNames.DEVICE_NAME, FieldClass.STRING);
		this.addFieldSeparator(",");
		this.addField(DeviceDataFieldNames.DEVICE_ADDRESS, FieldClass.STRING);
		this.addFieldSeparator(",");
		this.addField(DeviceDataFieldNames.IS_ALLOWED_TO_CONNECT, FieldClass.BOOLEAN);
		this.addFieldSeparator(";"+System.lineSeparator());
	}
}
