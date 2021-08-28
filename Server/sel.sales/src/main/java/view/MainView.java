package view;

import controller.IController;
import model.IModel;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponent;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIInnerFrame;
import view.repository.uiwrapper.UIRootComponent;
import view.composites.AddDishListener;
import view.composites.EditDishListener;
import view.composites.MainWindow;
import view.composites.MenuDesignArea;
import view.composites.OrderInspectionArea;
import view.composites.OrderTrackingArea;
import view.composites.RemoveDishListener;

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
		ClickEventListener addDishListener = new AddDishListener(
				this.getController(),
				mda.getDishNameBox(),
				mda.getMenuItemIDBox(),
				mda.getPortionBox(),
				mda.getProductionCostBox(),
				mda.getPriceBox()
		);
		mda.getAddButton().addClickListener(addDishListener);
		
		ClickEventListener removeDishListener = new RemoveDishListener(
				this.getController(),
				mda.getMenuItemIDBox()
		);
		mda.getRemoveButton().addClickListener(removeDishListener);
		
		ClickEventListener editDishListener = new EditDishListener(
				this.getController(),
				mda.getDishNameBox(),
				mda.getMenuItemIDBox(),
				mda.getPortionBox(),
				mda.getProductionCostBox(),
				mda.getPriceBox()
		);
		mda.getEditButton().addClickListener(editDishListener);
	}
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
	}
	@Override
	public void refreshMenu() {
		this.mda.refreshMenuDisplay(this.getModel().getMenuData());
	}
}
