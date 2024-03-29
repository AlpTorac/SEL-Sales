package test.external.dummy;

import java.io.Closeable;

import client.view.IClientView;
import client.view.StandardClientView;
import controller.IController;
import model.IModel;
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.order.IOrderData;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.IServerView;
import server.view.StandardServerView;
import view.IView;

public class DummyServer extends DummyInteractionPartaker {
	public DummyServer(String serviceID, String serviceName, String serverName, String serverAddress) {
		super(serviceID, serviceName, serverName, serverAddress);
	}
	
	@Override
	protected IModel initModel() {
		return new ServerModel(this.getTestFolderAddress());
	}

	@Override
	protected IController initController() {
		return new StandardServerController(this.getModel());
	}

	@Override
	protected IDummyExternal initExternal() {
		return new DummyServerExternal(this.getServiceID(),
				this.getServiceName(), this.getController(), this.getModel(), true);
	}
	
	@Override
	public IServerModel getModel() {
		return (IServerModel) super.getModel();
	}
	
	@Override
	public IServerController getController() {
		return (IServerController) super.getController();
	}
	
	@Override
	public IDummyExternal getExternal() {
		return (IDummyExternal) super.getExternal();
	}
	
	public IOrderData[] getAllUnconfirmedOrders() {
		return this.getModel().getAllUnconfirmedOrders();
	}
	
	public IOrderData[] getAllConfirmedOrders() {
		return this.getModel().getAllConfirmedOrders();
	}
}
