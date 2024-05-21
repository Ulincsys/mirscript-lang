package mirscript.lang.functional;

import java.util.function.Supplier;

public class SupplierGenerator<T> extends Generator<Supplier<T>, T> {
	public SupplierGenerator(Supplier<T> base) {
		super(base);
	}

	@Override
	protected T next() {
		return base.get();
	}
}
