package test.client.view;

import java.util.Collection;

import client.view.composites.CookingOrdersArea;
import client.view.composites.EditableMenuItemEntry;
import client.view.composites.OrderArea;
import client.view.composites.OrderTakingArea;
import client.view.composites.PastOrdersArea;
import client.view.composites.PendingPaymentOrderEntry;
import client.view.composites.PendingPaymentOrdersArea;
import controller.IController;
import model.IModel;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.IOrderItemData;
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
	
	public void orderTakingAreaSetEntryItem(IDishMenuItemData item, int pos) {
		this.setOrderTakingAreaTabActive();
		GeneralTestUtilityClass.performWait(this.waitTime);
		EditableMenuItemEntry e = this.ota.getEntry().getEntry(pos);
		GeneralTestUtilityClass.performWait(this.waitTime);
		e.getMenuItemChoiceBox().artificiallySelectItem(item);
//		for (IDishMenuItemData d : e.getActiveMenu().getAllDishMenuItems()) {
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
	
	public IOrderItemData[] getOrderTakingAreaCurrentOrder() {
		return this.ota.getEntry().getCurrentOrder();
	}
	
	public String getOrderTakingAreaSerialisedOrder() {
		return this.ota.getEntry().serialiseCurrentOrder();
	}
	
	public void getOrderTakingAreaDisplayOrder(IOrderData data) {
		this.oa.displayOrder(data);
	}
	
	public Collection<IOrderData> getCookingOrders() {
		return this.coa.getOrderAccordion().getDisplayedOrders();
	}
	
	public Collection<IOrderData> getPendingPaymentOrders() {
		return this.uoa.getOrderAccordion().getDisplayedOrders();
	}
	
	public Collection<IOrderData> getPendingSendOrders() {
		return this.poa.getPendingSendOrderAccordion().getDisplayedOrders();
	}
	
	public Collection<IOrderData> getSentOrders() {
		return this.poa.getSentOrderAccordion().getDisplayedOrders();
	}
	
	public void ppoaSetPaymentOption(String orderID, boolean isCash) {
		PendingPaymentOrderEntry e = this.uoa.getOrderAccordion().getEntry(orderID);
		e.getCashRadioButton().setToggled(isCash);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public void ppoaSetPlaceOption(String orderID, boolean isHere) {
		PendingPaymentOrderEntry e = this.uoa.getOrderAccordion().getEntry(orderID);
		e.getHereRadioButton().setToggled(isHere);
		GeneralTestUtilityClass.performWait(waitTime);
	}
	
	public boolean ppoaGetIsCash(String orderID) {
		return this.uoa.getOrderAccordion().getEntry(orderID).getCashRadioButton().isToggled();
	}
	
	public boolean ppoaGetIsHere(String orderID) {
		return this.uoa.getOrderAccordion().getEntry(orderID).getHereRadioButton().isToggled();
	}
	
	public String ppoaGetSerialisedOrder(String formerID) {
		return this.uoa.getOrderAccordion().getEntry(formerID).serialiseCurrentOrder();
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
}
