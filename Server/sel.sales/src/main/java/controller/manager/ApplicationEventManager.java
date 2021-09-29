package controller.manager;

import java.util.HashMap;

import controller.IApplicationEvent;
import controller.handler.IApplicationEventHandler;

public class ApplicationEventManager implements IApplicationEventManager {
	private HashMap<IApplicationEvent, IApplicationEventHandler> eventToHandlerMap = new HashMap<IApplicationEvent, IApplicationEventHandler>();
	
	public void addApplicationEventToHandlerMapping(IApplicationEvent event, IApplicationEventHandler handler) {
		this.eventToHandlerMap.put(event, handler);
	}
	
	public IApplicationEventHandler getHandler(IApplicationEvent event) {
		return this.eventToHandlerMap.get(event);
	}
}
