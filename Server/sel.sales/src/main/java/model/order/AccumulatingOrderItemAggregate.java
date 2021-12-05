package model.order;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import model.datamapper.menu.DishMenuItemAttribute;
import model.dish.DishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.entity.AccumulatingAggregate;
import model.entity.AccumulatingAggregateEntry;
import model.entity.id.EntityID;

public class AccumulatingOrderItemAggregate extends AccumulatingAggregate<DishMenuItemAttribute, DishMenuItemData>{
	private IDishMenuItemFinder finder;
	
	private Collection<AccumulatingAggregateEntry<EntityID>> cache = new CopyOnWriteArrayList<AccumulatingAggregateEntry<EntityID>>();
	
	public void setDishMenuFinder(IDishMenuItemFinder finder) {
		if (finder != null) {
			this.finder = finder;
			DishMenuItemData d;
			AccumulatingAggregateEntry<?>[] entryArr = this.cache.toArray(AccumulatingAggregateEntry<?>[]::new);
			for (AccumulatingAggregateEntry<?> e : entryArr) {
				if ((d = this.finder.getMenuItemData((EntityID) e.getItem())) != null) {
					this.addElement(d, e.getAmount());
					this.cache.remove(e);
				}
			}
		}
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
				.map(e -> e.getKey().getGrossPrice().multiply(e.getValue()).abs())
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2));
	}
	
	public BigDecimal getNetSum() {
		return this.getElementToAmountMap().entrySet().stream()
				.map(e -> e.getKey().getGrossPrice().multiply(e.getValue()))
				.reduce(BigDecimal.ZERO, (gp1,gp2) -> gp1.add(gp2));
	}
	
	public AccumulatingAggregateEntry<DishMenuItemData> getOrderedItem(EntityID id) {
		return super.getElementEntry(id);
	}
	
	public AccumulatingAggregateEntry<DishMenuItemData>[] getOrderedItems() {
		return super.getAllEntries();
	}
	
	@Override
	public BigDecimal getElementAmount(EntityID id) {
		BigDecimal result = BigDecimal.ZERO;
		
		BigDecimal amount = super.getElementAmount(id);
		if (amount != null) {
			result = amount;
		}
		
		return result.add(this.cache.stream().filter(i -> i.getItem().equals(id))
		.map(i -> i.getAmount())
		.reduce(BigDecimal.ZERO, (a1,a2)->a1.add(a2)));
	}
	
	public void addElement(EntityID id, BigDecimal amount) {
		DishMenuItemData data;
		if (this.finder != null && (data = this.finder.getMenuItemData(id)) != null) {
			this.addElement(data, amount);
		} else {
			this.cache.add(new AccumulatingAggregateEntry<EntityID>(id, amount));
		}
	}
}
