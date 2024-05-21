package mirscript.lang.functional;

import java.util.function.Function;

public class FunctionGenerator<T> extends Generator<Function<T, T>, T> {
	Generator<?, T> source;
	
	public FunctionGenerator(Function<T, T> base, Generator<?, T> source) {
		super(base);
		
		this.source = source;
	}

	@Override
	protected T next() {
		return base.apply(source.next());
	}

}
