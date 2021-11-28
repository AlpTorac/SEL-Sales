package model.filewriter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import model.id.EntityID;
import model.order.IOrderData;
import model.order.IOrderItemData;
import model.order.serialise.IOrderFormat;
import model.order.serialise.IOrderSerialiser;

public class FileOrderSerialiser implements IOrderSerialiser {
	private IOrderFormat format = new FileOrderFormat();
	@Override
	public String serialiseOrderDatas(IOrderData[] orderDatas) {
		String result = "";
		for (IOrderData data : orderDatas) {
			result += this.serialiseOrderData(data);
		}
		return result;
	}
	
	@Override
	public String serialiseOrderData(IOrderItemData[] orderData, LocalDateTime date, boolean isCash,
			boolean isHere, EntityID orderID) {
		String result = "";
		boolean isDiscounted = false;
		for (int i = 0; i < orderData.length; i++) {
//			isDiscounted = isDiscounted || orderData[i].getGrossPrice().compareTo(BigDecimal.ZERO) < 0;
			if (isDiscounted = orderData[i].getGrossPrice().compareTo(BigDecimal.ZERO) < 0) {
				break;
			}
		}
		for (IOrderItemData d : orderData) {
			result += this.getOrderFormat().getOrderStart();
			result += this.serialiseOrderID(orderID) + this.getOrderFormat().getOrderAttributeFieldSeperator();
			result += this.serialiseOrderDate(date) + this.getOrderFormat().getOrderAttributeFieldSeperator();
			result += this.serialiseIsCash(isCash) + this.getOrderFormat().getOrderAttributeFieldSeperator();
			result += this.serialiseIsHere(isHere) + this.getOrderFormat().getOrderAttributeFieldSeperator();
//			result += this.serialiseIsDiscounted(orderDiscount) + this.getOrderDataFieldEnd();
			result += this.serialiseBoolean(isDiscounted) + this.getOrderFormat().getOrderAttributeFieldEnd();
			result += this.serialiseDishMenuItemID(d.getItemData()) + this.getOrderFormat().getOrderItemDataFieldSeperator();
			result += this.serialiseOrderItemAmount(d) + this.getOrderFormat().getOrderItemDataFieldEnd();
			result += this.getOrderFormat().getOrderEnd();
		}
		return result;
	}

	@Override
	public IOrderFormat getOrderFormat() {
		return this.format;
	}
}
