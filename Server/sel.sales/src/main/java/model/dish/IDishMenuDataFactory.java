package model.dish;

public interface IDishMenuDataFactory {
	default IDishMenuData constructData(IDishMenuItem[] dishMenuItems) {
		IDishMenuItemData[] datas = new IDishMenuItemData[dishMenuItems.length];
		for (int i = 0; i < datas.length; i++) {
			datas[i] = this.getItemDataFac().menuItemToData(dishMenuItems[i]);
		}
		return this.constructData(datas);
	}
	IDishMenuData constructData(IDishMenuItemData[] dishMenuItemDatas);
	default IDishMenuData dishMenuToData(IDishMenu dishMenu) {
		return this.constructData(dishMenu.getAllItems());
	}
	IDishMenuItemDataFactory getItemDataFac();
}
