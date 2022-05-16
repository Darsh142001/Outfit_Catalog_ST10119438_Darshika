package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

public class User {

    //this is where all the users details will be stored.
    public String userFullName, email;

    //Create a constructor
    public User(){}

    //This will store the username, userSurname, and userEmail.
    public User(String userFullName, String email)
    {
        this.userFullName = userFullName;
        this.email = email;
    }
}
