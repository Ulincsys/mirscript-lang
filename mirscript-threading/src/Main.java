import mirscript.lang.concurrent.Coroutine;
import mirscript.lang.concurrent.NoYieldException;

public class Main {

	public static void main(String[] args) {
		Coroutine c = new Coroutine() {
			void routine() {
				System.out.println("Hello from thread " + Thread.currentThread().getName());
			}
		};
		
		System.out.println("Starting coroutine");
		c.start();
		
		
		Coroutine c2 = new Coroutine() {
			void routine(int start, int stop) {
				System.out.println("Hello from thread " + Thread.currentThread().getName());
				System.out.format("Stepping from %s to %s\n", start, stop);
				
				for(int i = start; i < stop; ++i) {
					waive(i);
					System.out.println("Yielded %s".formatted(i));
				}
			}
		};
		System.out.println("Starting next coro");
		c2.start(10, 20);
		
		try {
			while(true) {
				int i = c2.next();
				System.out.println(i);
			}
		} catch(NoYieldException e) {
			System.out.println("Coroutine ended");
		}
		
		var c3 = Coroutine.of((w) -> {
			System.out.println("Test");
		});
		
		c3.start();
	}
}


































