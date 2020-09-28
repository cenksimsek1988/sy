package cenk.sy.backend.exception;

public class OzConvertionListRequestFormatError extends OzException {
	private static final long serialVersionUID = 9004523609894883249L;
	private static final String MESSAGE = "You should provide at least one of the properties between 'transactionId' and 'transactionDate' in request's post body";
	private static final int ERROR_CODE = 1004;
	
	@Override
	public String getMessage() {
		return MESSAGE;
	}
	
	@Override
	public int errorCode() {
		return ERROR_CODE;
	}
}
