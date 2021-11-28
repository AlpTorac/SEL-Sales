package client.controller;

import controller.IApplicationEvent;

public enum ClientSpecificEvent implements IApplicationEvent {
	ADD_PENDING_PAYMENT_ORDER,
	SEND_ORDER,
	EDIT_ORDER,
	
	ORDER_SENT,
	MENU_RECEIVED,
	AVAILABLE_TABLE_NUMBERS_RECEIVED
	;
}
