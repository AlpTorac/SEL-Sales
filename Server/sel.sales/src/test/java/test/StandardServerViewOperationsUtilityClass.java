package test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;

import model.dish.DishMenuItemData;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;
import server.controller.IServerController;
import server.model.IServerModel;
import server.view.StandardServerView;
import server.view.composites.MainArea;
import server.view.composites.MenuDesignArea;
import server.view.composites.OrderInspectionArea;
import server.view.composites.OrderTrackingArea;
import server.view.composites.ServerSettingsArea;
import test.model.order.OrderTestUtilityClass;
import view.repository.HasText;
import view.repository.IButton;
import view.repository.IEventShooterOnClickUIComponent;

public class StandardServerViewOperationsUtilityClass extends ViewOperationsUtilityClass {
	private String menuOrderAreaTabName;
	private MainArea ma;
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
	
	private IButton menuSaveButton;
	
	private HasText tableNumberInput;
	private HasText exportAddressInput;
	
	public StandardServerViewOperationsUtilityClass(StandardServerView view, IServerController controller, IServerModel model) {
		super(view, controller, model);
		this.ma = GeneralTestUtilityClass.getPrivateFieldValue(view, "mainArea");
//		this.ca = GeneralTestUtilityClass.getPrivateFieldValue(view, "connArea");
//		this.sa = GeneralTestUtilityClass.getPrivateFieldValue(view, "settingsArea");
		mda = GeneralTestUtilityClass.getPrivateFieldValue(ma, "mda");
		ota = GeneralTestUtilityClass.getPrivateFieldValue(ma, "ota");
		oia = GeneralTestUtilityClass.getPrivateFieldValue(ma, "oia");
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
		menuSaveButton = mda.getSaveButton();
		
		tableNumberInput = GeneralTestUtilityClass.getPrivateFieldValue(sa, "tableNumberInput");
		exportAddressInput = GeneralTestUtilityClass.getPrivateFieldValue(sa, "exportFolderAddressInput");
	}
	
	protected IServerModel getModel() {
		return (IServerModel) super.getModel();
	}
	
	protected IServerController getController() {
		return (IServerController) super.getController();
	}
	
	protected StandardServerView getView() {
		return (StandardServerView) super.getView();
	}
	
	public DishMenuItemData addMenuItem(String name, String id, BigDecimal price, BigDecimal productionCost, BigDecimal portionSize, BigDecimal discount) {
		this.setMenuOrderTabActive();
		dishNameBox.setCaption(name);
		priceBox.setCaption(String.valueOf(price.doubleValue()));
		idBox.setCaption(id);
		prodCostBox.setCaption(String.valueOf(productionCost.doubleValue()));
		porSizeBox.setCaption(String.valueOf(portionSize.doubleValue()));
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		addButton.performArtificialClick();
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		return this.getModel().getMenuItem(id);
	}
	
	public DishMenuItemData removeMenuItem(String id) {
		this.setMenuOrderTabActive();
		DishMenuItemData itemToBeRemoved = this.getModel().getMenuItem(id);
		
		idBox.setCaption(id);
		
		GeneralTestUtilityClass.performWait(waitTime);
		removeButton.performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		
		if (this.getModel().getMenuItem(id) == null) {
			return itemToBeRemoved;
		} else {
			return null;
		}
	}
	
	public DishMenuItemData editMenuItem(String name, String id, BigDecimal price, BigDecimal productionCost, BigDecimal portionSize, BigDecimal discount) {
		this.setMenuOrderTabActive();
		dishNameBox.setCaption(name);
		priceBox.setCaption(String.valueOf(price.doubleValue()));
		idBox.setCaption(id);
		prodCostBox.setCaption(String.valueOf(productionCost.doubleValue()));
		porSizeBox.setCaption(String.valueOf(portionSize.doubleValue()));
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		editButton.performArtificialClick();
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		DishMenuItemData editedItem = this.getModel().getMenuItem(id);
		
		return editedItem;
	}
	
