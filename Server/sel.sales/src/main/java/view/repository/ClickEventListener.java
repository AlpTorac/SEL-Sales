package view.repository;

public abstract class ClickEventListener extends EditPart {
	protected ClickEventListener(IEventShooterUIComponent component) {
		super(component);
	}
	protected void attachListener() {
		this.getComponent().addClickListener(this);
	}
	protected abstract void clickAction();
	public void action() {
		this.clickAction();
	}
}
