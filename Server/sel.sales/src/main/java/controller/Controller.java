package controller;

public abstract class Controller implements IController {
	private IBusinessEventManager eventManager;
	
	public Controller() {
		this.eventManager = this.initEventManager();
	}
	
	public void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler) {
		this.eventManager.addBusinessEventToHandlerMapping(event, handler);
	}
	
	public void handleBusinessEvent(BusinessEvent event, Object[] args) {
		this.eventManager.getHandler(event).handleBusinessEvent(args);
	}
	
	protected abstract IBusinessEventManager initEventManager();
}
