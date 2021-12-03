package test.client.view;

import java.util.Collection;

import client.controller.IClientController;
import client.model.IClientModel;
import client.view.IClientView;
import client.view.composites.CookingOrderEntry;
import client.view.composites.CookingOrdersArea;
import client.view.composites.EditableMenuItemEntry;
import client.view.composites.OrderArea;
import client.view.composites.OrderTakingArea;
import client.view.composites.OrderTakingAreaOrderEntry;
import client.view.composites.PastOrdersArea;
import client.view.composites.PendingPaymentOrderEntry;
import client.view.composites.PendingPaymentOrdersArea;
import controller.IController;
import model.IModel;
import model.dish.DishMenuItemData;
import model.order.OrderData;
import model.order.AccumulatingOrderItemAggregate;
import test.GeneralTestUtilityClass;
import test.ViewOperationsUtilityClass;
import view.IView;
import view.repository.IButton;
import view.repository.uiwrapper.UITabPane;

public class StandardClientViewOperationsUtilityClass extends ViewOperationsUtilityClass {
	private String oaTabName;
	private OrderArea oa;
	private UITabPane oaTabPane;
	
	private OrderTakingArea ota;
	private CookingOrdersArea coa;
	private PendingPaymentOrdersArea uoa;
	private PastOrdersArea poa;
	
	private String otaTabName;
	private String coaTabName;
	private String uoaTabName;
	private String poaTabName;
	
	public StandardClientViewOperationsUtilityClass(IView view, IController controller, IModel model) {
		super(view, controller, model);
		
		this.oa = GeneralTestUtilityClass.getPrivateFieldValue(view, "oa");
		this.oaTabPane = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "tabPane");
		
		this.ota = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "ota");
		this.coa = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "coa");
		this.uoa = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "uoa");
		this.poa = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "poa");
		
		this.tabArea = GeneralTestUtilityClass.getPrivateFieldValue(view, "mainTabArea");
		this.tabPane = GeneralTestUtilityClass.getPrivateFieldValue(view, "mainTabPane");
		
		this.oaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "oaTabName");
		this.connAreaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "ccaTabName");
		this.settingsAreaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "csaTabName");
		
		this.otaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "otaTabName");
		this.coaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "coaTabName");
		this.uoaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "uoaTabName");
		this.poaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "poaTabName");
	}
	
	@Override
	protected IClientView getView() {
		return (IClientView) super.getView();
	}
	
	@Override
	protected IClientController getController() {
		return (IClientController) super.getController();
	}
	
	@Override
	protected IClientModel getModel() {
		return (IClientModel) super.getModel();
	}
	
	protected String getConnectionAreaFieldName() {
		return "cca";
	}
	
	protected String getSettingsAreaFieldName() {
		return "csa";
	}
	
