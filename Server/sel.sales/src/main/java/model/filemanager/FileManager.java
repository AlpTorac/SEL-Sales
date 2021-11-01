package model.filemanager;

import model.IModel;
import model.filewriter.ClientDataFile;
import model.filewriter.DishMenuFile;
import model.filewriter.OrderFile;
import model.filewriter.StandardClientDataFile;
import model.filewriter.StandardDishMenuFile;
import model.filewriter.StandardOrderFile;
import model.settings.SettingsField;

public class FileManager implements IFileManager {
	private OrderFile orderWriter;
	private DishMenuFile dishMenuWriter;
	
	private String settingsFolderAddress;
	private SettingsFile settingsFile;
	private ClientDataFile clientDataFile;
	private IModel model;
	
	public FileManager(IModel model, String settingsFolderAddress) {
		this.model = model;
		
		this.settingsFolderAddress = settingsFolderAddress;
		this.clientDataFile = new StandardClientDataFile(this.settingsFolderAddress);
		this.settingsFile = new StandardSettingsFile(this.settingsFolderAddress);
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
		this.initKnownClients();
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

	protected void initKnownClients() {
		this.setKnownClientsInModel(this.clientDataFile);
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
		this.clientDataFile.close();
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
	public boolean writeClientDatas(String clientDatas) {
		return this.clientDataFile.remakeFile() && this.clientDataFile.writeToFile(clientDatas);
	}

	@Override
	public void loadKnownClients(String fileAddress) {
		ClientDataFile file = new StandardClientDataFile(fileAddress);
		this.setKnownClientsInModel(file);
		file.close();
	}

	protected void setKnownClientsInModel(ClientDataFile file) {
		this.model.setKnownClients(file.readFile());
	}

	@Override
	public void loadOrders(String fileAddress) {
		OrderFile file = new StandardOrderFile(fileAddress);
		this.model.setWrittenOrders(file.readFile());
		file.close();
	}
}
