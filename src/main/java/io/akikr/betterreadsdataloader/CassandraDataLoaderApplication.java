package io.akikr.betterreadsdataloader;

import io.akikr.betterreadsdataloader.service.CassandraDataLoaderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @apiNote Main class for CassandraDataLoaderApplication
 * @author ankit
 * @since 1.0
 */

@Slf4j
@SpringBootApplication
public class CassandraDataLoaderApplication
{
	/**
	 * @apiNote The 'CONFIG_FILE_LOCATION' value for 'spring.config.location' is used here to externalize the spring configurations
	 */
	private static final String CONFIG_FILE_LOCATION = "/opt/akikr/config/better-reads-data-loader/better-reads-data-loader.yml";


	// A static block to check and load the application config properties from specified location
	static
	{
		log.info("Loading application config properties...");

		// If file exists then load the specified properties otherwise load default properties
		File file = Paths.get(CONFIG_FILE_LOCATION).toFile();
		if (file.exists())
		{
			log.info("Loading properties from location: " + CONFIG_FILE_LOCATION);
			System.setProperty("spring.config.location", CONFIG_FILE_LOCATION);
		}
		else
		{
			log.info("File doesn't exists at location: " + CONFIG_FILE_LOCATION);
			log.info("Loading default application config properties...");
		}
	}

	public static void main(String[] args)
	{
		log.info("CassandraDataLoaderApplication started with args: " + Arrays.toString(args) );
		ConfigurableApplicationContext context = SpringApplication.run(CassandraDataLoaderApplication.class, args);

		// Run the CassandraDataLoaderService
		CassandraDataLoaderService cassandraDataLoaderService = context.getBean(CassandraDataLoaderService.class);
		cassandraDataLoaderService.run();
	}
}
