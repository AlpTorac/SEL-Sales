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
	private IListView<IOrderData> pastOrderList;
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
		this.pastOrderList = this.initPastOrdersList();
		
		this.addUIComponents(new IUIComponent[] {
				this.getUnconfirmedOrderList(),
				this.initConfirmationSettings(),
				this.getPastOrderList()
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
		this.manual.setToggled(true);
		
		IRadioButton[] choices = new IRadioButton[] {this.auto, this.manual};
		
		this.group = this.fac.createToggleGroup();
		this.group.addAllToToggleGroup(choices);
		
		optionArea.addUIComponents(choices);
		
		return optionArea;
	}
	
	protected IListView<IOrderData> initPastOrdersList() {
		IListView<IOrderData> list = this.fac.createListView();
		return list;
	}
	
	public void addUnconfirmedOrders(IOrderData[] orderDatas) {
		for (IOrderData d : orderDatas) {
			this.addUnconfirmedOrder(d);
		}
	}
	
	public void addUnconfirmedOrder(IOrderData orderData) {
		if (this.getAuto().isToggled()) {
			this.confirmOrder(orderData);
		} else {
			this.getUnconfirmedOrderList().addItemIfNotPresent(orderData);
		}
	}
	
	public void confirmAllOrders() {
		IOrderData[] datas = this.getUnconfirmedOrderList().getAllItems().toArray(IOrderData[]::new);
		
		for (IOrderData d : datas) {
			this.confirmOrder(d);
		}
	}
	
	public void confirmOrder(IOrderData orderData) {
		this.getUnconfirmedOrderList().removeItem(orderData);
		this.addPastOrder(orderData);
	}
	
	public void addPastOrder(IOrderData orderData) {
		this.getPastOrderList().addItemIfNotPresent(orderData);
	}
	
	public void clearUnconfirmedOrderList() {
		this.getUnconfirmedOrderList().clear();
	}
	public void clearConfirmedOrderList() {
		this.getPastOrderList().clear();
	}

	public IListView<IOrderData> getUnconfirmedOrderList() {
		return unconfirmedOrderList;
	}

	public IListView<IOrderData> getPastOrderList() {
		return pastOrderList;
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
