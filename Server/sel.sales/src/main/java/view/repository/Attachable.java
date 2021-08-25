package view.repository;

public interface Attachable extends IUIComponent {
	default public void attachTo(IUIComponent parent) {
		((Attachable) this.getComponent()).attachTo(parent.getComponent());
	}

	default public void dettach() {
		((Attachable) this.getComponent()).dettach();
	}
}
