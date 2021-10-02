package view.composites;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.StatusEvent;
import model.connectivity.IClientData;
import view.repository.ITable;
import view.repository.uiwrapper.ClickEventListener;

public class RemoveKnownClientListener extends ClickEventListener implements IApplicationEventShooter {

	private ITable<IClientData> knownClients;
	private IController controller;
	
	RemoveKnownClientListener(IController controller, ITable<IClientData> knownClients) {
		this.controller = controller;
		this.knownClients = knownClients;
	}
	
	public void clickAction() {
		this.fireApplicationEvent(this.controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.knownClients.getSelectedElement().getClientAddress()};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return StatusEvent.KNOWN_CLIENT_REMOVED;
	}
}
