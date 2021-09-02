package test;

import java.math.BigDecimal;
import java.util.Collection;

import controller.IController;
import model.IModel;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import view.IView;
import view.MainView;
import view.composites.MenuDesignArea;
import view.composites.OrderInspectionArea;
import view.composites.OrderTrackingArea;
import view.repository.HasText;
import view.repository.IEventShooterOnClickUIComponent;

public class MainViewOperationsUtilityClass {
	private IModel model;
	private IController controller;
	private IView view;
	private MenuDesignArea mda;
	private OrderTrackingArea ota;
	private OrderInspectionArea oia;
	
	private HasText dishNameBox;
	private HasText priceBox;
	private HasText idBox;
	private HasText prodCostBox;
	private HasText porSizeBox;
	private HasText discBox;
	
	private IEventShooterOnClickUIComponent addButton;
	private IEventShooterOnClickUIComponent removeButton;
	private IEventShooterOnClickUIComponent editButton;
	
	public MainViewOperationsUtilityClass(MainView mv, IController controller, IModel model) {
		this.model = model;
		this.controller = controller;
		this.view = mv;
		mda = GeneralTestUtilityClass.getPrivateFieldValue(mv, "mda");
		ota = GeneralTestUtilityClass.getPrivateFieldValue(mv, "ota");
		oia = GeneralTestUtilityClass.getPrivateFieldValue(mv, "oia");
		dishNameBox = mda.getDishNameBox();
		priceBox = mda.getPriceBox();
		idBox = mda.getMenuItemIDBox();
		prodCostBox = mda.getProductionCostBox();
		porSizeBox = mda.getPortionBox();
		discBox = mda.getDiscountBox();
		
		addButton = mda.getAddButton();
		removeButton = mda.getRemoveButton();
		editButton = mda.getEditButton();
	}
	
	public IDishMenuItemData addMenuItem(String name, String id, BigDecimal price, BigDecimal productionCost, BigDecimal portionSize, BigDecimal discount) {
		dishNameBox.setCaption(name);
		priceBox.setCaption(String.valueOf(price.doubleValue()));
		idBox.setCaption(id);
		prodCostBox.setCaption(String.valueOf(productionCost.doubleValue()));
		porSizeBox.setCaption(String.valueOf(portionSize.doubleValue()));
		discBox.setCaption(String.valueOf(discount.doubleValue()));
		
		addButton.performArtificialClick();
		
		return model.getMenuItem(id);
	}
	
	public IDishMenuItemData removeMenuItem(String id) {
		IDishMenuItemData itemToBeRemoved = model.getMenuItem(id);
		
		idBox.setCaption(id);
		removeButton.performArtificialClick();
		
		if (model.getMenuItem(id) == null) {
			return itemToBeRemoved;
		} else {
			return null;
		}
	}
	
	public IDishMenuItemData editMenuItem(String name, String id, BigDecimal price, BigDecimal productionCost, BigDecimal portionSize, BigDecimal discount) {
		dishNameBox.setCaption(name);
		priceBox.setCaption(String.valueOf(price.doubleValue()));
		idBox.setCaption(id);
		prodCostBox.setCaption(String.valueOf(productionCost.doubleValue()));
		porSizeBox.setCaption(String.valueOf(portionSize.doubleValue()));
		discBox.setCaption(String.valueOf(discount.doubleValue()));
		
		editButton.performArtificialClick();
		
		IDishMenuItemData editedItem = model.getMenuItem(id);
		
		return editedItem;
	}
	
	public Collection<IOrderData> getUnconfirmedOrders() {
		return ota.getUnconfirmedOrderList().getAllItems();
	}
	
	public Collection<IOrderData> getConfirmedOrders() {
		return ota.getPastOrderList().getAllItems();
	}
}
