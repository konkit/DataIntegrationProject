package pl.edu.agh.entities;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by konkit on 12.04.16.
 */
public interface AuthorRepository extends CrudRepository<Author, Long> {
    Author findByTwitterName(@Param("twitter_name") String twitterName);
    Author findByGuardianName(@Param("guardian_name") String guardianName);
}
