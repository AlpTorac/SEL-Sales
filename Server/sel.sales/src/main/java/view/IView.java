package view;

import controller.IController;

public interface IView {
	public IController getController();
	public void setController(IController controller);
	public void show();
}
