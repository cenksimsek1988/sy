package cenk.sy.rest.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cenk.sy.backend.exception.SyException;
import cenk.sy.backend.service.SyTeamService;
import cenk.sy.jpa.common.SyConstants;
import cenk.sy.jpa.entity.team.SyTeam;
import cenk.sy.rest.pojo.SyErrorResponse;

@CrossOrigin(origins = "${cross.origin:http://localhost:3000}")
@RestController
public class SyTeamRestController implements SyConstants{
	private static final Logger logger = LoggerFactory.getLogger(SyPlayerRestController.class);

	@Autowired
	private SyTeamService tService;
	
	@PostMapping("/jpa/team")
	public ResponseEntity<Object> create(@RequestBody SyTeam team) {
		try {
			team = tService.create(team);
			return new ResponseEntity<Object>(team, HttpStatus.CREATED);
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
	
	@DeleteMapping("/jpa/team/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {		
		try {
			tService.delete(id);
			return new ResponseEntity<Object>(null, HttpStatus.NO_CONTENT);
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
	
	@GetMapping("/jpa/teams")
	public ResponseEntity<Object> players() {		
		try {
			List<SyTeam> teams = tService.getAll();
			return new ResponseEntity<Object>(teams, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			SyErrorResponse errResp = new SyErrorResponse(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/jpa/team")
	public ResponseEntity<Object> teamsByPlayer(@RequestParam Long playerId) {		
		try {
			List<SyTeam> teams = tService.getTeamsByPlayer(playerId);
			return new ResponseEntity<Object>(teams, HttpStatus.OK);
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
	
	@PutMapping("/jpa/team/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody SyTeam team) {		
		try {
			team = tService.update(team, id);
			return new ResponseEntity<Object>(team, HttpStatus.OK);
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
