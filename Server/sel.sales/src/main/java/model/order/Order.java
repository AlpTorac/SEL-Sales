package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import model.dish.DishMenuItemIDFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;

public class Order implements IOrder {
	private LocalDateTime date;
	private boolean isCash;
	private boolean isHere;
	private IOrderID id;
	private IDishMenuItemIDFactory menuItemIDFac = new DishMenuItemIDFactory();
	private IOrderItemFactory orderItemFac = new OrderItemFactory();
	/**
	 * Order specific discount, FULLY INDEPENDENT of discounts of individual ordered items.
	 */
	private BigDecimal orderDiscount = BigDecimal.ZERO;
	
	private Map<IDishMenuItemID, IOrderItem> orderItems = new ConcurrentSkipListMap<IDishMenuItemID, IOrderItem>();
	
	Order(LocalDateTime date, boolean isCash, boolean isHere, IOrderID id) {
		this.date = date;
		this.isCash = isCash;
		this.isHere = isHere;
		this.id = id;
	}
	
	/**
	 * @return True, if the item was new. False, if the id was already taken.
	 */
	@Override
	public boolean addOrderItem(IOrderItemData data) {
		IDishMenuItemID id = this.menuItemIDFac.createDishMenuItemID(data.getItemData().getId());
		IOrderItem item = this.orderItemFac.createOrderItem(data);
		
		if (!this.orderItems.containsKey(id)) {
			this.orderItems.put(id, item);
			return true;
		} else {
			IOrderItem formerOrder = this.orderItems.get(id);
			formerOrder.setAmount(formerOrder.getAmount().add(data.getAmount()));
			return false;
		}
	}

	@Override
	public boolean removeOrderItem(String id) {
		return this.orderItems.remove(this.menuItemIDFac.createDishMenuItemID(id)) != null;
	}

	@Override
	public IOrderItem getOrderItem(String id) {
		return this.orderItems.get(this.menuItemIDFac.createDishMenuItemID(id));
	}

	@Override
	public IOrderItem[] getAllOrderItems() {
		return this.orderItems.values().toArray(IOrderItem[]::new);
	}

	@Override
	public boolean setOrderedItemAmount(String id, BigDecimal amount) {
		if (!this.orderItems.containsKey(this.menuItemIDFac.createDishMenuItemID(id))) {
			return false;
		} else {
			IOrderItem formerOrder = this.orderItems.get(this.menuItemIDFac.createDishMenuItemID(id));
			formerOrder.setAmount(amount);
			return true;
		}
	}

	@Override
	public String getID() {
		return this.id.toString();
	}
	
	@Override
	public LocalDateTime getDate() {
		return this.date;
	}
	
	@Override
	public boolean getIsCash() {
		return this.isCash;
	}
	
	@Override
	public boolean getIsHere() {
		return this.isHere;
	}

	@Override
	public Collection<IOrderItem> getOrderItemCollection() {
		return this.orderItems.values();
	}

	@Override
	public BigDecimal getOrderDiscount() {
		return this.orderDiscount;
	}
}
