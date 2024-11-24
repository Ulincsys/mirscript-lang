package mirscript.lang.functional;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class Generator<T, U> implements Iterable<U> {
	protected T base;
	
	public Generator(T base) {
		this.base = base;
	}

	@Override
	public void forEach(Consumer<? super U> c) {
		while(true) {
			try {
				c.accept(next());
			} catch(StopIteration ex) {
				break;
			}
		}
	}
	
	public <V> Generator<?, V> map(Function<U, V> applier) {
		return Generators.of(applier, this);
	}
	
	public Stream<U> toStream() {
		return StreamSupport.stream(spliterator(), false);
	}
	
	public Generator<?, U> limit(final Integer size) {
		if(size < 0) {
			throw new IllegalArgumentException("limit cannot be negative");
		}
		
		return new Generator<Generator<T, U>, U>(this) {
			Integer l = size;
			@Override
			protected U next() {
				if(l-- > 0) {
					return base.next();
				}
				
				throw new StopIteration();
			}
			
		};
	}
	
	public Generator<?, U> filter(Function<U, Boolean> key) {
		return new Generator<Generator<T, U>, U>(this) {
			
			@Override
			protected U next() {
				U temp = base.next();
				
				while(!key.apply(temp)) {
					temp = base.next();
				}
				
				return temp;
			}
		};
	}
	
	protected abstract U next();
	
	@Override
	public Iterator<U> iterator() {
		return new GeneratorIterator<U>(this);
	}
	
	public List<U> take(Integer size) {
		List<U> l = new LinkedList<U>();
		
		limit(size).iterator().forEachRemaining(l::add);
		
		return l;
	}
	
	public List<U> toList() {
		List<U> l = new LinkedList<U>();
		
		iterator().forEachRemaining(l::add);
		
		return l;
	}
	
	@Override
	public String toString() {
		ParameterizedType me = (ParameterizedType)getClass().getGenericSuperclass();
		Type t = me.getActualTypeArguments()[0];
		Type u = me.getActualTypeArguments()[1];
		
		if(getClass().getSimpleName().length() == 0) {
			return String.format("Generator<%s, %s>()", t.getTypeName(), u.getTypeName());
		}
		
		try {
			var types = getClass().getTypeParameters();
			if(types.length == 0) {
				return getClass().getSimpleName() + "()";
			}
			
			return String.format("%s<%s>()", getClass().getSimpleName(), String.join(", ", Stream.of(types).map(y -> y.getTypeName()).toList()));
			
		} catch(Exception ex) {
			return String.format("%s<%s>()", getClass().getSimpleName(), u.getTypeName());
		}
	}
}

class GeneratorIterator<U> implements Iterator<U> {
	protected Generator<?, U> gen;
	protected Boolean stop;
	protected U tempNext;
	
	public GeneratorIterator(Generator<?, U> gen) {
		this.gen = gen;
		stop = false;
	}

	@Override
	public boolean hasNext() {
		if(tempNext != null) {
			return true;
		} else if(stop) {
			return false;
		}
		
		try {
			tempNext = next();
			return true;
		} catch(StopIteration ex) {
			return false;
		}
	}

	@Override
	public U next() {
		if(stop) {
			throw new StopIteration();
		}
		
		if(tempNext != null) {
			U temp = tempNext;
			tempNext = null;
			return temp;
		}
		
		try {
			return gen.next();
		} catch(StopIteration ex) {
			stop = true;
			throw ex;
		}
	}
}



































