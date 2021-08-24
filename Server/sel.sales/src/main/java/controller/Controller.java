package controller;

import java.util.HashMap;

public abstract class Controller implements IController {
	private HashMap<BusinessEvent, IEventHandler> eventToHandlerMap = new HashMap<BusinessEvent, IEventHandler>();
	
	public void addEventToHandlerMapping(BusinessEvent event, IEventHandler handler) {
		this.eventToHandlerMap.put(event, handler);
	}
	
	public IEventHandler getHandler(BusinessEvent event) {
		return this.eventToHandlerMap.get(event);
	}
}