//	public void clickOnNewMenuItemEntryButton() {
//		this.setOrderTakingAreaTabActive();
//		GeneralTestUtilityClass.performWait(waitTime);
//		this.ota.getAddMenuItemButton().performArtificialClick();
//		GeneralTestUtilityClass.performWait(waitTime);
//	}
//	
//	public void clickOnOTANextTabButton() {
//		this.setOrderTakingAreaTabActive();
//		GeneralTestUtilityClass.performWait(waitTime);
//		this.ota.getNextTabButton().performArtificialClick();
//		GeneralTestUtilityClass.performWait(waitTime);
//	}
	
	public void orderTakingAreaNewEntry() {
		this.setOrderTakingAreaTabActive();
		GeneralTestUtilityClass.performWait(this.waitTime);
		this.ota.getEntry().getAddMenuItemButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(this.waitTime);
	}
	
	public void orderTakingAreaSetEntryItem(DishMenuItemData item, int pos) {
		this.setOrderTakingAreaTabActive();
		GeneralTestUtilityClass.performWait(this.waitTime);
		EditableMenuItemEntry e = this.ota.getEntry().getEntry(pos);
		GeneralTestUtilityClass.performWait(this.waitTime);
		e.getMenuItemChoiceBox().artificiallySelectItem(item);
//		for (DishMenuItemData d : e.getActiveMenu().getAllDishMenuItems()) {
//			if (d.equals(item)) {
//				e.getMenuItemChoiceBox().artificiallySelectItem(d);
//				break;
//			}
//		}
		GeneralTestUtilityClass.performWait(this.waitTime);
	}
	
	public void orderTakingAreaIncEntryAmount(int pos) {
		this.setOrderTakingAreaTabActive();
		GeneralTestUtilityClass.performWait(this.waitTime);
		EditableMenuItemEntry e = this.ota.getEntry().getEntry(pos);
		e.getAmountIncButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(this.waitTime);
	}
	
	public void orderTakingAreaDecEntryAmount(int pos) {
		this.setOrderTakingAreaTabActive();
		GeneralTestUtilityClass.performWait(this.waitTime);
		EditableMenuItemEntry e = this.ota.getEntry().getEntry(pos);
		e.getAmountDecButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(this.waitTime);
	}
	
	public void orderTakingAreaRemoveEntry(int pos) {
		this.setOrderTakingAreaTabActive();
		GeneralTestUtilityClass.performWait(this.waitTime);
		EditableMenuItemEntry e = this.ota.getEntry().getEntry(pos);
		e.getRemoveButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(this.waitTime);
	}
	
	public void orderTakingAreaNextTab() {
		this.setOrderTakingAreaTabActive();
		GeneralTestUtilityClass.performWait(this.waitTime);
		this.ota.getEntry().getNextTabButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(this.waitTime);
	}
	
	public int getOrderTakingAreaEntryCount() {
		return this.ota.getEntry().getEntries().size();
	}
	
	public AccumulatingOrderItemAggregate[] getOrderTakingAreaCurrentOrderItems() {
		return this.ota.getEntry().getCurrentOrderItems();
	}
	
	public OrderData getOrderTakingAreaCurrentOrder() {
		return this.getModel().getOrderHelper().deserialiseOrderData(this.ota.getEntry().getCurrentOrder());
	}
	
	public String getOrderTakingAreaSerialisedOrder() {
		return this.ota.getEntry().getCurrentOrder();
	}
	
	public void orderTakingAreaDisplayOrder(OrderData data) {
		this.oa.displayOrder(data);
	}
	
	public Collection<OrderData> getCookingOrders() {
		return this.coa.getOrderAccordion().getDisplayedOrders();
	}
	
	public Collection<OrderData> getPendingPaymentOrders() {
		return this.uoa.getOrderAccordion().getDisplayedOrders();
	}
	
	public Collection<OrderData> getPendingSendOrders() {
		return this.poa.getPendingSendOrderAccordion().getDisplayedOrders();
	}
	
	public Collection<OrderData> getSentOrders() {
		return this.poa.getSentOrderAccordion().getDisplayedOrders();
	}
	
	public void ppoaSetPaymentOption(String orderID, boolean isCash) {
		PendingPaymentOrderEntry e = this.uoa.getOrderAccordion().getEntry(orderID);
		while (e.getCashRadioButton().isToggled() != isCash) {
			e.getCashRadioButton().setToggled(isCash);
			e.getCardRadioButton().setToggled(!isCash);
		}
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void ppoaSetPlaceOption(String orderID, boolean isHere) {
		PendingPaymentOrderEntry e = this.uoa.getOrderAccordion().getEntry(orderID);
		while (e.getHereRadioButton().isToggled() != isHere) {
			e.getHereRadioButton().setToggled(isHere);
			e.getToGoRadioButton().setToggled(!isHere);
		}
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public boolean ppoaGetIsCash(String orderID) {
		return this.uoa.getOrderAccordion().getEntry(orderID).getCashRadioButton().isToggled();
	}
	
	public boolean ppoaGetIsHere(String orderID) {
		return this.uoa.getOrderAccordion().getEntry(orderID).getHereRadioButton().isToggled();
	}
	
	public String ppoaGetSerialisedOrder(String formerID) {
		return this.uoa.getOrderAccordion().getEntry(formerID).getCurrentOrder();
	}
	
	protected void setOrderAreaTabActive() {
		this.tabPane.selectTab(this.oaTabName);
	}
	
	protected void setOrderTakingAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.otaTabName);
	}
	
	protected void setCookingOrderAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.coaTabName);
	}
	
	protected void setPendingPaymentOrderAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.uoaTabName);
	}
	
	protected void setSentOrderAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.poaTabName);
	}

	@Override
	public void inputTableNumberRanges(String tableNumberRanges) {
		
	}
	
	public void addCookingOrder(String orderID) {
		OrderTakingAreaOrderEntry e = this.ota.getEntry();
		GeneralTestUtilityClass.performWait(waitTime);
		while (this.getModel().getCookingOrder(orderID) == null) {
			e.getNextTabButton().performArtificialClick();
			GeneralTestUtilityClass.performWait(10);
//			System.out.println("Attempting to add cooking order");
		}
		GeneralTestUtilityClass.performWait(waitTime);
		this.setCookingOrderAreaTabActive();
	}
	
	public void addPendingPaymentOrder(String orderID) {
		CookingOrderEntry e = ((CookingOrderEntry) this.coa.getOrderAccordion().getEntry(orderID));
		GeneralTestUtilityClass.performWait(waitTime);
		while (this.getModel().getPendingPaymentOrder(orderID) == null) {
			e.getNextTabButton().performArtificialClick();
			GeneralTestUtilityClass.performWait(10);
//			System.out.println("Attempting to add pendingPayment order");
		}
		GeneralTestUtilityClass.performWait(waitTime);
		this.setPendingPaymentOrderAreaTabActive();
	}
	
	public void addPendingSendOrder(String orderID, boolean isCash, boolean isHere) {
		GeneralTestUtilityClass.performWait(waitTime);
		PendingPaymentOrderEntry e = ((PendingPaymentOrderEntry) this.uoa.getOrderAccordion().getEntry(orderID));
		while (this.getModel().getPendingSendOrder(orderID) == null &&
				this.getModel().getSentOrder(orderID) == null) {
			this.ppoaSetPaymentOption(orderID, isCash);
			this.ppoaSetPlaceOption(orderID, isHere);
			e.getNextTabButton().performArtificialClick();
			GeneralTestUtilityClass.performWait(10);
//			System.out.println("Attempting to add pendingSend order");
		}
		GeneralTestUtilityClass.performWait(waitTime);
		this.setSentOrderAreaTabActive();
	}
	
	public void selectTableNumber(Integer i) {
		GeneralTestUtilityClass.performWait(waitTime);
		this.ota.getEntry().setTableNumber(i);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void setOrderNote(String orderNote) {
		GeneralTestUtilityClass.performWait(waitTime);
		this.ota.getEntry().setOrderNode(orderNote);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void orderTakingAreaClear() {
		GeneralTestUtilityClass.performWait(waitTime);
		this.ota.getEntry().getClearButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void cookingOrderAreaRemove(String orderID) {
		GeneralTestUtilityClass.performWait(waitTime);
		((CookingOrderEntry) this.coa.getOrderAccordion().getEntry(orderID)).getRemoveButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void pendingPaymentOrderAreaRemove(String orderID) {
		GeneralTestUtilityClass.performWait(waitTime);
		((PendingPaymentOrderEntry) this.uoa.getOrderAccordion().getEntry(orderID)).getRemoveButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}
}
