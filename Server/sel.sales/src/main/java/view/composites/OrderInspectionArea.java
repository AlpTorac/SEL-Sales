package view.composites;

import view.repository.IButton;
import view.repository.IGridLayout;
import view.repository.IHBoxLayout;
import view.repository.ILabel;
import view.repository.IRadioButton;
import view.repository.ITable;
import view.repository.IToggleGroup;
import view.repository.IUIComponent;
import view.repository.IVBoxLayout;
import view.repository.UIComponentFactory;
import view.repository.UIVBoxLayout;

public class OrderInspectionArea extends UIVBoxLayout {
	private UIComponentFactory fac;
	
	public OrderInspectionArea(UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.fac = fac;
		this.init();
	}

	private void init() {
		this.addUIComponents(new IUIComponent[] {
				this.initDisplayLayout(),
				this.initOrderOperationsLayout()
		});
	}

	protected IGridLayout initDisplayLayout() {
		IGridLayout layout = this.fac.createGridLayout();
		
		layout.addUIComponent(this.initOrderNameDisplay(), 		0, 0, 2, 1);
		layout.addUIComponent(this.initOrderIDDisplay(), 		0, 1, 2, 1);
		layout.addUIComponent(this.initOrderTimeInDayDisplay(), 2, 0, 3, 1);
		layout.addUIComponent(this.initOrderDateDisplay(), 		2, 1, 3, 1);
		layout.addUIComponent(this.initOrderDetailsTable(), 	0, 2, 7, 1);
		layout.addUIComponent(this.initPaymentMethodSelection(), 0, 3, 3, 3);
		layout.addUIComponent(this.initPlaceSelection(), 0, 6, 3, 3);
		layout.addUIComponent(this.initGrossDiscNetSum(), 3, 3, 3, 5);
		
		return layout;
	}
	
	protected ILabel initOrderNameDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption("Order N");
		return label;
	}
	
	protected ILabel initOrderIDDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption("Order N - ID");
		return label;
	}
	
	protected ILabel initOrderTimeInDayDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption("hh:mm:ss");
		return label;
	}
	
	protected ILabel initOrderDateDisplay() {
		ILabel label = this.fac.createLabel();
		label.setCaption("dd/mm/yyyy");
		return label;
	}
	
	protected ITable initOrderDetailsTable() {
		ITable table = this.fac.createTable();
		table.addColumns(new String[] {
				"Menu Item",
				"Amount",
				"Portions",
				"Price/Portion",
				"Total Item Price"
		});
		return table;
	}
	
	protected IHBoxLayout initPaymentMethodSelection() {
		IHBoxLayout optionArea = this.fac.createHBoxLayout();
		
		IRadioButton cash = this.fac.createRadioButton();
		cash.setCaption("Cash");
		
		IRadioButton card = this.fac.createRadioButton();
		card.setCaption("Card");
		
		IRadioButton[] choices = new IRadioButton[] {cash, card};
		
		IToggleGroup group = this.fac.createToggleGroup();
		group.addAllToToggleGroup(choices);
		
		optionArea.addUIComponents(choices);
		
		return optionArea;
	}
	
	protected IHBoxLayout initPlaceSelection() {
		IHBoxLayout optionArea = this.fac.createHBoxLayout();
		
		IRadioButton toGo = this.fac.createRadioButton();
		toGo.setCaption("To-Go");
		
		IRadioButton here = this.fac.createRadioButton();
		here.setCaption("Here");
		
		IRadioButton[] choices = new IRadioButton[] {toGo, here};
		
		IToggleGroup group = this.fac.createToggleGroup();
		group.addAllToToggleGroup(choices);
		
		optionArea.addUIComponents(choices);
		
		return optionArea;
	}
	
	protected IVBoxLayout initGrossDiscNetSum() {
		IVBoxLayout sumArea = this.fac.createVBoxLayout();
		
		sumArea.addUIComponents(new IUIComponent[] {
				this.initGrossSumDisplay(),
				this.initDiscDisplay(),
				this.initNetSumDisplay()
		});
		
		return sumArea;
	}
	
	protected IHBoxLayout initDiscDisplay() {
		IHBoxLayout area = this.fac.createHBoxLayout();
		area.setSpacing(10);
		
		ILabel name = this.fac.createLabel();
		name.setCaption("Discount:");
		
		ILabel display = this.fac.createLabel();
		display.setCaption("dd.dd");
		
		area.addUIComponents(new IUIComponent[] {
				name,
				display
		});
		
		return area;
	}
	protected IHBoxLayout initNetSumDisplay() {
		IHBoxLayout area = this.fac.createHBoxLayout();
		area.setSpacing(10);
		
		ILabel name = this.fac.createLabel();
		name.setCaption("Net Sum:");
		
		ILabel display = this.fac.createLabel();
		display.setCaption("dd.dd");
		
		area.addUIComponents(new IUIComponent[] {
				name,
				display
		});
		
		return area;
	}
	protected IHBoxLayout initGrossSumDisplay() {
		IHBoxLayout area = this.fac.createHBoxLayout();
		area.setSpacing(10);
		
		ILabel name = this.fac.createLabel();
		name.setCaption("Gross Sum:");
		
		ILabel display = this.fac.createLabel();
		display.setCaption("dd.dd");
		
		area.addUIComponents(new IUIComponent[] {
				name,
				display
		});
		
		return area;
	}
	
	protected IGridLayout initOrderOperationsLayout() {
		IGridLayout layout = this.fac.createGridLayout();
		layout.setMarigins(10, 0, 0, 0);
		layout.setHSpacing(50);
		layout.setVSpacing(10);
		
		layout.addUIComponent(this.initOrderAddConfirmButton(),	0, 0, 1, 1);
		layout.addUIComponent(this.initOrderRemoveButton(), 	3, 0, 1, 1);
		layout.addUIComponent(this.initOrderEditButton(), 		0, 1, 1, 1);
		layout.addUIComponent(this.initOrderNewButton(), 		3, 1, 1, 1);
		layout.addUIComponent(this.initOrderConfirmAllButton(),	0, 2, 3, 1);
		
		return layout;
	}
	
	protected IButton initOrderAddConfirmButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Add / Confirm");
		return button;
	}
	protected IButton initOrderRemoveButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Remove");
		return button;
	}
	protected IButton initOrderEditButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Edit");
		return button;
	}
	protected IButton initOrderNewButton() {
		IButton button = this.fac.createButton();
		button.setCaption("New Order");
		return button;
	}
	protected IButton initOrderConfirmAllButton() {
		IButton button = this.fac.createButton();
		button.setCaption("Confirm All");
		return button;
	}
	
}
