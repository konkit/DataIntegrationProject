package pl.edu.agh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.web.bind.annotation.*;
import pl.edu.agh.entities.Search;
import pl.edu.agh.entities.SearchRepository;
import pl.edu.agh.services.guardian.GuardianService;
import pl.edu.agh.services.twitter.TwitterService;

import javax.validation.Valid;

@RestController
@RequestMapping("/searches")
public class SearchController {

    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private GuardianService guardianService;

    @RequestMapping(method= RequestMethod.POST)
    public Search create(@RequestBody @Valid Search search) {
        return this.searchRepository.save(search);
    }

    @RequestMapping(method=RequestMethod.GET)
    public Iterable<Search> list() {
        return this.searchRepository.findAll();
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public Search get(@PathVariable("id") long id) {
        return this.searchRepository.findOne(id);
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