package archive;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ValueContainer {
	private Map<FieldClass, Callable<?>> enumToGetter;
	
	public ValueContainer() {
		this.enumToGetter = new ConcurrentHashMap<FieldClass, Callable<?>>();
	}
	
	public Object getField(FieldClass fc) throws Exception {
		return this.enumToGetter.get(fc).call();
	}
}
