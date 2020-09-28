package cenk.sy.backend.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class OzUnknownCurrencyCodesException extends OzException {
	private static final long serialVersionUID = -2759458654632094302L;
	private static final String PREMESSAGE = "Unexpected currency codes:\n";
	private static final int ERROR_CODE = 1002;
	@Getter
	private List<String> unknownCodes = new ArrayList<>();
	
	@Override
	public String getMessage() {
		return PREMESSAGE + Arrays.toString(unknownCodes.toArray());
	}
	
	public void addUnknownCode(String unknownCode) {
		unknownCodes.add(unknownCode);
	}
	
	@Override
	public int errorCode() {
		return ERROR_CODE;
	}

}
