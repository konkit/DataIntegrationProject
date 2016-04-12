package pl.edu.agh.entities;

import java.util.Date;

import twitter4j.GeoLocation;

import javax.persistence.*;

@Entity
public class Tweet {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String text;
    private Long tweetId;
    private Date created_at;
    private Long timestamp;
    private String lang;
    private GeoLocation coordinates;

    @ManyToOne
    Author author;

    @ManyToOne
    private Search search;

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getTweetId() {
        return tweetId;
    }

    public void setTweetId(Long tweetId) {
        this.tweetId = tweetId;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public GeoLocation getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(GeoLocation coordinates) {
        this.coordinates = coordinates;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }
}
