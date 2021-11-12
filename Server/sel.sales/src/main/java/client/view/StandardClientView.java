package client.view;

import client.controller.IClientController;
import client.model.IClientModel;
import client.view.composites.ClientConnectionArea;
import client.view.composites.ClientSettingsArea;
import client.view.composites.OrderArea;
import view.repository.IUILibraryHelper;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponent;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIInnerFrame;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UIRootComponent;
import view.repository.uiwrapper.UITabPane;

public class StandardClientView extends ClientView {
	private IUILibraryHelper helper;
	
	private UIRootComponent mainWindow;
	private UIInnerFrame frame;
	
	private OrderArea oa;
	private ClientConnectionArea cca;
	private ClientSettingsArea csa;
	
	private UILayout mainTabArea;
	private UITabPane mainTabPane;
	
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private String oaTabName = "Order Area";
	private String ccaTabName = "Connection Area";
	private String csaTabName = "Settings Area";
	
	public StandardClientView(UIComponentFactory fac, AdvancedUIComponentFactory advFac, IClientController controller, IClientModel model) {
		super(controller, model);
		this.fac = fac;
		this.advFac = advFac;
		this.initUI();
	}
	
	protected void initUI() {
		this.helper = this.initHelper();
		this.mainWindow = this.initMainWindow();
		this.oa = this.initOrderTakingArea();
		this.cca = this.initClientConnectionArea();
		this.csa = this.initClientSettingsArea();
		this.mainTabArea = this.initMainTabArea();
		this.mainTabPane = this.initMainTabPane();
		this.mainTabPane.attachTo(this.mainTabArea);
		this.frame = this.initFrame(this.mainTabArea);
	}
	
	protected ClientSettingsArea initClientSettingsArea() {
		return new ClientSettingsArea(this.getController(), this.fac, this.advFac);
	}

	protected ClientConnectionArea initClientConnectionArea() {
		return new ClientConnectionArea(this.getController(), this.fac, this.advFac);
	}

	protected OrderArea initOrderTakingArea() {
		return new OrderArea(this.getModel(), this.getController(), this.fac, this.advFac);
	}

	protected UIInnerFrame initFrame(UIComponent parent) {
		return this.fac.createInnerFrame(parent);
	}
	
	protected IUILibraryHelper initHelper() {
		IUILibraryHelper helper = this.fac.createUILibraryHelper();
		helper.setImplicitExit(false);
		return helper;
	}
	
	protected UIRootComponent initMainWindow() {
		UIRootComponent mw = this.fac.createRootComponent();
		mw.maximise();
		mw.terminateOnClose();
		return mw;
	}
	
	protected UILayout initMainTabArea() {
		return this.fac.createHBoxLayout();
	}
	
	protected UITabPane initMainTabPane() {
		UITabPane pane = this.fac.createTabPane();
		pane.addTab(this.oaTabName, this.oa);
		pane.addTab(this.ccaTabName, this.cca);
		pane.addTab(this.csaTabName, this.csa);
		return pane;
	}
	
	@Override
	public void show() {
		this.mainWindow.setInnerFrame(frame);
		this.mainWindow.show();
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
	public void refreshMenu() {
		this.helper.queueAsynchroneRunnable(()->{this.oa.refreshMenu(this.getModel().getMenuData());});
	}

	@Override
	public void refreshDiscoveredDevices() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshKnownDevices() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshSettings() {
		// TODO Auto-generated method stub

	}

	@Override
	public void refreshOrders() {
		this.helper.queueAsynchroneRunnable(()->{this.oa.refreshCookingOrders(this.getModel().getAllCookingOrders());});
	}

}
