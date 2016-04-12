package pl.edu.agh.services.guardian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.*;
import org.springframework.stereotype.Service;
import pl.edu.agh.entities.*;
import twitter4j.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Mere on 2016-04-02.
 */
@Service
public class GuardianService {

    @Value("${guardian.key}")
    private String key;

    @Value("${guardian.api.endpoint}")
    private String apiEndpoint;

    static Logger logger = LoggerFactory.getLogger(GuardianService.class);

    @Autowired
    IGuardianRepository guardianRepository;

    @Autowired
    AuthorRepository authorRepository;

    public void fetchNow(Search search) {
        try {
            BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);

            String address = new String(apiEndpoint + "&api-key=" + key);

            URL url = new URL(address);
            Scanner scanner = new Scanner(url.openStream());
            String str = new String();
            while(scanner.hasNext()) {
                str += scanner.nextLine();
            }
            scanner.close();

            JSONObject jsonObject = new JSONObject(str);
            JSONObject jsonResponse = jsonObject.getJSONObject("response");
            JSONArray resultsArray = jsonResponse.getJSONArray("results");

            for(int i = 0; i < resultsArray.length(); i++) {
                JSONObject singleResult = resultsArray.getJSONObject(i);

                GuardianArticle article = new GuardianArticle();

                article.setWebTitle(singleResult.getString("webTitle"));
                article.setApiUrl(singleResult.getString("apiUrl"));
                article.setWebUrl(singleResult.getString("webUrl"));
                article.setSectionName(singleResult.getString("sectionName"));
                article.setPublicationDate(singleResult.getString("webPublicationDate"));

                article.setSearch(search);

                Author author = getAuthor();
                article.setAuthor(author);

                guardianRepository.save(article);
            }
        } catch(MalformedURLException e) {
            logger.error("Error fetching from Guardian!!!" + e.getMessage());
            e.printStackTrace();
        } catch(IOException e) {
            logger.error("Error fetching from Guardian!!!" + e.getMessage());
            e.printStackTrace();
        }
    }

    private Author getAuthor() {
        //TODO: TwitterService can serve as an example to fix it.
        return null;
    }
}

