package mirscript.lang.functional;

import java.util.function.Function;

public class FunctionGenerator<T, U> extends Generator<Function<U, T>, T> {
	Generator<?, U> source;
	
	public FunctionGenerator(Function<U, T> base, Generator<?, U> source) {
		super(base);
		
		this.source = source;
	}

	@Override
	protected T next() {
		return base.apply(source.next());
	}

}
