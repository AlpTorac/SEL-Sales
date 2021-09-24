package model.dish;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class DishMenu implements IDishMenu {
	private Map<IDishMenuItemID, IDishMenuItem> dishes;
	private IDishMenuItemIDFactory idFac;
	private IDishMenuItemFactory fac;
	private IDishMenuItemDataFactory dataFac;
	
	public DishMenu() {
		this.dishes = new ConcurrentSkipListMap<IDishMenuItemID, IDishMenuItem>();
		this.idFac = new DishMenuItemIDFactory();
		this.fac = new DishMenuItemFactory(this.idFac);
		this.dataFac = new DishMenuItemDataFactory();
	}
	
	/**
	 * @return True, if item has been added. False, if the id is already taken.
	 */
	@Override
	public boolean addMenuItem(IDishMenuItemData data) {
		IDishMenuItemID id = this.idFac.createDishMenuItemID(data.getId());
		IDishMenuItem item = this.fac.createMenuItem(data);
		
		if (!this.dishes.containsKey(id)) {
			return this.dishes.put(id, item) == null;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean removeMenuItem(String id) {
		return this.dishes.remove(this.idFac.createDishMenuItemID(id)) != null;
	}
	
	@Override
	public void editMenuItem(IDishMenuItemData newItem) {
		IDishMenuItem oldItem = this.dishes.get(this.idFac.createDishMenuItemID(newItem.getId()));
		
		oldItem.getDish().setName(newItem.getDishName());
		oldItem.setDiscount(newItem.getDiscount());
		oldItem.setPortionSize(newItem.getPortionSize());
		oldItem.setPrice(newItem.getGrossPrice());
		oldItem.setProductionCost(newItem.getProductionCost());
	}
	
	@Override
	public IDishMenuItemData getItem(String id) {
		IDishMenuItem item = this.dishes.get(this.idFac.createDishMenuItemID(id));
		if (item != null) {
			return this.dataFac.menuItemToData(item);
		}
		return null;
	}

	@Override
	public IDishMenuItemData[] getAllItemData() {
		return this.dishes.values().stream().map(i -> this.dataFac.menuItemToData(i)).toArray(IDishMenuItemData[]::new);
	}
	
}