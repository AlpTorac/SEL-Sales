package view;

import controller.IController;
import model.IModel;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponent;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIInnerFrame;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UIRootComponent;
import view.repository.uiwrapper.UITabPane;
import view.composites.ConnectionArea;
import view.composites.MainArea;

public class MainView extends View {

	private UIRootComponent mainWindow;
	private UIInnerFrame frame;
	
	private UILayout tabArea;
	
	private UITabPane tabPane;
	
	private MainArea mainArea;
	private ConnectionArea connArea;
	
//	private MenuDesignArea mda;
//	private OrderTrackingArea ota;
//	private OrderInspectionArea oia;
	
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	public MainView(UIComponentFactory fac, AdvancedUIComponentFactory advFac, IController controller, IModel model) {
		super(controller, model);
		this.fac = fac;
		this.advFac = advFac;
		this.initUI();
		this.initListeners();
	}
	
	private void initUI() {
		this.mainWindow = this.initMainWindow();
		this.mainArea = this.initMainArea();
		this.connArea = this.initConnArea();
		this.tabArea = this.initTabArea();
		this.tabPane = this.initTabPane();
		this.tabPane.attachTo(this.tabArea);
		this.frame = this.initFrame(this.tabArea);
	}
	
	protected UILayout initTabArea() {
		return this.fac.createHBoxLayout();
	}
	
	protected UITabPane initTabPane() {
		UITabPane tp = this.fac.createTabPane();
		tp.addTab("Menu/Orders", this.mainArea);
		tp.addTab("Connectivity", this.connArea);
		return tp;
	}
	
	protected UIRootComponent initMainWindow() {
		UIRootComponent rc = this.fac.createRootComponent();
		rc.setPrefSize(1000, 1000);
		return rc;
	}
	protected MainArea initMainArea() {
		return new MainArea(this.getController(), this.getModel(), this.fac, this.advFac);
	}
	protected ConnectionArea initConnArea() {
		return new ConnectionArea(this.getController(), this.fac, this.advFac);
	}
	protected UIInnerFrame initFrame(UIComponent parent) {
		return this.fac.createInnerFrame(parent);
	}
	protected void initListeners() {

	}
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
	}
	@Override
	public void refreshMenu() {
		this.mainArea.refreshMenu();
	}

	@Override
	public void refreshUnconfirmedOrders() {
		this.mainArea.refreshUnconfirmedOrders();
	}

	@Override
	public void refreshConfirmedOrders() {
		this.mainArea.refreshConfirmedOrders();
	}

	@Override
	public void hide() {
		this.mainWindow.hide();
	}

	@Override
	public void close() {
		this.mainWindow.close();
	}

	@Override
	public void refreshDiscoveredClients() {
		this.connArea.refreshDiscoveredClients(this.getModel().getAllDiscoveredClientData());
	}

	@Override
	public void refreshKnownClients() {
		this.connArea.refreshKnownClients(this.getModel().getAllKnownClientData());
	}
}
