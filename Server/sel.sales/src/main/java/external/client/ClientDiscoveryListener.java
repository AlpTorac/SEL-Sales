package external.client;

import controller.IApplicationEvent;
import controller.IApplicationEventShooter;
import controller.IController;
import controller.StatusEvent;

public class ClientDiscoveryListener implements IApplicationEventShooter {

	private IController controller;
	private String clientName;
	private String clientAddress;
	
	public ClientDiscoveryListener(IController controller) {
		this.controller = controller;
	}
	
	public void clientDiscovered(String clientName, String clientAddress) {
		this.clientName = clientName;
		this.clientAddress = clientAddress;
		this.fireApplicationEvent(controller);
	}
	
	@Override
	public Object[] getArgs() {
		return new Object[] {this.clientName, this.clientAddress};
	}

	@Override
	public IApplicationEvent getApplicationEvent() {
		return StatusEvent.CLIENT_DISCOVERED;
	}
	
}
