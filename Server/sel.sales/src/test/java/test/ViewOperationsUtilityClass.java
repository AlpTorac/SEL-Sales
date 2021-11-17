package test;

import java.util.Collection;

import controller.IController;
import model.IModel;
import model.connectivity.IDeviceData;
import view.IView;
import view.composites.ConnectionArea;
import view.composites.SettingsArea;
import view.repository.HasText;
import view.repository.IButton;
import view.repository.uiwrapper.UILayout;
import view.repository.uiwrapper.UITabPane;

public abstract class ViewOperationsUtilityClass {

	protected long waitTime = 100;
	private IModel model;
	private IController controller;
	private IView view;
	protected UILayout tabArea;
	protected UITabPane tabPane;
	protected String connAreaTabName;
	protected String settingsAreaTabName;
	protected ConnectionArea ca;
	protected SettingsArea sa;
	protected HasText menuFolderAddress;
	protected HasText orderFolderAddress;
	protected HasText ppTimeout;
	protected HasText ppMinimalDelay;
	protected HasText ppResendLimit;
	protected HasText sendTimeout;
	protected IButton applySettingsButton;

	public ViewOperationsUtilityClass(IView view, IController controller, IModel model) {
		this.view = view;
		this.controller = controller;
		this.model = model;
		this.ca = GeneralTestUtilityClass.getPrivateFieldValue(view, "connArea");
		this.sa = GeneralTestUtilityClass.getPrivateFieldValue(view, "settingsArea");
		
		menuFolderAddress = sa.getMenuFolderAddress();
		orderFolderAddress = sa.getOrderFolderAddress();
		ppTimeout = sa.getPpTimeout();
		ppMinimalDelay = sa.getPpMinimalDelay();
		ppResendLimit = sa.getPpResendLimit();
		sendTimeout = sa.getSendTimeout();
		applySettingsButton = sa.getApplyButton();
	}
	
	protected IModel getModel() {
		return this.model;
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected IView getView() {
		return this.view;
	}
	
	public void clickOnDiscoverDevices() {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getRefreshButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public Collection<IDeviceData> getDiscoveredDevices() {
		this.setConnAreaTabActive();
		return this.ca.getDiscoveredDevices().getAllItems();
	}

	public Collection<IDeviceData> getKnownDevices() {
		this.setConnAreaTabActive();
		return this.ca.getKnownDevices().getAllItems();
	}

	public void addKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getDiscoveredDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		System.out.println("discovered Device size: " + ca.getDiscoveredDevices().getSize());
		this.ca.getAddKnownDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void removeKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getRemoveKnownDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void allowKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getAllowDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void blockKnownDevice(int index) {
		this.setConnAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getKnownDevices().artificiallySelectItem(index);
		GeneralTestUtilityClass.performWait(waitTime);
		this.ca.getBlockDeviceButton().performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void inputMenuFolderAddress(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.menuFolderAddress.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void inputOrderFolderAddress(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.orderFolderAddress.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void inputPPMinimalDelay(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ppMinimalDelay.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void inputPPResendLimit(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ppResendLimit.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void inputPPTimeout(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.ppTimeout.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void inputSendTimeout(String text) {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.sendTimeout.setCaption(text);
		GeneralTestUtilityClass.performWait(waitTime);
	}

	public void applySettings() {
		this.setSettingsAreaTabActive();
		GeneralTestUtilityClass.performWait(waitTime);
		this.applySettingsButton.performArtificialClick();
		GeneralTestUtilityClass.performWait(waitTime);
	}

	protected void setConnAreaTabActive() {
		this.tabPane.selectTab(this.connAreaTabName);
	}

	protected void setSettingsAreaTabActive() {
		this.tabPane.selectTab(this.settingsAreaTabName);
	}

}