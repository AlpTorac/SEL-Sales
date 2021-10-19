package model.filemanager;

import model.IModel;
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.filewriter.DishMenuFile;
import model.filewriter.OrderFile;
import model.filewriter.StandardDishMenuFile;
import model.filewriter.StandardOrderFile;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.settings.ISettings;
import model.settings.SettingsField;

public class FileManager implements IFileManager {
	private OrderFile orderWriter;
	private DishMenuFile dishMenuWriter;
	
	private String settingsFolderAddress;
	private SettingsFile settingsFile;
	private IModel model;
	
	public FileManager(IModel model, IDishMenuDataFactory menuDataFac, IDishMenuItemFinder finder, IOrderDataFactory orderDataFac,
			String settingsFolderAddress) {
		this.model = model;
		
		this.settingsFolderAddress = settingsFolderAddress;
		this.settingsFile = new StandardSettingsFile(this.settingsFolderAddress);
		this.orderWriter = new StandardOrderFile(this.model.getSettings().getSetting(SettingsField.ORDER_FOLDER), finder, orderDataFac, menuDataFac.getItemDataFac());
		this.dishMenuWriter = new StandardDishMenuFile(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER), menuDataFac);
	}
	
	protected void initSettings() {
		ISettings s = this.settingsFile.loadSettings();
		if (s != null && !s.isEmpty()) {
			System.out.println("Settings preloaded");
			this.model.setSettings(s);
		}
	}
	
	protected void initDishMenu() {
		IDishMenuData menu = this.dishMenuWriter.loadDishMenu();
		if (menu != null && !menu.isEmpty()) {
			System.out.println("Dish menu preloaded");
			this.model.setDishMenu(menu);
		}
	}
	
	@Override
	public boolean writeOrderDatas(IOrderData[] data) {
		return this.orderWriter.writeOrderData(data);
	}

	@Override
	public boolean writeDishMenuData(IDishMenuData data) {
		return this.dishMenuWriter.writeDishMenuData(data);
	}
	
	@Override
	public boolean writeSettings(ISettings settings) {
		return this.settingsFile.writeSettings(settings);
	}

	@Override
	public void loadSaved() {
		this.initSettings();
		this.initDishMenu();
	}

	@Override
	public void refreshValue() {
		System.out.println("Old order folder address: "+this.orderWriter.getFolderAddress());
		this.orderWriter.setFolderAddress(this.model.getSettings().getSetting(SettingsField.ORDER_FOLDER));
		System.out.println("New order folder address: "+this.orderWriter.getFolderAddress());
		System.out.println("Old menu folder address: "+this.dishMenuWriter.getFolderAddress());
		this.dishMenuWriter.setFolderAddress(this.model.getSettings().getSetting(SettingsField.DISH_MENU_FOLDER));
		System.out.println("New menu folder address: "+this.dishMenuWriter.getFolderAddress());
	}
}
