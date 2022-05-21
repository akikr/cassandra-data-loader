package io.akikr.betterreadsdataloader.db.repository;

import io.akikr.betterreadsdataloader.db.entity.Book;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * @implNote: The book repository to do CURD operation on book table in cassandra database
 * @author ankit
 * @since 1.0
 */

@Repository
public interface BookRepository extends CassandraRepository<Book, String>
{

}
