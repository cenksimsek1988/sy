package cenk.sy.backend.exception;

import java.text.MessageFormat;

public class SyMissingRequestBodyParameterError extends SyException {
	private static final int ERROR_CODE = 1003;
	private static final String MESSAGE = "You should provide {0} information in request's body";
	private static final long serialVersionUID = 9004523609894883249L;
	private final String msg;
	
	public SyMissingRequestBodyParameterError(String paramName){
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
