package cenk.sy.backend.service;
import org.springframework.stereotype.Service;

import cenk.sy.backend.exception.SyEntityNotFoundError;
import cenk.sy.backend.exception.SyMissingRequestParameterError;
import cenk.sy.backend.pojo.SyContractValueResponse;

@Service
public class SyContractService extends SyContractServiceBasic{

	public SyContractValueResponse getContractValue(Long playerId) throws SyEntityNotFoundError, SyMissingRequestParameterError {
		return getLatestContractValueResponse(playerId);
	}
	
}