package mirscript.lang.concurrent;

@FunctionalInterface
public interface Routine8<A, B, C, D, E, F, G, H> extends Routine {
	public void routine(Waivable wv, A a, B b, C c, D d, E e, F f, G g, H h);
}
