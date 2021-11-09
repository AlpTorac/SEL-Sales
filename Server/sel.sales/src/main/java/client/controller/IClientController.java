package client.controller;

import client.model.IClientModel;
import controller.IController;

public interface IClientController extends IController {
	@Override
	IClientModel getModel();
}
