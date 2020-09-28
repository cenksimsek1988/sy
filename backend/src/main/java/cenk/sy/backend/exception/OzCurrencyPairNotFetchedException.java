package cenk.sy.backend.exception;

import java.text.MessageFormat;

import cenk.ozan.jpa.entity.rate.OzCurrency;

public class OzCurrencyPairNotFetchedException extends OzException {
	private static final long serialVersionUID = -1506233638779013127L;
	private static final String PREMESSAGE = "Currency pair from {0} to {1} is never fetched";
	private static final int ERROR_CODE = 2001;
	private final String msg;
	
	public OzCurrencyPairNotFetchedException(OzCurrency from, OzCurrency to) {
		msg = MessageFormat.format(PREMESSAGE, from, to);
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
