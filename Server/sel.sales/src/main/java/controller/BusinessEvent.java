package controller;

public enum BusinessEvent implements IApplicationEvent {
	ADD_DISH,
	SHOW_MENU,
	REMOVE_DISH,
	EDIT_DISH,
	WRITE_DISH_MENU,
	
	ADD_ORDER,
	CONFIRM_ORDER,
	CONFIRM_ALL_ORDERS,
	REMOVE_ORDER,
	WRITE_ORDERS,
	
	DISCOVER_CLIENTS;
}
