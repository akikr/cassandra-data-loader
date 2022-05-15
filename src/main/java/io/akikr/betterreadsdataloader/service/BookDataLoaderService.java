package io.akikr.betterreadsdataloader.service;

import io.akikr.betterreadsdataloader.db.entity.Book;
import io.akikr.betterreadsdataloader.db.repository.AuthorRepository;
import io.akikr.betterreadsdataloader.db.repository.BookRepository;
import io.akikr.betterreadsdataloader.utils.JsonStringParser;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

/**
 * @author ankit
 * @since 1.0
 * @implNote: A Book data loader service that parse the data from data-dump files and then loads into cassandra database
 */

@Slf4j
@Service
public class BookDataLoaderService
{
    /**
     * @implNote: The book data-dump file location.
     */
    @Value("${datadump.location.works}")
    private String bookDataDumpLocation;

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    /**
     * @implNote : Construction injection to Autowire the {@link BookRepository} & {@link AuthorRepository}.
     * @param bookRepository {@link BookRepository}
     * @param authorRepository {@link AuthorRepository}
     */
    @Autowired
    public BookDataLoaderService(BookRepository bookRepository, AuthorRepository authorRepository)
    {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    /**
     * @implNote : Book data initializer method that gets the data from data-dump files, parse it and then sends to
     * {@link BookRepository} to save it.
     */
    public void initWorks()
    {
        log.info("Book's data initializer started !!");
        log.debug("Reading the book data from : " + bookDataDumpLocation);
        Path path = Paths.get(bookDataDumpLocation);
        // To format the date of type: 2009-12-11T01:58:39.514198 => pattern: yyyy-MM-dd'T'HH:mm:ss.SSSSSS
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        try (Stream<String> lines = Files.lines(path))
        {
            log.info("Saving the book data...");
            lines.forEach(line -> {
                // Read and parse the lines
                String jsonString = line.substring(line.indexOf("{"));
                JSONObject jsonObject = new JsonStringParser().getJSONObjectFromString(jsonString);
                // Construct the Book object
                Book book = new Book();
                try
                {
                    book.setId(jsonObject.getString("key").replace("/works/", ""));
                }
                catch (JSONException e)
                {
                    log.error("Error occurred while setting the book's Id:" + e.getMessage());
                }
                try
                {
                    book.setName(jsonObject.getString("title"));
                }
                catch (JSONException e)
                {
                    log.error("Error occurred while setting the book's Title/Name: " + e.getMessage());
                }

                JSONObject descriptionJSONObj = jsonObject.optJSONObject("description");
                if (nonNull(descriptionJSONObj))
                    book.setDescription(descriptionJSONObj.optString("value"));

                JSONObject publishedJSONObj = jsonObject.optJSONObject("created");
                if (nonNull(publishedJSONObj))
                {
                    try
                    {
                        book.setPublishedDate(LocalDate.parse(publishedJSONObj.getString("value"), dateTimeFormatter));
                    }
                    catch (JSONException e)
                    {
                        log.error("Error occurred while setting the book's published date: " + e.getMessage());
                    }
                }

                JSONArray coverJSONArray = jsonObject.optJSONArray("covers");
                if(nonNull(coverJSONArray))
                {
                    List<String> coverIds = new ArrayList<>();
                    for (int i=0; i < coverJSONArray.length(); i++)
                    {
                        try
                        {
                            coverIds.add(coverJSONArray.getString(i));
                        }
                        catch (JSONException e)
                        {
                            log.error("Error occurred while setting the book's coverId: " + e.getMessage());
                        }
                    }
                    book.setCoverIds(coverIds);
                }

                try
                {
                    JSONArray authorsJSONArray = jsonObject.getJSONArray("authors");
                    if (authorsJSONArray != null)
                    {
                        List<String> authorIds = new ArrayList<>();
                        for (int i = 0; i < authorsJSONArray.length(); i++)
                        {
                            authorIds.add(authorsJSONArray.getJSONObject(i).getJSONObject("author").getString("key")
                                    .replace("/authors/", ""));
                        }
                        book.setAuthorIds(authorIds);
                        // Getting the authorNames form authorRepository by authorIds
                        List<String> authorNames = authorIds.stream().map(authorRepository::findById)
                                .map(optionalAuthor -> {
                                    if (optionalAuthor.isEmpty()) return "Unknown Author";
                                    return optionalAuthor.get().getName();
                                }).collect(Collectors.toList());

                        book.setAuthorNames(authorNames);
                    }
                }
                catch (JSONException e)
                {
                    log.error("Error occurred while setting the book's authorId: " + e.getMessage());
                }
                // Persists the book object into BookRepository
                log.debug("Saving the book: " + book.getName() + " ...");
                bookRepository.save(book);
            });
        }
        catch (Exception e)
        {
            log.error("Error occurred while reading the lines in works/book-data-dump file: " + e.getMessage());
        }
        finally
        {
            log.info("Book's data service completed !!");
        }
    }
}
