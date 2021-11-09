package server.controller;

import controller.IApplicationEvent;

public enum ServerSpecificEvent implements IApplicationEvent {
	ADD_DISH,
	REMOVE_DISH,
	EDIT_DISH,
	WRITE_DISH_MENU,
	LOAD_DISH_MENU,
	
	CONFIRM_ORDER,
	CONFIRM_ALL_ORDERS,
	
	ORDER_CONFIRM_MODEL_CHANGED;
}
