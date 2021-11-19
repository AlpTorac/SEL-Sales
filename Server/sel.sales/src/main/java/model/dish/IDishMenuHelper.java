package model.dish;

import java.math.BigDecimal;

import model.dish.serialise.ExternalDishMenuSerialiser;
import model.dish.serialise.FileDishMenuSerialiser;
import model.dish.serialise.IDishMenuDeserialiser;
import model.dish.serialise.IntraAppDishMenuItemSerialiser;
import model.id.EntityID;
import model.id.EntityIDFactory;

public interface IDishMenuHelper {
	void setIDFactory(EntityIDFactory fac);
	
	void setDishMenuFactory(IDishMenuFactory fac);
	void setDishMenuDataFactory(IDishMenuDataFactory fac);
	
	void setDishMenuItemFactory(IDishMenuItemFactory fac);
	void setDishMenuItemDataFactory(IDishMenuItemDataFactory fac);
	
	void setDishMenuDeserialiser(IDishMenuDeserialiser deserialiser);
	
	void setAppSerialiser(IntraAppDishMenuItemSerialiser serialiser);
	void setExternalSerialiser(ExternalDishMenuSerialiser serialiser);
	void setFileSerialiser(FileDishMenuSerialiser serialiser);
	
	String serialiseMenuForFile(IDishMenuData menuData);
	String serialiseMenuItemForApp(String dishName, String id, BigDecimal portionSize, BigDecimal productionCost, BigDecimal price);
	String serialiseMenuItemForApp(IDishMenuItemData menuItemData);
	String serialiseForExternal(IDishMenuData menuData);
	
	IDishMenu createDishMenu();
	IDishMenuData parseFileMenuData(String serialisedMenu);
	IDishMenuData parseExternalMenuData(String serialisedMenu);
	IDishMenuData dishMenuToData(IDishMenu menu);
	
	default IDishMenuItem createDishMenuItem(String serialisedDishMenuItem) {
		IDishMenuItemData data = this.deserialiseDishMenuItem(serialisedDishMenuItem);
		return this.dishMenuItemDataToItem(data);
	}
	default IDishMenuItem dishMenuItemDataToItem(IDishMenuItemData data) {
		return this.createDishMenuItem(
				data.getDishName(),
				data.getPortionSize(),
				data.getGrossPrice(),
				data.getProductionCost(),
				data.getID());
	}
	IDishMenuItem createDishMenuItem(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, EntityID idParameters);
	default IDishMenuItem createDishMenuItem(String dishName, BigDecimal portionSize, BigDecimal price, BigDecimal productionCost, Object... idParameters) {
		return this.createDishMenuItem(dishName, portionSize, price, productionCost, this.createID(idParameters));
	}
	IDishMenuItemData dishMenuItemToData(IDishMenuItem item);
	
	IDishMenuItemData deserialiseDishMenuItem(String serialisedDishMenuItem);
	EntityID createID(Object... idParameters);
}
