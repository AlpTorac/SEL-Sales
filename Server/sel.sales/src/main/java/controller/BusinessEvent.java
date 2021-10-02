package controller;

public enum BusinessEvent implements IApplicationEvent {
	ADD_DISH,
	SHOW_MENU,
	REMOVE_DISH,
	EDIT_DISH,
	
	ADD_ORDER,
	CONFIRM_ORDER,
	REMOVE_ORDER,
	
	DISCOVER_CLIENTS;
}
