package mirscript.lang.concurrent;

import java.lang.reflect.Method;

public interface Routine {
	default Method extractTarget() {
		Method target;
		
		Method[] methods = getClass().getDeclaredMethods();
		
		target = null;
		
		for(Method m : methods) {
			if(m.getName().equals("routine")) {
				if(target == null) {
					target = m;
				} else {
					throw new InvalidTargetException("There must not be more than one `routine` method defined in a coroutine");
				}
			}
		}
		
		if(target == null) {
			throw new InvalidTargetException("There must be exactly one `routine` method defined in a coroutine");
		}
		
		return target;
	}
}
