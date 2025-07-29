package mirscript.lang.concurrent;

@FunctionalInterface
public interface Routine3<A, B, C> extends Routine {
	public void routine(Waivable wv, A a, B b, C c);
}
