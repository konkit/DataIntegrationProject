package pl.edu.agh.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by konkit on 12.04.16.
 */
@Entity
public class Search implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String twitterHashtags;

    private String guardianTags;

    @OneToMany
    private Set<Tweet> tweets;

    @OneToMany
    private Set<GuardianArticle> guardianArticles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTwitterHashtags() {
        return twitterHashtags;
    }

    public void setTwitterHashtags(String twitterHashtags) {
        this.twitterHashtags = twitterHashtags;
    }

    public String getGuardianTags() {
        return guardianTags;
    }

    public void setGuardianTags(String guardianTags) {
        this.guardianTags = guardianTags;
    }
}
