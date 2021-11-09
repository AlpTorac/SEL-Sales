package server.view;

import server.controller.IServerController;
import server.model.IServerModel;
import server.view.composites.ServerConnectionArea;
import server.view.composites.MainArea;
import server.view.composites.ServerSettingsArea;
import server.view.composites.listeners.LoadDishMenuListener;
import view.repository.IUILibraryHelper;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponent;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIInnerFrame;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UIRootComponent;
import view.repository.uiwrapper.UITabPane;

public class StandardServerView extends ServerView {

	private IUILibraryHelper helper;
	
	private UIRootComponent mainWindow;
	private UIInnerFrame frame;
	
	private UILayout tabArea;
	
	private UITabPane tabPane;
	
	private MainArea mainArea;
	private ServerConnectionArea connArea;
	private ServerSettingsArea settingsArea;
	
//	private MenuDesignArea mda;
//	private OrderTrackingArea ota;
//	private OrderInspectionArea oia;
	
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private String menuOrderAreaTabName = "Menu/Orders";
	private String connAreaTabName = "Connectivity";
	private String settingsAreaTabName = "Settings";
	
	public StandardServerView(UIComponentFactory fac, AdvancedUIComponentFactory advFac, IServerController controller, IServerModel model) {
		super(controller, model);
		this.fac = fac;
		this.advFac = advFac;
		this.initUI();
		this.initListeners();
	}
	
	protected void initUI() {
		this.helper = this.initHelper();
		this.mainWindow = this.initMainWindow();
		this.mainArea = this.initMainArea();
		this.connArea = this.initConnArea();
		this.settingsArea = this.initSettingsArea();
		this.tabArea = this.initTabArea();
		this.tabPane = this.initTabPane();
		this.tabPane.attachTo(this.tabArea);
		this.frame = this.initFrame(this.tabArea);
		
		ClickEventListener loadDishMenuListener = new LoadDishMenuListener(this.getController(), this.fac, this.mainWindow.getComponent());
		mainArea.getMenuDesignArea().getLoadButton().addClickListener(loadDishMenuListener);
	}
	
	protected IUILibraryHelper initHelper() {
		IUILibraryHelper helper = this.fac.createUILibraryHelper();
		helper.setImplicitExit(false);
		return helper;
	}
	
	protected ServerSettingsArea initSettingsArea() {
		return new ServerSettingsArea(this.getController(), this.fac, this.mainWindow.getComponent());
	}

	protected UILayout initTabArea() {
		return this.fac.createHBoxLayout();
	}
	
	protected UITabPane initTabPane() {
		UITabPane tp = this.fac.createTabPane();
		tp.addTab(this.menuOrderAreaTabName, this.mainArea);
		tp.addTab(this.connAreaTabName, this.connArea);
		tp.addTab(this.settingsAreaTabName, this.settingsArea);
		return tp;
	}
	
	protected UIRootComponent initMainWindow() {
		UIRootComponent rc = this.fac.createRootComponent();
		rc.maximise();
		rc.terminateOnClose();
		return rc;
	}
	protected MainArea initMainArea() {
		return new MainArea(this.getController(), this.getModel(), this.fac, this.advFac);
	}
	protected ServerConnectionArea initConnArea() {
		return new ServerConnectionArea(this.getController(), this.fac, this.advFac);
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
		this.helper.queueAsynchroneRunnable(() -> {this.mainArea.refreshMenu();});
	}

	@Override
	public void refreshUnconfirmedOrders() {
		this.helper.queueAsynchroneRunnable(() -> {this.mainArea.refreshUnconfirmedOrders();});
	}

	@Override
	public void refreshConfirmedOrders() {
		this.helper.queueAsynchroneRunnable(() -> {this.mainArea.refreshConfirmedOrders();});
	}

	@Override
	public void hide() {
		this.mainWindow.hide();
	}

	@Override
	public void close() {
		this.mainWindow.closeArtificially();
	}

	@Override
	public void refreshDiscoveredDevices() {
		this.helper.queueAsynchroneRunnable(() -> {this.connArea.refreshDiscoveredDevices(this.getModel().getAllDiscoveredDeviceData());});
	}

	@Override
	public void refreshKnownDevices() {
		this.helper.queueAsynchroneRunnable(() -> {this.connArea.refreshKnownDevices(this.getModel().getAllKnownDeviceData());});
	}

	@Override
	public void refreshConfirmMode() {
		if (this.getModel().getAutoConfirmOrders()) {
			this.mainArea.getOrderTrackingArea().getAuto().setToggled(true);
			this.mainArea.getOrderTrackingArea().getManual().setToggled(false);
		} else {
			this.mainArea.getOrderTrackingArea().getAuto().setToggled(false);
			this.mainArea.getOrderTrackingArea().getManual().setToggled(true);
		}
		this.refreshUnconfirmedOrders();
		this.refreshConfirmedOrders();
	}

	@Override
	public void refreshSettings() {
		this.settingsArea.refreshSettings(this.getModel().getSettings());
	}
}
