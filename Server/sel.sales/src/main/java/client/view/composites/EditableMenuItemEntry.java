package client.view.composites;

import model.dish.IDishMenuItemData;
import view.repository.IButton;
import view.repository.IChoiceBox;
import view.repository.IUIComponent;
import view.repository.uiwrapper.ClickEventListener;
import view.repository.uiwrapper.UIComponentFactory;

public class EditableMenuItemEntry extends MenuItemEntry {
	private IButton amountIncBtn;
	private IButton amountDecBtn;
	private IButton removeBtn;
	
	public EditableMenuItemEntry(UIComponentFactory fac, PriceUpdateTarget<MenuItemEntry> notifyTarget) {
		super(fac, notifyTarget);
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
	
	protected void choiceBoxInitExtra(IChoiceBox<IDishMenuItemData> choiceBox) {
		
	}
	
	protected IButton initAmountIncBtn() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("+");
		btn.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				int amount = getAmount().intValue();
				getAmountTextBox().setCaption(String.valueOf(amount+1));
				notifyPriceDisplayingTarget();
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
				int amount = getAmount().intValue();
				if (amount > 0) {
					getAmountTextBox().setCaption(String.valueOf(amount-1));
					notifyPriceDisplayingTarget();
				}
			}
		});
		return btn;
	}
	
	protected void removeAction() {
		this.removeFromParent();
		this.notifyPriceDisplayingTarget();
	}
	
	protected IButton initRemoveBtn() {
		IButton btn = this.getUIFactory().createButton();
		btn.setCaption("Remove");
		btn.addClickListener(new ClickEventListener() {
			@Override
			public void clickAction() {
				removeAction();
			}
		});
		return btn;
	}
	
	public IButton getAmountIncButton() {
		return this.amountIncBtn;
	}
	
	public IButton getAmountDecButton() {
		return this.amountDecBtn;
	}
	
	public IButton getRemoveButton() {
		return this.removeBtn;
	}
	
	protected EditableMenuItemEntry constructClone() {
		return new EditableMenuItemEntry(this.getUIFactory(), this.getNotifyTarget());
	}
	
	public EditableMenuItemEntry clone() {
		return (EditableMenuItemEntry) super.clone();
	}
}
