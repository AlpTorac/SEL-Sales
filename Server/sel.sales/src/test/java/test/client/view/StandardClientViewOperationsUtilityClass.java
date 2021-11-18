package test.client.view;

import client.view.composites.CookingOrdersArea;
import client.view.composites.OrderArea;
import client.view.composites.OrderTakingArea;
import client.view.composites.PastOrdersArea;
import client.view.composites.PendingPaymentOrdersArea;
import controller.IController;
import model.IModel;
import test.GeneralTestUtilityClass;
import test.ViewOperationsUtilityClass;
import view.IView;
import view.repository.IButton;
import view.repository.uiwrapper.UITabPane;

public class StandardClientViewOperationsUtilityClass extends ViewOperationsUtilityClass {
	private String oaTabName;
	private OrderArea oa;
	private UITabPane oaTabPane;
	
	private OrderTakingArea ota;
	private CookingOrdersArea coa;
	private PendingPaymentOrdersArea uoa;
	private PastOrdersArea poa;
	
	private String otaTabName;
	private String coaTabName;
	private String uoaTabName;
	private String poaTabName;
	
	public StandardClientViewOperationsUtilityClass(IView view, IController controller, IModel model) {
		super(view, controller, model);
		
		this.oa = GeneralTestUtilityClass.getPrivateFieldValue(view, "oa");
		this.oaTabPane = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "tabPane");
		
		this.ota = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "ota");
		this.coa = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "coa");
		this.uoa = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "uoa");
		this.poa = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "poa");
		
		this.tabArea = GeneralTestUtilityClass.getPrivateFieldValue(view, "mainTabArea");
		this.tabPane = GeneralTestUtilityClass.getPrivateFieldValue(view, "mainTabPane");
		
		this.oaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "oaTabName");
		this.connAreaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "ccaTabName");
		this.settingsAreaTabName = GeneralTestUtilityClass.getPrivateFieldValue(view, "csaTabName");
		
		this.otaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "otaTabName");
		this.coaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "coaTabName");
		this.uoaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "uoaTabName");
		this.poaTabName = GeneralTestUtilityClass.getPrivateFieldValue(this.oa, "poaTabName");
	}
	
//	public void clickOnNewMenuItemEntryButton() {
//		this.setOrderTakingAreaTabActive();
//		GeneralTestUtilityClass.performWait(waitTime);
//		this.ota.getAddMenuItemButton().performArtificialClick();
//		GeneralTestUtilityClass.performWait(waitTime);
//	}
//	
//	public void clickOnOTANextTabButton() {
//		this.setOrderTakingAreaTabActive();
//		GeneralTestUtilityClass.performWait(waitTime);
//		this.ota.getNextTabButton().performArtificialClick();
//		GeneralTestUtilityClass.performWait(waitTime);
//	}
	
	protected void setOrderAreaTabActive() {
		this.tabPane.selectTab(this.oaTabName);
	}
	
	protected void setOrderTakingAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.otaTabName);
	}
	
	protected void setCookingOrderAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.coaTabName);
	}
	
	protected void setPendingPaymentOrderAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.uoaTabName);
	}
	
	protected void setSentOrderAreaTabActive() {
		this.setOrderAreaTabActive();
		this.oaTabPane.selectTab(this.poaTabName);
	}
}
