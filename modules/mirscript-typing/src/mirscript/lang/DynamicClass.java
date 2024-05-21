package mirscript.lang;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Stream;

public class DynamicClass {
	private static Map<Class<?>, DynamicClass> loaded;
	private static Map<Class<?>, Class<?>> primitives;
	private static Map<Class<?>, Class<?>> boxes;
	
	private static Class<?>[][] typeArray = {
			{ boolean.class, Boolean.class },
			{ byte.class, Byte.class },
			{ char.class, Character.class },
			{ double.class, Double.class },
			{ float.class, Float.class },
			{ int.class, Integer.class },
			{ long.class, Long.class },
			{ short.class, Short.class },
			{ void.class, Void.class },
	};
	
	private static List<Class<?>> integerPrimitives;
	
	static {
		loaded = new HashMap<Class<?>, DynamicClass>();
		primitives = new HashMap<Class<?>, Class<?>>();
		boxes = new HashMap<Class<?>, Class<?>>();
		
		for(var row : typeArray) {
			primitives.put(row[0], row[1]);
			boxes.put(row[1], row[0]);
		}
		integerPrimitives = List.of(byte.class, short.class, int.class, long.class);
	}
	
	protected Map<String, List<Method>> methods;
	protected Map<String, Field> fields;
	protected List<Constructor<?>> constructors;
	
	private Class<?> base;
	
	private DynamicClass(Class<?> c) {
		base = c;
		
		methods = new HashMap<String, List<Method>>();
		for(var m : c.getDeclaredMethods()) {
			if(methods.get(m.getName()) == null) {
				methods.put(m.getName(), new Vector<Method>());
			}
			
			methods.get(m.getName()).add(m);
		}
		
		for(var m : c.getMethods()) {
			if(methods.get(m.getName()) == null) {
				methods.put(m.getName(), new Vector<Method>());
			}
			
			if(!methods.get(m.getName()).contains(m)) {
				methods.get(m.getName()).add(m);
			}
		}
		
		fields = new HashMap<String, Field>();
		for(var f : c.getDeclaredFields()) {
			fields.put(f.getName(), f);
		}
		
		constructors = Collections.synchronizedList(List.of(c.getDeclaredConstructors()));
	}
	
	public static DynamicClass of(Class<?> c) {
		var obj = loaded.get(c);
		
		if(obj == null) {
			obj = new DynamicClass(c);
			loaded.put(c, obj);
		}
		
		return obj;
	}
	
	public static DynamicClass of(Object o) {
		if(o == null) {
			return of(Void.class);
		}
		
		return of(o.getClass());
	}
	
	private static Boolean argsMatch(Method m, Object[] args, BoxingStrategy[] strat) {
		var params = m.getParameters();
		for(int i = 0; i < args.length; ++i) {
			var paramType = params[i].getType();
			var argType = args[i].getClass();
			if(paramType.isPrimitive()) {
				if(argType == Character.class && integerPrimitives.contains(paramType)) {
					strat[i] = BoxingStrategy.CHAR_TO_INT;
				} else if(argType == Float.class && paramType == double.class) {
					strat[i] = BoxingStrategy.FLOAT_TO_DOUBLE;
				} else if(paramType == float.class || paramType == double.class) {
					if(integerPrimitives.contains(unboxed(argType))) {
						strat[i] = BoxingStrategy.INT_TO_FLOAT;
					} else if(argType == Character.class) {
						strat[i] = BoxingStrategy.CHAR_TO_FLOAT;
					} else {
						return false;
					}
				} else if(boxed(paramType).isAssignableFrom(argType)) {
					strat[i] = BoxingStrategy.AUTOBOX;
				} else {
					return false;
				}
				continue;
			} else {
				strat[i] = BoxingStrategy.REFERENCE;
			}
			if(!paramType.isAssignableFrom(args[i].getClass())) {
				return false;
			}
		}
		
		return true;
	}
	
	public DynamicObject call(String methodName, Object instance, Object... args) {
		var methodList = methods.get(methodName);
		
		if(methodList == null) {
			throw new DynamicInvocationException(String.format("class %s has no method with name \"%s\"", base.getName(), methodName));
		}
		
		List<Method> matches = new ArrayList<Method>();
		List<BoxingStrategy[]> strats = new ArrayList<BoxingStrategy[]>();
		
		for(var m : methodList) {
			if(m.getParameterCount() == args.length) {
				BoxingStrategy[] strat = new BoxingStrategy[args.length];
				if(argsMatch(m, args, strat)) {
					matches.add(m);
					strats.add(strat);
				}
			}
		}
		
		if(matches.size() == 0) {
			throw new DynamicInvocationException(String.format("class %s has no method: %s(%s)",
					base.getName(), methodName, String.join(", ", Stream.of(args).map(o -> o.getClass().getName()).toList())));
		}
		
		Method choice = null;
		
		if(matches.size() == 1) {
			choice = matches.get(0);
		}
		
		try {
			choice.setAccessible(true);
			return new DynamicObject(choice.invoke(instance, args));
		} catch(Exception ex) {
			throw new DynamicInvocationException("An exception occurred during dynamic invocation", ex);
		}
	}
	
	@Override
	public String toString() {
		return base.getName();
	}
	
	public static Class<?> boxed(Class<?> c) {
		// byte, short, int, long, float, double, char, boolean
		if(!c.isPrimitive()) {
			return c;
		}
		
		return primitives.get(c);
	}
	
	public static Class<?> unboxed(Class<?> c) {
		if(c.isPrimitive()) {
			return c;
		}
		
		return boxes.get(c);
	}
}

enum BoxingStrategy {
	AUTOBOX, CHAR_TO_INT, CHAR_TO_FLOAT, FLOAT_TO_DOUBLE, INT_TO_FLOAT, REFERENCE
}








































