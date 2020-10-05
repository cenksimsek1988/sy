package cenk.sy.backend.exception;

import java.text.MessageFormat;

public class SyMissingRequestParameterError extends SyException {
	private static final int ERROR_CODE = 1002;
	private static final String MESSAGE = "You should set parameter '{0}' in request's url";
	private static final long serialVersionUID = 9004523609894883249L;
	private final String msg;
	
	public SyMissingRequestParameterError(String paramName){
		msg = MessageFormat.format(MESSAGE, paramName);
	}
	
	@Override
	public int errorCode() {
		return ERROR_CODE;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
}
