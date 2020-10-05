package cenk.sy.rest.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import cenk.sy.backend.exception.SyException;
import lombok.Data;

@Data
public class SyErrorResponse {
	@JsonProperty("ERROR CODE")
	private int errorCode;
	@JsonProperty("ERROR MESSAGE")
	private String msg;
	
	public SyErrorResponse() {}
	
	public SyErrorResponse(int code, String msg) {
		errorCode = code;
		this.msg = msg;
	}

	public SyErrorResponse(SyException e) {
		errorCode = e.errorCode();
		msg = e.getMessage();
	}

}
