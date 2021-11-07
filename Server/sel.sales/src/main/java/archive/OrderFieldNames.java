package archive;

public enum OrderFieldNames implements IFieldNameEnum {
	ORDER_ID("orderID"),
	ORDER_DATE("orderDate"),
	IS_CASH("isCash"),
	IS_HERE("isHere"),
	
	;

	private String fieldName;
	
	private OrderFieldNames(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public IFieldNameEnum[] getValues() {
		return OrderFieldNames.values();
	}

	@Override
	public String serialiseFieldName() {
		return this.fieldName;
	}

}
