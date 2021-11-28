package model.settings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.SortedSet;
import java.util.TreeSet;

import model.IModel;

public class TableNumberContainer implements HasSettingsField {
	private SortedSet<IRange> rangeSet;
	private TableNumberRangeIntervalFormat format;
	private IModel model;
	
	public TableNumberContainer(IModel model) {
		this.model = model;
		this.format = new TableNumberRangeIntervalFormat();
		this.rangeSet = new TreeSet<IRange>();
	}
	
	@Override
	public void refreshValue() {
		String ranges = this.model.getSettings().getSetting(SettingsField.TABLE_NUMBERS);
		if (ranges != null) {
			this.rangeSet.clear();
			this.addSerialisedRanges(ranges);
		}
	}
	
	public boolean tableExists(int number) {
		return this.rangeSet.stream().anyMatch(range -> range.isInRange(number));
	}
	
	public Collection<Integer> getAllTableNumbers() {
		if (this.rangeSet.isEmpty()) {
			return null;
		}
		
		Collection<Integer> col = new ArrayList<Integer>();
		
		int min = rangeSet.first().getMin();
		int max = rangeSet.last().getMax();
		
		for (int i = min; i <= max; i++) {
			if (this.tableExists(i)) {
				col.add(i);
			}
		}
		
		return col;
	}
	
	protected void addSerialisedRanges(String serialisedRanges) {
		if (serialisedRanges == null) {
			return;
		}
		
		String[] ranges = serialisedRanges.split(this.format.getRangeSeparator());
		
		for (int i = 0; i < ranges.length; i++) {
			if (ranges[i] != null) {
				this.rangeSet.add(this.parseInterval(ranges[i]));
			}
		}
	}
	
	protected IRange createRange(int min, int max) {
		return new TableNumberRangeInterval(min, max);
	}
	
	protected IRange parseInterval(String serialisedInterval) {
		String[] limits = serialisedInterval.split(this.format.getIntervalSeparator());
		int min = Integer.valueOf(limits[0]);
		int max = limits.length == 2 ? Integer.valueOf(limits[1]) : min;
		return this.createRange(min, max);
	}
	
	protected class TableNumberRangeIntervalFormat {
		/**
		 * Separates different ranges (ex: 1 + rangeSeparator + 2 + intervalSeparator + 3)
		 */
		private String rangeSeparator = ",";
		/*
		 * Stands between the minimum and the maximum value of the interval
		 */
		private String intervalSeparator = "-";
		
		public String getRangeSeparator() {
			return this.rangeSeparator;
		}
		
		public String getIntervalSeparator() {
			return this.intervalSeparator;
		}
	}
	
	protected interface IRange extends Comparable<TableNumberRangeInterval> {
		boolean isInRange(int number);
		int getMin();
		int getMax();
	}
	
	/**
	 *	Both the minimum and maximum values are included in the range.
	 */
	protected class TableNumberRangeInterval implements IRange {
		private int min;
		private int max;
		
		protected TableNumberRangeInterval(int min, int max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public boolean isInRange(int number) {
			return (number >= this.getMin()) && (number <= this.getMax());
		}

		@Override
		public int compareTo(TableNumberRangeInterval o) {
			return (int) Math.signum(this.getMin() - o.getMin());
		}
		
		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof TableNumberRangeInterval)) {
				return false;
			}
			TableNumberRangeInterval castedO = (TableNumberRangeInterval) o;
			return castedO.getMin() == this.getMin() && castedO.getMax() == this.getMax();
		}

		@Override
		public int getMin() {
			return this.min;
		}

		@Override
		public int getMax() {
			return this.max;
		}
	}
}
