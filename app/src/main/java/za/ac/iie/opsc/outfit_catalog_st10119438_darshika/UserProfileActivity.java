package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton backToMain;

    FirebaseAuth mAuth;

    TextView fullName;
    TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);
        mAuth = FirebaseAuth.getInstance();

        backToMain = findViewById(R.id.backBtn);
        backToMain.setOnClickListener(this);

        fullName = findViewById(R.id.displayUserNameTV);
        email = findViewById(R.id.displayUserEmailTV);

        //loadUserDetails();
    }

    //This method is called only when the user wants to go back to the activity_main_with_nav_bar.
    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId()) {
            case R.id.backBtn:
                startActivity(new Intent(this, ClothesCategory.class)); //This will go back to the activity with the nav bar.
                break;
        }
    }

    //Need to fix!!!
    String userFullName, userEmail;
    public void loadUserDetails()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        userFullName = fullName.getText().toString().trim();
        userEmail = email.getText().toString().trim();

        userFullName = user.getDisplayName();
        userEmail = user.getEmail();


    }


    @Override
    public void onBackPressed() //Therefore user cannot press the back button on their phone. they have to use the icon.
    {
        // super.onBackPressed();
    }


}
