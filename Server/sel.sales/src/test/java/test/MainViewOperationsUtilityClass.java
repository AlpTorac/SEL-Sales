package test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;

import controller.IController;
import model.IModel;
import model.connectivity.IClientData;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderItemData;
import test.model.order.OrderTestUtilityClass;
import view.IDateSettings;
import view.IView;
import view.MainView;
import view.composites.ConnectionArea;
import view.composites.MainArea;
import view.composites.MenuDesignArea;
import view.composites.OrderInspectionArea;
import view.composites.OrderTrackingArea;
import view.composites.SettingsArea;
import view.repository.HasText;
import view.repository.IEventShooterOnClickUIComponent;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UITabPane;

public class MainViewOperationsUtilityClass {
	private long waitTime = 100;
	private IModel model;
	private IController controller;
	private IView view;
	
	private UILayout tabArea;
	private UITabPane tabPane;
	
	private String menuOrderAreaTabName;
	private String connAreaTabName;
	private String settingsAreaTabName;
	
	private MainArea ma;
	private ConnectionArea ca;
	private SettingsArea sa;
	
	private MenuDesignArea mda;
	private OrderTrackingArea ota;
	private OrderInspectionArea oia;
	
	private HasText dishNameBox;
	private HasText priceBox;
	private HasText idBox;
	private HasText prodCostBox;
	private HasText porSizeBox;
	
	private IEventShooterOnClickUIComponent addButton;
	private IEventShooterOnClickUIComponent removeButton;
	private IEventShooterOnClickUIComponent editButton;
	
	private IDateSettings ds;
	
