package pl.edu.agh.entities;

import org.springframework.data.repository.CrudRepository;

public interface TweetRepository extends CrudRepository<Tweet, Long> {
    Long countBySearch(Search search);
}
