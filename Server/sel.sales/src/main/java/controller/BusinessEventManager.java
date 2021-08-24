package controller;

import java.util.HashMap;

public class BusinessEventManager implements IBusinessEventManager {
	private HashMap<BusinessEvent, IBusinessEventHandler> eventToHandlerMap = new HashMap<BusinessEvent, IBusinessEventHandler>();
	
	public void addBusinessEventToHandlerMapping(BusinessEvent event, IBusinessEventHandler handler) {
		this.eventToHandlerMap.put(event, handler);
	}
	
	public IBusinessEventHandler getHandler(BusinessEvent event) {
		return this.eventToHandlerMap.get(event);
	}
}
