package model.entity;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import model.datamapper.IAttribute;

public abstract class AttributeOwner<A extends IAttribute> {
	private Map<A, Object> attributes;
	
	protected AttributeOwner() {
		this.initMap();
	}
	
	public Object getAttributeValue(A attribute) {
		return this.getEntityMap().get(attribute);
	}
	
	public void setAttributeValue(A attribute, Object value) {
		if (value != null) {
			this.getEntityMap().put(attribute, value);
		}
	}
	
	protected Map<A, Object> getEntityMap() {
		return this.attributes;
	}
	
	protected void initMap() {
		this.attributes = new ConcurrentHashMap<A, Object>();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof AttributeOwner<?>)) {
			return false;
		}
		AttributeOwner<?> castedO = (AttributeOwner<?>) o;
		Map<?, ?> map = castedO.getEntityMap();
		for (Entry<A, Object> e : this.getEntityMap().entrySet()) {
			if (!map.containsKey(e.getValue()) || !this.valuesEqual(e.getValue(), map.get(e.getKey()))) {
				return false;
			}
		}
		return true;
	}
	
	private <T> boolean valuesEqual(T value1, T value2) {
		if (value1 == value2) {
			return true;
		}
		
		if (value1 == null || value2 == null) {
			return false;
		}
		
		if (value1.equals(value2)) {
			return true;
		}
		
		if (value1 instanceof BigDecimal && value2 instanceof BigDecimal) {
			return ((BigDecimal) value1).compareTo(((BigDecimal) value2)) == 0;
		}
		
		if (value1 instanceof Iterable && value2 instanceof Iterable) {
			for (Object o1 : ((Iterable<?>) value1)) {
				boolean equal = false;
				for (Object o2 : ((Iterable<?>) value2)) {
					equal = equal || this.valuesEqual(o1, o2);
				}
				if (equal == false) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
