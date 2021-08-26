package view.composites;

import view.repository.ILayout;
import view.repository.UIComponentFactory;
import view.repository.UIHBoxLayout;

public class MainWindow extends UIHBoxLayout {
	private UIComponentFactory fac;
	
	private MenuDesignArea menuDesignArea;
	private OrderTrackingArea orderTrackingArea;
	private OrderInspectionArea orderInspectionArea;
	
	public MainWindow(UIComponentFactory fac) {
		super(fac.createHBoxLayout().getComponent());
		this.fac = fac;
		this.init();
	}
	
	private void init() {
		this.setSpacing(100);
		this.getComponent().setMarigins(100, 100, 100, 100);
		this.initAreas();
	}
	
	private void initAreas() {
		this.menuDesignArea = this.initMenuDesignArea();
		this.orderTrackingArea = this.initOrderTrackingArea();
		this.orderInspectionArea = this.initOrderInspectionArea();
		
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
		return new OrderInspectionArea(this.fac);
	}

	public OrderInspectionArea getOrderInspectionArea() {
		return orderInspectionArea;
	}

	public OrderTrackingArea getOrderTrackingArea() {
		return orderTrackingArea;
	}

	public MenuDesignArea getMenuDesignArea() {
		return menuDesignArea;
	}
}
