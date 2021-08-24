package view.repository;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class FXButton extends Button implements IButton, FXComponent {
	FXButton(double x, double y, double width, double height, String name, Pane parent) {
		super(name);
		this.attachTo((IUIComponent) parent);
		this.setLayoutX(x);
		this.setLayoutY(y);
		this.setPrefSize(width, height);
	}

	@Override
	public void addClickListener(ClickEventListener l) {
		Button ref = this;
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				l.action();
			}
		};
		ref.setOnAction(event);
	}

	@Override
	public void removeClickListener(ClickEventListener l) {
		this.setOnAction(null);
	}
	
	@Override
	public void setCaption(String caption) {
		this.setText(caption);
	}
}
