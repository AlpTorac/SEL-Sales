package dummy;

import controller.IBusinessEventHandler;

public class DummyHandler implements IBusinessEventHandler {
	@Override
	public void handleBusinessEvent(Object[] args) {
		System.out.println("Showing menu...");
	}
}
