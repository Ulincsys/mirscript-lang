package mirscript.lang.concurrent;

@FunctionalInterface
public interface Routine1<A> extends Routine {
	public void routine(Waivable wv, A a);
}
