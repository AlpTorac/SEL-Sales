package view.repository;

public interface IRootComponent extends IUIComponent, ISizable {
	default void setInnerFrame(IInnerFrame scene) {
		((IRootComponent) this.getComponent()).setInnerFrame(scene);
	}
	default void close() {
		((IRootComponent) this.getComponent()).close();
	}
}
