package io.akikr.betterreadsdataloader.service;

import io.akikr.betterreadsdataloader.db.entity.Author;
import io.akikr.betterreadsdataloader.db.repository.AuthorRepository;
import io.akikr.betterreadsdataloader.utils.JsonStringParser;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author ankit
 * @since 1.0
 * @implNote: A Author data loader service that parse the data from data-dump files and then loads into cassandra database
 */

@Slf4j
@Service
public class AuthorDataLoaderService
{
    /**
     * @implNote: The author data-dump file location.
     */
    @Value("${datadump.location.author}")
    private String authorDataDumpLocation;

    private final AuthorRepository authorRepository;

    /**
     * @apiNote: Construction injection to Autowire the {@link AuthorRepository}
     * @param authorRepository {@link AuthorRepository}
     */
    @Autowired
    public AuthorDataLoaderService(AuthorRepository authorRepository)
    {
        this.authorRepository = authorRepository;
    }

    /**
     * @implNote: Author data initializer method that gets the data from data-dump files, parse it and then sends to
     * {@link AuthorRepository} to save it.
     */
    public void initAuthors()
    {
        log.info("Author's data initializer started !!");
        log.info("Reading the author data from : " + authorDataDumpLocation);
        Path path = Paths.get(authorDataDumpLocation);

        try (Stream<String> lines = Files.lines(path))
        {
            log.info("Saving the author data...");
            lines.forEach(line -> {
                // Read and parse the lines
                String jsonString = line.substring(line.indexOf("{"));
                JSONObject jsonObject =  new JsonStringParser().getJSONObjectFromString(jsonString);

                // Construct the Author object
                Author author = new Author();
                author.setPersonalName(jsonObject.optString("personal_name"));
                try
                {
                    author.setId(jsonObject.getString("key").replace("/authors/", ""));
                }
                catch (JSONException e)
                {
                    log.error("Error occurred while setting the author's Id: " + e.getMessage());
                }
                author.setName(jsonObject.optString("name"));
                // Persists the author object into AuthorRepository
                log.debug("Saving the author: " + author.getName() + " ...");
                authorRepository.save(author);
            });
        }
        catch (Exception e)
        {
            log.error("Error occurred while reading the lines in author-data-dump file: " + e.getMessage());
        }
    }
}
