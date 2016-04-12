package pl.edu.agh.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import twitter4j.User;

/**
 * Created by konkit on 11.04.16.
 */
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private String twitterName;

    private String guardianName;

    public static Author createFromTwitter(User user) {
        Author author = new Author();
        author.twitterName = user.getScreenName();
        return author;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
