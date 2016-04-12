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
import org.springframework.stereotype.Service;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;

import pl.edu.agh.entities.TweetRepository;
import twitter4j.StatusListener;

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
    TwitterStreamListenerService twitterStreamListenerService;

    @Value("${threads.count}")
    private int numOfThreads;

    private BasicClient client = null;

    static Logger logger = LoggerFactory.getLogger(TwitterService.class);

    public void start() {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);

        StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
        List<String> keywords = new ArrayList<>();
        keywords.add("refugees");
        endpoint.trackTerms(keywords);

        Authentication auth = new OAuth1(consumerKey, consumerSecret, accessToken, accessSecret);

        client = new ClientBuilder()
                    .hosts(Constants.STREAM_HOST)
                    .endpoint(endpoint)
                    .authentication(auth)
                    .processor(new StringDelimitedProcessor(queue))
                    .build();

        ExecutorService service = Executors.newFixedThreadPool(numOfThreads);

        List<StatusListener> listeners = new ArrayList<>();
        listeners.add(twitterStreamListenerService.createListener());

        Twitter4jStatusClient t4jClient = new Twitter4jStatusClient(client, queue, listeners, service);

        t4jClient.connect();
        for (int threads = 0; threads < numOfThreads; threads++) {
            t4jClient.process();
        }
    }

    public void stop() {
        client.stop();
    }

}
