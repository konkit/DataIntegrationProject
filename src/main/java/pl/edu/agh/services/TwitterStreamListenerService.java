package pl.edu.agh.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.entities.Tweet;

import pl.edu.agh.entities.TweetRepository;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;

import com.twitter.hbc.twitter4j.handler.StatusStreamHandler;
import com.twitter.hbc.twitter4j.message.DisconnectMessage;
import com.twitter.hbc.twitter4j.message.StallWarningMessage;

/**
 * Created by konkit on 01.04.16.
 */
@Service
public class TwitterStreamListenerService {
    static Logger log = LoggerFactory.getLogger(TwitterStreamListenerService.class);

    @Autowired
    TweetRepository tweetRepository;

    public StatusListener createListener() {

        return new StatusStreamHandler() {

            @Override
            public void onException(Exception arg0) {
            }

            @Override
            public void onTrackLimitationNotice(int arg0) {
                log.info("onTrackLimitationNotice: {}", arg0);
            }

            @Override
            public void onStatus(Status status) {
                Tweet tweet = new Tweet();
                tweet.setTweetId(status.getId());
                tweet.setCreated_at(status.getCreatedAt());
                tweet.setLang(status.getLang());
                tweet.setText(status.getText());
                tweet.setTimestamp(status.getCreatedAt().getTime());
                tweet.setCoordinates(status.getGeoLocation());
                tweet.setScreenName(status.getUser().getScreenName());

                System.out.println(tweet.getText() + "\n\n\n");

                tweetRepository.save(tweet);
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                log.warn("{}", arg0);
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // A location was deleted
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice arg0) {
                // A status was deleted
            }

            @Override
            public void onUnknownMessageType(String arg0) {
            }

            @Override
            public void onStallWarningMessage(StallWarningMessage arg0) {
                log.warn("{}", arg0);
            }

            @Override
            public void onDisconnectMessage(DisconnectMessage arg0) {
                log.warn("{}", arg0);
            }
        };

    }
}
