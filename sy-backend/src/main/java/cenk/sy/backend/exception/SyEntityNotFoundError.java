package cenk.sy.backend.exception;

import java.text.MessageFormat;

public class SyEntityNotFoundError extends SyException {
	private static final int ERROR_CODE = 3001;
	private static final String PREMESSAGE = "No {0} found with id: {1}";
	private static final long serialVersionUID = -1506233638779013127L;
	private final String msg;
	
	public SyEntityNotFoundError(String entityName, Long id) {
		msg = MessageFormat.format(PREMESSAGE, entityName, id);
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
