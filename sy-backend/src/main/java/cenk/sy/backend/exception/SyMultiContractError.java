package cenk.sy.backend.exception;

import java.text.MessageFormat;

import cenk.sy.jpa.entity.contract.SyContract;

public class SyMultiContractError extends SyException {
	private static final int ERROR_CODE = 4001;
	private static final String MESSAGE = "Player name: '{0}', id: {1} has already a contract with team name: {2}, id: {3} for the year: {4}";
	private static final long serialVersionUID = -1379681542467503996L;
	private final String msg;
	
	public SyMultiContractError(SyContract c){
		msg = MessageFormat.format(MESSAGE, c.getPlayer().getName(), c.getPlayer().getId(), c.getTeam().getName(), c.getTeam().getId(), c.getYear());
	}
	
	@Override
	public int errorCode() {
		return ERROR_CODE;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
}
