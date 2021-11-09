package client.view.composites;

import controller.IController;
import view.repository.IButton;
import view.repository.IUIComponent;
import view.repository.uiwrapper.AdvancedUIComponentFactory;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class EditableMenuItemEntry extends MenuItemEntry {
	private IButton amountIncBtn;
	private IButton amountDecBtn;
	private IButton removeBtn;
	
	public EditableMenuItemEntry(IController controller, UIComponentFactory fac, AdvancedUIComponentFactory advFac) {
		super(controller, fac, advFac);
	}

	@Override
	protected void initComponent() {
		super.initComponent();
		this.amountIncBtn = this.initAmountIncBtn();
		this.amountDecBtn = this.initAmountDecBtn();
		this.removeBtn = this.initRemoveBtn();
		
		this.addUIComponents(new IUIComponent[] {
				this.amountIncBtn,
				this.amountDecBtn,
				this.removeBtn
		});
	}
	
	protected IButton initAmountIncBtn() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("+");
		btn.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				int amount = Integer.valueOf(getAmountTextBox().getText());
				getAmountTextBox().setCaption(String.valueOf(amount+1));
			}
		});
		return btn;
	}
	
	protected IButton initAmountDecBtn() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("-");
		btn.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				int amount = Integer.valueOf(getAmountTextBox().getText());
				if (amount > 0) {
					getAmountTextBox().setCaption(String.valueOf(amount-1));
				}
			}
		});
		return btn;
	}
	
	protected IButton initRemoveBtn() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("Remove");
		btn.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				removeFromParent();
			}
		});
		return btn;
	}
	
}
