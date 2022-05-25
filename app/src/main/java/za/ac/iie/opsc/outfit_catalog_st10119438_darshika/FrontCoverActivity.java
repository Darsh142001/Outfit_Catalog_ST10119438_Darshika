package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class FrontCoverActivity extends AppCompatActivity implements View.OnClickListener{

    Button signUp;
    Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontcover);

        signUp = findViewById(R.id.signUpBtn);
        signUp.setOnClickListener(this);

        signIn = findViewById(R.id.signInBtn);
        signIn.setOnClickListener(this);

    }

    //This onClick method will direct the user to the correct activity depending if they want to register or sign in.
    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId())
        {
            case R.id.signUpBtn:
                startActivity(new Intent(this, RegisterUser.class)); //This button will take them to the register activity.
                break;

            case R.id.signInBtn:
                startActivity(new Intent(this, MainActivity.class)); //This page will take them to the login activity/main activity.
                break;
        }
    }

}
