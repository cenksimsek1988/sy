package cenk.sy.backend.service;

import static cenk.sy.backend.util.SyUtil.getAge;
import static cenk.sy.backend.util.SyUtil.getYear;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import cenk.sy.backend.exception.SyEntityNotFoundError;
import cenk.sy.backend.exception.SyMissingRequestParameterError;
import cenk.sy.backend.pojo.SyContractValueResponse;
import cenk.sy.jpa.common.SyConstants;
import cenk.sy.jpa.entity.contract.SyContract;
import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.jpa.entity.team.SyTeam;
import cenk.sy.jpa.repo.SyPlayerRepo;


// this class is for encapsulation
// outside of the module can not reach to this class' properties and methods
class SyContractServiceBasic implements SyConstants{
	private static final SyContractValueResponse NO_CONTRACT_VALUE_RESPONSE = new SyContractValueResponse("USD", 0f);
	private static SyContractValueResponse getContractValueResponse(SyPlayer player, int year) {
		// it is a rule of thumb calculation
		// for any year, except current one, played with a contract, I assume 12 months of experience
		// if player is currently not contracted, I do not take in account the club's commision and
		// appropriate currency will be the latest team's currency
		// players who did not completed any year with a professional contract are all valued as free
		int experienceInMonth = 0;
		SyTeam currentTeam = null;
		SyTeam latestTeam = null;
		int latestContractedYear = 0;
		for(SyContract c:player.getContracts()) {
			if(c.getYear()<year) {
				experienceInMonth += 12;
				if(c.getYear()>latestContractedYear) {
					latestTeam = c.getTeam();
				}
			} else if(c.getYear()==year) {
				currentTeam = c.getTeam();
			}
		}
		int age = getAge(player, year);
		float transferFee = getTransferFee(experienceInMonth, age);
		SyContractValueResponse answer = new SyContractValueResponse();
		if(currentTeam==null) {
			if(latestTeam==null) {
				return NO_CONTRACT_VALUE_RESPONSE;
			} else {
				answer.setCurrencyCode(latestTeam.getCurrency().getCode());
				answer.setValue(transferFee);
			}
			
		} else {
			answer.setCurrencyCode(currentTeam.getCurrency().getCode());
			answer.setValue(transferFee*1.1f);
		}
		return answer;
	}

	private static float getTransferFee(int experienceInMonth, int age) {
		return experienceInMonth * 100000f / age;
	}

	@Autowired
	private SyPlayerRepo pRepo;

	protected SyContractValueResponse getLatestContractValueResponse(Long playerId) throws SyMissingRequestParameterError, SyEntityNotFoundError {
		SyPlayer player = preparePlayer(playerId);
		int year = getYear();
		return getContractValueResponse(player, year);
	}

	private SyPlayer preparePlayer(Long playerId) throws SyEntityNotFoundError, SyMissingRequestParameterError {
		if(playerId==null) {
			throw new SyMissingRequestParameterError("playerId");
		}
		Optional<SyPlayer> playerOpt = pRepo.findById(playerId);
		try {
			return playerOpt.get();
		} catch (NoSuchElementException e) {
			throw new SyEntityNotFoundError("player", playerId);
		}
	}
}
