package mirscript.lang;

public class DynamicObject {
	private DynamicClass c;
	private Object o;
	
	public DynamicObject(Object o) {
		c = DynamicClass.of(o);
		this.o = o;
	}
	
	public DynamicObject call(String methodName, Object... args) {
		return c.call(methodName, o, args);
	}
	
	@SuppressWarnings("unchecked")
	public <Any> Any unpack() {
		if(o == null) {
			return null;
		}
		
		return (Any) o.getClass().cast(o);
	}
	
	@Override
	public String toString() {
		if(o == null) {
			return "Void";
		}
		
		return String.valueOf(o);
	}
}