	public OrderData addConfirmOrder() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		OrderData data = this.ota.getUnconfirmedOrderList().getItem(0);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.displayOrder(data);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.getAddConfirmButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		this.getView().refreshUnconfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		this.getView().refreshConfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		return data;
	}
	
	public void clickOnConfirmAllOrdersButton() {
		this.oia.getConfirmAllButton().performArtificialClick();
	}
	
	public Collection<OrderData> confirmAllOrders() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
//		Collection<OrderData> unconfirmedOrders = this.getUnconfirmedOrders();
//		OrderData[] confirmedOrders = new OrderData[unconfirmedOrders.size()];
//		GeneralTestUtilityClass.performWait(waitTime);
//		int size = unconfirmedOrders.size();
//		
//		for (int i = 0; i < size; i++) {
//			
//			OrderData data = this.addConfirmOrder();
//			confirmedOrders[i] = data;
//		}
//		
//		Collection<OrderData> co = new ArrayList<OrderData>();
//		
//		for (OrderData d : confirmedOrders) {
//			co.add(d);
//		}
//		
//		return co;
		this.clickOnConfirmAllOrdersButton();
		GeneralTestUtilityClass.performWait(waitTime);
		Collection<OrderData> col = new ArrayList<OrderData>();
		for (OrderData d : this.getModel().getAllConfirmedOrders()) {
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
	
	public OrderData removeUnconfirmedOrder() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		OrderData data = this.ota.getUnconfirmedOrderList().getItem(0);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.displayOrder(data);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.getRemoveButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		this.getView().refreshUnconfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		this.getView().refreshConfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		return data;
	}
	
	public OrderData removeConfirmedOrder() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		OrderData data = this.ota.getConfirmedOrderList().getItem(0);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.displayOrder(data);
		GeneralTestUtilityClass.performWait(waitTime);
		this.oia.getRemoveButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
		this.getView().refreshUnconfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		this.getView().refreshConfirmedOrders();
		GeneralTestUtilityClass.performWait(waitTime);
		return data;
	}
	
	public Collection<OrderData> getUnconfirmedOrders() {
		this.setMenuOrderTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		return ota.getUnconfirmedOrderList().getAllItems();
	}
	
	public Collection<OrderData> getConfirmedOrders() {
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
	
	public void assertShownOrderEquals(OrderData orderData) {
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
		String[] itemIDs = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getItem().getID().toString()).toArray(String[]::new);
		BigDecimal[] itemAmounts = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getAmount()).toArray(BigDecimal[]::new);
		BigDecimal[] itemGrossPrices = oia.getOrderDetailsDisplay().getAllItems().stream().map(d -> d.getItem().getGrossPrice()).toArray(BigDecimal[]::new);
		
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
		AccumulatingAggregateEntry<DishMenuItemData>[] idatas = orderData.getOrderedItems();
		int orderItemLen = idatas.length;
		for (int i = 0; i < orderItemLen; i++) {
			OrderTestUtilityClass.assertOrderItemEqual(idatas[i], itemIDs[i], itemAmounts[i]);
			Assertions.assertEquals(idatas[i].getItem().getGrossPrice().compareTo(itemGrossPrices[i]), 0);
		}
	}
	
	public void writeDishMenu() {
//		GeneralTestUtilityClass.performWait(waitTime);
		this.setMenuOrderTabActive();
//		GeneralTestUtilityClass.performWait(waitTime);
		this.menuSaveButton.performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	private void setMenuOrderTabActive() {
		this.tabPane.selectTab(this.menuOrderAreaTabName);
	}

	public void inputTableNumberRanges(String text) {
		this.setSettingsAreaTabActive();
//		GeneralTestUtilityClass.performWait(waitTime);
		this.tableNumberInput.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void inputExportAddress(String text) {
		this.setSettingsAreaTabActive();
//		GeneralTestUtilityClass.performWait(waitTime);
		this.exportAddressInput.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}
}
