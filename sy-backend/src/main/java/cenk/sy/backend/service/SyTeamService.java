package cenk.sy.backend.service;
import java.util.List;

import org.springframework.stereotype.Service;

import cenk.sy.backend.exception.SyEntityNotFoundError;
import cenk.sy.backend.exception.SyMissingPathParameterError;
import cenk.sy.backend.exception.SyMissingRequestBodyParameterError;
import cenk.sy.backend.exception.SyMissingRequestParameterError;
import cenk.sy.backend.exception.SyMultiContractError;
import cenk.sy.backend.exception.SyNotAllowedRequestBodyParameterError;
import cenk.sy.jpa.entity.team.SyTeam;

@Service
public class SyTeamService extends SyTeamServiceBasic{

	public SyTeam create(SyTeam team) throws SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyEntityNotFoundError, SyMultiContractError {
		return super.create(team);
	}
	
	public void delete(Long id) throws SyMissingPathParameterError, SyEntityNotFoundError {
		super.delete(id);
	}
	
	public List<SyTeam> getAll() {
		return super.getAll();
	}
	
	public List<SyTeam> getTeamsByPlayer(Long playerId) throws SyMissingRequestParameterError, SyEntityNotFoundError {
		return super.getTeamsByPlayer(playerId);
	}

	public SyTeam update(SyTeam team, Long id) throws SyMissingPathParameterError, SyEntityNotFoundError, SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyMultiContractError {
		return super.update(team, id);
	}
	
}