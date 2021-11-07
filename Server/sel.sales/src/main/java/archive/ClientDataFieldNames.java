package archive;

public enum ClientDataFieldNames implements IFieldNameEnum {
	CLIENT_NAME("clientName"),
	CLIENT_ADDRESS("clientAddress"),
	IS_ALLOWED_TO_CONNECT("isAllowedToConnect")
	;

	private String fieldName;
	
	ClientDataFieldNames(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public IFieldNameEnum[] getValues() {
		return ClientDataFieldNames.values();
	}

	@Override
	public String serialiseFieldName() {
		return this.fieldName;
	}

}
