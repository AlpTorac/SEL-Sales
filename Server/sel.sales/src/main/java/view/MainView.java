package view;

import controller.IController;
import model.IModel;
import model.order.IOrderData;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.ItemChangeListener;
import view.repository.uiwrapper.UIComponent;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIInnerFrame;
import view.repository.uiwrapper.UIRootComponent;
import view.composites.AddDishListener;
import view.composites.ConfirmAllOrdersListener;
import view.composites.ConfirmOrderListener;
import view.composites.EditDishListener;
import view.composites.MainWindow;
import view.composites.MenuDesignArea;
import view.composites.OrderInspectionArea;
import view.composites.OrderInspectionListener;
import view.composites.OrderTrackingArea;
import view.composites.RemoveDishListener;
import view.composites.RemoveOrderListener;
import view.composites.UnconfirmedOrderListener;

public class MainView extends View {

	private UIRootComponent mainWindow;
	private UIInnerFrame frame;
	private MainWindow mainDisplay;
	
	private MenuDesignArea mda;
	private OrderTrackingArea ota;
	private OrderInspectionArea oia;
	
	private UIComponentFactory fac;
	
	public MainView(UIComponentFactory fac, IController controller, IModel model) {
		super(controller, model);
		this.fac = fac;
		this.initUI();
		this.initListeners();
	}
	
	private void initUI() {
		this.mainWindow = this.initMainWindow();
		this.mainDisplay = this.initMainDisplay();
		this.frame = this.initFrame(this.mainDisplay);
		
		this.mda = this.mainDisplay.getMenuDesignArea();
		this.ota = this.mainDisplay.getOrderTrackingArea();
		this.oia = this.mainDisplay.getOrderInspectionArea();
	}
	
	protected UIRootComponent initMainWindow() {
		UIRootComponent rc = this.fac.createRootComponent();
		rc.setPrefSize(1000, 1000);
		return rc;
	}
	protected MainWindow initMainDisplay() {
		return new MainWindow(this.fac);
	}
	protected UIInnerFrame initFrame(UIComponent parent) {
		return this.fac.createInnerFrame(parent);
	}
	protected void initListeners() {
		ClickEventListener addDishListener = new AddDishListener(this.getController(), mda);
		mda.getAddButton().addClickListener(addDishListener);
		
		ClickEventListener removeDishListener = new RemoveDishListener(this.getController(), mda);
		mda.getRemoveButton().addClickListener(removeDishListener);
		
		ClickEventListener editDishListener = new EditDishListener(this.getController(), mda);
		mda.getEditButton().addClickListener(editDishListener);
		
		ClickEventListener unconfirmedOrderInspectionListener = new OrderInspectionListener(ota, oia);
		ota.getUnconfirmedOrderList().addClickListener(unconfirmedOrderInspectionListener);
		
		ClickEventListener pastOrderInspectionListener = new OrderInspectionListener(ota, oia);
		ota.getPastOrderList().addClickListener(pastOrderInspectionListener);
		
		ClickEventListener orderConfirmListener = new ConfirmOrderListener(this.getController(), oia);
		oia.getAddConfirmButton().addClickListener(orderConfirmListener);
		
		ClickEventListener removeOrderListener = new RemoveOrderListener(this.getController(), oia);
		oia.getRemoveButton().addClickListener(removeOrderListener);
		
		ClickEventListener confirmAllOrdersListener = new ConfirmAllOrdersListener(oia, ota);
		oia.getConfirmAllButton().addClickListener(confirmAllOrdersListener);
		
		ItemChangeListener unconfirmedOrderListener = new UnconfirmedOrderListener(ota, oia);
		ota.getUnconfirmedOrderList().addItemChangeListener(unconfirmedOrderListener);
	}
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
	}
	@Override
	public void refreshMenu() {
		this.mda.refreshMenuDisplay(this.getModel().getMenuData());
	}

	@Override
	public void refreshUnconfirmedOrders() {
		this.ota.clearUnconfirmedOrderList();
		this.ota.addUnconfirmedOrders(this.getModel().getAllUnconfirmedOrders());
	}

	@Override
	public void refreshConfirmedOrders() {
		this.ota.clearConfirmedOrderList();
		IOrderData[] confirmedOrders = this.getModel().getAllConfirmedOrders();
		
		for (IOrderData order : confirmedOrders) {
			this.ota.confirmOrder(order);
		}
	}
}
