package view.composites;

import controller.IController;
import model.connectivity.IClientData;
import view.composites.listeners.AddKnownClientListener;
import view.composites.listeners.ConnectionAllowedListener;
import view.composites.listeners.ConnectionBlockedListener;
import view.composites.listeners.RediscoverClientsListener;
import view.composites.listeners.RemoveKnownClientListener;
import view.repository.IButton;
import view.repository.IConnectionDetailsTable;
import view.repository.ITable;
import view.repository.IUIComponent;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.ItemChangeListener;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;
import view.repository.uiwrapper.UIVBoxLayout;

public class ConnectionArea extends UIHBoxLayout {
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	private IController controller;
	
	private ITable<IClientData> discoveredClients;
	private IButton refreshButton;
	
	private IButton addKnownClientButton;
	private IButton removeKnownClientButton;
	
	private ITable<IClientData> knownClients;
	
	private IButton allowClientButton;
	private IButton blockClientButton;
	
	public ConnectionArea(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createHBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.init();
		this.initListeners();
	}

	private void initListeners() {
		ClickEventListener refreshListener = new RediscoverClientsListener(this.controller);
		this.refreshButton.addClickListener(refreshListener);
		
		ClickEventListener addKnownClientListener = new AddKnownClientListener(this.controller, this.discoveredClients);
		this.addKnownClientButton.addClickListener(addKnownClientListener);
		
		ClickEventListener removeKnownClientListener = new RemoveKnownClientListener(this.controller, this.knownClients);
		this.removeKnownClientButton.addClickListener(removeKnownClientListener);
		
		ClickEventListener connectionAllowedListener = new ConnectionAllowedListener(this.controller, this.knownClients);
		this.allowClientButton.addClickListener(connectionAllowedListener);
		
		ClickEventListener connectionBlockedListener = new ConnectionBlockedListener(this.controller, this.knownClients);
		this.blockClientButton.addClickListener(connectionBlockedListener);
	}
	
	private void init() {
		this.setSpacing(100);
		this.getComponent().setMarigins(100, 100, 100, 100);
		UIVBoxLayout discoveryArea = this.fac.createVBoxLayout();
		discoveryArea.addUIComponents(new IUIComponent[] {
				this.discoveredClients = this.initDiscoveredClients(),
						this.refreshButton = this.initRefreshButton()
		});
		this.addUIComponent(discoveryArea);
		
		UIVBoxLayout discoveryOperationsArea = this.fac.createVBoxLayout();
		discoveryOperationsArea.addUIComponents(new IUIComponent[] {
				this.addKnownClientButton = this.initAddKnownClientButton(),
						this.removeKnownClientButton = this.initRemoveKnownClientButton()
		});
		this.addUIComponent(discoveryOperationsArea);
		
		UIVBoxLayout knownClientArea = this.fac.createVBoxLayout();
		knownClientArea.addUIComponents(new IUIComponent[] {
				this.knownClients = this.initKnownClients(),
						this.allowClientButton = this.initAllowClientButton(),
						this.blockClientButton = this.initBlockClientButton()
		});
		
		this.addUIComponent(knownClientArea);
	}
	
	private ITable<IClientData> initDiscoveredClients() {
		ITable<IClientData> dc = this.fac.createTable();
		dc.addColumn("Name", "ClientName");
		dc.addColumn("address", "ClientAddress");
		return dc;
	}
	
	private IButton initRefreshButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Refresh");
		return b;
	}
	
	private IButton initAddKnownClientButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Add");
		return b;
	}
	
	private IButton initRemoveKnownClientButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Remove");
		return b;
	}
	
	private IButton initAllowClientButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Allow");
		return b;
	}
	
	private IButton initBlockClientButton() {
		IButton b = this.fac.createButton();
		b.setCaption("Block");
		return b;
	}
	
	private ITable<IClientData> initKnownClients() {
		ITable<IClientData> kc = this.fac.createTable();
		//"Allowed to Connect", "isAllowedToConnect", "Connection Status", "isConnected"
		kc.addColumn("Client Name", "ClientName");
		kc.addColumn("Client Address", "ClientAddress");
		kc.addColumn("Allowed to Connect", "IsAllowedToConnect");
		kc.addColumn("Connection Status", "isConnected");
		return kc;
	}
	
	public void refreshDiscoveredClients(IClientData[] discoveredClientData) {
		this.discoveredClients.clear();
		this.discoveredClients.addItemsIfNotPresent(discoveredClientData);
	}
	public void refreshKnownClients(IClientData[] knownClientData) {
		this.knownClients.clear();
		this.knownClients.addItemsIfNotPresent(knownClientData);
	}
}
