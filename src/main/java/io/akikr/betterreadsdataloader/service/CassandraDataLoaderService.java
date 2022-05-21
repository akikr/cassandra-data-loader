package io.akikr.betterreadsdataloader.service;

import io.akikr.betterreadsdataloader.db.connection.DataStaxAstraProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * @apiNote CassandraDataLoaderService class to execute all cassandra data loader service(s)
 * @author ankit
 * @since 1.0
 */

@Slf4j
@Service
@EnableConfigurationProperties(DataStaxAstraProperties.class)
public class CassandraDataLoaderService
{
	private final AuthorDataLoaderService authorDataLoaderService;
	private final BookDataLoaderService bookDataLoaderService;

	/**
	 * @implNote: Use this to Autowire the AuthorDataLoaderService using constructor instead of using @Autowired annotation.
	 * @param authorDataLoaderService {@link AuthorDataLoaderService}
	 * @param bookDataLoaderService {@link BookDataLoaderService}
	 */
	@Autowired
	public CassandraDataLoaderService(AuthorDataLoaderService authorDataLoaderService, BookDataLoaderService bookDataLoaderService)
	{
		this.authorDataLoaderService = authorDataLoaderService;
		this.bookDataLoaderService = bookDataLoaderService;
	}

	/**
	 * @implNote A run method to initiate the data-loader service execution
	 */
	public void run()
	{
		log.info("Cassandra-Data-Loader-Application started !!");
		log.info("Now running the cassandra data loader service(s)...");
		authorDataLoaderService.initAuthors();
		bookDataLoaderService.initWorks();
		log.info("All Cassandra data loader service(s) completed !!");
	}
}
