package model.order.datamapper;

public enum OrderAttribute {
	STATUS("status"),
	IS_WRITTEN("isWritten"),
	TABLE_NUMBER("tableNumber"),
	NOTE("note");
	
	private String description;
	
	private OrderAttribute(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public static OrderAttribute descriptionToAttribute(String description) {
		if (description != null) {
			for (OrderAttribute oa : OrderAttribute.values()) {
				if (description.equals(oa.getDescription())) {
					return oa;
				}
			}
		}
		return null;
	}
}
