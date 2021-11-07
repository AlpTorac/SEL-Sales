package archive;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FieldInfo implements IFieldInfo {
	private int lastPosition = 0;
	private int lastSeparatorPosition = 0;
	
	private Map<IFieldNameEnum, FieldInfoEntry> positions;
	
	/**
	 * 0 : start indicator
	 * 1 to (endIndex - 1) : separators
	 * endIndex : end indicator
	 */
	private Map<Integer, String> separators;
	
	protected FieldInfo() {
		this.positions = new ConcurrentHashMap<IFieldNameEnum, FieldInfoEntry>();
		this.separators = new ConcurrentHashMap<Integer, String>();
	}
	
	protected void decrementLastPosition() {
		if (this.lastPosition > 0) {
			this.lastPosition--;
		}
	}
	
	protected void incrementLastPosition() {
		this.lastPosition++;
	}
	
	protected int getLastPosition() {
		return this.lastPosition;
	}
	
	protected void decrementLastSeparatorPosition() {
		if (this.lastSeparatorPosition > 0) {
			this.lastSeparatorPosition--;
		}
	}
	
	protected void incrementLastSeparatorPosition() {
		this.lastSeparatorPosition++;
	}
	
	protected int getLastSeparatorPosition() {
		return this.lastSeparatorPosition;
	}
	@Override
	public int getFieldCount() {
		return this.positions.size();
	}
	
	@Override
	public int getFieldSeparatorCount() {
		return this.separators.size();
	}
	
	@Override
	public String getFieldSeparator(int position) {
		return this.separators.get(position);
	}
	
	@Override
	public void addField(IFieldNameEnum fieldName, FieldClass className) {
		this.positions.put(fieldName, new FieldInfoEntry(className));
		this.incrementLastPosition();
	}
	
	@Override
	public void removeField(IFieldNameEnum fieldName) {
		this.positions.remove(fieldName);
		this.decrementLastPosition();
	}
	
	@Override
	public int getFieldPosition(IFieldNameEnum fieldName) {
		return this.positions.get(fieldName).getPosition();
	}
	
	@Override
	public FieldClass getFieldClass(IFieldNameEnum fieldName) {
		return this.positions.get(fieldName).getFieldClass();
	}
	
	@Override
	public void addFieldSeparator(String separator) {
		this.separators.put(this.getLastSeparatorPosition(), separator);
		this.incrementLastSeparatorPosition();
	}
	
	@Override
	public void removeSeparatorField(int position) {
		this.separators.remove(position);
		this.decrementLastSeparatorPosition();
	}
	
	private class FieldInfoEntry {
		private FieldClass className;
		private int position;
		
		private FieldInfoEntry(FieldClass className) {
			this.className = className;
			this.position = lastPosition;
		}
		
		private FieldClass getFieldClass() {
			return this.className;
		}
		
		private int getPosition() {
			return this.position;
		}
	}
}
