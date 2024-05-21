package mirscript.lang.functional;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Supplier;

public class Generators {
	public static <T> Generator<Supplier<T>, T> of(Supplier<T> s) {
		return new SupplierGenerator<T>(s);
	}
	
	public static <T> Generator<Iterator<T>, T> of(Iterator<T> i) {
		return new IteratorGenerator<T>(i);
	}
	
	public static <T> Generator<Iterator<T>, T> of(Iterable<T> i) {
		return new IteratorGenerator<T>(i);
	}
	
	public static <T, U> Generator<Function<U, T>, T> of(U u, Function<U, T> f) {
		return new Generator<Function<U, T>, T>(f) {
			@Override
			public T next() {
				return base.apply(u);
			}
		};
	}
	
	@SafeVarargs
	public static <T> Generator<Iterator<T>, T> ofArray(T... a) {
		return new IteratorGenerator<T>(Arrays.stream(a).iterator());
	}
}
