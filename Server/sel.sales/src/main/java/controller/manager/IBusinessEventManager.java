package controller.manager;

import controller.BusinessEvent;
import controller.handler.IBusinessEventHandler;

public interface IBusinessEventManager {
	public void addBusinessEventToHandlerMapping(BusinessEvent event, IBusinessEventHandler handler);
	public IBusinessEventHandler getHandler(BusinessEvent event);
}
