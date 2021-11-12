package view.repository;

public interface ITabPane extends Attachable {
	default void addTab(String title, IUIComponent tabComponent) {
		((ITabPane) this.getComponent()).addTab(title, tabComponent);
	}
	default void selectTab(String title) {
		((ITabPane) this.getComponent()).selectTab(title);
	}
	default void selectTab(int index) {
		((ITabPane) this.getComponent()).selectTab(index);
	}
	default void removeTab(String title) {
		((ITabPane) this.getComponent()).removeTab(title);
	}
	default void removeTab(IUIComponent tabComponent) {
		((ITabPane) this.getComponent()).removeTab(tabComponent);
	}
	default void removeAllTabs() {
		((ITabPane) this.getComponent()).removeAllTabs();
	}
}
