package model.filemanager;

import model.IModel;
import model.filewriter.IFileAccess;
import model.filewriter.StandardFileAccess;
import model.settings.SettingsField;

public class FileManager implements IFileManager {
	private final String menuFileName = "menu";
	private final String orderFileName = "orders";
	private final String settingsFileName = "settings";
	private final String deviceDataFileName = "knownDevices";
	
	private IFileAccess menuFile;
	private IFileAccess settingsFile;
	private IFileAccess deviceDataFile;
	private IFileAccess orderFile;
	
	private String settingsFolderAddress;
	
	private IModel model;
	
	public FileManager(IModel model, String settingsFolderAddress) {
		this.model = model;
		
		this.setResourcesFolderAddress(settingsFolderAddress);
		this.menuFile = new StandardFileAccess(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER), this.menuFileName);
		this.orderFile = new StandardFileAccess(this.model.getSettings().getSetting(SettingsField.ORDER_FOLDER), this.orderFileName);
	}
	
	protected void setDishMenuInModel(IFileAccess f) {
		String menu = f.readFile();
		if (menu != null && !menu.isEmpty()) {
			this.model.setDishMenuFromFile(menu);
		}
	}
	
	protected void setOrdersInModel(IFileAccess f) {
		String orders = f.readFile();
		if (orders != null && !orders.isEmpty()) {
			this.model.setWrittenOrders(orders);
		}
	}
	
	protected void initSettings() {
		String s = this.settingsFile.readFile();
		if (s != null && !s.isEmpty()) {
			this.model.setSettings(s);
		}
	}
	
	protected void initOrders() {
		String s = this.orderFile.readFile();
		if (s != null && !s.isEmpty()) {
			this.model.setWrittenOrders(s);
		}
	}
	
	protected void initDishMenu() {
		this.setDishMenuInModel(this.menuFile);
	}
	
	@Override
	public boolean writeDishMenuData(String data) {
		return this.menuFile.remakeFile() && this.menuFile.writeToFile(data);
	}
	
	@Override
	public boolean writeSettings(String settings) {
		return this.settingsFile.remakeFile() && this.settingsFile.writeToFile(settings);
	}

	protected void initKnownDevices() {
		this.setKnownDevicesInModel(this.deviceDataFile);
	}

	@Override
	public void refreshValue() {
		this.menuFile.setAddress(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER));
		this.orderFile.setAddress(this.model.getSettings().getSetting(SettingsField.ORDER_FOLDER));
	}
	@Override
	public void close() {
		this.menuFile.close();
		this.settingsFile.close();
		this.deviceDataFile.close();
		this.orderFile.close();
	}

	@Override
	public void loadOrders(String fileAddress) {
		IFileAccess file = new StandardFileAccess(fileAddress, this.orderFileName);
		this.setOrdersInModel(file);
		file.close();
	}
	
	@Override
	public void loadDishMenu(String fileAddress) {
		IFileAccess file = new StandardFileAccess(fileAddress, this.menuFileName);
		this.setDishMenuInModel(file);
		file.close();
	}

	@Override
	public boolean writeDeviceDatas(String deviceDatas) {
		return this.deviceDataFile.remakeFile() && this.deviceDataFile.writeToFile(deviceDatas);
	}

	@Override
	public void loadKnownDevices(String fileAddress) {
		IFileAccess file = new StandardFileAccess(fileAddress, this.deviceDataFileName);
		this.setKnownDevicesInModel(file);
		file.close();
	}

	protected void setKnownDevicesInModel(IFileAccess file) {
		this.model.setKnownDevices(file.readFile());
	}

	@Override
	public void setResourcesFolderAddress(String folderAddress) {
		this.settingsFolderAddress = folderAddress;
		if (this.deviceDataFile != null) {
			this.deviceDataFile.close();
		}
		if (this.settingsFile != null) {
			this.settingsFile.close();
		}
		this.deviceDataFile = new StandardFileAccess(this.settingsFolderAddress, this.deviceDataFileName);
		this.settingsFile = new StandardFileAccess(this.settingsFolderAddress, this.settingsFileName);
	}

	@Override
	public void loadSavedSettings() {
		this.initSettings();
	}

	@Override
	public void loadSavedDishMenu() {
		this.initDishMenu();
	}

	@Override
	public void loadSavedKnownDevices() {
		this.initKnownDevices();
	}

	@Override
	public void loadSavedOrders() {
		this.initOrders();
	}

	@Override
	public boolean writeOrderDatas(String orderDatas) {
		return this.orderFile.remakeFile() && this.orderFile.writeToFile(orderDatas);
	}
}
