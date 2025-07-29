package mirscript.lang.concurrent;

@FunctionalInterface
public interface Routine2<A, B> extends Routine {
	public void routine(Waivable wv, A a, B b);
}
