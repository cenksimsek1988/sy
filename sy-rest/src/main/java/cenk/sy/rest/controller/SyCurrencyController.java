package cenk.sy.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import cenk.sy.backend.service.SyCurrencyService;
import cenk.sy.jpa.common.SyConstants;
import cenk.sy.jpa.entity.team.SyCurrency;
import cenk.sy.rest.pojo.SyErrorResponse;

@CrossOrigin(origins = "${cross.origin:http://localhost:3000}")
@RestController
public class SyCurrencyController implements SyConstants{

	@Autowired
	private SyCurrencyService currService;

	@GetMapping("/jpa/currencies")
	public ResponseEntity<Object> allCurrencies() {
		try {
			List<SyCurrency> currencies = currService.getAll();
			return new ResponseEntity<Object>(currencies, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			SyErrorResponse errResp = new SyErrorResponse(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
