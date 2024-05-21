package mirscript.lang.functional;

import java.util.Iterator;

public class IteratorGenerator<T> extends Generator<Iterator<T>, T> {

	public IteratorGenerator(Iterator<T> base) {
		super(base);
	}
	
	public IteratorGenerator(Iterable<T> base) {
		super(base.iterator());
	}

	@Override
	protected T next() {
		if(base.hasNext()) {
			return base.next();
		}
		throw new StopIteration();
	}
}
