package view.repository;

public interface IUIComponent {
	default void setEnabled(boolean isEnabled) {
		this.getComponent().setEnabled(isEnabled);
	}
	IUIComponent getComponent();
	default void setOpacity(double opacity) {
		this.getComponent().setOpacity(opacity);
	}
	void show();
	void hide();
}
