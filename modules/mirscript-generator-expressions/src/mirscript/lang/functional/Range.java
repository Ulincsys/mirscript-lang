package mirscript.lang.functional;

import java.util.Iterator;

public class Range extends Generator<RangeIterator, Integer> {

	private Range(RangeIterator base) {
		super(base);
	}
	
	public Range(Integer start, Integer end, Integer step) {
		this(new RangeIterator(start, end, step));
	}
	
	public Range(Integer start, Integer end) {
		this(new RangeIterator(start, end));
	}
	
	public Range(Integer end) {
		this(new RangeIterator(end));
	}

	@Override
	protected Integer next() {
		return base.next();
	}
	
	@Override
	public String toString() {
		return String.format("Range(%s, %s, %s)", base.start, base.end, base.step);
	}
}

class RangeIterator implements Iterator<Integer> {
	Integer start, step, end;
	
	public RangeIterator(Integer start, Integer end, Integer step) {
		if(start == null || end == null || step == null) {
			throw new NullPointerException("Range values cannot be null");
		}
		
		if(step == 0) {
			throw new IllegalArgumentException("Range step cannot be 0");
		}
		
		if(start > end && step > 0) {
			throw new IllegalArgumentException("Range cannot be infinite");
		}
		
		if(start < end && step < 0) {
			throw new IllegalArgumentException("Range cannot be infinite");
		}
		
		this.start = start;
		this.end = end;
		this.step = step;
	}
	
	public RangeIterator(Integer start, Integer end) {
		this(start, end, end > start ? 1 : -1);
	}
	
	public RangeIterator(Integer end) {
		this(0, end);
	}

	@Override
	public boolean hasNext() {
		if(step < 0) {
			return (start + step) > end;
		}
		
		return (start + step) < end;
	}
	
	private boolean hasCurrent() {
		if(step < 0) {
			return start > end;
		}
		
		return start < end;
	}

	@Override
	public Integer next() {
		if(!hasCurrent()) {
			throw new StopIteration();
		}
		
		Integer temp = start;
		start += step;
		return temp;
	}
}

































