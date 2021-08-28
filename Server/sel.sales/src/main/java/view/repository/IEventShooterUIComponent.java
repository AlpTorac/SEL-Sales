package view.repository;

import view.repository.uiwrapper.ClickEventListener;

public interface IEventShooterUIComponent extends IUIComponent {
	public void addClickListener(ClickEventListener l);
	public void removeClickListener(ClickEventListener l);
}
