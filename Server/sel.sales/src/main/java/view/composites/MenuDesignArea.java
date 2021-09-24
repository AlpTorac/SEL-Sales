package view.composites;

import model.dish.IDishMenuData;
import model.dish.IDishMenuItemData;
import view.repository.IButton;
import view.repository.ITable;
import view.repository.ITextBox;
import view.repository.uiwrapper.UIComponentFactory;
import view.repository.uiwrapper.UIGridLayout;

public class MenuDesignArea extends UIGridLayout {
	
	private ITextBox dishNameBox;
	private ITextBox menuItemIDBox;
	private ITextBox portionBox;
	private ITextBox productionCostBox;
	private ITextBox priceBox;
	private ITextBox discountBox;
	private IButton addButton;
	private IButton editButton;
	private IButton removeButton;
	private ITable<IDishMenuItemData> menuItemTable;
	
	private UIComponentFactory fac;
	
	public MenuDesignArea(UIComponentFactory fac) {
		super(fac.createGridLayout().getComponent());
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
		this.discountBox = this.initDiscountBox();
		
		this.addUIComponent(this.getMenuItemTable(), 		0, 0, 8, 1);
		this.addUIComponent(this.getDishNameBox(),   		0, 1, 1, 1);
		this.addUIComponent(this.getMenuItemIDBox(), 		1, 1, 1, 1);
		this.addUIComponent(this.getPortionBox(), 			2, 1, 1, 1);
		this.addUIComponent(this.getProductionCostBox(), 	3, 1, 1, 1);
		this.addUIComponent(this.getPriceBox(), 			4, 1, 1, 1);
		this.addUIComponent(this.getDiscountBox(), 			5, 1, 1, 1);
		this.addUIComponent(this.getAddButton(),     		0, 2, 2, 1);
		this.addUIComponent(this.getEditButton(),     		2, 2, 2, 1);
		this.addUIComponent(this.getRemoveButton(),     	4, 2, 2, 1);
	}
	
	protected ITable<IDishMenuItemData> initMenuItemTable() {
		ITable<IDishMenuItemData> table = this.fac.createTable();
		
		table.addColumn("Dish Name", "DishName");
		table.addColumn("ID", "Id");
		table.addColumn("Portion Size", "PortionSize");
		table.addColumn("Production Cost", "ProductionCost");
		table.addColumn("Gross Price", "GrossPrice");
		table.addColumn("Discount", "Discount");
		table.addColumn("Net Price", "NetPrice");
//		table.addColumns(new String[] {
//				"Dish Name",
//				"ID",
//				"Portion",
//				"Production Cost",
//				"Price"
//		});
		return table;
	}
	
	protected ITextBox initDishNameBox() {
		ITextBox dishNameBox = this.fac.createTextBox();
		dishNameBox.setCaption("Dish Name");
		return dishNameBox;
	}
	
	protected ITextBox initMenuItemIDBox() {
		ITextBox menuItemIDBox = this.fac.createTextBox();
		menuItemIDBox.setCaption("ID");
		return menuItemIDBox;
	}
	
	protected ITextBox initPortionBox() {
		ITextBox portionBox = this.fac.createTextBox();
		portionBox.setCaption("Portion");
		return portionBox;
	}
	
	protected ITextBox initProductionCostBox() {
		ITextBox productionCostBox = this.fac.createTextBox();
		productionCostBox.setCaption("ProductionCost");
		return productionCostBox;
	}
	
	protected ITextBox initPriceBox() {
		ITextBox priceBox = this.fac.createTextBox();
		priceBox.setCaption("Price");
		return priceBox;
	}
	
	protected ITextBox initDiscountBox() {
		ITextBox priceBox = this.fac.createTextBox();
		priceBox.setCaption("Discount");
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

	public ITextBox getDishNameBox() {
		return this.dishNameBox;
	}

	public ITextBox getMenuItemIDBox() {
		return this.menuItemIDBox;
	}

	public ITextBox getPortionBox() {
		return this.portionBox;
	}

	public ITextBox getProductionCostBox() {
		return this.productionCostBox;
	}

	public ITextBox getPriceBox() {
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

	public ITable<IDishMenuItemData> getMenuItemTable() {
		return this.menuItemTable;
	}
	
	public void refreshMenuDisplay(IDishMenuData data) {
		this.getMenuItemTable().clear();
		
//		String[][] transformedData = new String[data.length][];
//		
//		for (int i = 0; i < transformedData.length; i++) {
//			transformedData[i] = data[i].allToString();
//		}
		
		this.getMenuItemTable().addItems(data.getAllDishMenuItems());
	}

	public ITextBox getDiscountBox() {
		return this.discountBox;
	}
}
