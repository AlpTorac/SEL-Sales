package view.repository;

import view.repository.uiwrapper.ClickEventListener;

public interface IEventShooterOnClickUIComponent extends IUIComponent {
	default void addClickListener(ClickEventListener l) {
		((IEventShooterOnClickUIComponent) this.getComponent()).addClickListener(l);
	}
	default void removeClickListener(ClickEventListener l) {
		((IEventShooterOnClickUIComponent) this.getComponent()).removeClickListener(l);
	}
}
