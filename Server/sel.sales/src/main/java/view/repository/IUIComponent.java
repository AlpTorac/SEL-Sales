package view.repository;

public interface IUIComponent {
	default void setEnabled(boolean isEnabled) {
		this.getComponent().setEnabled(isEnabled);
	}
	IUIComponent getComponent();
	void show();
}
