package model.order.serialise;

import java.time.LocalDateTime;

import model.id.EntityID;
import model.order.IOrderData;
import model.order.IOrderItemData;

public class IntraAppOrderSerialiser implements IOrderSerialiser {
	private IOrderFormat format;
	
	public IntraAppOrderSerialiser() {
		this.format = new IntraAppOrderFormat();
	}

	@Override
	public IOrderFormat getOrderFormat() {
		return this.format;
	}
	
	@Override
	public String serialiseOrderDatas(IOrderData[] orderDatas) {
		String result = "";
		for (IOrderData data : orderDatas) {
//			result += this.getOrderFormat().getOrderStart();
			result += this.serialiseOrderData(data);
//			result += this.getOrderFormat().getOrderEnd();
		}
		return result;
	}
	
	@Override
	public String serialiseOrderData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash, boolean isHere, EntityID orderID) {
		String result = "";
		result += this.getOrderFormat().getOrderStart();
		result += this.serialiseOrderID(orderID) + this.getOrderFormat().getOrderAttributeFieldSeperator();
		result += this.serialiseOrderDate(date) + this.getOrderFormat().getOrderAttributeFieldSeperator();
		result += this.serialiseIsCash(isCash) + this.getOrderFormat().getOrderAttributeFieldSeperator();
		result += this.serialiseIsHere(isHere) + this.getOrderFormat().getOrderAttributeFieldEnd();
		result += this.serialiseOrderItemDatas(orderData);
		result += this.getOrderFormat().getOrderEnd();
		return result;
	}
}
