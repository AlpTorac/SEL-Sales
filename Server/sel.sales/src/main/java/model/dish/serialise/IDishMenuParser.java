package model.dish.serialise;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;

public interface IDishMenuParser {
	default IDishMenuData parseDishMenuData(String s) {
		String[] serialisedDishMenuItemDatas = this.getSerialisedDishMenuItems(s);
		return this.getDishMenuFactory().constructData(this.parseDishMenuItemDatas(serialisedDishMenuItemDatas));
	}
	
	default IDishMenuItemData[] parseDishMenuItemDatas(String[] ss) {
		Collection<IDishMenuItemData> datas = new ArrayList<IDishMenuItemData>();
		for (String s : ss) {
			datas.add(this.parseDishMenuItemData(s));
		}
		return datas.toArray(IDishMenuItemData[]::new);
	}
	
	default IDishMenuItemData parseDishMenuItemData(String s) {
		String[] dishMenuItemDataFields = this.getSerialisedDishMenuItemFields(s);
		
		String name = this.getSerialisedName(dishMenuItemDataFields);
		BigDecimal porSize = this.getSerialisedPortionSize(dishMenuItemDataFields);
		BigDecimal price = this.getSerialisedPrice(dishMenuItemDataFields);
		BigDecimal prodCost = this.getSerialisedProductionCost(dishMenuItemDataFields);
		BigDecimal disc = this.getSerialisedDiscount(dishMenuItemDataFields);
		String id = this.getSerialisedID(dishMenuItemDataFields);
		
		return this.getDishMenuItemDataFactory().constructData(name, porSize, price, prodCost, disc, id);
	}
	
	default String getSerialisedName(String[] dishMenuItemDataFields) {
		return dishMenuItemDataFields[0];
	}
	default BigDecimal getSerialisedPortionSize(String[] dishMenuItemDataFields) {
		return BigDecimal.valueOf(Double.valueOf(dishMenuItemDataFields[2]));
	}
	default BigDecimal getSerialisedPrice(String[] dishMenuItemDataFields) {
		return BigDecimal.valueOf(Double.valueOf(dishMenuItemDataFields[4]));
	}
	default BigDecimal getSerialisedProductionCost(String[] dishMenuItemDataFields) {
		return BigDecimal.valueOf(Double.valueOf(dishMenuItemDataFields[3]));
	}
	default BigDecimal getSerialisedDiscount(String[] dishMenuItemDataFields) {
		return BigDecimal.valueOf(Double.valueOf(dishMenuItemDataFields[5]));
	}
	default String getSerialisedID(String[] dishMenuItemDataFields) {
		return dishMenuItemDataFields[1];
	}
	default String[] getSerialisedDishMenuItems(String s) {
		String serialisedDishMenuItems = s;
		if (serialisedDishMenuItems.endsWith(this.getDishMenuFormat().getDishMenuItemDataFieldEnd())) {
			serialisedDishMenuItems = serialisedDishMenuItems.substring(0, serialisedDishMenuItems.length() - 1);
		}
		return serialisedDishMenuItems.split(this.getDishMenuFormat().getDishMenuItemDataFieldEnd());
	}
	default String[] getSerialisedDishMenuItemFields(String s) {
		String serialisedDishMenuItemFields = s;
		if (serialisedDishMenuItemFields.endsWith(this.getDishMenuFormat().getDishMenuItemDataFieldEnd())) {
			serialisedDishMenuItemFields = serialisedDishMenuItemFields.substring(0, serialisedDishMenuItemFields.length()-1);
		}
		return serialisedDishMenuItemFields.split(this.getDishMenuFormat().getDishMenuItemFormat().getDishMenuItemDataFieldSeperator());
	}
	
	IDishMenuFormat getDishMenuFormat();
	IDishMenuItemDataFactory getDishMenuItemDataFactory();
	IDishMenuDataFactory getDishMenuFactory();
}