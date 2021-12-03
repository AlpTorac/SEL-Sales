package model.filemanager;

import model.IModel;
import model.filewriter.DeviceDataFile;
import model.filewriter.DishMenuFile;
import model.filewriter.StandardDeviceDataFile;
import model.filewriter.StandardDishMenuFile;
import model.settings.SettingsField;

public class FileManager implements IFileManager {
	private DishMenuFile dishMenuWriter;
	
	private String settingsFolderAddress;
	private SettingsFile settingsFile;
	private DeviceDataFile deviceDataFile;
	private IModel model;
	
	public FileManager(IModel model, String settingsFolderAddress) {
		this.model = model;
		
		this.setResourcesFolderAddress(settingsFolderAddress);
		this.dishMenuWriter = new StandardDishMenuFile(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER));
	}
	
	protected void setDishMenuInModel(DishMenuFile f) {
		String menu = f.readFile();
		if (menu != null && !menu.isEmpty()) {
//			System.out.println("Dish menu preloaded");
			this.model.setDishMenuFromFile(menu);
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
	public boolean writeDishMenuData(String data) {
		return this.dishMenuWriter.remakeFile() && this.dishMenuWriter.writeToFile(data);
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
		this.dishMenuWriter.setAddress(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER));
	}
	@Override
	public void close() {
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
	public void setResourcesFolderAddress(String folderAddress) {
		this.settingsFolderAddress = folderAddress;
		if (this.deviceDataFile != null) {
			this.deviceDataFile.close();
		}
		if (this.settingsFile != null) {
			this.settingsFile.close();
		}
		this.deviceDataFile = new StandardDeviceDataFile(this.settingsFolderAddress);
		this.settingsFile = new StandardSettingsFile(this.settingsFolderAddress);
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
}
