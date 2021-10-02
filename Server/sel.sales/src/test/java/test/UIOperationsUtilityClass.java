package test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;

import controller.IController;
import model.IModel;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderItemData;
import test.model.order.OrderTestUtilityClass;
import view.IDateSettings;
import view.IView;
import view.MainView;
import view.composites.MainArea;
import view.composites.MenuDesignArea;
import view.composites.OrderInspectionArea;
import view.composites.OrderTrackingArea;
import view.repository.HasText;
import view.repository.IEventShooterOnClickUIComponent;

public class UIOperationsUtilityClass {
	private IModel model;
	private IController controller;
	private IView view;
	private MainArea ma;
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
	
	private IDateSettings ds;
	
	public UIOperationsUtilityClass(MainView view, IController controller, IModel model) {
		this.model = model;
		this.controller = controller;
		this.view = view;
		this.ma = GeneralTestUtilityClass.getPrivateFieldValue(view, "mainArea");
		mda = GeneralTestUtilityClass.getPrivateFieldValue(ma, "mda");
		ota = GeneralTestUtilityClass.getPrivateFieldValue(ma, "ota");
		oia = GeneralTestUtilityClass.getPrivateFieldValue(ma, "oia");
		ds = GeneralTestUtilityClass.getPrivateFieldValue(oia, "ds");
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
	
	public IOrderData addConfirmOrder() {
		IOrderData data = this.getUnconfirmedOrders().stream().findFirst().get();
		this.oia.displayOrder(data);
		this.oia.getAddConfirmButton().performArtificialClick();
		this.view.refreshUnconfirmedOrders();
		this.view.refreshConfirmedOrders();
		return data;
	}
	
	public Collection<IOrderData> confirmAllOrders() {
		Collection<IOrderData> unconfirmedOrders = this.getUnconfirmedOrders();
		IOrderData[] confirmedOrders = new IOrderData[unconfirmedOrders.size()];
		int size = unconfirmedOrders.size();
		
		for (int i = 0; i < size; i++) {
			IOrderData data = this.addConfirmOrder();
			confirmedOrders[i] = data;
		}
		
		Collection<IOrderData> co = new ArrayList<IOrderData>();
		
		for (IOrderData d : confirmedOrders) {
			co.add(d);
		}
		
		return co;
	}
	
	public void confirmAllOrdersWithoutReturn() {
		this.oia.getConfirmAllButton().performArtificialClick();
	}
	
	public void toggleAutoConfirm() {
		this.ota.getAuto().setToggled(true);
	}
	
	public IOrderData removeUnconfirmedOrder() {
		IOrderData data = this.getUnconfirmedOrders().stream().findFirst().get();
		this.oia.displayOrder(data);
		this.oia.getRemoveButton().performArtificialClick();
		this.view.refreshUnconfirmedOrders();
		this.view.refreshConfirmedOrders();
		return data;
	}
	
	public IOrderData removeConfirmedOrder() {
		IOrderData data = this.getConfirmedOrders().stream().findFirst().get();
		this.oia.displayOrder(data);
		this.oia.getRemoveButton().performArtificialClick();
		this.view.refreshUnconfirmedOrders();
		this.view.refreshConfirmedOrders();
		return data;
	}
	
	public Collection<IOrderData> getUnconfirmedOrders() {
		return ota.getUnconfirmedOrderList().getAllItems();
	}
	
	public Collection<IOrderData> getConfirmedOrders() {
		return ota.getConfirmedOrderList().getAllItems();
	}
	
	public void clickOnUnconfirmedOrder(int index) {
		ota.getUnconfirmedOrderList().artificiallySelectItem(index);
		ota.getUnconfirmedOrderList().performArtificialClicks(2);
	}
	
	public void clickOnConfirmedOrder(int index) {
		ota.getConfirmedOrderList().artificiallySelectItem(index);
		ota.getConfirmedOrderList().performArtificialClicks(2);
	}
	
	public void assertShownOrderEquals(IOrderData orderData) {
		String dateText = oia.getOrderTimeInDayLabel().getText() + " " + oia.getOrderDateLabel().getText();
		String orderID = oia.getOrderIDLabel().getText();
		LocalDateTime ldt = LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern("HH:mm:ss:SSS dd/MM/yyyy"));
		boolean isCash = oia.getCashRadioButton().isToggled();
		boolean isHere = oia.getHereRadioButton().isToggled();
		BigDecimal totalOrderDiscount = BigDecimal.valueOf(Double.valueOf(oia.getDiscountDisplay().getText()));
		BigDecimal grossSum = BigDecimal.valueOf(Double.valueOf(oia.getGrossSumDisplay().getText()));
		BigDecimal netSum = BigDecimal.valueOf(Double.valueOf(oia.getNetSumDisplay().getText()));
		String[] itemIDs = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getItemData().getId()).toArray(String[]::new);
		BigDecimal[] itemAmounts = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getAmount()).toArray(BigDecimal[]::new);
		BigDecimal[] itemGrossPrices = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getGrossPrice()).toArray(BigDecimal[]::new);
		BigDecimal[] itemTotalDiscounts = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getTotalDiscount()).toArray(BigDecimal[]::new);
		BigDecimal[] itemNetPrices = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getNetPrice()).toArray(BigDecimal[]::new);
		
		OrderTestUtilityClass.assertOrderDataEqual(
				orderData,
				orderID,
				ldt,
				isCash,
				isHere,
				orderData.getOrderDiscount());
		Assertions.assertEquals(orderData.getGrossSum().compareTo(grossSum), 0);
		Assertions.assertEquals(orderData.getTotalDiscount().compareTo(totalOrderDiscount), 0);
		Assertions.assertEquals(orderData.getNetSum().compareTo(netSum), 0);
		IOrderItemData[] idatas = orderData.getOrderedItems();
		int orderItemLen = idatas.length;
		for (int i = 0; i < orderItemLen; i++) {
			OrderTestUtilityClass.assertOrderItemDataEqual(idatas[i], itemIDs[i], itemAmounts[i]);
			Assertions.assertEquals(idatas[i].getGrossPrice().compareTo(itemGrossPrices[i]), 0);
			Assertions.assertEquals(idatas[i].getTotalDiscount().compareTo(itemTotalDiscounts[i]), 0);
			Assertions.assertEquals(idatas[i].getNetPrice().compareTo(itemNetPrices[i]), 0);
		}
	}
}
