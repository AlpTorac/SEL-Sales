package view.repository.uiwrapper;

public abstract class ClickEventListener extends EditPart {
	protected ClickEventListener() {
		super();
	}
	public void clickAction() {
		
	}
	public void clickAction(Object[] parameters) {
		
	}
	public void clickAction(int amountOfClicks) {
		if (amountOfClicks == 1) {
			this.clickAction();
		} else {
			this.multiClickAction(amountOfClicks);
		}
	}
	public void clickAction(int amountOfClicks, Object[] parameters) {
		if (amountOfClicks == 1) {
			this.clickAction(parameters);
		} else {
			this.multiClickAction(amountOfClicks, parameters);
		}
	}
	public void multiClickAction(int amountOfClicks) {
		if (amountOfClicks == 1) {
			this.clickAction();
		}
	}
	public void multiClickAction(int amountOfClicks, Object[] parameters) {
		if (amountOfClicks == 1) {
			this.clickAction(parameters);
		}
	}
//	public void action() {
//		this.clickAction();
//	}
//	
//	public void action(Object[] parameters) {
//		this.clickAction(parameters);
//	}
//	public void action(int numberOfInputEvents) {
//		this.multiClickAction(numberOfInputEvents);
//	}
//	public void action(int numberOfInputEvents, Object[] parameters) {
//		this.multiClickAction(numberOfInputEvents, parameters);
//	}
}
