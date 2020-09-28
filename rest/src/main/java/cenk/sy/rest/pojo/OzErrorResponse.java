package cenk.sy.rest.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import cenk.ozan.backend.exception.OzException;
import lombok.Data;

@Data
public class OzErrorResponse {
	@JsonProperty("ERROR CODE")
	private int errorCode;
	@JsonProperty("ERROR MESSAGE")
	private String msg;
	
	public OzErrorResponse() {}
	
	public OzErrorResponse(OzException e) {
		errorCode = e.errorCode();
		msg = e.getMessage();
	}

	public OzErrorResponse(int code, String msg) {
		errorCode = code;
		this.msg = msg;
	}

}
