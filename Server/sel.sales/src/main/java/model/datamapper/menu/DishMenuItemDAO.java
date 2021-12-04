package model.datamapper.menu;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import model.datamapper.AttributeDAO;
import model.datamapper.EntityDAO;
import model.datamapper.IAttribute;
import model.dish.DishMenu;
import model.dish.DishMenuItem;
import model.dish.DishMenuItemData;
import model.dish.DishMenuItemFactory;
import model.entity.IFactory;

public class DishMenuItemDAO extends EntityDAO<DishMenuItemAttribute, DishMenuItem, DishMenuItemData, DishMenu> {

	public DishMenuItemDAO(String address) {
		super(address);
	}
	
	public AttributeDAO<DishMenuItemAttribute, DishMenuItem, DishMenuItemData, DishMenu> getDAO(String serialisedDesc) {
		return this.getDAOMap().get(IAttribute.parseAttribute(DishMenuItemAttribute.values(), serialisedDesc));
	}
	
	@Override
	protected Map<DishMenuItemAttribute, AttributeDAO<DishMenuItemAttribute, DishMenuItem, DishMenuItemData, DishMenu>> initDAOMap() {
		return new ConcurrentSkipListMap<DishMenuItemAttribute, AttributeDAO<DishMenuItemAttribute, DishMenuItem, DishMenuItemData, DishMenu>>();
	}
	
	@Override
	protected void fillDAOList() {
		this.getDAOMap().put(DishMenuItemAttribute.DISH_NAME, new DishMenuItemDishNameDAO());
		this.getDAOMap().put(DishMenuItemAttribute.GROSS_PRICE, new DishMenuItemGrossPriceDAO());
		this.getDAOMap().put(DishMenuItemAttribute.PORTION_SIZE, new DishMenuItemPortionSizeDAO());
		this.getDAOMap().put(DishMenuItemAttribute.PRODUCTION_COST, new DishMenuItemProductionCostDAO());
	}

	@Override
	protected IFactory<DishMenuItemAttribute, DishMenuItem, DishMenuItemData> initFactory() {
		return new DishMenuItemFactory();
	}
}
