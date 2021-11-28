package model.order;

public enum OrderStatus {
	CANCELLED("cancelled"),
	EDITING("editing"),
	COOKING("cooking"),
	PENDING_PAYMENT("pendingPayment"),
	PENDING_SEND("pendingSend"),
	SENT("sent"),
	
	UNCONFIRMED("unconfirmed"),
	CONFIRMED("confirmed"),
	
	PAST("past"),
	UNDEFINED("undefined")
	;
	private String serialisedVersion;
	
	private OrderStatus(String serialisedVersion) {
		this.serialisedVersion = serialisedVersion;
	}
	
	public static OrderStatus stringToOrderStatus(String serialisedVersion) {
		for (OrderStatus os : OrderStatus.values()) {
			if (os.getSerialisedVersion().equals(serialisedVersion)) {
				return os;
			}
		}
		return null;
	}
	
	public String getSerialisedVersion() {
		return this.serialisedVersion;
	}
}
