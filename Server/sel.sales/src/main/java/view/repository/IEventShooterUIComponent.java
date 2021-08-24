package view.repository;

public interface IEventShooterUIComponent extends IUIComponent {
	public void addClickListener(ClickEventListener l);
	public void removeClickListener(ClickEventListener l);
}
