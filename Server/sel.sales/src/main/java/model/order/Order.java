package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentSkipListMap;

import model.id.EntityID;

public class Order implements IOrder {
	private LocalDateTime date;
	private boolean isCash;
	private boolean isHere;
	private EntityID id;
//	private IDishMenuItemIDFactory menuItemIDFac = new DishMenuItemIDFactory();
	private IOrderItemFactory orderItemFac = new OrderItemFactory();
	
	private Map<EntityID, IOrderItem> orderItems = new ConcurrentSkipListMap<EntityID, IOrderItem>();
	
	public Order(LocalDateTime date, boolean isCash, boolean isHere, EntityID id) {
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
//		IDishMenuItemID id = this.menuItemIDFac.createDishMenuItemID(data.getItemData().getID());
		IOrderItem item = this.orderItemFac.createOrderItem(data);
		
		if (!this.orderItems.containsKey(data.getItemData().getID())) {
			this.orderItems.put(data.getItemData().getID(), item);
			return true;
		} else {
			IOrderItem formerOrder = this.orderItems.get(data.getItemData().getID());
			formerOrder.setAmount(formerOrder.getAmount().add(data.getAmount()));
			return false;
		}
	}

	private IOrderItem getOrderItemFromMap(String id) {
		Optional<IOrderItem> optional = this.orderItems.entrySet().stream()
				.filter(e -> e.getKey().serialisedIDequals(id))
				.map(e -> e.getValue())
				.findFirst();
		
		return optional.isPresent() ? optional.get() : null;
	}
	
	@Override
	public boolean removeOrderItem(String id) {
		return this.orderItems.keySet().removeIf(k -> k.toString().equals(id));
	}

	@Override
	public IOrderItem getOrderItem(String id) {
		return this.getOrderItemFromMap(id);
	}

	@Override
	public IOrderItem[] getAllOrderItems() {
		return this.orderItems.values().toArray(IOrderItem[]::new);
	}

	@Override
	public boolean setOrderedItemAmount(String id, BigDecimal amount) {
		if (this.getOrderItemFromMap(id) == null) {
			return false;
		} else {
			IOrderItem formerOrder = this.getOrderItemFromMap(id);
			formerOrder.setAmount(amount);
			return true;
		}
	}

	@Override
	public EntityID getID() {
		return this.id;
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
}
