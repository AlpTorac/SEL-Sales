package model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import model.datamapper.IAttribute;

public abstract class AttributeOwner<A extends IAttribute> {
	private Map<A, Object> attributes;
	
	protected AttributeOwner() {
		this.attributes = this.initMap();
	}
	
	public Object getAttributeValue(A attribute) {
		return this.getAttributeMap().get(attribute);
	}
	
	public void setAttributeValue(A attribute, Object value) {
		if (value != null) {
			this.getAttributeMap().put(attribute, value);
		}
	}
	
	protected Map<A, Object> getAttributeMap() {
		return this.attributes;
	}
	
	protected Map<A, Object> initMap() {
		Map<A, Object> map = new ConcurrentHashMap<A, Object>();
		for (A attr : this.getAllAttributeEnumValues()) {
			map.put(attr, attr.getDefaultValue());
		}
		return map;
	}
	
	public void setAttributesSameAs(AttributeOwner<A> anotherAO) {
		if (anotherAO == null) {
			return;
		}
		for (A key : anotherAO.getAttributeMap().keySet()) {
			this.setAttributeValue(key, anotherAO.copyAttribute(key));
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object copyAttribute(A attribute) {
		Object value = this.getAttributeMap().get(attribute);
		if (value == null) {
			return attribute.getDefaultValue();
		}
		
		if (value.getClass().isPrimitive() || value.getClass().isEnum()) {
			return value;
		}
		
		if (value instanceof String) {
			return String.valueOf((String) value);
		}
		
		if (value instanceof Boolean) {
			return Boolean.valueOf(((Boolean) value).booleanValue());
		}
		
		if (value instanceof BigDecimal) {
			return BigDecimal.valueOf(((BigDecimal) value).doubleValue());
		}
		
		if (value instanceof Integer) {
			return Integer.valueOf(((Integer) value).intValue());
		}
		
		if (value instanceof LocalDateTime) {
			return ((LocalDateTime) value).plusNanos(0);
		}
		
		if (value instanceof IAccumulatingAggregate) {
			IAccumulatingAggregate castedValue = (IAccumulatingAggregate) value;
			IAccumulatingAggregate newValue = ((IAccumulatingAggregate) castedValue).getEmptyClone();
			newValue.addAllElements(castedValue.getAllElements());
			return newValue;
		}
		
		if (value instanceof IAggregate) {
			IAggregate castedValue = (IAggregate) value;
			IAggregate newValue = ((IAggregate) castedValue).getEmptyClone();
			newValue.addAllElements(castedValue.getAllElements());
			return newValue;
		}
		
		return null;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof AttributeOwner<?>)) {
			return false;
		}
		AttributeOwner<?> castedO = (AttributeOwner<?>) o;
		Map<?, ?> map = castedO.getAttributeMap();
		for (Entry<A, Object> e : this.getAttributeMap().entrySet()) {
			if (!map.containsKey(e.getKey()) || !this.valuesEqual(e.getValue(), map.get(e.getKey()))) {
				return false;
			}
		}
		return true;
	}
	
	private <T> boolean valuesEqual(T value1, T value2) {
		if (value1 == value2) {
			return true;
		}
		
		if (value1 == null ^ value2 == null) {
			return false;
		}
		
		if (value1.equals(value2)) {
			return true;
		}
		
		if (value1 instanceof BigDecimal && value2 instanceof BigDecimal) {
			return ((BigDecimal) value1).compareTo(((BigDecimal) value2)) == 0;
		}
		
		if (value1 instanceof IAccumulatingAggregate && value2 instanceof IAccumulatingAggregate) {
			return ((IAccumulatingAggregate<?, ?>) value1).equals((IAccumulatingAggregate<?, ?>) value2);
		}
		
		if (value1 instanceof IAggregate && value2 instanceof IAggregate) {
			return ((IAggregate<?, ?>) value1).equals((IAggregate<?, ?>) value2);
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
	
	public abstract A[] getAllAttributeEnumValues();
}
