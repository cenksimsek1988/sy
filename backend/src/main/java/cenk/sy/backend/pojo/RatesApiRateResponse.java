package cenk.sy.backend.pojo;

import java.util.Map;

import lombok.Data;

@Data
public class RatesApiRateResponse {
	private String base;
	private String date;
	private Map<String, Float> rates;
}
