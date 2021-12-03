package model.order;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import model.datamapper.DishMenuItemAttribute;
import model.dish.DishMenuItem;
import model.dish.DishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.entity.AccumulatingAggregate;
import model.entity.AccumulatingAggregateEntry;
import model.entity.id.EntityID;

public class AccumulatingOrderItemAggregate extends AccumulatingAggregate<DishMenuItemAttribute, DishMenuItemData>{
	private IDishMenuItemFinder finder;
	
	private Collection<AccumulatingAggregateEntry<EntityID>> cache = new CopyOnWriteArrayList<AccumulatingAggregateEntry<EntityID>>();
	
	public void setDishMenuFinder(IDishMenuItemFinder finder) {
		this.finder = finder;
	}
	
	@Override
	public AccumulatingAggregate<DishMenuItemAttribute, DishMenuItemData> getEmptyClone() {
		return new AccumulatingOrderItemAggregate();
	}
	
	public boolean getIsDiscounted() {
		return this.getElementToAmountMap().entrySet().stream()
				.anyMatch(e -> e.getKey().getGrossPrice().compareTo(BigDecimal.ZERO) < 0);
	}
	
	public BigDecimal getGrossSum() {
		return this.getElementToAmountMap().entrySet().stream()
				.filter(e -> e.getKey().getGrossPrice().compareTo(BigDecimal.ZERO) > 0)
				.map(e -> e.getKey().getGrossPrice().multiply(e.getValue()))
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2));
	}
	
	public BigDecimal getOrderDiscount() {
		return this.getElementToAmountMap().entrySet().stream()
				.filter(e -> e.getKey().getGrossPrice().compareTo(BigDecimal.ZERO) < 0)
				.map(e -> e.getKey().getGrossPrice().multiply(e.getValue()))
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2)).abs();
	}
	
	public BigDecimal getNetSum() {
		return this.getElementToAmountMap().entrySet().stream()
				.map(e -> e.getKey().getGrossPrice().multiply(e.getValue()))
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2));
	}
	
	public AccumulatingAggregateEntry<DishMenuItemData>[] getOrderedItems() {
		return super.getAllEntries();
	}
	
	public void addElement(EntityID id, BigDecimal amount) {
		if (this.finder != null) {
			this.addElement(this.finder.getMenuItemData(id), amount);
		} else {
			this.cache.add(new AccumulatingAggregateEntry<EntityID>(id, amount));
		}
	}
}
