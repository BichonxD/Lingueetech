package exception;

public class ActionNotFoundException extends ActionHandlerException {

	private static final long serialVersionUID = 1L;

	public ActionNotFoundException(String message){
		super(message);
	}
}
