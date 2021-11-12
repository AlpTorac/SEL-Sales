package view;

import controller.IController;
import model.DiscoveredDeviceUpdatable;
import model.IModel;
import model.KnownDeviceUpdatable;
import model.MenuUpdatable;
import model.OrderUpdatable;
import model.SettingsUpdatable;

public interface IView extends OrderUpdatable, MenuUpdatable, DiscoveredDeviceUpdatable, KnownDeviceUpdatable, SettingsUpdatable {
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
