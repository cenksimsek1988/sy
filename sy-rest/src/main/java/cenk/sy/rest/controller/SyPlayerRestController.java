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
import cenk.sy.backend.service.SyPlayerService;
import cenk.sy.jpa.common.SyConstants;
import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.rest.pojo.SyErrorResponse;

@CrossOrigin(origins = "${cross.origin:http://localhost:3000}")
@RestController
public class SyPlayerRestController implements SyConstants{
	private static final Logger logger = LoggerFactory.getLogger(SyPlayerRestController.class);

	@Autowired
	private SyPlayerService pService;
	@PostMapping("/jpa/player")
	public ResponseEntity<Object> create(@RequestBody SyPlayer player) {
		try {
			player = pService.create(player);
			return new ResponseEntity<Object>(player, HttpStatus.CREATED);
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
	
	@DeleteMapping("/jpa/player/{id}")
	public ResponseEntity<Object> delete(@PathVariable Long id) {		
		try {
			pService.delete(id);
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
	
	@GetMapping("/jpa/players")
	public ResponseEntity<Object> players() {		
		try {
			List<SyPlayer> players = pService.getAll();
			return new ResponseEntity<Object>(players, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			SyErrorResponse errResp = new SyErrorResponse(UNKNOWN_ERROR_CODE, UNKNOWN_ERROR_MESSAGE);
			return new ResponseEntity<Object>(errResp, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/jpa/player")
	public ResponseEntity<Object> playersByYearAndTeam(@RequestParam Long teamId, @RequestParam Integer year) {		
		try {
			List<SyPlayer> players = pService.getPlayersByYearAndTeam(teamId, year);
			return new ResponseEntity<Object>(players, HttpStatus.OK);
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
	
	@PutMapping("/jpa/player/{id}")
	public ResponseEntity<Object> update(@PathVariable Long id, @RequestBody SyPlayer player) {		
		try {
			player = pService.update(player, id);
			return new ResponseEntity<Object>(player, HttpStatus.OK);
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
