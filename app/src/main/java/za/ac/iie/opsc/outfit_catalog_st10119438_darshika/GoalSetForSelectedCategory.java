package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

public class GoalSetForSelectedCategory {

    String id, category, SetGoal, uid,email;
    long timestamp;

    public GoalSetForSelectedCategory()
    {

    }

    public GoalSetForSelectedCategory(String id, String category, String SetGoal, String uid, long timestamp, String email)
    {
        this.id = id;
        this.category = category;
        this.SetGoal = SetGoal;
        this.uid = uid;
        this.timestamp = timestamp;
        this.email = email;
    }

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

    public String getSetGoal() {
        return SetGoal;
    }

    public void setSetGoal(String setGoal) {
        SetGoal = setGoal;
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
