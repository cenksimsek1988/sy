package cenk.sy.jpa.common;

import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public interface OzConstants {
	public static final int UNKNOWN_ERROR_CODE = 9000;
	public static final String UNKNOWN_ERROR_MESSAGE = "An uncategorized error occured. Please check server's log";

	public static final ObjectMapper JACKSON_MAPPER = new ObjectMapper();
	public static final ObjectWriter JACKSON_WRITER = JACKSON_MAPPER.writer().withDefaultPrettyPrinter();

	
	public static final DateTimeFormatter RATES_API_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;


	public static final String[] CURRENCY_CODES = new String[] {
			"GBP",
	        "HKD",
	        "IDR",
	        "ILS",
	        "DKK",
	        "INR",
	        "CHF",
	        "MXN",
	        "CZK",
	        "SGD",
	        "THB",
	        "HRK",
	        "MYR",
	        "NOK",
	        "CNY",
	        "BGN",
	        "PHP",
	        "SEK",
	        "PLN",
	        "ZAR",
	        "CAD",
	        "ISK",
	        "BRL",
	        "RON",
	        "NZD",
	        "TRY",
	        "JPY",
	        "RUB",
	        "KRW",
	        "USD",
	        "HUF",
	        "AUD",
	        "EUR"
	        };

}
