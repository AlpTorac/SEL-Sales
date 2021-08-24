package view.repository;

public class UITextBox extends EventShooterUIComponent implements ITextBox {
	UITextBox(ITextBox component) {
		super(component);
	}
	
	public ITextBox getComponent() {
		return (ITextBox) super.getComponent();
	}

	@Override
	public void setCaption(String caption) {
		this.getComponent().setCaption(caption);
	}
}
