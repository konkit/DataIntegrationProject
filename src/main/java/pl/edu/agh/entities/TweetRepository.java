package pl.edu.agh.entities;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

public interface TweetRepository extends MongoRepository<Tweet, String> {

//    Tweet findByTweetId(@Param("tweetId") Long tweetId);
}
