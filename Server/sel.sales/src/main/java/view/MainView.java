package view;

import controller.IController;
import view.repository.UIComponent;
import view.repository.UIComponentFactory;
import view.repository.UIInnerFrame;
import view.repository.UIRootComponent;
import view.composites.MainWindow;
import view.composites.MenuDesignArea;
import view.composites.OrderInspectionArea;
import view.composites.OrderTrackingArea;

public class MainView extends View {

	UIRootComponent mainWindow;
	UIInnerFrame frame;
	MainWindow mainDisplay;
	
	MenuDesignArea menuDesignArea;
	OrderTrackingArea orderTrackingArea;
	OrderInspectionArea orderInspectionArea;
	
	UIComponentFactory fac;
	
	public MainView(UIComponentFactory fac, IController controller) {
		super(controller);
		this.fac = fac;
		
		this.mainWindow = this.initMainWindow();
		this.mainDisplay = this.initMainDisplay();
		this.frame = this.initFrame(this.mainDisplay);
		
		this.menuDesignArea = this.initMenuDesignArea();
		this.orderTrackingArea = this.initOrderTrackingArea();
		this.orderInspectionArea = this.initOrderInspectionArea();
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
	protected MenuDesignArea initMenuDesignArea() {
		return new MenuDesignArea(this.fac);
	}
	protected OrderTrackingArea initOrderTrackingArea() {
		return new OrderTrackingArea(this.fac);
	}
	protected OrderInspectionArea initOrderInspectionArea() {
		return new OrderInspectionArea(this.fac);
	}
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
	}
}
