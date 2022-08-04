package com.softline.csrv;

import io.jmix.core.annotation.MessageSourceBasenames;
import io.jmix.notifications.NotificationType;
import io.jmix.notifications.NotificationTypesRepository;
import io.jmix.ui.icon.Icons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@EnableScheduling
@SpringBootApplication
@MessageSourceBasenames({"com/softline/csrv/messages","com/softline/csrv/entities","com/softline/csrv/screen"})
public class CsrvApplication {

	@Autowired
	NotificationTypesRepository notificationTypesRepository;

	@Autowired
	Icons icons;

	public static void main(String[] args) {
		SpringApplication.run(CsrvApplication.class, args);
	}

	@Bean
	@Primary
	TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		return tt;
	}

	@Bean
	@Primary
	@ConfigurationProperties(prefix="main.datasource")
	DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@PostConstruct
	public void postConstruct() {
		notificationTypesRepository.registerTypes(
				new NotificationType("ИНФО", "INFO_CIRCLE"),
				new NotificationType("ПРЕДУПРЕЖДЕНИЕ", "WARNING"),
				new NotificationType("ВНИМАНИЕ", "CLOCK_O")
		);
	}
}