package mirscript.lang.concurrent;

@FunctionalInterface
public interface Routine5<A, B, C, D, E> extends Routine {
	public void routine(Waivable wv, A a, B b, C c, D d, E e);
}
