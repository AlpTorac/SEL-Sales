package external;

import controller.IController;
import model.IModel;
import model.Updatable;

public interface IExternal extends Updatable {
	IController getController();
	IModel getModel();
	default void refreshMenu() {
		this.sendMenuData();
	}
	default void refreshUnconfirmedOrders() {
		
	}
	default void refreshConfirmedOrders() {
		
	}
	void sendMenuData();
}
