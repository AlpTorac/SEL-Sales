package controller;

import controller.handler.IApplicationEventHandler;
import model.IModel;

public interface IController {
	void addApplicationEventMapping(IApplicationEvent event, IApplicationEventHandler handler);
	void handleApplicationEvent(IApplicationEvent event, Object[] args);
	IModel getModel();
}