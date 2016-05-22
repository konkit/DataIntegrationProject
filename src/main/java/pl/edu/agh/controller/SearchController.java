package pl.edu.agh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.entities.*;
import pl.edu.agh.services.guardian.GuardianService;
import pl.edu.agh.services.twitter.TwitterService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/searches")
public class SearchController {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private IGuardianRepository guardianRepository;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private GuardianService guardianService;

    @RequestMapping(method= RequestMethod.POST)
    public Search create(@RequestBody @Valid Search search) {
        return this.searchRepository.save(search);
    }

    @RequestMapping(value = "/fetchall", method = RequestMethod.GET)
    public void startCollectingAll() {
        Iterable<Search> searchList = this.searchRepository.findAll();
        for(Search search : searchList) {
            twitterService.fetchNow(search);
            guardianService.fetchNow(search);
        }
    }

    @RequestMapping(method=RequestMethod.GET)
    public Iterable<Search> list() {
        return this.searchRepository.findAll();
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public SearchDTO get(@PathVariable("id") long id) {
        Search search = this.searchRepository.findOne(id);
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setFromSearch(search);
        searchDTO.setTweetsCount(tweetRepository.countBySearch(search));
        searchDTO.setGuardianArticlesCount(guardianRepository.countBySearch(search));
        return searchDTO;
    }

    @RequestMapping(value="/{id}", method=RequestMethod.PUT)
    public Search update(@PathVariable("id") long id, @RequestBody @Valid Search search) {
        return searchRepository.save(search);
    }

    @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
    public ResponseEntity<Boolean> delete(@PathVariable("id") long id) {
        this.searchRepository.delete(id);
        return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/fetchNow", method = RequestMethod.GET)
    public void startCollecting(@PathVariable("id") long id) {
        Search search = this.searchRepository.findOne(id);
        twitterService.fetchNow(search);
        guardianService.fetchNow(search);
    }
}
