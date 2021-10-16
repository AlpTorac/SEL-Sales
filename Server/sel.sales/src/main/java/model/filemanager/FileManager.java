package model.filemanager;

import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFinder;
import model.filewriter.DishMenuFile;
import model.filewriter.OrderFile;
import model.filewriter.StandardDishMenuFile;
import model.filewriter.StandardOrderFile;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderItemDataFactory;

public class FileManager implements IFileManager {

	private String orderFolderAddress;
	private OrderFile orderWriter;
	
	private String dishMenuFolderAddress;
	private DishMenuFile dishMenuWriter;
	
	public FileManager(IDishMenuDataFactory menuDataFac, IDishMenuItemFinder finder, IOrderDataFactory orderDataFac) {
		this.orderFolderAddress = "src/main/resources/orders";
		this.orderWriter = new StandardOrderFile(this.orderFolderAddress, finder, orderDataFac, menuDataFac.getItemDataFac());
		
		this.dishMenuFolderAddress = "src/main/resources/dishMenu";
		this.dishMenuWriter = new StandardDishMenuFile(this.dishMenuFolderAddress, menuDataFac);
	}
	
	@Override
	public boolean writeOrderData(IOrderData data) {
		return this.orderWriter.writeOrderData(data);
	}

	@Override
	public boolean writeDishMenuData(IDishMenuData data) {
		return this.dishMenuWriter.writeDishMenuData(data);
	}

	@Override
	public void setOrderDataFolderAddress(String address) {
		this.orderWriter.setFolderAddress(address);
	}

	@Override
	public void setDishMenuDataFolderAddress(String address) {
		this.dishMenuWriter.setFolderAddress(address);
	}

}
