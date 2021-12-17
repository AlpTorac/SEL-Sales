package server.view.composites;

import model.dish.DishMenuData;
import model.dish.DishMenuItemData;
import view.repository.IButton;
import view.repository.IHBoxLayout;
import view.repository.ITable;
import view.repository.ISingleRowTextBox;
import view.repository.IUIComponent;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIVBoxLayout;

public class MenuDesignArea extends UIVBoxLayout {
	
	private ISingleRowTextBox dishNameBox;
	private ISingleRowTextBox menuItemIDBox;
	private ISingleRowTextBox portionBox;
	private ISingleRowTextBox productionCostBox;
	private ISingleRowTextBox priceBox;
	private IButton addButton;
	private IButton editButton;
	private IButton removeButton;
	private IButton saveButton;
	private IButton loadButton;
	private ITable<DishMenuItemData> menuItemTable;
	
	private UIComponentFactory fac;
	
	public MenuDesignArea(UIComponentFactory fac) {
		super(fac.createVBoxLayout().getComponent());
		this.fac = fac;
		this.init();
	}
	
	private void init() {
		this.dishNameBox = this.initDishNameBox();
		this.menuItemIDBox = this.initMenuItemIDBox();
		this.portionBox = this.initPortionBox();
		this.productionCostBox = this.initProductionCostBox();
		this.priceBox = this.initPriceBox();
		this.addButton = this.initAddButton();
		this.editButton = this.initEditButton();
		this.removeButton = this.initRemoveButton();
		this.menuItemTable = this.initMenuItemTable();
		this.saveButton = this.initSaveButton();
		this.loadButton = this.initLoadButton();
		
		this.addUIComponents(new IUIComponent[] {
				this.getMenuItemTable(),
				this.initInputArea(),
				this.initButtonArea()
		});
		
//		this.addUIComponent(this.getMenuItemTable(), 		0, 0, 15, 1);
//		this.addUIComponent(this.getDishNameBox(),   		0, 1, 2, 1);
//		this.addUIComponent(this.getMenuItemIDBox(), 		2, 1, 2, 1);
//		this.addUIComponent(this.getPortionBox(), 			4, 1, 2, 1);
//		this.addUIComponent(this.getProductionCostBox(), 	6, 1, 2, 1);
//		this.addUIComponent(this.getPriceBox(), 			8, 1, 2, 1);
//		this.addUIComponent(this.getDiscountBox(), 			10, 1, 2, 1);
//		this.addUIComponent(this.getAddButton(),     		0, 2, 2, 1);
//		this.addUIComponent(this.getEditButton(),     		2, 2, 2, 1);
//		this.addUIComponent(this.getRemoveButton(),     	4, 2, 2, 1);
//		this.addUIComponent(this.getWriteButton(), 			6, 2, 2, 1);
	}
	
	protected IButton initLoadButton() {
		IButton button = fac.createButton();
		button.setCaption("Load existing menu");
		return button;
	}

	protected IHBoxLayout initInputArea() {
		IHBoxLayout layout = fac.createHBoxLayout();
		layout.addUIComponents(new IUIComponent[] {
				this.getDishNameBox(),
				this.getMenuItemIDBox(),
				this.getPortionBox(),
				this.getProductionCostBox(),
				this.getPriceBox(),
		});
		return layout;
	}
	
	protected IHBoxLayout initButtonArea() {
		IHBoxLayout layout = fac.createHBoxLayout();
		layout.setSpacing(20);
		layout.addUIComponents(new IUIComponent[] {
				this.getAddButton(),
				this.getEditButton(),
				this.getRemoveButton(),
				this.getSaveButton(),
				this.getLoadButton()
		});
		return layout;
	}
	
	public IButton getLoadButton() {
		return this.loadButton;
	}

	public IButton getSaveButton() {
		return this.saveButton;
	}

	protected IButton initSaveButton() {
		IButton button = fac.createButton();
		button.setCaption("Save Dish Menu");
		return button;
	}

	protected ITable<DishMenuItemData> initMenuItemTable() {
		ITable<DishMenuItemData> table = this.fac.createTable();
		
		table.addColumn("Dish Name", (d)->{return d.getDishName();});
		table.addColumn("ID", (d)->{return d.getID().toString();});
		table.addColumn("Portion Size", (d)->{return d.getPortionSize().toPlainString();});
		table.addColumn("Production Cost", (d)->{return d.getProductionCost().toPlainString();});
		table.addColumn("Gross Price", (d)->{return d.getGrossPrice();});
		return table;
	}
	
	protected ISingleRowTextBox initDishNameBox() {
		ISingleRowTextBox dishNameBox = this.fac.createSingleRowTextBox();
		dishNameBox.setPlaceholderText("Dish Name");
		return dishNameBox;
	}
	
	protected ISingleRowTextBox initMenuItemIDBox() {
		ISingleRowTextBox menuItemIDBox = this.fac.createSingleRowTextBox();
		menuItemIDBox.setPlaceholderText("ID");
		return menuItemIDBox;
	}
	
	protected ISingleRowTextBox initPortionBox() {
		ISingleRowTextBox portionBox = this.fac.createSingleRowTextBox();
		portionBox.setPlaceholderText("Portion");
		return portionBox;
	}
	
	protected ISingleRowTextBox initProductionCostBox() {
		ISingleRowTextBox productionCostBox = this.fac.createSingleRowTextBox();
		productionCostBox.setPlaceholderText("ProductionCost");
		return productionCostBox;
	}
	
	protected ISingleRowTextBox initPriceBox() {
		ISingleRowTextBox priceBox = this.fac.createSingleRowTextBox();
		priceBox.setPlaceholderText("Price");
		return priceBox;
	}
	
	protected IButton initAddButton() {
		IButton addButton = this.fac.createButton();
		addButton.setCaption("Add");
		return addButton;
	}
	
	protected IButton initEditButton() {
		IButton editButton = this.fac.createButton();
		editButton.setCaption("Edit");
		return editButton;
	}
	
	protected IButton initRemoveButton() {
		IButton removeButton = this.fac.createButton();
		removeButton.setCaption("Remove");
		return removeButton;
	}

	public ISingleRowTextBox getDishNameBox() {
		return this.dishNameBox;
	}

	public ISingleRowTextBox getMenuItemIDBox() {
		return this.menuItemIDBox;
	}

	public ISingleRowTextBox getPortionBox() {
		return this.portionBox;
	}

	public ISingleRowTextBox getProductionCostBox() {
		return this.productionCostBox;
	}

	public ISingleRowTextBox getPriceBox() {
		return this.priceBox;
	}

	public IButton getAddButton() {
		return this.addButton;
	}

	public IButton getEditButton() {
		return this.editButton;
	}

	public IButton getRemoveButton() {
		return this.removeButton;
	}

	public ITable<DishMenuItemData> getMenuItemTable() {
		return this.menuItemTable;
	}
	
	public void refreshMenuDisplay(DishMenuData data) {
		this.getMenuItemTable().clear();
		
//		String[][] transformedData = new String[data.length][];
//		
//		for (int i = 0; i < transformedData.length; i++) {
//			transformedData[i] = data[i].allToString();
//		}
		
		this.getMenuItemTable().addItems(data.getAllElements());
	}
}
