package view.repository;

public abstract class EditPart {
	private IEventShooterUIComponent component;
	
	EditPart(IEventShooterUIComponent component) {
		this.component = component;
		this.attachListener();
	}
	
	protected IEventShooterUIComponent getComponent() {
		return this.component;
	}
	
	protected abstract void attachListener();
	
	public abstract void action();
}
