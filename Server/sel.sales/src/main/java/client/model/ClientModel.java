package client.model;

import model.Updatable;
import model.connectivity.IDeviceData;
import model.dish.IDishMenuData;
import model.dish.IDishMenuHelper;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.order.IOrderData;
import model.order.IOrderHelper;
import model.settings.ISettings;
import model.settings.SettingsField;

public class ClientModel implements IClientModel {

	@Override
	public void addOrder(String serialisedOrderData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void subscribe(Updatable updatable) {
		// TODO Auto-generated method stub

	}

	@Override
	public IDishMenuItemData getMenuItem(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDishMenuData getMenuData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrderData getOrder(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrderData[] getAllWrittenOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDishMenuHelper getDishMenuHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IOrderHelper getOrderHelper() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDiscoveredDevice(String deviceName, String deviceAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addKnownDevice(String deviceAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeKnownDevice(String deviceAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void allowKnownDevice(String deviceAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void blockKnownDevice(String deviceAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDeviceRediscoveryRequested() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void requestDeviceRediscovery(Runnable afterDiscoveryAction) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceConnected(String deviceAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceDisconnected(String deviceAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public IDeviceData[] getAllKnownDeviceData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDeviceData[] getAllDiscoveredDeviceData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAllOrders() {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeOrder(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean writeOrders() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ISettings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSetting(SettingsField sf, String serialisedValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSettings(String settings) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSettings(ISettings settings) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSettings(String[][] settings) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDishMenu(String menu) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setKnownDevices(String serialisedDeviceData) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadSaved() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadDishMenu(String fileAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadKnownDevices(String fileAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadOrders(String fileAddress) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean writeSettings() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWrittenOrders(String readFile) {
		// TODO Auto-generated method stub

	}

	@Override
	public IDishMenuItemFinder getActiveDishMenuItemFinder() {
		// TODO Auto-generated method stub
		return null;
	}

}
