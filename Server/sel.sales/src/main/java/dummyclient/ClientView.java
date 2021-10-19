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
	public void refreshUnconfirmedOrders() {
		System.out.println("Unconfirmed orders refreshed");
	}

	@Override
	public void refreshConfirmedOrders() {
		System.out.println("Confirmed orders refreshed");
	}

	@Override
	public void refreshDiscoveredClients() {
		System.out.println("Discovered clients refreshed");
	}

	@Override
	public void refreshKnownClients() {
		System.out.println("Known clients refreshed");
	}

	@Override
	public void refreshConfirmMode() {
		System.out.println("Confirm model changed");
	}

	@Override
	public void refreshSettings() {
		System.out.println("Settings changed");
	}

}
