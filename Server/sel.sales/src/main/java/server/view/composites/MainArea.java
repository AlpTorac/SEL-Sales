package server.view.composites;

import model.DateSettings;
import model.IDateSettings;
import model.order.IOrderData;
import server.controller.IServerController;
import server.controller.ServerSpecificEvent;
import server.model.IServerModel;
import server.view.composites.listeners.AddDishListener;
import server.view.composites.listeners.ConfirmAllOrdersListener;
import server.view.composites.listeners.ConfirmOrderListener;
import server.view.composites.listeners.EditDishListener;
import server.view.composites.listeners.OrderInspectionListener;
import server.view.composites.listeners.RemoveDishListener;
import server.view.composites.listeners.RemoveOrderListener;
import server.view.composites.listeners.WriteDishMenuListener;
import server.view.composites.listeners.WriteOrdersListener;
import view.repository.ILayout;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ChangeEventListener;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class MainArea extends UIHBoxLayout {
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	private IDateSettings ds;
	
	private MenuDesignArea mda;
	private OrderTrackingArea ota;
	private OrderInspectionArea oia;
	
	private IServerController controller;
	private IServerModel model;
	
	public MainArea(IServerController controller, IServerModel model, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createHBoxLayout().getComponent());
		this.controller = controller;
		this.model = model;
		this.ds = new DateSettings();
		this.fac = fac;
		this.advFac = advFac;
		this.init();
	}
	
	private void init() {
		this.setSpacing(100);
		this.getComponent().setMarigins(100, 100, 100, 100);
		this.initAreas();
		this.initListeners();
	}
	
	private void initListeners() {
		ClickEventListener addDishListener = new AddDishListener(controller, mda);
		mda.getAddButton().addClickListener(addDishListener);
		
		ClickEventListener removeDishListener = new RemoveDishListener(controller, mda);
		mda.getRemoveButton().addClickListener(removeDishListener);
		
		ClickEventListener editDishListener = new EditDishListener(controller, mda);
		mda.getEditButton().addClickListener(editDishListener);
		
		ClickEventListener unconfirmedOrderInspectionListener = new OrderInspectionListener(oia);
		ota.getUnconfirmedOrderList().addClickListener(unconfirmedOrderInspectionListener);
		
		ClickEventListener pastOrderInspectionListener = new OrderInspectionListener(oia);
		ota.getConfirmedOrderList().addClickListener(pastOrderInspectionListener);
		
		ClickEventListener orderConfirmListener = new ConfirmOrderListener(controller, oia);
		oia.getAddConfirmButton().addClickListener(orderConfirmListener);
		
		ClickEventListener removeOrderListener = new RemoveOrderListener(controller, oia);
		oia.getRemoveButton().addClickListener(removeOrderListener);
		
		ClickEventListener confirmAllOrdersListener = new ConfirmAllOrdersListener(controller);
		oia.getConfirmAllButton().addClickListener(confirmAllOrdersListener);
		
//		ItemChangeListener unconfirmedOrderListener = new UnconfirmedOrderListener(ota, oia);
//		ota.getUnconfirmedOrderList().addItemChangeListener(unconfirmedOrderListener);
		
		ClickEventListener writeOrdersListener = new WriteOrdersListener(controller);
		ota.getWriteButton().addClickListener(writeOrdersListener);
		
		ClickEventListener writeDishMenuListener = new WriteDishMenuListener(controller);
		mda.getSaveButton().addClickListener(writeDishMenuListener);
		
		ota.getAuto().addChangeEventListener(new ChangeEventListener() {
			@Override
			public void changeAction(Object oldValue, Object newValue) {
				controller.handleApplicationEvent(ServerSpecificEvent.ORDER_CONFIRM_MODEL_CHANGED, new Object[] {newValue});
			}
		});
	}
	
	public void refreshMenu() {
		this.mda.refreshMenuDisplay(model.getMenuData());
	}

	public void refreshUnconfirmedOrders() {
		this.ota.clearUnconfirmedOrderList();
		this.ota.addUnconfirmedOrders(model.getAllUnconfirmedOrders());
//		if (this.ota.getAuto().isToggled()) {
//			this.controller.handleApplicationEvent(BusinessEvent.CONFIRM_ALL_ORDERS, null);
//			this.ota.addConfirmedOrders(model.getAllConfirmedOrders());
//		} else {
//			this.ota.addUnconfirmedOrders(model.getAllUnconfirmedOrders());
//		}
	}

	public void refreshConfirmedOrders() {
		this.ota.clearConfirmedOrderList();
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		this.ota.addConfirmedOrders(confirmedOrders);
//		for (IOrderData order : confirmedOrders) {
//			this.ota.confirmOrder(order);
//		}
	}
	
	private void initAreas() {
		this.mda = this.initMenuDesignArea();
		this.ota = this.initOrderTrackingArea();
		this.oia = this.initOrderInspectionArea();
		
		this.getComponent().addUIComponents(new ILayout[] {
				this.getMenuDesignArea(),
				this.getOrderTrackingArea(),
				this.getOrderInspectionArea()
		});
	}
	
	protected MenuDesignArea initMenuDesignArea() {
		return new MenuDesignArea(this.fac);
	}
	
	protected OrderTrackingArea initOrderTrackingArea() {
		return new OrderTrackingArea(this.fac);
	}
	
	protected OrderInspectionArea initOrderInspectionArea() {
		return new OrderInspectionArea(this.fac, this.ds);
	}

	public OrderInspectionArea getOrderInspectionArea() {
		return oia;
	}

	public OrderTrackingArea getOrderTrackingArea() {
		return ota;
	}

	public MenuDesignArea getMenuDesignArea() {
		return mda;
	}
}
