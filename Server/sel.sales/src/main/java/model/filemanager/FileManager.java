package model.filemanager;

import model.IModel;
import model.filewriter.DeviceDataFile;
import model.filewriter.DishMenuFile;
import model.filewriter.OrderFile;
import model.filewriter.StandardDeviceDataFile;
import model.filewriter.StandardDishMenuFile;
import model.filewriter.StandardOrderFile;
import model.settings.SettingsField;

public class FileManager implements IFileManager {
	private OrderFile orderWriter;
	private DishMenuFile dishMenuWriter;
	
	private String settingsFolderAddress;
	private SettingsFile settingsFile;
	private DeviceDataFile deviceDataFile;
	private IModel model;
	
	public FileManager(IModel model, String settingsFolderAddress) {
		this.model = model;
		
		this.setResourcesFolderAddress(settingsFolderAddress);
		this.orderWriter = new StandardOrderFile(this.model.getSettings().getSetting(SettingsField.ORDER_FOLDER));
		this.dishMenuWriter = new StandardDishMenuFile(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER));
	}
	
	protected void setDishMenuInModel(DishMenuFile f) {
		String menu = f.readFile();
		if (menu != null && !menu.isEmpty()) {
//			System.out.println("Dish menu preloaded");
			this.model.setDishMenu(menu);
		}
	}
	
	protected void initSettings() {
		String s = this.settingsFile.readFile();
		if (s != null && !s.isEmpty()) {
//			System.out.println("Settings preloaded");
			this.model.setSettings(s);
		}
	}
	
	protected void initDishMenu() {
//		String menu = this.dishMenuWriter.readFile();
//		if (menu != null && !menu.isEmpty()) {
//			System.out.println("Dish menu preloaded");
//			this.model.setDishMenu(menu);
//		}
		this.setDishMenuInModel(this.dishMenuWriter);
	}
	
	@Override
	public boolean writeOrderData(String data) {
		return this.orderWriter.writeToFile(data);
	}
	
	@Override
	public boolean writeDishMenuData(String data) {
		return this.dishMenuWriter.remakeFile() && this.dishMenuWriter.writeToFile(data);
	}
	
	@Override
	public boolean writeSettings(String settings) {
		return this.settingsFile.remakeFile() && this.settingsFile.writeToFile(settings);
	}

	@Override
	public void loadSaved() {
		this.initSettings();
		this.initDishMenu();
		this.initOrders();
		this.initKnownDevices();
	}

	protected void initOrders() {
		this.setWrittenOrdersInModel(this.orderWriter);
	}

	private void setWrittenOrdersInModel(OrderFile orderFile) {
		String orders = orderFile.readFile();
		if (orders != null && !orders.isEmpty()) {
			this.model.setWrittenOrders(orders);
		}
	}

	protected void initKnownDevices() {
		this.setKnownDevicesInModel(this.deviceDataFile);
	}

	@Override
	public void refreshValue() {
//		System.out.println("Old order folder address: "+this.orderWriter.getAddress());
		this.orderWriter.setAddress(this.model.getSettings().getSetting(SettingsField.ORDER_FOLDER));
//		System.out.println("New order folder address: "+this.orderWriter.getAddress());
//		System.out.println("Old menu folder address: "+this.dishMenuWriter.getAddress());
		this.dishMenuWriter.setAddress(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER));
//		System.out.println("New menu folder address: "+this.dishMenuWriter.getAddress());
	}
	@Override
	public void close() {
		this.orderWriter.close();
		this.dishMenuWriter.close();
		this.settingsFile.close();
		this.deviceDataFile.close();
	}

	@Override
	public void loadDishMenu(String fileAddress) {
		DishMenuFile file = new StandardDishMenuFile(fileAddress);
//		String menu = file.readFile();
//		if (menu != null && !menu.isEmpty()) {
//			this.model.setDishMenu(menu);
//		}
		this.setDishMenuInModel(file);
		file.close();
	}

	@Override
	public boolean writeDeviceDatas(String deviceDatas) {
		return this.deviceDataFile.remakeFile() && this.deviceDataFile.writeToFile(deviceDatas);
	}

	@Override
	public void loadKnownDevices(String fileAddress) {
		DeviceDataFile file = new StandardDeviceDataFile(fileAddress);
		this.setKnownDevicesInModel(file);
		file.close();
	}

	protected void setKnownDevicesInModel(DeviceDataFile file) {
		this.model.setKnownDevices(file.readFile());
	}

	@Override
	public void loadOrders(String fileAddress) {
		OrderFile file = new StandardOrderFile(fileAddress);
		this.model.setWrittenOrders(file.readFile());
		file.close();
	}

	@Override
	public void setResourcesFolderAddress(String folderAddress) {
		this.settingsFolderAddress = folderAddress;
		this.deviceDataFile = new StandardDeviceDataFile(this.settingsFolderAddress);
		this.settingsFile = new StandardSettingsFile(this.settingsFolderAddress);
	}
}
