package cenk.sy.backend.exception;

import java.text.MessageFormat;

public class SyMissingPathParameterError extends SyException {
	private static final int ERROR_CODE = 1001;
	private static final String MESSAGE = "You should add parameter '{0}' at the end the path";
	private static final long serialVersionUID = 9004523609894883249L;
	private final String msg;
	
	public SyMissingPathParameterError(String paramName){
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
