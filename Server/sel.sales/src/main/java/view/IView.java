package view;

import controller.IController;
import model.IModel;
import model.MenuUpdatable;
import model.OrderUpdatable;

public interface IView extends MenuUpdatable, OrderUpdatable {
	IController getController();
	IModel getModel();
	void show();
	void hide();
	default void subscribe() {
		this.getModel().subscribe(this);
	}
	void close();
	void startUp();
}
