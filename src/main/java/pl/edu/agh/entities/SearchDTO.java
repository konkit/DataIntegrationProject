package pl.edu.agh.entities;

/**
 * Created by konkit on 08.05.16.
 */
public class SearchDTO {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String twitterHashtags;

    private String guardianTags;

    private Long tweetsCount;

    private Long guardianArticlesCount;

    private String twitterSearchStatus;

    private String guardianSearchStatus;

    public void setFromSearch(Search search) {
        id = search.getId();

        twitterHashtags = search.getTwitterHashtags();
        guardianTags = search.getGuardianTags();

        twitterSearchStatus = search.getTwitterSearchStatus();
        guardianSearchStatus = search.getGuardianSearchStatus();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(Long tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public Long getGuardianArticlesCount() {
        return guardianArticlesCount;
    }

    public void setGuardianArticlesCount(Long  guardianArticlesCount) {
        this.guardianArticlesCount = guardianArticlesCount;
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

    public String getTwitterSearchStatus() {
        return twitterSearchStatus;
    }

    public void setTwitterSearchStatus(String twitterSearchStatus) {
        this.twitterSearchStatus = twitterSearchStatus;
    }

    public String getGuardianSearchStatus() {
        return guardianSearchStatus;
    }

    public void setGuardianSearchStatus(String guardianSearchStatus) {
        this.guardianSearchStatus = guardianSearchStatus;
    }
}
