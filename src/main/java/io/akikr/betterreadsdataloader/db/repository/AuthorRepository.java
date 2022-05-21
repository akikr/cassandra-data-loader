package io.akikr.betterreadsdataloader.db.repository;

import io.akikr.betterreadsdataloader.db.entity.Author;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

/**
 * @implNote: The author repository to do CURD operation on author table in cassandra database
 * @author ankit
 * @since 1.0
 */

@Repository
public interface AuthorRepository extends CassandraRepository<Author, String>
{

}