	public MainViewOperationsUtilityClass(MainView view, IController controller, IModel model) {
		this.model = model;
		this.controller = controller;
		this.view = view;
		this.ma = GeneralTestUtilityClass.getPrivateFieldValue(view, "mainArea");
		this.ca = GeneralTestUtilityClass.getPrivateFieldValue(view, "connArea");
		this.sa = GeneralTestUtilityClass.getPrivateFieldValue(view, "settingsArea");
		mda = GeneralTestUtilityClass.getPrivateFieldValue(ma, "mda");
		ota = GeneralTestUtilityClass.getPrivateFieldValue(ma, "ota");
		oia = GeneralTestUtilityClass.getPrivateFieldValue(ma, "oia");
		ds = GeneralTestUtilityClass.getPrivateFieldValue(oia, "ds");
		this.tabPane = GeneralTestUtilityClass.getPrivateFieldValue(view, "tabPane");
		this.menuOrderAreaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "menuOrderAreaTabName");
		this.connAreaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "connAreaTabName");
		this.settingsAreaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "settingsAreaTabName");
		dishNameBox = mda.getDishNameBox();
		priceBox = mda.getPriceBox();
		idBox = mda.getMenuItemIDBox();
		prodCostBox = mda.getProductionCostBox();
		porSizeBox = mda.getPortionBox();
		
		addButton = mda.getAddButton();
		removeButton = mda.getRemoveButton();
		editButton = mda.getEditButton();
	}
	
	public IDishMenuItemData addMenuItem(String name, String id, BigDecimal price, BigDecimal productionCost, BigDecimal portionSize, BigDecimal discount) {
		this.setMenuOrderTabActive();
		dishNameBox.setCaption(name);
		priceBox.setCaption(String.valueOf(price.doubleValue()));
		idBox.setCaption(id);
		prodCostBox.setCaption(String.valueOf(productionCost.doubleValue()));
		porSizeBox.setCaption(String.valueOf(portionSize.doubleValue()));
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		addButton.performArtificialClick();
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		return model.getMenuItem(id);
	}
	
	public IDishMenuItemData removeMenuItem(String id) {
		this.setMenuOrderTabActive();
		IDishMenuItemData itemToBeRemoved = model.getMenuItem(id);
		
		idBox.setCaption(id);
		
		GeneralTestUtilityClass.performWait(waitTime);
		removeButton.performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		
		if (model.getMenuItem(id) == null) {
			return itemToBeRemoved;
		} else {
			return null;
		}
	}
	
	public IDishMenuItemData editMenuItem(String name, String id, BigDecimal price, BigDecimal productionCost, BigDecimal portionSize, BigDecimal discount) {
		this.setMenuOrderTabActive();
		dishNameBox.setCaption(name);
		priceBox.setCaption(String.valueOf(price.doubleValue()));
		idBox.setCaption(id);
		prodCostBox.setCaption(String.valueOf(productionCost.doubleValue()));
		porSizeBox.setCaption(String.valueOf(portionSize.doubleValue()));
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		editButton.performArtificialClick();
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		IDishMenuItemData editedItem = model.getMenuItem(id);
		
		return editedItem;
	}
	
	public IOrderData addConfirmOrder() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		IOrderData data = this.ota.getUnconfirmedOrderList().getItem(0);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.displayOrder(data);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.getAddConfirmButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		this.view.refreshUnconfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		this.view.refreshConfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		return data;
	}
	
	public Collection<IOrderData> confirmAllOrders() {
		this.setMenuOrderTabActive();
		Collection<IOrderData> unconfirmedOrders = this.getUnconfirmedOrders();
		IOrderData[] confirmedOrders = new IOrderData[unconfirmedOrders.size()];
		GeneralTestUtilityClass.performWait(waitTime);
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
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.getConfirmAllButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void toggleOnAutoConfirm() {
		this.setMenuOrderTabActive();
		this.ota.getAuto().setToggled(true);
	}
	public void toggleOffAutoConfirm() {
		this.setMenuOrderTabActive();
		this.ota.getAuto().setToggled(false);
	}
	
	public IOrderData removeUnconfirmedOrder() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		IOrderData data = this.ota.getUnconfirmedOrderList().getItem(0);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.displayOrder(data);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.getRemoveButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		this.view.refreshUnconfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		this.view.refreshConfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		return data;
	}
	
	public IOrderData removeConfirmedOrder() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		IOrderData data = this.ota.getConfirmedOrderList().getItem(0);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.displayOrder(data);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.getRemoveButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		this.view.refreshUnconfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		this.view.refreshConfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		return data;
	}
	
	public Collection<IOrderData> getUnconfirmedOrders() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		return ota.getUnconfirmedOrderList().getAllItems();
	}
	
	public Collection<IOrderData> getConfirmedOrders() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		return ota.getConfirmedOrderList().getAllItems();
	}
	
	public void clickOnUnconfirmedOrder(int index) {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		ota.getUnconfirmedOrderList().artificiallySelectItem(index);
		ota.getUnconfirmedOrderList().performArtificialClicks(2);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void clickOnConfirmedOrder(int index) {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		ota.getConfirmedOrderList().artificiallySelectItem(index);
		ota.getConfirmedOrderList().performArtificialClicks(2);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void assertShownOrderEquals(IOrderData orderData) {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		oia.displayOrder(orderData);
		GeneralTestUtilityClass.performWait(waitTime);
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
		
		OrderTestUtilityClass.assertOrderDataEqual(
				orderData,
				orderID,
				ldt,
				isCash,
				isHere,
				orderData.getOrderDiscount());
		Assertions.assertEquals(orderData.getGrossSum().compareTo(grossSum), 0);
		Assertions.assertEquals(orderData.getOrderDiscount().compareTo(totalOrderDiscount), 0);
		Assertions.assertEquals(orderData.getNetSum().compareTo(netSum), 0);
		IOrderItemData[] idatas = orderData.getOrderedItems();
		int orderItemLen = idatas.length;
		for (int i = 0; i < orderItemLen; i++) {
			OrderTestUtilityClass.assertOrderItemDataEqual(idatas[i], itemIDs[i], itemAmounts[i]);
			Assertions.assertEquals(idatas[i].getGrossPrice().compareTo(itemGrossPrices[i]), 0);
		}
	}
	
	public void clickOnDiscoverClients() {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getRefreshButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public Collection<IClientData> getDiscoveredClients() {
		this.setConnAreaTabActive();
		return this.ca.getDiscoveredClients().getAllItems();
	}
	
	public Collection<IClientData> getKnownClients() {
		this.setConnAreaTabActive();
		return this.ca.getKnownClients().getAllItems();
	}
	
	public void addKnownClient(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getDiscoveredClients().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		System.out.println("discovered client size: " + ca.getDiscoveredClients().getSize());
		this.ca.getAddKnownClientButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void removeKnownClient(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownClients().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getRemoveKnownClientButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void allowKnownClient(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownClients().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getAllowClientButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void blockKnownClient(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownClients().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getBlockClientButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	private void setMenuOrderTabActive() {
		this.tabPane.selectTab(this.menuOrderAreaTabName);
	}
	private void setConnAreaTabActive() {
		this.tabPane.selectTab(this.connAreaTabName);
	}
	private void setSettingsAreaTabActive() {
		this.tabPane.selectTab(this.settingsAreaTabName);
	}
}
