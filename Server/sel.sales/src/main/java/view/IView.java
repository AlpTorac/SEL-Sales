package view;

import controller.IController;
import model.DiscoveredClientUpdatable;
import model.IModel;
import model.KnownClientUpdatable;
import model.MenuUpdatable;
import model.OrderUpdatable;

public interface IView extends MenuUpdatable, OrderUpdatable, DiscoveredClientUpdatable, KnownClientUpdatable {
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
