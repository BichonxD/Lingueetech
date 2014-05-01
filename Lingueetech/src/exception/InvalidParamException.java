package exception;

public class InvalidParamException extends ActionHandlerException{

	private static final long serialVersionUID = 1L;

	public InvalidParamException(String message) {
		super(message);
	}

}
