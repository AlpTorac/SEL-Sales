package client.view.composites;

import java.util.Collection;

import controller.IController;
import model.IModel;
import model.dish.IDishMenuData;
import model.order.IOrderData;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UITabPane;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderArea extends UIVBoxLayout {
	private UILayout tabArea;
	
	private OrderTakingArea ota;
	private CookingOrdersArea coa;
	private PendingPaymentOrdersArea uoa;
	private PastOrdersArea poa;
	
	private UITabPane tabPane;
	
	private IModel model;
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private String otaTabName = "Order Taking Area";
	private String coaTabName = "Cooking Orders Area";
	private String uoaTabName = "Unpaid Orders Area";
	private String poaTabName = "Past Orders Area";
	
	public OrderArea(IModel model, IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.model = model;
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		
		this.tabArea = this.initTabArea();
		
		this.ota = this.initOrderTakingArea();
		this.coa = this.initCookingOrdersArea();
		this.uoa = this.initUnpaidOrdersArea();
		this.poa = this.initPastOrdersArea();
		
		this.tabPane = this.initTabPane();
		this.tabPane.attachTo(this.tabArea);
		
		this.addUIComponent(this.tabArea);
	}

	protected OrderTakingArea initOrderTakingArea() {
		return new OrderTakingArea(this.controller, this.fac);
	}

	protected CookingOrdersArea initCookingOrdersArea() {
		return new CookingOrdersArea(this.controller, this.fac);
	}

	protected PendingPaymentOrdersArea initUnpaidOrdersArea() {
		return new PendingPaymentOrdersArea(this.controller, this.fac);
	}

	protected PastOrdersArea initPastOrdersArea() {
		return new PastOrdersArea(this.controller, this.fac);
	}

	protected UITabPane initTabPane() {
		UITabPane pane = this.fac.createTabPane();
		pane.addTab(this.otaTabName, this.ota);
		pane.addTab(this.coaTabName, this.coa);
		pane.addTab(this.uoaTabName, this.uoa);
		pane.addTab(this.poaTabName, this.poa);
		return pane;
	}

	protected UILayout initTabArea() {
		return this.fac.createHBoxLayout();
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		this.ota.refreshMenu(menuData);
	}
	
	public void refreshCookingOrders(IOrderData[] orderDatas) {
		this.coa.refreshDisplayedOrders(orderDatas);
	}
	
	public void refreshPendingPaymentOrders(IOrderData[] orderDatas) {
		this.uoa.refreshDisplayedOrders(orderDatas);
	}
	
	public void refreshPastOrdersTab(IOrderData[] pendingSendOrders, IOrderData[] sentOrders) {
		this.refreshPendingSendOrders(pendingSendOrders);
		this.refreshSentOrders(sentOrders);
	}
	
	protected void refreshPendingSendOrders(IOrderData[] orderDatas) {
		this.poa.refreshPendingSendOrders(orderDatas);
	}

	protected void refreshSentOrders(IOrderData[] orderDatas) {
		this.poa.refreshSentOrders(orderDatas);
	}
	
	public void displayOrder(IOrderData data) {
		this.ota.displayOrder(data);
	}

	public void refreshEditTarget(IOrderData editTarget) {
		if (editTarget != null) {
			this.displayOrder(editTarget);
		}
		this.setEditAvailability(editTarget == null);
	}
	
	protected void setEditAvailability(boolean editEnabled) {
		this.coa.getOrderAccordion().setEditAvailability(editEnabled);
		this.uoa.getOrderAccordion().setEditAvailability(editEnabled);
	}

	public void refreshTableNumbers(Collection<Integer> tableNumbers) {
		this.ota.refreshTableNumbers(tableNumbers);
	}
}
