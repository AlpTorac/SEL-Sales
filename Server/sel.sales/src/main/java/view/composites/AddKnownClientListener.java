package view.composites;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.StatusEvent;
import model.connectivity.IClientData;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class AddKnownClientListener extends ClickEventListener implements IApplicationEventShooter {

	private ITable<IClientData> discoveredClients;
	private IController controller;
	
	AddKnownClientListener(IController controller, ITable<IClientData> discoveredClients) {
		this.controller = controller;
		this.discoveredClients = discoveredClients;
	}
	
	public void clickAction() {
		this.fireApplicationEvent(this.controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.discoveredClients.getSelectedElement().getClientAddress()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return StatusEvent.KNOWN_CLIENT_ADDED;
	}
}
