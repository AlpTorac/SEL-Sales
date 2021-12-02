package model.dish;

public class DishMenuDataFactory {
	private DishMenuItemFactory itemFac;
	
	public DishMenuDataFactory(DishMenuItemFactory itemFac) {
		this.itemFac = itemFac;
	}
	
	public DishMenuData constructData(DishMenuItemData[] dishMenuItemDatas) {
		DishMenuData dmd = new DishMenuData();
		for (DishMenuItemData data : dishMenuItemDatas) {
			dmd.addElement(data);
		}
		return dmd;
	}

	public DishMenuItemFactory getItemDataFac() {
		return this.itemFac;
	}

	public DishMenuData constructData(DishMenuItem[] dishMenuItems) {
		DishMenuItemData[] datas = new DishMenuItemData[dishMenuItems.length];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = this.getItemDataFac().entityToValue(dishMenuItems[i]);
		}
		return this.constructData(datas);
	}
	
	public  DishMenuData dishMenuToData(DishMenu dishMenu) {
		return this.constructData(dishMenu.getAllElements());
	}
}
