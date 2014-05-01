package exception;

public class InvalidRequestException extends ActionHandlerException {

	private static final long serialVersionUID = 1L;

	public InvalidRequestException(String message) {
		super(message);
	}

}
