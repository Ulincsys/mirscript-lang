package mirscript.lang.concurrent;

@FunctionalInterface
public interface Routine4<A, B, C, D> extends Routine {
	public void routine(Waivable wv, A a, B b, C c, D d);
}
