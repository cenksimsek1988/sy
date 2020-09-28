package cenk.sy.backend.exception;

public class OzDateParsingException extends OzException {
	private static final long serialVersionUID = 5471881119368075063L;
	private static final int ERROR_CODE = 1003;
	private final String msg;
	
	public OzDateParsingException(String from, Exception e) {
		msg = "Date parsing error occured while fetching currency: " + from + "\n" + e.getMessage();
	}
	
	@Override
	public String getMessage() {
		return msg;
	}

	@Override
	public int errorCode() {
		return ERROR_CODE;
	}

}
