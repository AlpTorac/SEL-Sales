package model.dish;

public class DishMenuDataFactory implements IDishMenuDataFactory {
	private IDishMenuItemDataFactory itemDataFac;
	
	public DishMenuDataFactory(IDishMenuItemDataFactory itemDataFac) {
		this.itemDataFac = itemDataFac;
	}
	
	@Override
	public DishMenuData constructData(IDishMenuItemData[] dishMenuItemDatas) {
		return new DishMenuData(dishMenuItemDatas);
	}

	@Override
	public IDishMenuItemDataFactory getItemDataFac() {
		return this.itemDataFac;
	}

}
