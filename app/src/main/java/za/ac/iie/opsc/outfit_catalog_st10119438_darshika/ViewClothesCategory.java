package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

public class ViewClothesCategory {

    String id, uid, name, description, category, url;
    long timestamp;

    public ViewClothesCategory()
    {

    }

    public ViewClothesCategory(String id, String uid, String name, String description, String category, String url, long timestamp) {
        this.id = id;
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.category = category;
        this.url = url;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
