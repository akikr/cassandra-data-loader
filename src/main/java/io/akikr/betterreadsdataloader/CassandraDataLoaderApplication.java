package io.akikr.betterreadsdataloader;

import io.akikr.betterreadsdataloader.service.CassandraDataLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @apiNote Main class for CassandraDataLoaderApplication
 * @author ankit
 * @since 1.0
 */

@Slf4j
@SpringBootApplication
public class CassandraDataLoaderApplication
{
	public static void main(String[] args)
	{
		ConfigurableApplicationContext context = SpringApplication.run(CassandraDataLoaderApplication.class, args);

		// Run the CassandraDataLoaderService
		CassandraDataLoaderService cassandraDataLoaderService = context.getBean(CassandraDataLoaderService.class);
		cassandraDataLoaderService.run();

		// Exit application after all data loader services are completed
		System.exit(SpringApplication.exit(context, () -> {
			log.info("Exiting CassandraDataLoaderApplication !!");
			return 0;
		}));
	}
}
