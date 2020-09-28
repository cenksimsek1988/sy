package cenk.sy.backend.exception;

import java.text.MessageFormat;
import java.time.LocalDate;

import cenk.ozan.jpa.entity.rate.OzCurrency;

public class OzNoRateForGivenDateException extends OzException {
	private static final long serialVersionUID = -6099200393706319299L;
	private static final int ERROR_CODE = 3001;
	private static final String PREMESSAGE = "No exchange rate registry found for the currency pair {0}/{1} at date: {2}";
	private final String msg;

	public OzNoRateForGivenDateException(LocalDate latestFetch, OzCurrency from, OzCurrency to) {
		msg = MessageFormat.format(PREMESSAGE, from.getCode(), to.getCode(), latestFetch);
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
