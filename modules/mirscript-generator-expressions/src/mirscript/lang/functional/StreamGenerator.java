package mirscript.lang.functional;

import java.util.Iterator;
import java.util.stream.Stream;

public class StreamGenerator<U> extends Generator<Stream<U>, U> {
	Iterator<U> source;

	public StreamGenerator(Stream<U> base) {
		super(base);
		
		source = base.iterator();
	}

	@Override
	protected U next() {
		if(!source.hasNext()) {
			throw new StopIteration();
		}
		
		return source.next();
	}
}
