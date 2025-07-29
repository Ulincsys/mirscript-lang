package mirscript.lang.concurrent;

@FunctionalInterface
public interface Routine6<A, B, C, D, E, F> extends Routine {
	public void routine(Waivable wv, A a, B b, C c, D d, E e, F f);
}
