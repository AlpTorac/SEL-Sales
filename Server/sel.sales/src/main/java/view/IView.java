package view;

import controller.IController;
import model.IModel;
import model.Updatable;

public interface IView extends Updatable {
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
