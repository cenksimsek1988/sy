package cenk.sy.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cenk.ozan.backend.exception.OzException;
import cenk.ozan.backend.pojo.OzConvertionListRequest;
import cenk.ozan.backend.pojo.OzConvertionResponse;
import cenk.ozan.backend.service.OzConvertionService;
import cenk.ozan.backend.service.OzRateService;
import cenk.ozan.jpa.common.OzConstants;
import cenk.ozan.jpa.entity.convertion.OzConvertion;
import cenk.ozan.jpa.entity.rate.OzRate;
import cenk.sy.rest.pojo.OzErrorResponse;

@CrossOrigin(origins = "${cross.origin:http://localhost:3000}")
@RestController
public class OzRestController implements OzConstants{

	@Autowired
	private OzRateService rateService;

	@Autowired
	private OzConvertionService convService;

	@GetMapping("/api/rate")
	public ResponseEntity<Object> getFxRate(@RequestParam String from, @RequestParam String to) {
		try {
			OzRate rate = rateService.getRate(from, to);
			return new ResponseEntity<Object>(rate, HttpStatus.OK);
		} catch (OzException e) {
			e.printStackTrace();
			OzErrorResponse errResp = new OzErrorResponse(e);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			OzErrorResponse errResp = new OzErrorResponse(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/api/convert")
	public ResponseEntity<Object> convert(@RequestParam String from, @RequestParam String to, @RequestParam float amount) {		
		try {
			OzConvertionResponse convResp = rateService.convert(from, to, amount);
			return new ResponseEntity<Object>(convResp, HttpStatus.OK);
		} catch (OzException e) {
			e.printStackTrace();			OzErrorResponse errResp = new OzErrorResponse(e);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			OzErrorResponse errResp = new OzErrorResponse(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/api/list")
	public ResponseEntity<Object> list(@RequestBody OzConvertionListRequest req) {
		try {
			List<OzConvertion> convList = convService.list(req);
			return new ResponseEntity<Object>(convList, HttpStatus.OK);
		} catch (OzException e) {
			e.printStackTrace();			OzErrorResponse errResp = new OzErrorResponse(e);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			OzErrorResponse errResp = new OzErrorResponse(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
