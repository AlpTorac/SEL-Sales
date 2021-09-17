package view.repository.uifx;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import view.repository.IEventShooterOnClickUIComponent;

public interface FXEventShooterOnClickUI extends IEventShooterOnClickUIComponent, FXComponent {
	/**
	 * Performs a click on the middle of the list (the middle of the list component itself,
	 * not necessarily on an element)
	 * <p>
	 * Make sure that the list is in a Window.
	 */
	@Override
	default public void performArtificialClicks(int clickCount) {
		Region ref = (Region) this;
		double width = ref.getMinWidth();
		double height = ref.getMinHeight();
		
		Point2D sceneP = ref.localToScene(0, 0); // top left corner in scene coords
		double sceneX = sceneP.getX();
		double sceneY = sceneP.getY();
		Point2D clickAreaOnScene = new Point2D(width / 2d + sceneX, height / 2d + sceneY);
		
		Point2D screenP = ref.localToScreen(0, 0); // top left corner in screen coords
		double screenX = screenP.getX();
		double screenY = screenP.getY();
		Point2D clickAreaOnScreen = new Point2D(width / 2d + screenX, height / 2d + screenY);
		
		ref.fireEvent(new MouseEvent(
				MouseEvent.MOUSE_CLICKED,
				clickAreaOnScene.getX(),
				clickAreaOnScene.getY(),
				clickAreaOnScreen.getX(),
				clickAreaOnScreen.getY(),
				MouseButton.PRIMARY,
				clickCount,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				false,
				true,
				null));
	}
}
