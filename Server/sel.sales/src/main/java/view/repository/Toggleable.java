package view.repository;

import view.repository.uiwrapper.ChangeEventListener;

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
	
	default public void addChangeEventListener(ChangeEventListener cel) {
		((Toggleable) this.getComponent()).addChangeEventListener(cel);
	}
}
