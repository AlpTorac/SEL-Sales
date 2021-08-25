package view.composites;

import view.repository.IListView;
import view.repository.IRadioButton;
import view.repository.IToggleGroup;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.UIComponentFactory;
import view.repository.UIVBoxLayout;

public class OrderTrackingArea extends UIVBoxLayout {
	private UIComponentFactory fac;
	
	public OrderTrackingArea(UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.fac = fac;
		this.init();
	}
	
	private void init() {
		this.setSpacing(20);
		this.addUIComponents(new IUIComponent[] {
				this.initOrderConfirmationTrackingList(),
				this.initConfirmationSettings(),
				this.initPastOrdersList()
		});
	}
	
	protected IListView initOrderConfirmationTrackingList() {
		IListView list = this.fac.createListView();
		return list;
	}
	
	protected IVBoxLayout initConfirmationSettings() {
		IVBoxLayout optionArea = this.fac.createVBoxLayout();
		
		IRadioButton auto = this.fac.createRadioButton();
		auto.setCaption("Auto-Confirm");
		
		IRadioButton manual = this.fac.createRadioButton();
		manual.setCaption("Manual Confirmation");
		
		IRadioButton[] choices = new IRadioButton[] {auto, manual};
		
		IToggleGroup group = this.fac.createToggleGroup();
		group.addAllToToggleGroup(choices);
		
		optionArea.addUIComponents(choices);
		
		return optionArea;
	}
	
	protected IListView initPastOrdersList() {
		IListView list = this.fac.createListView();
		return list;
	}
}
