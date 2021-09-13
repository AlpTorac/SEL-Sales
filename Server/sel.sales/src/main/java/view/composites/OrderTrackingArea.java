package view.composites;

import model.order.IOrderData;
import view.repository.IListView;
import view.repository.IRadioButton;
import view.repository.IToggleGroup;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderTrackingArea extends UIVBoxLayout {
	private IListView<IOrderData> unconfirmedOrderList;
	private IListView<IOrderData> confirmedOrderList;
	private IRadioButton auto;
	private IRadioButton manual;
	private IToggleGroup group;
	
	private UIComponentFactory fac;
	
	public OrderTrackingArea(UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.fac = fac;
		this.init();
	}
	
	private void init() {
		this.setSpacing(20);
		
		this.unconfirmedOrderList = this.initOrderConfirmationTrackingList();
		this.confirmedOrderList = this.initConfirmedOrdersList();
		
		this.addUIComponents(new IUIComponent[] {
				this.getUnconfirmedOrderList(),
				this.initConfirmationSettings(),
				this.getConfirmedOrderList()
		});
	}
	
	protected IListView<IOrderData> initOrderConfirmationTrackingList() {
		IListView<IOrderData> list = this.fac.createListView();
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
	
	protected IListView<IOrderData> initConfirmedOrdersList() {
		IListView<IOrderData> list = this.fac.createListView();
		return list;
	}
	
	public void addUnconfirmedOrders(IOrderData[] orderDatas) {
		for (IOrderData d : orderDatas) {
			this.addUnconfirmedOrder(d);
		}
	}
	
	public void addUnconfirmedOrder(IOrderData orderData) {
		this.getUnconfirmedOrderList().addItemIfNotPresent(orderData);
	}
	
	public void confirmOrder(IOrderData orderData) {
		this.getUnconfirmedOrderList().removeItem(orderData);
		this.addConfirmedOrder(orderData);
	}
	
	public void addConfirmedOrder(IOrderData orderData) {
		this.getConfirmedOrderList().addItemIfNotPresent(orderData);
	}
	
	public void clearUnconfirmedOrderList() {
		this.getUnconfirmedOrderList().clear();
	}
	public void clearConfirmedOrderList() {
		this.getConfirmedOrderList().clear();
	}

	public IListView<IOrderData> getUnconfirmedOrderList() {
		return unconfirmedOrderList;
	}

	public IListView<IOrderData> getConfirmedOrderList() {
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
	
	
}
