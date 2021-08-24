package view.repository;

public class UIButton extends EventShooterUIComponent implements IButton {
	UIButton(IButton component) {
		super(component);
	}
	
	public IButton getComponent() {
		return (IButton) super.getComponent();
	}
	
	public void setCaption(String caption) {
		this.getComponent().setCaption(caption);
	}
}
