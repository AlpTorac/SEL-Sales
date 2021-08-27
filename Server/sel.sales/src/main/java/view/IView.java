package view;

import controller.IController;
import model.IModel;
import model.Updatable;

public interface IView extends Updatable {
	IController getController();
	void setController(IController controller);
	IModel getModel();
	void setModel(IModel Model);
	void show();
	default void subscribe() {
		this.getModel().subscribe(this);
	}
	void startUp();
}
