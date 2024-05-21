import mirscript.lang.DynamicClass;
import mirscript.lang.DynamicObject;

public class Main {

	public static void main(String[] args) {
		var o = new DynamicObject("test");
		
		Float f = 3.14f;
		Integer i = 0;
		
		float fc = '7';
		
		var t = new DynamicObject(new Test());
		
		System.out.println(t.call("func", (char)97));
	}
	
	public static void func(char a) {
		System.out.println(a);
	}
}

class Test {
	private void func(char d) {
		System.out.println(d);
	}
}
