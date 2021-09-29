package controller.manager;

import controller.IApplicationEvent;
import controller.handler.IApplicationEventHandler;

public interface IApplicationEventManager {
	void addApplicationEventToHandlerMapping(IApplicationEvent event, IApplicationEventHandler handler);
	IApplicationEventHandler getHandler(IApplicationEvent event);
}
