package cenk.sy.backend.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cenk.sy.jpa.entity.team.SyCurrency;

@Service
public class SyCurrencyService extends SyCurrencyServiceBasic {
	private static final Logger logger = LoggerFactory.getLogger(SyCurrencyService.class);
	
	public List<SyCurrency> getAll() {
		return super.getAll();
	}

	public void prepare() {
		logger.info("preparing..");
		populateCurrenciesIfNotExists();
		logger.info("preparation is completed");
	}

}
