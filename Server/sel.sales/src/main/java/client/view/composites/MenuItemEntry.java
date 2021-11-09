package client.view.composites;

import controller.IController;
import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import view.repository.IChoiceBox;
import view.repository.ITextBox;
import view.repository.IUIComponent;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIHBoxLayout;

public class MenuItemEntry extends UIHBoxLayout {
	private IController controller;
	private UIComponentFactory fac;
	private AdvancedUIComponentFactory advFac;
	
	private IChoiceBox<String> cb;
	private ITextBox amount;
	
	public MenuItemEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(fac.createHBoxLayout().getComponent());
		this.controller = controller;
		this.fac = fac;
		this.advFac = advFac;
		this.initComponent();
	}

	protected void initComponent() {
		this.cb = this.initChoiceBox();
		this.amount = this.initAmount();
		
		this.addUIComponents(new IUIComponent[] {
				this.cb,
				this.amount
		});
	}
	
	protected IChoiceBox<String> initChoiceBox() {
		return this.fac.createChoiceBox();
	}
	
	protected ITextBox initAmount() {
		ITextBox tb = this.fac.createTextBox();
		tb.setCaption("1");
		tb.setEnabled(false);
		tb.setOpacity(1);
		return tb;
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected UIComponentFactory getUIFactory() {
		return this.fac;
	}
	
	protected IChoiceBox<String> getMenuItemChoiceBox() {
		return this.cb;
	}
	
	protected ITextBox getAmountTextBox() {
		return this.amount;
	}
	
	protected void removeFromParent() {
		this.dettach();
	}
	
	public void refreshMenu(IDishMenuData menuData) {
		for (IDishMenuItemData data : menuData.getAllDishMenuItems()) {
			this.cb.addItem(data.getDishName());
		}
	}
}
