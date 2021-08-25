package view.repository;

public interface Toggleable extends IUIComponent {
	default public void setSelected(boolean selected) {
		((Toggleable) this.getComponent()).setSelected(selected);
	}
	
	default public void setToggleGroup(IToggleGroup tg) {
		((Toggleable) this.getComponent()).setToggleGroup(tg);
	}
}
