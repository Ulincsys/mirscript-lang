package mirscript.lang.concurrent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

@SuppressWarnings("unused")
public abstract class Coroutine implements Runnable {
	private Object yielded = null;
	private Boolean nextReady = false;
	private Boolean nextReleased = false;
	private Boolean shutdown = false;
	private Method target;
	private Thread t;
	private Object[] args;
	
	private ArrayList<Coroutine> followers;
	
	protected Coroutine() {
		followers = new ArrayList<Coroutine>();
	}
	
	protected Coroutine(Method target) {
		this();
		
		this.target = target;
	}
	
	protected synchronized void waive(Object o) {
		yielded = o;
		nextReady = true;
		
		while(true) {
			try {
				notifyAll();
				wait();
				
				if(nextReleased) {
					nextReleased = false;
					yielded = null;
					return;
				}
			} catch(InterruptedException e) {
				if(shutdown == false) {
					continue;
				}
				
				break;
			}
		}
	}
	
	public synchronized <Any> Any next(long timeout) {
		if(Thread.currentThread() == t) {
			throw new IllegalAccessError("A coroutine may not await itself");
		}
		
		if(timeout <= 0) {
			throw new IllegalArgumentException("Timeout must be a positive number");
		}
		
		try {
			wait(timeout);
			
			if(nextReady) {
				return next();
			}
		} catch(InterruptedException e) {
			
		}
		
		return null;
	}
	
	public synchronized Boolean nextReady() {
		return t != null && t.getState() == Thread.State.WAITING;
	}
	
	@SuppressWarnings("unchecked")
	public synchronized <Any> Any next() {
		if(Thread.currentThread() == t) {
			throw new IllegalAccessError("A coroutine may not await itself");
		}
		
		try {
			while(t.isAlive() && !nextReady && !shutdown) {				
				wait(1000);
			}
			
			if(!nextReady || shutdown) {
				throw new NoYieldException("Coroutine exited while waiting");
			}
			
			Object result = yielded;
			nextReleased = true;
			nextReady = false;
			notify();
			return (Any)result;
		} catch(InterruptedException e) {
			
		}
		
		return null;
	}
	
	public synchronized void compose(Object... args) {
		this.args = args;
		
		
	}
	
	public synchronized void start(Object... args) {
		if(t != null) {
			
		}
		
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
		
		t = new Thread(this);
		this.args = args;
		t.start();
	}
	
	public void run() {
		try {
			target.setAccessible(true);
			target.invoke(this, args);
			
			synchronized (this) {
				// Wake up any waiting threads on routine exit
				shutdown = true;
				notifyAll();
			}
			
			for(Coroutine c : followers) {
				
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.getCause().printStackTrace();
		}
	}
	
	private static Method extractTarget(Class<?> c) {
		Method target;
		
		Method[] methods = c.getDeclaredMethods();
		
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
	
	private static Coroutine of(Routine r) {
		Method target = r.extractTarget();
		
		Coroutine c = new Coroutine(target) {};
		
		return c;
	}
	
	public static Coroutine of(Routine0 r) { return of((Routine)r); }
}
































