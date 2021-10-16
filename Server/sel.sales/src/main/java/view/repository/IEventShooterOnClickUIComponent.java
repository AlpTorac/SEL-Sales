package view.repository;

import view.repository.uiwrapper.ClickEventListener;

public interface IEventShooterOnClickUIComponent extends Attachable {
	default void performArtificialClick() {
		((IEventShooterOnClickUIComponent) this.getComponent()).performArtificialClicks(1);
	}
	default void performArtificialClicks(int clickCount) {
		((IEventShooterOnClickUIComponent) this.getComponent()).performArtificialClicks(clickCount);
	}
	default void addClickListener(ClickEventListener l) {
		((IEventShooterOnClickUIComponent) this.getComponent()).addClickListener(l);
	}
	default void removeClickListener(ClickEventListener l) {
		((IEventShooterOnClickUIComponent) this.getComponent()).removeClickListener(l);
	}
}
