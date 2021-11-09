package test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;

import controller.IController;
import model.IDateSettings;
import model.connectivity.IDeviceData;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderItemData;
import server.controller.IServerController;
import server.model.IServerModel;
import server.view.IServerView;
import server.view.StandardServerView;
import server.view.composites.ServerConnectionArea;
import server.view.composites.MainArea;
import server.view.composites.MenuDesignArea;
import server.view.composites.OrderInspectionArea;
import server.view.composites.OrderTrackingArea;
import server.view.composites.ServerSettingsArea;
import test.model.order.OrderTestUtilityClass;
import view.IView;
import view.repository.HasText;
import view.repository.IButton;
import view.repository.IEventShooterOnClickUIComponent;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UITabPane;

public class MainViewOperationsUtilityClass {
	private long waitTime = 100;
	private IServerModel model;
	private IServerController controller;
	private IServerView view;
	
	private UILayout tabArea;
	private UITabPane tabPane;
	
	private String menuOrderAreaTabName;
	private String connAreaTabName;
	private String settingsAreaTabName;
	
	private MainArea ma;
	private ServerConnectionArea ca;
	private ServerSettingsArea sa;
	
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
	
	private HasText menuFolderAddress;
	private HasText orderFolderAddress;
	private HasText ppTimeout;
	private HasText ppMinimalDelay;
	private HasText ppResendLimit;
	private HasText sendTimeout;
	
	private IButton applySettingsButton;
	
	private IDateSettings ds;
	
	public MainViewOperationsUtilityClass(StandardServerView view, IServerController controller, IServerModel model) {
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
		
		menuFolderAddress = sa.getMenuFolderAddress();
		orderFolderAddress = sa.getOrderFolderAddress();
		ppTimeout = sa.getPpTimeout();
		ppMinimalDelay = sa.getPpMinimalDelay();
		ppResendLimit = sa.getPpResendLimit();
		sendTimeout = sa.getSendTimeout();
		applySettingsButton = sa.getApplyButton();
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
	
	public void clickOnConfirmAllOrdersButton() {
		this.oia.getConfirmAllButton().performArtificialClick();
	}
	
	public Collection<IOrderData> confirmAllOrders() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
//		Collection<IOrderData> unconfirmedOrders = this.getUnconfirmedOrders();
//		IOrderData[] confirmedOrders = new IOrderData[unconfirmedOrders.size()];
//		GeneralTestUtilityClass.performWait(waitTime);
//		int size = unconfirmedOrders.size();
//		
//		for (int i = 0; i < size; i++) {
//			
//			IOrderData data = this.addConfirmOrder();
//			confirmedOrders[i] = data;
//		}
//		
//		Collection<IOrderData> co = new ArrayList<IOrderData>();
//		
//		for (IOrderData d : confirmedOrders) {
//			co.add(d);
//		}
//		
//		return co;
		this.clickOnConfirmAllOrdersButton();
		GeneralTestUtilityClass.performWait(waitTime);
		Collection<IOrderData> col = new ArrayList<IOrderData>();
		for (IOrderData d : model.getAllConfirmedOrders()) {
			col.add(d);
		}
		return col;
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
		String[] itemIDs = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getItemData().getID().toString()).toArray(String[]::new);
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
	
	public void clickOnDiscoverDevices() {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getRefreshButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public Collection<IDeviceData> getDiscoveredDevices() {
		this.setConnAreaTabActive();
		return this.ca.getDiscoveredDevices().getAllItems();
	}
	
	public Collection<IDeviceData> getKnownDevices() {
		this.setConnAreaTabActive();
		return this.ca.getKnownDevices().getAllItems();
	}
	
	public void addKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getDiscoveredDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		System.out.println("discovered Device size: " + ca.getDiscoveredDevices().getSize());
		this.ca.getAddKnownDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void removeKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getRemoveKnownDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void allowKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getAllowDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void blockKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getBlockDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void inputMenuFolderAddress(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.menuFolderAddress.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void inputOrderFolderAddress(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.orderFolderAddress.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void inputPPMinimalDelay(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ppMinimalDelay.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void inputPPResendLimit(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ppResendLimit.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void inputPPTimeout(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ppTimeout.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void inputSendTimeout(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.sendTimeout.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void applySettings() {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.applySettingsButton.performArtificialClick();
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
