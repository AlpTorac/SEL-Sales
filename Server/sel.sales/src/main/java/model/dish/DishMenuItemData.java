package model.dish;

import model.datamapper.DishMenuItemAttribute;
import model.entity.ValueObject;
import model.entity.id.EntityID;

public class DishMenuItemData extends ValueObject<DishMenuItemAttribute> {
	DishMenuItemData(EntityID id) {
		super(id);
	}
	
//	@Override
//	public boolean equals(Object o) {
//		if (o == null || !(o instanceof DishMenuItemData)) {
//			return false;
//		}
//		DishMenuItemData castedO = (DishMenuItemData) o;
//		
//		return this.getDishName().equals(castedO.getDishName()) &&
//				this.getID().equals(castedO.getID()) &&
//				this.getPortionSize().compareTo(castedO.getPortionSize()) == 0 &&
//				this.getGrossPrice().compareTo(castedO.getGrossPrice()) == 0 &&
//				this.getProductionCost().compareTo(castedO.getProductionCost()) == 0;
//	}
}
