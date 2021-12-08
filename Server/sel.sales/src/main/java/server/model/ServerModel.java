package server.model;

import java.io.File;

import model.Model;
import model.datamapper.ExportSerialiser;
import model.datamapper.StandardExcelOrderExportSerialiser;
import model.datamapper.order.OrderAttribute;
import model.dish.DishMenuItemData;
import model.entity.id.EntityID;
import model.order.OrderData;
import model.order.OrderStatus;
import model.settings.SettingsField;

public class ServerModel extends Model implements IServerModel {
	private ExportSerialiser<OrderAttribute, OrderData> orderExportSerialiser = new StandardExcelOrderExportSerialiser();
	private volatile boolean autoConfirmOrders = false;
	
	public ServerModel() {
		super();
	}
	
	public ServerModel(String resourceFolder) {
		this();
		this.getFileManager().setResourcesFolderAddress(resourceFolder);
	}
	
	@Override
	public void addOrder(OrderData data) {
		this.addUnconfirmedOrder(data);
	}
	
	protected void unconfirmedOrdersChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshUnconfirmedOrders());
	}
	
	protected void confirmedOrdersChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshConfirmedOrders());
	}
	
	protected void orderConfirmModeChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshConfirmMode());
	}
	
	@Override
	public boolean exportOrders() {
		String exportFolderAddress = this.getSettings().getSetting(SettingsField.EXPORT_FOLDER);
		String exportFileName = this.getDateSettings().serialiseDateForExportFileName();
		
		if (exportFolderAddress != null) {
			return this.getFileManager().exportOrderDatas(
					this.orderExportSerialiser.serialise(
							this.getOrderCollector().getMatchingElementsAsValueObject(v -> {
								OrderStatus status = (OrderStatus) v.getAttributeValue(OrderAttribute.STATUS);
								return status.equals(OrderStatus.CONFIRMED);
							}).toArray(OrderData[]::new)),
					exportFolderAddress+File.separator+exportFileName+".txt");
		}
		
		return false;
	}
	
	public void addMenuItem(String serialisedItemData) {
		this.addMenuItem(this.getDishMenuItemDAO().parseValueObject(serialisedItemData));
	}
	
	public void removeMenuItem(EntityID id) {
		if (this.getDishMenu().removeElement(id) != null) {
			this.menuChanged();
		}
	}

	public void removeMenuItem(String id) {
		this.removeMenuItem(this.createMinimalID(id));
	}

	@Override
	public void addUnconfirmedOrder(OrderData orderData) {
		if (this.autoConfirmOrders) {
			this.confirmOrder(orderData);
		} else {
			this.getOrderCollector().addElement(orderData);
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderData.getID(), OrderStatus.UNCONFIRMED);
			this.unconfirmedOrdersChanged();
		}
	}

	@Override
	public OrderData[] getAllUnconfirmedOrders() {
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED)).toArray(OrderData[]::new);
	}

	@Override
	public void removeAllUnconfirmedOrders() {
		this.getOrderCollector().removeAllElementsWithAttributeValue(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED);
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void editMenuItem(DishMenuItemData data) {
		if (data != null) {
			this.getDishMenu().removeElement(data.getID());
			this.getDishMenu().addElement(data);
			this.menuChanged();
		}
	}
	
	@Override
	public void confirmOrder(OrderData data) {
		this.getOrderCollector().addElement(data);
		this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, data.getID(), OrderStatus.CONFIRMED);
		this.writeOrder(data.getID().toString());
		this.ordersChanged();
	}

	@Override
	public OrderData[] getAllConfirmedOrders() {
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.CONFIRMED)).toArray(OrderData[]::new);
	}
	
	public void removeUnconfirmedOrder(EntityID id) {
		this.getOrderCollector().removeElementIfAttributeValueEquals(OrderAttribute.STATUS, id, OrderStatus.UNCONFIRMED);
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void removeUnconfirmedOrder(String id) {
		this.removeUnconfirmedOrder(this.createMinimalID(id));
	}
	
	public void removeConfirmedOrder(EntityID id) {
		this.getOrderCollector().removeElementIfAttributeValueEquals(OrderAttribute.STATUS, id, OrderStatus.CONFIRMED);
		this.confirmedOrdersChanged();
	}

	@Override
	public void removeConfirmedOrder(String id) {
		this.removeConfirmedOrder(this.createMinimalID(id));
	}

	@Override
	public void removeAllConfirmedOrders() {
		this.getOrderCollector().removeAllElementsWithAttributeValue(OrderAttribute.STATUS, OrderStatus.CONFIRMED);
		this.confirmedOrdersChanged();
	}

	@Override
	public boolean writeDishMenu() {
		return this.getFileManager().writeDishMenuData(this.getDishMenuItemDAO().serialiseValueObjects(this.getDishMenu().toData().getAllElements().toArray(DishMenuItemData[]::new)));
	}
	
	@Override
	public void confirmAllOrders() {
		OrderData[] unconfirmedOrders = this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED)).toArray(OrderData[]::new);
		for (OrderData uco : unconfirmedOrders) {
			this.confirmOrder(uco);
		}
		this.ordersChanged();
	}

	@Override
	public void setAutoConfirmOrders(boolean autoConfirm) {
		this.autoConfirmOrders = autoConfirm;
		if (this.autoConfirmOrders) {
			this.confirmAllOrders();
		}
		this.orderConfirmModeChanged();
	}

	@Override
	public boolean getAutoConfirmOrders() {
		return this.autoConfirmOrders;
	}

	@Override
	public void loadDishMenu(String fileAddress) {
		this.getFileManager().loadDishMenu(fileAddress);
	}

	@Override
	public void addMenuItem(DishMenuItemData data) {
		this.getDishMenu().addElement(data);
		this.menuChanged();
	}
}
