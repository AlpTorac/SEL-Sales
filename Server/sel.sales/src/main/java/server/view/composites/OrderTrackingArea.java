package server.view.composites;

import model.order.OrderData;
import view.repository.IButton;
import view.repository.IListView;
import view.repository.IRadioButton;
import view.repository.IToggleGroup;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderTrackingArea extends UIVBoxLayout {
	private IListView<OrderData> unconfirmedOrderList;
	private IListView<OrderData> confirmedOrderList;
	private IRadioButton auto;
	private IRadioButton manual;
	private IToggleGroup group;
	private IButton exportButton;
	
	private UIComponentFactory fac;
	
	public OrderTrackingArea(UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.fac = fac;
		this.init();
	}
	
	private void init() {
		this.setSpacing(20);
		
		this.unconfirmedOrderList = this.initOrderConfirmationTrackingList();
		this.unconfirmedOrderList.setMinWidth(150);
		this.confirmedOrderList = this.initConfirmedOrdersList();
		this.confirmedOrderList.setMinWidth(150);
		
		this.addUIComponents(new IUIComponent[] {
				this.getUnconfirmedOrderList(),
				this.initConfirmationSettings(),
				this.getConfirmedOrderList(),
				this.exportButton = this.initExportButton()
		});
	}
	
	protected IButton initExportButton() {
		IButton button = fac.createButton();
		button.setCaption("Export Orders");
		return button;
	}
	
	protected IListView<OrderData> initOrderConfirmationTrackingList() {
		IListView<OrderData> list = this.fac.createListView();
		return list;
	}
	
	protected IVBoxLayout initConfirmationSettings() {
		IVBoxLayout optionArea = this.fac.createVBoxLayout();
		
		this.auto = this.fac.createRadioButton();
		this.auto.setCaption("Auto-Confirm");
		
		this.manual = this.fac.createRadioButton();
		this.manual.setCaption("Manual Confirmation");
		
		IRadioButton[] choices = new IRadioButton[] {this.auto, this.manual};
		
		this.group = this.fac.createToggleGroup();
		this.group.addAllToToggleGroup(choices);
		
		optionArea.addUIComponents(choices);
		
		this.manual.setToggled(true);
		
		return optionArea;
	}
	
	protected IListView<OrderData> initConfirmedOrdersList() {
		IListView<OrderData> list = this.fac.createListView();
		return list;
	}
	
	public void addUnconfirmedOrders(OrderData[] orderDatas) {
		for (OrderData d : orderDatas) {
			this.addUnconfirmedOrder(d);
		}
	}
	
	public void addUnconfirmedOrder(OrderData orderData) {
		this.getUnconfirmedOrderList().addItemIfNotPresent(orderData);
	}
	
//	public void confirmOrder(OrderData orderData) {
//		this.getUnconfirmedOrderList().removeItem(orderData);
//		this.addConfirmedOrder(orderData);
//	}
	
	public void addConfirmedOrder(OrderData orderData) {
		this.getConfirmedOrderList().addItemIfNotPresent(orderData);
	}
	
	public void clearUnconfirmedOrderList() {
		this.getUnconfirmedOrderList().clear();
	}
	public void clearConfirmedOrderList() {
		this.getConfirmedOrderList().clear();
	}

	public IListView<OrderData> getUnconfirmedOrderList() {
		return unconfirmedOrderList;
	}

	public IListView<OrderData> getConfirmedOrderList() {
		return confirmedOrderList;
	}

	public IRadioButton getAuto() {
		return auto;
	}

	public IRadioButton getManual() {
		return manual;
	}

	public IToggleGroup getGroup() {
		return group;
	}

	public void addConfirmedOrders(OrderData[] allConfirmedOrders) {
		for (OrderData d : allConfirmedOrders) {
			this.addConfirmedOrder(d);
		}
	}
	
	public IButton getExportButton() {
		return this.exportButton;
	}
}
