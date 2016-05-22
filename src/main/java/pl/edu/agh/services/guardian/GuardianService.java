package pl.edu.agh.services.guardian;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.edu.agh.entities.*;
import twitter4j.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Value("${guardian.search}")
    private String apiSearch;

    @Value("${guardian.show_fields}")
    private String showFields;

    @Value("${guardian.show_tags}")
    private String showTags;

    @Value("${guardian.page}")
    private String page;

    static Logger logger = LoggerFactory.getLogger(GuardianService.class);

    @Autowired
    IGuardianRepository guardianRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    SearchRepository searchRepository;

    @Async
    public void fetchNow(Search search) {
        try {
            search.setTwitterSearchStatus("searching");
            searchRepository.save(search);

            BlockingQueue<String> queue = new LinkedBlockingQueue<>(10000);

            String guardianTagsStr = "";
            String[] splittedGuardianTags = search.getGuardianTags().split("\\s*,\\s*");
            for(String tag : splittedGuardianTags) {
                guardianTagsStr += tag + "%2C%20";
            }

            if(splittedGuardianTags.length != 0) {
                guardianTagsStr = guardianTagsStr.substring(0, guardianTagsStr.length() - 6);
            }

            int page_num = 1;
            int pages = 1;

            while(page_num <= pages) {
                String address = new String(apiEndpoint + apiSearch + showTags + showFields + page + page_num + "&q=" + guardianTagsStr + "&api-key=" + key);

                URL url = new URL(address);
                Scanner scanner = new Scanner(url.openStream());
                String str = new String();
                while(scanner.hasNext()) {
                    str += scanner.nextLine();
                }
                scanner.close();

                JSONObject jsonObject = new JSONObject(str);
                JSONObject jsonResponse = jsonObject.getJSONObject("response");
                pages = jsonResponse.getInt("pages");

                JSONArray resultsArray = jsonResponse.getJSONArray("results");

                for(int i = 0; i < resultsArray.length(); i++) {
                    try {
                        JSONObject singleResult = resultsArray.getJSONObject(i);

                        JSONObject fields = singleResult.getJSONObject("fields");

                        GuardianArticle article = new GuardianArticle();
                        article.setWebTitle(singleResult.getString("webTitle"));
                        article.setApiUrl(singleResult.getString("apiUrl"));
                        article.setWebUrl(singleResult.getString("webUrl"));
                        article.setSectionName(singleResult.getString("sectionName"));
                        article.setPublicationDate(singleResult.getString("webPublicationDate"));
                        //getting body (content) of the article
                        article.setText(fields.getString("body"));

                        article.setSearch(search);

                        //getting author (contributor) of the article
                        JSONArray tags = singleResult.getJSONArray("tags");
                        JSONObject contributorTag = null;

                        for(int j = 0; j < tags.length(); j++) {
                            JSONObject singleTag = tags.getJSONObject(j);
                            if(singleTag.getString("type").equals("contributor")) {
                                contributorTag = singleTag;
                                break;
                            }
                        }

                        if(contributorTag != null) {
                            Author author = getAuthor(contributorTag);
                            article.setAuthor(author);
                        }
                        guardianRepository.save(article);
                    } catch( DataIntegrityViolationException e ) {
                        System.out.println(e.getMessage());
                    }
                }

                page_num++;
                Thread.sleep(2500);
            }

            search.setTwitterSearchStatus("finished");
            searchRepository.save(search);

//        } catch(MalformedURLException e) {
//            logger.error("Error fetching from Guardian!!!" + e.getMessage());
//            e.printStackTrace();
        } catch(Exception e) {
            logger.error("Error fetching from Guardian!!!" + e.getMessage());
            e.printStackTrace();

            search.setTwitterSearchStatus("error");
            searchRepository.save(search);
        }
    }

    private Author getAuthor(JSONObject contributorTag) {

        String guardianName = contributorTag.getString("webTitle");
        String twitterName = "";

        try {
            twitterName = contributorTag.getString("twitterHandle");
        }
        catch(Exception ex) {

        }

        Author author = authorRepository.findByGuardianName(guardianName);

        //byc moze ma konto na Twitterze
        if(author == null) {
            if(twitterName != "") {
                author = authorRepository.findByTwitterName(twitterName);
            }


            if(author != null) {
                author.setGuardianName(guardianName);
            }
            else {
                author = Author.createFromGuardian(guardianName, twitterName);
            }

            authorRepository.save(author);
        }

        return author;
    }
}

