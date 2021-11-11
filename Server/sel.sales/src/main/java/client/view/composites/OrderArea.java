package client.view.composites;

import controller.IController;
import model.IModel;
import model.dish.IDishMenuData;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UITabPane;
import view.repository.uiwrapper.UIVBoxLayout;

public class OrderArea extends UIVBoxLayout {
	private UILayout tabArea;
	
	private OrderTakingArea ota;
	private CookingOrdersArea coa;
	private UnpaidOrdersArea uoa;
	private PastOrdersArea poa;
	
	private UITabPane tabPane;
	
	private IModel model;
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private String otaTabName = "Order Taking Area";
	private String coaTabName = "Cooking Orders Area";
	private String uoaTabName = "Unpaid Orders Area";
	private String poaTabName = "Past Orders Area";
	
	public OrderArea(IModel model, IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createVBoxLayout().getComponent());
		this.model = model;
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		
		this.tabArea = this.initTabArea();
		
		this.ota = this.initOrderTakingArea();
		this.coa = this.initCookingOrdersArea();
		this.uoa = this.initUnpaidOrdersArea();
		this.poa = this.initPastOrdersArea();
		
		this.tabPane = this.initTabPane();
		this.tabPane.attachTo(this.tabArea);
		
		this.addUIComponent(this.tabArea);
	}

	protected OrderTakingArea initOrderTakingArea() {
		return new OrderTakingArea(this.controller, this.fac, this.advFac);
	}

	protected CookingOrdersArea initCookingOrdersArea() {
		return new CookingOrdersArea(this.controller, this.fac, this.advFac);
	}

	protected UnpaidOrdersArea initUnpaidOrdersArea() {
		return new UnpaidOrdersArea(this.controller, this.fac, this.advFac);
	}

	protected PastOrdersArea initPastOrdersArea() {
		return new PastOrdersArea(this.controller, this.fac, this.advFac);
	}

	protected UITabPane initTabPane() {
		UITabPane pane = this.fac.createTabPane();
		pane.addTab(this.otaTabName, this.ota);
		pane.addTab(this.coaTabName, this.coa);
		pane.addTab(this.uoaTabName, this.uoa);
		pane.addTab(this.poaTabName, this.poa);
		return pane;
	}

	protected UILayout initTabArea() {
		return this.fac.createHBoxLayout();
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		this.ota.refreshMenu(menuData);
	}
}
