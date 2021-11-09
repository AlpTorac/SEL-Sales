package server.controller;

import controller.IController;
import server.model.IServerModel;

public interface IServerController extends IController {
	@Override
	IServerModel getModel();
}
