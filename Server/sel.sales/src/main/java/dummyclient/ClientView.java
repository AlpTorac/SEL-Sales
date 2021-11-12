package dummyclient;

import controller.IController;
import model.IModel;
import view.View;

public class ClientView extends View {
	
	public ClientView(IController controller, IModel model) {
		super(controller, model);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refreshMenu() {
		System.out.println("Menu refreshed");
	}

	@Override
	public void refreshDiscoveredDevices() {
		System.out.println("Discovered Devices refreshed");
	}

	@Override
	public void refreshKnownDevices() {
		System.out.println("Known Devices refreshed");
	}

	@Override
	public void refreshSettings() {
		System.out.println("Settings changed");
	}

	@Override
	public void refreshOrders() {
		System.out.println("Orders refreshed");
	}

}
