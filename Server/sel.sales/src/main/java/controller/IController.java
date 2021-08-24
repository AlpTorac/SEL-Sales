package controller;

public interface IController {
	public void addBusinessEventMapping(BusinessEvent event, IBusinessEventHandler handler);
	public void handleBusinessEvent(BusinessEvent event, Object[] args);
}
