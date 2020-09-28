package cenk.sy.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cenk.ozan.jpa.common.OzConstants;
import cenk.ozan.jpa.entity.convertion.OzConvertion;
import cenk.ozan.jpa.entity.rate.OzCurrency;
import cenk.ozan.jpa.entity.rate.OzRate;
import cenk.ozan.jpa.entity.rate.OzRateMeta;
import cenk.ozan.jpa.repo.OzConvertionRepo;
import cenk.ozan.jpa.repo.OzCurrencyRepo;
import cenk.ozan.jpa.repo.OzRateRepo;
import cenk.sy.backend.exception.OzCurrencyPairNotFetchedException;
import cenk.sy.backend.exception.OzNoRateForGivenDateException;
import cenk.sy.backend.exception.OzUnknownCurrencyCodeExpectation;
import cenk.sy.backend.exception.OzUnknownCurrencyCodesException;


// this class is for encapsulation
// outside of the module can not reach to this class' properties and methods
class OzRateServiceBasic implements OzConstants{
	private static final Logger logger = LoggerFactory.getLogger(OzRateServiceBasic.class);
	@Autowired
	protected OzRateRepo rateRepo;
	@Autowired
	protected OzConvertionRepo convRepo;
	@Autowired
	protected OzCurrencyRepo currRepo;
	private static Map<OzCurrency, Map<OzCurrency, OzRate>> allLatestRates = new HashMap<>();
	private static Map<OzCurrency, Map<OzCurrency, OzRateMeta>> latestFetches = new HashMap<>();
	private static Map<String, OzCurrency> currencyMap = new HashMap<>();

	protected void mapRate(String base, LocalDate date, Map<String, Float> rates) throws OzUnknownCurrencyCodesException {
		OzUnknownCurrencyCodesException exp = new OzUnknownCurrencyCodesException();
		try {
			List<OzRate> ozRates = new ArrayList<>();
			List<OzRateMeta> ozRateMetas = new ArrayList<>();
			OzCurrency from = getCurrency(base);

			// latest infos before daily fetch
			Map<OzCurrency, OzRate> rateMapOfBase = allLatestRates.getOrDefault(from, new HashMap<>());
			Map<OzCurrency, OzRateMeta> metaMapOfBase = latestFetches.getOrDefault(base, new HashMap<>());

			for(String toString:rates.keySet()) {
				try {
					OzCurrency to = getCurrency(toString);
					Float rate = rates.get(toString);
					if(rate!=null && rate > 0) {
						OzRate ozRate = rateRepo.findByDateAndFromAndTo(date, from, to);
						if(ozRate==null) {
							ozRate = new OzRate();
						}
						ozRate.setDate(date);
						ozRate.setFrom(from);
						ozRate.setTo(to);
						ozRate.setRate(rate);

						// holding here in order to persist in db
						ozRates.add(ozRate);

						rateMapOfBase.put(to, ozRate);

						OzRateMeta meta = new OzRateMeta();
						meta.setDate(date);
						meta.setFrom(from);
						meta.setTo(to);
						// holding here in order to persist in db
						ozRateMetas.add(meta);
						// keeping in memory for performance issues
						metaMapOfBase.put(to, meta);
					} else {
						logger.error("Exchange rate is not received with a proper format./nfrom: {}, to: {}, rate: {}", base, toString, rate);
					}
				} catch (OzUnknownCurrencyCodeExpectation uce) {
					exp.addUnknownCode(uce.getUnknownCode());
				}
			}

			// persisting them into historical data for relation with transactions
			rateRepo.saveAll(ozRates);

			// keeping just the latest rates in memory for performance
			latestFetches.put(from, metaMapOfBase);
			allLatestRates.put(from, rateMapOfBase);

			if(!exp.getUnknownCodes().isEmpty()) {
				throw exp;
			}
		} catch (OzUnknownCurrencyCodeExpectation e) {
			exp.addUnknownCode(base);
			throw exp;
		}
	}

	protected OzConvertion saveConvertion(OzRate ozRate, float amount) {
		OzConvertion conv = new OzConvertion();
		conv.setRate(ozRate);
		conv.setAmount(amount);
		LocalDate date = LocalDate.now();
		conv.setDate(date);
		conv = convRepo.save(conv);
		return conv;
	}

	protected OzRate getLatestRate(String fromCode, String toCode) throws OzCurrencyPairNotFetchedException, OzNoRateForGivenDateException, OzUnknownCurrencyCodeExpectation {
		OzCurrency from = currencyMap.get(fromCode);
		if(from==null) {
			throw new OzUnknownCurrencyCodeExpectation(fromCode);
		}
		OzCurrency to = currencyMap.get(toCode);
		if(to==null) {
			throw new OzUnknownCurrencyCodeExpectation(toCode);
		}
		if(from.equals(to)) {
			LocalDate date = LocalDate.now();
			return new OzRate(date, from, to, 1f);
		}
		
		// check first from memory for performance
		Map<OzCurrency, OzRate> baseCurrencyMap = allLatestRates.get(from);
		if(baseCurrencyMap!=null) {
			OzRate ozRate = baseCurrencyMap.get(to);
			if(ozRate!=null) {
				return ozRate;
			}
		}
		
		
		LocalDate latestUpdate = getLatestUpdateDate(from, to);
		OzRate  ozRate = rateRepo.findByDateAndFromAndTo(latestUpdate, from, to);
		if(ozRate!=null) {
			return ozRate;
		} else {
			throw new OzNoRateForGivenDateException(latestUpdate, from, to);
		}
	}

	private OzCurrency getCurrency(String code) throws OzUnknownCurrencyCodeExpectation {
		OzCurrency answer = currencyMap.get(code);
		if(answer==null) {
			throw new OzUnknownCurrencyCodeExpectation(code);
		}
		return answer;
	}

	private LocalDate getLatestUpdateDate(OzCurrency from, OzCurrency to) throws OzCurrencyPairNotFetchedException {
		Map<OzCurrency, OzRateMeta> metaMap = latestFetches.get(from);
		if(metaMap!=null) {
			OzRateMeta meta = metaMap.get(to);
			if(meta!=null) {
				return meta.getDate();
			}
		}
		throw new OzCurrencyPairNotFetchedException(from, to);
	}

	protected void prepare() {
		List<OzCurrency> currencies = currRepo.findAll();
		if(currencies.isEmpty()) {
			for(int i=0; i < CURRENCY_CODES.length; i++) {
				OzCurrency curr = new OzCurrency();
				String code = CURRENCY_CODES[i];
				curr.setCode(code);
				currencies.add(curr);
			}
			currencies = currRepo.saveAll(currencies);
		}
		for(OzCurrency curr:currencies) {
			currencyMap.put(curr.getCode(), curr);
		}
	}

}
