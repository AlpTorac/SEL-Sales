package archive;

public enum ClientDataFileFieldNames implements IFieldNameEnum {
	CLIENT_DATA_ARRAY("clientDataArray")
	;
	
	private String fieldName;
	
	private ClientDataFileFieldNames(String fieldName) {
		this.fieldName = fieldName;
	}

	@Override
	public IFieldNameEnum[] getValues() {
		return ClientDataFileFieldNames.values();
	}

	@Override
	public String serialiseFieldName() {
		return this.fieldName;
	}
}
