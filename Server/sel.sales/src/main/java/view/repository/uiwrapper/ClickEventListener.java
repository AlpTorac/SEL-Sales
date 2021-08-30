package view.repository.uiwrapper;

public abstract class ClickEventListener extends EditPart {
	protected ClickEventListener() {
		super();
	}
	protected void clickAction() {
		
	}
	protected void clickAction(Object[] parameters) {
		
	}
	protected void multiClickAction(int amountOfClicks) {
		if (amountOfClicks == 1) {
			this.clickAction();
		}
	}
	protected void multiClickAction(int amountOfClicks, Object[] parameters) {
		if (amountOfClicks == 1) {
			this.clickAction(parameters);
		}
	}
	public void action() {
		this.clickAction();
	}
	
	public void action(Object[] parameters) {
		this.clickAction(parameters);
	}
	public void action(int numberOfInputEvents) {
		this.multiClickAction(numberOfInputEvents);
	}
	public void action(int numberOfInputEvents, Object[] parameters) {
		this.multiClickAction(numberOfInputEvents, parameters);
	}
}
