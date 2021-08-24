package controller;

public interface IBusinessEventManager {
	public void addBusinessEventToHandlerMapping(BusinessEvent event, IBusinessEventHandler handler);
	public IBusinessEventHandler getHandler(BusinessEvent event);
}
