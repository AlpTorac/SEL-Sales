package controller;

public class DummyHandler implements IBusinessEventHandler {
	@Override
	public void handleBusinessEvent(Object[] args) {
		System.out.println("Showing menu...");
	}
}
