package model.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.filewriter.FileOrderSerialiser;
import model.id.EntityID;
import model.id.EntityIDFactory;
import model.id.FixIDFactory;
import model.order.serialise.IOrderDeserialiser;
import model.order.serialise.IntraAppOrderFormat;
import model.order.serialise.IntraAppOrderSerialiser;
import model.order.serialise.StandardOrderDeserialiser;

public class OrderHelper implements IOrderHelper {

	private EntityIDFactory idFac;
	private IOrderCollectorFactory orderColFac;
	
	private IOrderFactory orderFac;
	private IOrderDataFactory orderDataFac;
	
	private IOrderItemDataFactory orderItemDataFac;
	
	private IOrderDeserialiser deserialiser;
	private IDishMenuItemFinder finder;
	
	private IntraAppOrderSerialiser appOrderSerialiser;
	private FileOrderSerialiser fileOrderSerialiser;
	
	private IntraAppOrderFormat appOrderFormat;
	
	public OrderHelper() {
		this.idFac = new FixIDFactory();
		this.orderColFac = new OrderCollectorFactory();
		this.orderFac = new OrderFactory();
		this.orderItemDataFac = new OrderItemDataFactory();
		this.orderDataFac = new OrderDataFactory(this.orderItemDataFac, new FixIDFactory());
		this.deserialiser = new StandardOrderDeserialiser();
		this.appOrderSerialiser = new IntraAppOrderSerialiser();
		this.fileOrderSerialiser = new FileOrderSerialiser();
		this.appOrderFormat = new IntraAppOrderFormat();
	}
	
	@Override
	public void setOrderCollectorFactory(IOrderCollectorFactory fac) {
		this.orderColFac = fac;
	}

	@Override
	public void setOrderFactory(IOrderFactory fac) {
		this.orderFac = fac;
	}

	@Override
	public void setOrderDataFactory(IOrderDataFactory fac) {
		this.orderDataFac = fac;
	}

	@Override
	public void setOrderItemDataFactory(IOrderItemDataFactory fac) {
		this.orderItemDataFac = fac;
	}

	@Override
	public void setOrderDeserialiser(IOrderDeserialiser deserialiser) {
		this.deserialiser = deserialiser;
	}
	
	@Override
	public void setAppSerialiser(IntraAppOrderSerialiser appOrderSerialiser) {
		this.appOrderSerialiser = appOrderSerialiser;
	}

	@Override
	public void setFileSerialiser(FileOrderSerialiser fileOrderSerialiser) {
		this.fileOrderSerialiser = fileOrderSerialiser;
	}
	
	@Override
	public void setFinder(IDishMenuItemFinder finder) {
		this.finder = finder;
		this.deserialiser.setFinder(this.finder);
	}
	
	@Override
	public IOrderCollector createOrderCollector() {
		return this.orderColFac.createOrderCollector();
	}

	@Override
	public IOrder createOrder(String serialisedOrder) {
		return this.orderFac.createOrder(this.deserialiseOrderData(serialisedOrder));
	}

	@Override
	public IOrderData orderToData(IOrder order) {
		return this.orderDataFac.orderToData(order);
	}

	@Override
	public IOrderData deserialiseOrderData(String serialisedOrder) {
		return this.deserialiser.deserialise(serialisedOrder);
	}

	@Override
	public String serialiseForApp(IOrderData[] data) {
		return this.appOrderSerialiser.serialiseOrderDatas(data);
	}

	@Override
	public String serialiseForFile(IOrderData[] data) {
		return this.fileOrderSerialiser.serialiseOrderDatas(data);
	}

	@Override
	public String serialiseForApp(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere,
			EntityID orderID) {
		return this.appOrderSerialiser.serialiseOrderData(orderData, date, isCash, isHere, orderID);
	}

	@Override
	public IOrderData[] deserialiseOrderDatas(String serialisedOrders) {
		return this.deserialiser.deserialiseOrders(serialisedOrders);
	}

	@Override
	public String serialiseForApp(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere,
			Object... idParameters) {
		return this.appOrderSerialiser.serialiseOrderData(orderData, date, isCash, isHere, this.idFac.createID(idParameters));
	}

	@Override
	public String serialiseForFile(IOrderData data) {
		return this.fileOrderSerialiser.serialiseOrderData(data);
	}

	@Override
	public IOrderItemData createOrderItemData(IDishMenuItemData menuItem, BigDecimal amount) {
		return this.orderItemDataFac.constructData(menuItem, amount);
	}

	@Override
	public String serialiseForApp(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere) {
		return this.serialiseForApp(orderData, date, isCash, isHere, this.appOrderFormat.formatDate(date));
	}
}
