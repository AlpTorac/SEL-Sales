package view.repository.uifx;

import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import view.repository.ITextBox;
import view.repository.uiwrapper.ClickEventListener;

public class FXTextBox extends TextArea implements ITextBox, FXHasPlaceholderText, FXEventShooterOnClickUI {
	@Override
	public void addClickListener(ClickEventListener l) {
		TextArea ref = this;
		EventHandler<MouseEvent> event = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				l.clickAction();
			}
		};
		ref.setOnMouseClicked(event);
	}
	
	@Override
	public void setCaption(String caption) {
		super.setText(caption);
	}
	
	@Override
	public void removeClickListener(ClickEventListener l) {
		this.setOnMouseClicked(null);
	}
	
	@Override
	public void clearText() {
		super.clear();
	}
}
