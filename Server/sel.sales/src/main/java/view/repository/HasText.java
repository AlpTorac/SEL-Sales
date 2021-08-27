package view.repository;

public interface HasText extends Attachable {
	default public void setCaption(String caption) {
		((HasText) this.getComponent()).setCaption(caption);
	}
	default public String getText() {
		return ((HasText) this.getComponent()).getText();
	}
	default public void clearText() {
		((HasText) this.getComponent()).clearText();
	}
}
