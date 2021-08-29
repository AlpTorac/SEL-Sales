package view.repository;

public interface Toggleable extends IUIComponent {
	default public void setToggled(boolean isToggled) {
		((Toggleable) this.getComponent()).setToggled(isToggled);
	}
	
	default public void setToggleGroup(IToggleGroup tg) {
		((Toggleable) this.getComponent()).setToggleGroup(tg);
	}
	
	default public boolean isToggled() {
		return ((Toggleable) this.getComponent()).isToggled();
	}
}
