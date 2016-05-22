package pl.edu.agh.services.twitter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import pl.edu.agh.entities.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

@Service
public class TwitterService {

    @Value("${twitter.consumer.key}")
    private String consumerKey;

    @Value("${twitter.consumer.secret}")
    private String consumerSecret;

    @Value("${twitter.access.token}")
    private String accessToken;

    @Value("${twitter.access.secret}")
    private String accessSecret;

    @Autowired
    TweetRepository tweetRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    SearchRepository searchRepository;

    @Value("${threads.count}")
    private int numOfThreads;

    static Logger logger = LoggerFactory.getLogger(TwitterService.class);

    @Async
    public void fetchNow(Search search) {
        try {
            search.setTwitterSearchStatus("searching");
            searchRepository.save(search);

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(consumerKey);
            cb.setOAuthConsumerSecret(consumerSecret);
            cb.setOAuthAccessToken(accessToken);
            cb.setOAuthAccessTokenSecret(accessSecret);

            Twitter twitter = new TwitterFactory(cb.build()).getInstance();

            String twitterHashtags = search.getTwitterHashtags();
            for(String hashtag : twitterHashtags.split(",")) {

                Query query = new Query(hashtag);
                long lastID = Long.MAX_VALUE;
                query.setCount(100);

                while (true) {
                    Thread.sleep(2500);

                    QueryResult result = twitter.search(query);
                    List<Status> tweets = result.getTweets();

                    createTweets(search, tweets);
                    searchRepository.save(search);

                    if( tweets.size() < 100 ) {
                        break;
                    }

                    for( Status cntTweet : tweets ) {
                        if(cntTweet.getId() < lastID) {
                            lastID = cntTweet.getId();
                        }
                    }

                    query.setMaxId(lastID-1);
                }
            }


            search.setTwitterSearchStatus("finished");
            searchRepository.save(search);
        } catch( Exception e) {
            logger.error(e.getClass().toString());
            logger.error(e.getMessage());
            e.printStackTrace();

            search.setTwitterSearchStatus("error");
            searchRepository.save(search);
        }
    }

    private void createTweets(Search search, List<Status> tweetsData) {
        for(Status status: tweetsData) {
            try {
                Tweet tweet = new Tweet();

                tweet.setTweetId(status.getId());
                tweet.setCreated_at(status.getCreatedAt());
                tweet.setLang(status.getLang());
                tweet.setText(status.getText());
                tweet.setTimestamp(status.getCreatedAt().getTime());
                tweet.setCoordinates(status.getGeoLocation());

                Author author = getAuthor(status.getUser());
                tweet.setAuthor(author);
                tweet.setSearch(search);

                tweetRepository.save(tweet);
            } catch( DataIntegrityViolationException e ) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Author getAuthor(User user) {
        Author author = authorRepository.findByTwitterName(user.getScreenName());

        if( author == null ) {
            author = Author.createFromTwitter(user);
            authorRepository.save(author);
        }

        return author;
    }
}
