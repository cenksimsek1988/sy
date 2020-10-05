package cenk.sy.backend.service;
import java.util.List;

import org.springframework.stereotype.Service;

import cenk.sy.backend.exception.SyEntityNotFoundError;
import cenk.sy.backend.exception.SyMissingPathParameterError;
import cenk.sy.backend.exception.SyMissingRequestBodyParameterError;
import cenk.sy.backend.exception.SyMissingRequestParameterError;
import cenk.sy.backend.exception.SyNotAllowedRequestBodyParameterError;
import cenk.sy.jpa.entity.player.SyPlayer;

@Service
public class SyPlayerService extends SyPlayerServiceBasic{

	public SyPlayer create(SyPlayer player) throws SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyEntityNotFoundError {
		return super.create(player);
	}
	
	public void delete(Long id) throws SyMissingPathParameterError, SyEntityNotFoundError {
		super.delete(id);
	}
	
	public List<SyPlayer> getAll() {
		return super.getAll();
	}

	public List<SyPlayer> getPlayersByYearAndTeam(Long teamId, Integer year) throws SyMissingRequestParameterError, SyEntityNotFoundError {
		return super.getPlayersByYearAndTeam(teamId, year);
	}

	public SyPlayer update(SyPlayer player, Long id) throws SyMissingPathParameterError, SyEntityNotFoundError, SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError {
		return super.update(player, id);
	}
	
}