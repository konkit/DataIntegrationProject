package pl.edu.agh.services.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;

import pl.edu.agh.entities.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import javax.persistence.EntityNotFoundException;

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

    private BasicClient client = null;

    static Logger logger = LoggerFactory.getLogger(TwitterService.class);

    public void fetchNow(Search search) {
        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setOAuthConsumerKey(consumerKey);
            cb.setOAuthConsumerSecret(consumerSecret);
            cb.setOAuthAccessToken(accessToken);
            cb.setOAuthAccessTokenSecret(accessSecret);

            Twitter twitter = new TwitterFactory(cb.build()).getInstance();

            Query query = new Query(search.getTwitterHashtags());
            QueryResult result = twitter.search(query);

            createTweets(search, result.getTweets());
            searchRepository.save(search);
        } catch (TwitterException e) {
            logger.info("Couldn't connect: " + e);
            e.printStackTrace();
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
