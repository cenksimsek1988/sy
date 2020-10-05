package cenk.sy.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cenk.sy.jpa.common.SyConstants;
import cenk.sy.jpa.entity.team.SyCurrency;
import cenk.sy.jpa.repo.SyCurrencyRepo;

public class SyCurrencyServiceBasic implements SyConstants{
	private static final Logger logger = LoggerFactory.getLogger(SyCurrencyServiceBasic.class);

	@Autowired
	private SyCurrencyRepo currRepo;
	
	public List<SyCurrency> getAll() {
		return currRepo.findAll();
	}

	protected void populateCurrenciesIfNotExists(){
		List<SyCurrency> existings = currRepo.findAll();
		if(existings==null || existings.isEmpty()) {
			logger.info("No currency inserted in db yet. 33 currencies will be added now");
			for(String code:CURRENCY_CODES) {
				SyCurrency cur = new SyCurrency();
				cur.setCode(code);
				currRepo.save(cur);
			}
			logger.info("Currencies are inserted");
		}
	}

}
