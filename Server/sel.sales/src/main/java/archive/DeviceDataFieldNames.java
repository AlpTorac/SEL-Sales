package archive;

public enum DeviceDataFieldNames implements IFieldNameEnum {
	DEVICE_NAME("deviceName"),
	DEVICE_ADDRESS("deviceAddress"),
	IS_ALLOWED_TO_CONNECT("isAllowedToConnect")
	;

	private String fieldName;
	
	DeviceDataFieldNames(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public IFieldNameEnum[] getValues() {
		return DeviceDataFieldNames.values();
	}

	@Override
	public String serialiseFieldName() {
		return this.fieldName;
	}

}
