package view.repository;

public interface IUIComponent {
	public void attachTo(IUIComponent parent);
	public void dettach();
	public void show();
}
