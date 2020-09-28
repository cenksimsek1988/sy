package cenk.sy.backend.exception;

import lombok.Getter;


public class OzUnknownCurrencyCodeExpectation extends OzException {
	private static final long serialVersionUID = -7312298664486520214L;
	private static final String PREMESSAGE = "No currency found with code: ";
	private static final int ERROR_CODE = 1001;
	@Getter
	private final String unknownCode;

	public OzUnknownCurrencyCodeExpectation(String code) {
		unknownCode = code;
	}

	@Override
	public int errorCode() {
		return ERROR_CODE;
	}

	@Override
	public String getMessage() {
		return PREMESSAGE + unknownCode;
	}

}
