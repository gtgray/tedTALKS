package tk.atna.tedtalks.model;

/**
 * Internal representation of a single talk
 */
public class Talk {

    private int id;
    private String name;
    private String desc;
    private String publishedAt;
    private String recordedAt;
    private String updatedAt;
    private int viewedCount;
    private int emailedCount;
    private int commentedCount;
    private String imageUrl;
    private String videoLowUrl;
    private String videoHighUrl;


    public Talk(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Talk setName(String name) {
        this.name = name;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Talk setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public Talk setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public Talk setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Talk setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public int getViewedCount() {
        return viewedCount;
    }

    public Talk setViewedCount(int viewedCount) {
        this.viewedCount = viewedCount;
        return this;
    }

    public int getEmailedCount() {
        return emailedCount;
    }

    public Talk setEmailedCount(int emailedCount) {
        this.emailedCount = emailedCount;
        return this;
    }

    public int getCommentedCount() {
        return commentedCount;
    }

    public Talk setCommentedCount(int commentedCount) {
        this.commentedCount = commentedCount;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Talk setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getVideoLowUrl() {
        return videoLowUrl;
    }

    public Talk setVideoLowUrl(String videoLowUrl) {
        this.videoLowUrl = videoLowUrl;
        return this;
    }

    public String getVideoHighUrl() {
        return videoHighUrl;
    }

    public Talk setVideoHighUrl(String videoHighUrl) {
        this.videoHighUrl = videoHighUrl;
        return this;
    }

}
