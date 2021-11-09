package server.view.composites;

import controller.IController;
import model.connectivity.IDeviceData;
import server.view.composites.listeners.AddKnownDeviceListener;
import server.view.composites.listeners.ConnectionAllowedListener;
import server.view.composites.listeners.ConnectionBlockedListener;
import server.view.composites.listeners.RediscoverDevicesListener;
import server.view.composites.listeners.RemoveKnownDeviceListener;
import view.repository.IButton;
import view.repository.ITable;
import view.repository.IUIComponent;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;
import view.repository.uiwrapper.UIVBoxLayout;

public class ConnectionArea extends UIHBoxLayout {
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	private IController controller;
	
	private ITable<IDeviceData> discoveredDevices;
	private IButton refreshButton;
	
	private IButton addKnownDeviceButton;
	private IButton removeKnownDeviceButton;
	
	private ITable<IDeviceData> knownDevices;
	
	private IButton allowDeviceButton;
	private IButton blockDeviceButton;
	
	public ConnectionArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createHBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.init();
		this.initListeners();
	}

	private void initListeners() {
		ClickEventListener refreshListener = new RediscoverDevicesListener(this.controller);
		this.refreshButton.addClickListener(refreshListener);
		
		ClickEventListener addKnownDeviceListener = new AddKnownDeviceListener(this.controller, this.discoveredDevices);
		this.addKnownDeviceButton.addClickListener(addKnownDeviceListener);
		
		ClickEventListener removeKnownDeviceListener = new RemoveKnownDeviceListener(this.controller, this.knownDevices);
		this.removeKnownDeviceButton.addClickListener(removeKnownDeviceListener);
		
		ClickEventListener connectionAllowedListener = new ConnectionAllowedListener(this.controller, this.knownDevices);
		this.allowDeviceButton.addClickListener(connectionAllowedListener);
		
		ClickEventListener connectionBlockedListener = new ConnectionBlockedListener(this.controller, this.knownDevices);
		this.blockDeviceButton.addClickListener(connectionBlockedListener);
	}
	
	private void init() {
		this.setSpacing(100);
		this.getComponent().setMarigins(100, 100, 100, 100);
		UIVBoxLayout discoveryArea = this.fac.createVBoxLayout();
		discoveryArea.addUIComponents(new IUIComponent[] {
				this.discoveredDevices = this.initDiscoveredDevices(),
						this.refreshButton = this.initRefreshButton()
		});
		this.addUIComponent(discoveryArea);
		
		UIVBoxLayout discoveryOperationsArea = this.fac.createVBoxLayout();
		discoveryOperationsArea.addUIComponents(new IUIComponent[] {
				this.addKnownDeviceButton = this.initAddKnownDeviceButton(),
						this.removeKnownDeviceButton = this.initRemoveKnownDeviceButton()
		});
		this.addUIComponent(discoveryOperationsArea);
		
		UIVBoxLayout knownDeviceArea = this.fac.createVBoxLayout();
		knownDeviceArea.addUIComponents(new IUIComponent[] {
				this.knownDevices = this.initKnownDevices(),
						this.allowDeviceButton = this.initAllowDeviceButton(),
						this.blockDeviceButton = this.initBlockDeviceButton()
		});
		
		this.addUIComponent(knownDeviceArea);
	}
	
	private ITable<IDeviceData> initDiscoveredDevices() {
		ITable<IDeviceData> dc = this.fac.createTable();
		dc.addColumn("Name", "DeviceName");
		dc.addColumn("address", "DeviceAddress");
		return dc;
	}
	
	private IButton initRefreshButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Refresh");
		return b;
	}
	
	private IButton initAddKnownDeviceButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Add");
		return b;
	}
	
	private IButton initRemoveKnownDeviceButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Remove");
		return b;
	}
	
	private IButton initAllowDeviceButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Allow");
		return b;
	}
	
	private IButton initBlockDeviceButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Block");
		return b;
	}
	
	private ITable<IDeviceData> initKnownDevices() {
		ITable<IDeviceData> kc = this.fac.createTable();
		//"Allowed to Connect", "isAllowedToConnect", "Connection Status", "isConnected"
		kc.addColumn("Device Name", "DeviceName");
		kc.addColumn("Device Address", "DeviceAddress");
		kc.addColumn("Allowed to Connect", "IsAllowedToConnect");
		kc.addColumn("Connection Status", "isConnected");
		return kc;
	}
	
	public void refreshDiscoveredDevices(IDeviceData[] discoveredDeviceData) {
		this.discoveredDevices.clear();
		this.discoveredDevices.addItemsIfNotPresent(discoveredDeviceData);
	}
	public void refreshKnownDevices(IDeviceData[] knownDeviceData) {
		this.knownDevices.clear();
		this.knownDevices.addItemsIfNotPresent(knownDeviceData);
	}

	public ITable<IDeviceData> getDiscoveredDevices() {
		return discoveredDevices;
	}

	public IButton getRefreshButton() {
		return refreshButton;
	}

	public IButton getAddKnownDeviceButton() {
		return addKnownDeviceButton;
	}

	public IButton getRemoveKnownDeviceButton() {
		return removeKnownDeviceButton;
	}

	public ITable<IDeviceData> getKnownDevices() {
		return knownDevices;
	}

	public IButton getAllowDeviceButton() {
		return allowDeviceButton;
	}

	public IButton getBlockDeviceButton() {
		return blockDeviceButton;
	}
}
