package model.order;

public abstract class OrderAttributeSetter {
	private OrderAttributeFormat format = new OrderAttributeFormat();
	
	protected abstract void setOrderAttributeAlgorithm(IOrderCollector orderCollector, String orderID, String serialisedValue);
	
	public void setOrderAttributes(IOrderCollector orderCollector, String data) {
		if (data == null) {
			return;
		}
		String body = this.getBody(data);
		String[] entries = body.split(format.getFieldEnd());
		String[] idNAttr;
		String orderID;
		String serialisedAttribute;
		for (String e : entries) {
			if (e != null) {
				idNAttr = e.split(format.getFieldSeparator());
				if (orderCollector.contains(orderID = idNAttr[0])) {
					serialisedAttribute = idNAttr[1];
					this.setOrderAttributeAlgorithm(orderCollector, orderID, serialisedAttribute);
				}
			}
		}
	}
	
	protected String getBody(String data) {
		int begin = 0;
		int end = 0;
		
		if (data.startsWith(format.getFileStart())) {
			begin = format.getFileStart().length();
		}
		
		if (data.endsWith(format.getFileEnd())) {
			end = data.length() - format.getFileEnd().length();
		}
		
		return data.substring(begin, end);
	}
}
