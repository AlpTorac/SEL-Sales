package view.repository;

public interface IUIComponent {
	public IUIComponent getComponent();
	public void attachTo(IUIComponent parent);
	public void dettach();
	public void show();
}
