import java.util.function.Function;
import java.util.function.Supplier;

import mirscript.lang.functional.Generator;
import mirscript.lang.functional.Generators;
import mirscript.lang.functional.Range;
import mirscript.lang.functional.StopIteration;
import mirscript.lang.functional.SupplierGenerator;

public class Main {
	public static void main(String[] args) {
		Generators.of(new Supplier<Integer>() {
			Integer i = 0;
			@Override
			public Integer get() {
				if(i < 10) {
					return ++i;
				}
				throw new StopIteration();
			}
		}).map(x -> x * 2).forEach(x -> {
			System.out.println(x);
		});
		
		Generators.ofArray(new Integer[] { 0, 1, 2, 3, 4, 5}).forEach(e -> {
			System.out.println(e);
		});
		
		var g = new Generator<Sequence, Integer>(new Sequence(0, 1, 10)) {
			@Override
			public Integer next() {
				return base.next();
			}
		};
		
		for(var fib : g) {
			System.out.println(fib);
		}
		
		Generators.of(new Sequence(0, 1, 30), s -> {
			return s.next();
		}).forEach(fib -> System.out.println(fib));
		
		var zero = Generators.of(() -> 0);
		
		System.out.println(zero.limit(10).toList());
		
		new Range(3).forEach(i -> {
			System.out.println(zero.take(10));
		});
		
		Range r = new Range(10);
		
		System.out.println(g);
		System.out.println(r);
		System.out.println(zero);
		System.out.println(zero.limit(0));
		
//		for(var i : r) {
//			System.out.println(i);
//		}
		r.forEach(i -> {
			System.out.println(i);
		});
		
		System.out.println(Generators.of(new Sequence(0, 1, 30), s -> s.next()).toList());
	}
	
	public static class Sequence {
		Integer a, b, n;
		
		public Sequence(Integer A, Integer B, Integer N) {
			a = A;
			b = B;
			n = N;
		}
		
		public Integer next() {
			if(n-- <= 0) {
				throw new StopIteration();
			}
			
			var temp = a;
			a = b;
			b += temp;
			
			return temp;
		}
	}
}
























