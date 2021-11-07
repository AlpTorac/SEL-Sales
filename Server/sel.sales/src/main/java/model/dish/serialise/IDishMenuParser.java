package model.dish.serialise;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.id.EntityIDFactory;
import model.util.IParser;

public interface IDishMenuParser extends IParser {
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
		String id = this.getSerialisedID(dishMenuItemDataFields);
		
		return this.getDishMenuItemDataFactory().constructData(name, porSize, price, prodCost, this.getIDFactory().createID(id));
	}
	
	default String getSerialisedName(String[] dishMenuItemDataFields) {
		return dishMenuItemDataFields[0];
	}
	default BigDecimal getSerialisedPortionSize(String[] dishMenuItemDataFields) {
		return this.parseBigDecimal(dishMenuItemDataFields[2]);
//		return BigDecimal.valueOf(Double.valueOf(dishMenuItemDataFields[2]));
	}
	default BigDecimal getSerialisedPrice(String[] dishMenuItemDataFields) {
		return this.parseBigDecimal(dishMenuItemDataFields[4]);
//		return BigDecimal.valueOf(Double.valueOf(dishMenuItemDataFields[4]));
	}
	default BigDecimal getSerialisedProductionCost(String[] dishMenuItemDataFields) {
		return this.parseBigDecimal(dishMenuItemDataFields[3]);
//		return BigDecimal.valueOf(Double.valueOf(dishMenuItemDataFields[3]));
	}
	default String getSerialisedID(String[] dishMenuItemDataFields) {
		return dishMenuItemDataFields[1];
	}
	default String[] getSerialisedDishMenuItems(String s) {
		String serialisedDishMenuItems = s;
		if (serialisedDishMenuItems.endsWith(this.getDishMenuFormat().getDishMenuItemDataFieldEnd())) {
			serialisedDishMenuItems = serialisedDishMenuItems.substring(0, serialisedDishMenuItems.length() - this.getDishMenuFormat().getDishMenuItemDataFieldEnd().length());
		}
		return serialisedDishMenuItems.split(this.getDishMenuFormat().getDishMenuItemDataFieldEnd());
	}
	default String[] getSerialisedDishMenuItemFields(String s) {
		String serialisedDishMenuItemFields = s;
		if (serialisedDishMenuItemFields.endsWith(this.getDishMenuFormat().getDishMenuItemFormat().getDishMenuItemDataFieldSeperator())) {
			serialisedDishMenuItemFields = serialisedDishMenuItemFields.substring(0, serialisedDishMenuItemFields.length()-this.getDishMenuFormat().getDishMenuItemFormat().getDishMenuItemDataFieldSeperator().length());
		}
		return serialisedDishMenuItemFields.split(this.getDishMenuFormat().getDishMenuItemFormat().getDishMenuItemDataFieldSeperator());
	}
	
	IDishMenuFormat getDishMenuFormat();
	IDishMenuItemDataFactory getDishMenuItemDataFactory();
	IDishMenuDataFactory getDishMenuFactory();
	EntityIDFactory getIDFactory();
}
