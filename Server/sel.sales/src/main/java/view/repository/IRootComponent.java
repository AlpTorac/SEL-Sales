package view.repository;

/**
 * Equivalent of a window with a {@link IInnerFrame} as its view.
 * 
 * @author atora
 */
public interface IRootComponent extends IUIComponent, ISizable {
	default void setInnerFrame(IInnerFrame scene) {
		((IRootComponent) this.getComponent()).setInnerFrame(scene);
	}
	default void maximise() {
		((IRootComponent) this.getComponent()).maximise();
	}
	default void setFullScreen() {
		((IRootComponent) this.getComponent()).setFullScreen();
	}
	default void close() {
		((IRootComponent) this.getComponent()).close();
	}
	default void terminateOnClose() {
		((IRootComponent) this.getComponent()).terminateOnClose();
	}
	default void closeArtificially() {
		((IRootComponent) this.getComponent()).closeArtificially();
	}
}
