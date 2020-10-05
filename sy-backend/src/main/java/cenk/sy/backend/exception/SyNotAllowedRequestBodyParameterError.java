package cenk.sy.backend.exception;

import java.text.MessageFormat;

public class SyNotAllowedRequestBodyParameterError extends SyException {
	private static final int ERROR_CODE = 2001;
	private static final String MESSAGE = "Parameter '{0}' is not allowed in request's body";
	private static final long serialVersionUID = 9004523609894883249L;
	private final String msg;
	
	public SyNotAllowedRequestBodyParameterError(String paramName){
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
