package cenk.sy.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import cenk.sy.backend.service.SyCurrencyService;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@ComponentScan(basePackages = {"cenk.sy"})
@EnableJpaRepositories(basePackages = {"cenk.sy.jpa.repo"})
@EntityScan(basePackages = "cenk.sy.jpa.entity")
public class SyRestApi {
	public static void main(String[] args) {
		SpringApplication.run(SyRestApi.class, args);
	}
		
	@Autowired
	private SyCurrencyService currService;

	private final Logger logger = LoggerFactory.getLogger(SyRestApi.class);
	
	@Bean
	public CommonsRequestLoggingFilter requestLoggingFilter() {
		CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(10000);
		filter.setIncludeHeaders(false);
		filter.setAfterMessagePrefix("Request:\n");
		return filter;
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void start() {
		currService.prepare();
		logger.info("Ready to go!");
	}
}
