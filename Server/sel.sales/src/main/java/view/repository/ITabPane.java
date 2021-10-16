package view.repository;

public interface ITabPane extends Attachable {
	default void addTab(String title, IUIComponent tabComponent) {
		((ITabPane) this.getComponent()).addTab(title, tabComponent);
	}
}
