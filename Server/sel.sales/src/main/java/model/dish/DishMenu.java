package model.dish;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

import model.id.EntityID;

public class DishMenu implements IDishMenu {
	private Map<EntityID, IDishMenuItem> dishes;
//	private IDishMenuItemIDFactory idFac;
//	private IDishMenuItemFactory fac;
	
	public DishMenu() {
		this.dishes = new ConcurrentSkipListMap<EntityID, IDishMenuItem>();
//		this.idFac = new DishMenuItemIDFactory();
//		this.fac = new DishMenuItemFactory();
	}
	
	/**
	 * @return True, if item has been added. False, if the id is already taken.
	 */
	@Override
	public boolean addMenuItem(IDishMenuItem item) {
//		IDishMenuItemID id = this.idFac.createDishMenuItemID(data.getId());
//		IDishMenuItem item = this.fac.createMenuItem(data);
		
		if (!this.dishes.containsKey(item.getID())) {
			return this.dishes.put(item.getID(), item) == null;
		} else {
			return false;
		}
	}
	
	@Override
	public boolean removeMenuItem(String id) {
		return this.dishes.keySet().removeIf(i -> i.toString().equals(id));
	}
	
	private IDishMenuItem getItemFromMap(String id) {
		Optional<IDishMenuItem> optional = this.dishes.entrySet().stream()
				.filter(e -> e.getKey().toString().equals(id)).map(e -> e.getValue()).findFirst();
		
		return optional.isPresent() ? optional.get() : null ;
	}
	
	@Override
	public void editMenuItem(IDishMenuItemData newItem) {
		IDishMenuItem oldItem = this.dishes.get(newItem.getID());
		
		oldItem.getDish().setName(newItem.getDishName());
		oldItem.setPortionSize(newItem.getPortionSize());
		oldItem.setPrice(newItem.getGrossPrice());
		oldItem.setProductionCost(newItem.getProductionCost());
	}

	@Override
	public IDishMenuItem getItem(String id) {
		return this.getItemFromMap(id);
	}

	@Override
	public IDishMenuItem[] getAllItems() {
		return this.dishes.values().toArray(IDishMenuItem[]::new);
	}

	@Override
	public IDishMenuItem getItem(EntityID id) {
		return this.dishes.get(id);
	}
	
//	@Override
//	public IDishMenuItemData getItem(String id) {
//		IDishMenuItem item = this.dishes.get(this.idFac.createDishMenuItemID(id));
//		if (item != null) {
//			return this.dataFac.menuItemToData(item);
//		}
//		return null;
//	}
//
//	@Override
//	public IDishMenuItemData[] getAllItemData() {
//		return this.dishes.values().stream().map(i -> this.dataFac.menuItemToData(i)).toArray(IDishMenuItemData[]::new);
//	}
	
}