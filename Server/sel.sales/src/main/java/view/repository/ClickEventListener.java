package view.repository;

public abstract class ClickEventListener extends EditPart {
	protected ClickEventListener() {
		super();
	}
	protected abstract void clickAction();
	public void action() {
		this.clickAction();
	}
}
