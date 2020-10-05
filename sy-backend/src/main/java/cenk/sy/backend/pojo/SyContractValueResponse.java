package cenk.sy.backend.pojo;

import lombok.Data;

@Data
public class SyContractValueResponse {
	private String currencyCode;
	private float value;
	
	public SyContractValueResponse() {}

	public SyContractValueResponse(String currencyCode, float value) {
		this.currencyCode= currencyCode;
		this.value = value;
	}

}
