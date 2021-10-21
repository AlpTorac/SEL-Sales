package view.repository.uifx;

import view.repository.HasText;
import javafx.scene.control.Labeled;

public interface FXHasText extends FXAttachable, HasText {
	default public void setCaption(String caption) {
		((Labeled) this.getComponent()).setText(caption);
	}
	default public String getText() {
		return ((Labeled) this.getComponent()).getText();
	}
	default public void clearText() {
		this.setCaption("");
	}
}
