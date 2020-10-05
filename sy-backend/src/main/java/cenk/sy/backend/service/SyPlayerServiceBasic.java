package cenk.sy.backend.service;

import static cenk.sy.backend.util.SyUtil.getYear;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.WordUtils;

import cenk.sy.backend.exception.SyEntityNotFoundError;
import cenk.sy.backend.exception.SyMissingPathParameterError;
import cenk.sy.backend.exception.SyMissingRequestBodyParameterError;
import cenk.sy.backend.exception.SyMissingRequestParameterError;
import cenk.sy.backend.exception.SyNotAllowedRequestBodyParameterError;
import cenk.sy.jpa.entity.contract.SyContract;
import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.jpa.entity.team.SyTeam;


// this class is for encapsulation
// outside of the module can not reach to this class' properties and methods
class SyPlayerServiceBasic extends SyServiceBasic {

	private static void checkBirthYear(int birthYear) throws SyMissingRequestBodyParameterError {
		if(birthYear<1900) {
			throw new SyMissingRequestBodyParameterError("birthYear not earlier than 1900");
		}
		int currentYear = getYear();
		if(currentYear-birthYear<16) {
			throw new SyMissingRequestBodyParameterError("birthYear which implies at least 16-year old professional player");
		}
	}

	private void checkContracts(List<SyContract> contracts, Long playerId) throws SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyEntityNotFoundError {
		for(SyContract c:contracts) {
			if(c.getPlayer()!=null) {
				throw new SyNotAllowedRequestBodyParameterError("player (in contract)");
			}
			SyPlayer player = new SyPlayer();
			player.setId(playerId);
			c.setPlayer(player);
			if(c.getYear()==null) {
				throw new SyMissingRequestBodyParameterError("year (in contract)");
			}
			if(c.getTeam()==null) {
				throw new SyMissingRequestBodyParameterError("team (in contract)");
			} else {
				if(c.getTeam().getId()==null) {
					throw new SyMissingRequestBodyParameterError("id (in team in contract)");
				} else {
					getIfTeamExists(c.getTeam().getId());
				}
				if(c.getTeam().getContracts()!=null) {
					throw new SyNotAllowedRequestBodyParameterError("contracts (in team in contract)");
				}
				if(c.getTeam().getCurrency()!=null) {
					throw new SyNotAllowedRequestBodyParameterError("currency (in team in contract)");
				}
				if(c.getTeam().getName()!=null) {
					throw new SyNotAllowedRequestBodyParameterError("name (in team in contract)");
				}
			}
		}
	}

	protected SyPlayer create(SyPlayer player) throws SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError, SyEntityNotFoundError {
		checkDoesNotHaveExplicitId(player);
		if(player.getBirthYear()==null) {
			throw new SyMissingRequestBodyParameterError("birthYear");
		} else {
			checkBirthYear(player.getBirthYear());
		}
		if(player.getName()==null) {
			throw new SyMissingRequestBodyParameterError("name");
		} else {
			String name = player.getName().trim();
			if(name.isEmpty()) {
				throw new SyMissingRequestBodyParameterError("non empty name");
			}
			name = WordUtils.capitalizeFully(name);
			player.setName(name);
		}
		player = pRepo.save(player);
		if(player.getContracts()!=null) {
			checkContracts(player.getContracts(), player.getId());
		}
		return pRepo.save(player);
	}
	
	protected void delete(Long id) throws SyMissingPathParameterError, SyEntityNotFoundError {
		if(id==null) {
			throw new SyMissingPathParameterError("id");
		}
		SyPlayer existing = getIfPlayerExists(id);
		pRepo.delete(existing);
	}

	public List<SyPlayer> getAll() {
		return pRepo.findAll();
	}

	public List<SyPlayer> getPlayersByYearAndTeam(Long teamId, Integer year) throws SyMissingRequestParameterError, SyEntityNotFoundError {
		if(teamId==null) {
			throw new SyMissingRequestParameterError("teamId");
		}
		int currentYear = getYear();
		if(year>currentYear || year<1900) {
			throw new SyMissingRequestParameterError("year between 1900 and " + currentYear);
		}
		SyTeam team = getIfTeamExists(teamId);
		List<SyPlayer> answer = new ArrayList<>();
		for(SyContract tc:team.getContracts()) {
			if(tc.getYear().equals(year)) {
				answer.add(tc.getPlayer());
			}
		}
		return answer;
	}

	protected SyPlayer update(SyPlayer player, Long id) throws SyMissingPathParameterError, SyEntityNotFoundError, SyNotAllowedRequestBodyParameterError, SyMissingRequestBodyParameterError {
		if(id==null) {
			throw new SyMissingPathParameterError("id");
		}
		checkDoesNotHaveExplicitId(player);
		SyPlayer existing = getIfPlayerExists(id);
		if(player.getBirthYear()!=null) {
			checkBirthYear(player.getBirthYear());
			existing.setBirthYear(player.getBirthYear());
		}
		if(player.getContracts()!=null) {
			checkContracts(player.getContracts(), id);
			existing.getContracts().clear();
			existing.setContracts(player.getContracts());
		}
		if(player.getName()!=null) {
			String name = player.getName().trim();
			if(name.isEmpty()) {
				throw new SyMissingRequestBodyParameterError("non empty name to change the name of the player");
			}
			name = WordUtils.capitalizeFully(name);
			existing.setName(name);
		}
		return pRepo.save(existing);
	}

}
