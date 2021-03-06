package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

public class ModelCategory {

    //Declare variables.
    String id, category, uid, email;
    long timestamp;

    //Constructor empty required for firebase
    public ModelCategory()
    {

    }
//Parametrized constructor:
    public ModelCategory(String id, String category, String uid, long timestamp, String email) {
        this.id = id;
        this.category = category;
        this.uid = uid;
        this.timestamp = timestamp;
        this.email = email;
    }

    //Getters & setters:

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail(){return email;}
    public void setEmail(){this.email = email;}
}
