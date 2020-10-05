package cenk.sy.backend.service;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import cenk.sy.backend.exception.SyEntityNotFoundError;
import cenk.sy.backend.exception.SyNotAllowedRequestBodyParameterError;
import cenk.sy.jpa.common.SyConstants;
import cenk.sy.jpa.common.SyEntity;
import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.jpa.entity.team.SyTeam;
import cenk.sy.jpa.repo.SyCurrencyRepo;
import cenk.sy.jpa.repo.SyPlayerRepo;
import cenk.sy.jpa.repo.SyTeamRepo;

public class SyServiceBasic implements SyConstants{
	protected static void checkDoesNotHaveExplicitId(SyEntity entity) throws SyNotAllowedRequestBodyParameterError {
		if(entity.getId()!=null) {
			throw new SyNotAllowedRequestBodyParameterError("id");
		}
	}
	@Autowired
	protected SyCurrencyRepo currRepo;
	@Autowired
	protected SyPlayerRepo pRepo;
	
	@Autowired
	protected SyTeamRepo tRepo;

	protected SyPlayer getIfPlayerExists(Long id) throws SyEntityNotFoundError {
		Optional<SyPlayer> playerOpt = pRepo.findById(id);
		try {
			return playerOpt.get();
		} catch (NoSuchElementException e) {
			throw new SyEntityNotFoundError("player", id);
		}
	}
	
	protected SyTeam getIfTeamExists(Long id) throws SyEntityNotFoundError {
		Optional<SyTeam> teamOpt = tRepo.findById(id);
		try {
			return teamOpt.get();
		} catch (NoSuchElementException e) {
			throw new SyEntityNotFoundError("player", id);
		}
	}

}
