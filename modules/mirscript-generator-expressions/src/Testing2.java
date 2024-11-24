import java.util.function.Supplier;

import mirscript.lang.functional.Generator;
import mirscript.lang.functional.SupplierGenerator;
import mirscript.lang.functional.Range;

public class Testing2 {

	public static void main(String[] args) {
		Generator<Supplier<Integer>, Integer> g = new SupplierGenerator<Integer>(() -> {
			return 0;
		});
		
		System.out.println(g.limit(10).toList());
		
		System.out.println(new Range(10).map(i -> i + 3).filter(i -> i % 2 == 0).toList());
		
		System.out.println(String.join(", ", new Range(10).map(String::valueOf)));
		
		System.out.println(new SupplierGenerator<Integer>(() -> 0).limit(10).toList());
		
		Range r = new Range(50);
		System.out.println(r.take(10));
		System.out.println(r.take(10));
		System.out.println(r.limit(10).toList());
		
		r.toStream().forEach(i -> System.out.println(i));
		
		for(int i : new Range(10)) {
			System.out.print(i);
		}
	}
}
