package view;

import controller.IController;
import entrypoint.MainApp;
import view.repository.ClickEventListener;
import view.repository.UIComponent;
import view.repository.UIComponentFactory;
import view.repository.UIInnerFrame;
import view.repository.UIRootComponent;
import view.composites.AddDishListener;
import view.composites.MainWindow;
import view.composites.MenuDesignArea;
import view.composites.OrderInspectionArea;
import view.composites.OrderTrackingArea;

public class MainView extends View {

	private UIRootComponent mainWindow;
	private UIInnerFrame frame;
	private MainWindow mainDisplay;
	
	private UIComponentFactory fac;
	
	public MainView(UIComponentFactory fac, IController controller) {
		super(controller);
		this.fac = fac;
		
		this.mainWindow = this.initMainWindow();
		this.mainDisplay = this.initMainDisplay();
		this.frame = this.initFrame(this.mainDisplay);
		this.initListeners();
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
		MenuDesignArea mda = this.mainDisplay.getMenuDesignArea();
		OrderTrackingArea ota = this.mainDisplay.getOrderTrackingArea();
		OrderInspectionArea oia = this.mainDisplay.getOrderInspectionArea();
		
		ClickEventListener l = new AddDishListener(this.getController(),
				mda.getDishNameBox(),
				mda.getMenuItemIDBox(),
				mda.getPortionBox(),
				mda.getProductionCostBox(),
				mda.getPriceBox()
		);
		mda.getAddButton().addClickListener(l);
	}
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
	}
}
