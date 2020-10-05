package cenk.sy.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.WordUtils;

import cenk.sy.backend.exception.SyEntityNotFoundError;
import cenk.sy.backend.exception.SyMissingPathParameterError;
import cenk.sy.backend.exception.SyMissingRequestBodyParameterError;
import cenk.sy.backend.exception.SyMissingRequestParameterError;
import cenk.sy.backend.exception.SyMultiContractError;
import cenk.sy.backend.exception.SyNotAllowedRequestBodyParameterError;
import cenk.sy.jpa.entity.contract.SyContract;
import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.jpa.entity.team.SyCurrency;
import cenk.sy.jpa.entity.team.SyTeam;


// this class is for encapsulation
// outside of the module can not reach to this class' properties and methods
class SyTeamServiceBasic extends SyServiceBasic {

	private void checkContracts(List<SyContract> contracts, Long teamId) throws SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyEntityNotFoundError, SyMultiContractError {
		for(SyContract c:contracts) {
			if(c.getTeam()!=null) {
				throw new SyNotAllowedRequestBodyParameterError("team (in contract)");
			} else {
				SyTeam team = new SyTeam();
				team.setId(teamId);
				c.setTeam(team);
			}
			if(c.getYear()==null) {
				throw new SyMissingRequestBodyParameterError("year (in contract)");
			}
			if(c.getPlayer()==null) {
				throw new SyMissingRequestBodyParameterError("player (in contract)");
			} else {
				if(c.getPlayer().getId()==null) {
					throw new SyMissingRequestBodyParameterError("id (in player in contract)");
				} else {
					SyPlayer existingPlayer = getIfPlayerExists(c.getPlayer().getId());
					for(SyContract pc:existingPlayer.getContracts()) {
						if(pc.getYear().equals(c.getYear())) {
							throw new SyMultiContractError(pc);
						}
					}
				}
				if(c.getPlayer().getContracts()!=null) {
					throw new SyNotAllowedRequestBodyParameterError("contracts (in player in contract)");
				}
				if(c.getPlayer().getBirthYear()!=null) {
					throw new SyNotAllowedRequestBodyParameterError("birthYear (in player in contract)");
				}
				if(c.getPlayer().getName()!=null) {
					throw new SyNotAllowedRequestBodyParameterError("name (in player in contract)");
				}
			}
		}
	}

	protected SyTeam create(SyTeam team) throws SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyEntityNotFoundError, SyMultiContractError {
		checkDoesNotHaveExplicitId(team);
		if(team.getCurrency()==null) {
			throw new SyMissingRequestBodyParameterError("currency");
		} else {
			SyCurrency eCurr = getCurrencyIfExists(team.getCurrency());
			team.setCurrency(eCurr);
		}
		if(team.getName()==null) {
			throw new SyMissingRequestBodyParameterError("name");
		} else {
			String name = team.getName().trim();
			if(name.isEmpty()) {
				throw new SyMissingRequestBodyParameterError("non empty name");
			}
			name = WordUtils.capitalizeFully(name);
			team.setName(name);
		}
		team = tRepo.save(team);
		if(team.getContracts()!=null) {
			checkContracts(team.getContracts(), team.getId());
		}
		return tRepo.save(team);
	}

	protected void delete(Long id) throws SyMissingPathParameterError, SyEntityNotFoundError {
		if(id==null) {
			throw new SyMissingPathParameterError("id");
		}
		SyTeam existing = getIfTeamExists(id);
		tRepo.delete(existing);
	}

	public List<SyTeam> getAll() {
		return tRepo.findAll();
	}

	private SyCurrency getCurrencyIfExists(SyCurrency currency) throws SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyEntityNotFoundError {
		if(currency.getId()==null) {
			throw new SyMissingRequestBodyParameterError("id (in currency)");
		} else if (currency.getCode()!=null){
			throw new SyNotAllowedRequestBodyParameterError("code (in currency)");
		}
		Optional<SyCurrency> currOpt = currRepo.findById(currency.getId());
		if(currOpt.isPresent()) {
			return currOpt.get();
		} else {
			throw new SyEntityNotFoundError("currency", currency.getId());
		}
	}

	public List<SyTeam> getTeamsByPlayer(Long playerId) throws SyEntityNotFoundError, SyMissingRequestParameterError {
		if(playerId==null) {
			throw new SyMissingRequestParameterError("playerId");
		}
		SyPlayer player = getIfPlayerExists(playerId);
		List<SyTeam> answer = new ArrayList<>();
		for(SyContract pc:player.getContracts()) {
			if(!answer.contains(pc.getTeam())) {
				answer.add(pc.getTeam());
			}
		}
		return answer;
	}

	protected SyTeam update(SyTeam team, Long id) throws SyMissingPathParameterError, SyEntityNotFoundError, SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyMultiContractError {
		if(id==null) {
			throw new SyMissingPathParameterError("id");
		}
		checkDoesNotHaveExplicitId(team);
		SyTeam existing = getIfTeamExists(id);
		if(team.getCurrency()!=null) {
			SyCurrency eCurr = getCurrencyIfExists(team.getCurrency());
			existing.setCurrency(eCurr);
		}

		if(team.getContracts()!=null) {
			checkContracts(team.getContracts(), id);
			existing.getContracts().clear();
			existing.setContracts(team.getContracts());
		}
		if(team.getName()!=null) {
			String name = team.getName().trim();
			if(name.isEmpty()) {
				throw new SyMissingRequestBodyParameterError("non empty name to change the name of the team");
			}
			name = WordUtils.capitalizeFully(name);
			existing.setName(name);
		}
		return tRepo.save(existing);
	}
}
