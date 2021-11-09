package archive;

public enum DeviceDataFileFieldNames implements IFieldNameEnum {
	DEVICE_DATA_ARRAY("deviceDataArray")
	;
	
	private String fieldName;
	
	private DeviceDataFileFieldNames(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public IFieldNameEnum[] getValues() {
		return DeviceDataFileFieldNames.values();
	}

	@Override
	public String serialiseFieldName() {
		return this.fieldName;
	}
}
