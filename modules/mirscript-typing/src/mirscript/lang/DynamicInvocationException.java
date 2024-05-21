package mirscript.lang;

public class DynamicInvocationException extends RuntimeException {

	public DynamicInvocationException(String message) {
		super(message);
	}
	
	public DynamicInvocationException(Throwable cause) {
		super(cause);
	}
	
	public DynamicInvocationException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = 6912111572069573073L;
}
