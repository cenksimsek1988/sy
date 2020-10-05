package cenk.sy.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cenk.sy.backend.exception.SyException;
import cenk.sy.backend.pojo.SyContractValueResponse;
import cenk.sy.backend.service.SyContractService;
import cenk.sy.jpa.common.SyConstants;
import cenk.sy.rest.pojo.SyErrorResponse;

@CrossOrigin(origins = "${cross.origin:http://localhost:3000}")
@RestController
public class SyContractRestController implements SyConstants{
	private static final Logger logger = LoggerFactory.getLogger(SyContractRestController.class);

	@Autowired
	private SyContractService conService;

	@GetMapping("/api/contractValue")
	public ResponseEntity<Object> getContractValue(@RequestParam Long playerId) {
		try {
			SyContractValueResponse resp = conService.getContractValue(playerId);
			return new ResponseEntity<Object>(resp, HttpStatus.OK);
		} catch (SyException e) {
			logger.error(e.getMessage());
			SyErrorResponse errResp = new SyErrorResponse(e);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			SyErrorResponse errResp = new SyErrorResponse(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
