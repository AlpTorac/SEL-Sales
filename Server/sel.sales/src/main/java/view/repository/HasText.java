package view.repository;

public interface HasText extends Attachable {
	default public void setCaption(String caption) {
		((HasText) this.getComponent()).setCaption(caption);
	}
}
