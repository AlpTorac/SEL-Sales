package view.composites;

import controller.BusinessEvent;
import controller.IController;
import model.IModel;
import model.order.IOrderData;
import view.DateSettings;
import view.IDateSettings;
import view.repository.ILayout;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.ItemChangeListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class MainArea extends UIHBoxLayout {
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	private IDateSettings ds;
	
	private MenuDesignArea mda;
	private OrderTrackingArea ota;
	private OrderInspectionArea oia;
	
	private IController controller;
	private IModel model;
	
	public MainArea(IController controller, IModel model, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
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
		
		ClickEventListener unconfirmedOrderInspectionListener = new OrderInspectionListener(ota, oia);
		ota.getUnconfirmedOrderList().addClickListener(unconfirmedOrderInspectionListener);
		
		ClickEventListener pastOrderInspectionListener = new OrderInspectionListener(ota, oia);
		ota.getConfirmedOrderList().addClickListener(pastOrderInspectionListener);
		
		ClickEventListener orderConfirmListener = new ConfirmOrderListener(controller, oia);
		oia.getAddConfirmButton().addClickListener(orderConfirmListener);
		
		ClickEventListener removeOrderListener = new RemoveOrderListener(controller, oia);
		oia.getRemoveButton().addClickListener(removeOrderListener);
		
		ClickEventListener confirmAllOrdersListener = new ConfirmAllOrdersListener(controller);
		oia.getConfirmAllButton().addClickListener(confirmAllOrdersListener);
		
		ItemChangeListener unconfirmedOrderListener = new UnconfirmedOrderListener(ota, oia);
		ota.getUnconfirmedOrderList().addItemChangeListener(unconfirmedOrderListener);
	}
	
	public void refreshMenu() {
		this.mda.refreshMenuDisplay(model.getMenuData());
	}

	public void refreshUnconfirmedOrders() {
		this.ota.clearUnconfirmedOrderList();
		if (this.ota.getAuto().isToggled()) {
			/*
			 * ToDo: Make a listener for the auto-confirm radio button and add a variable for auto-confirming in model (?)
			 */
			this.controller.handleApplicationEvent(BusinessEvent.CONFIRM_ALL_ORDERS, null);
			this.ota.addConfirmedOrders(model.getAllConfirmedOrders());
		} else {
			this.ota.addUnconfirmedOrders(model.getAllUnconfirmedOrders());
		}
	}

	public void refreshConfirmedOrders() {
		this.ota.clearConfirmedOrderList();
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		
		for (IOrderData order : confirmedOrders) {
			this.ota.confirmOrder(order);
		}
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
