package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton backToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        backToMain = findViewById(R.id.backBtn);
        backToMain.setOnClickListener(this);
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


    @Override
    public void onBackPressed() //Therefore user cannot press the back button on their phone. they have to use the icon.
    {
        // super.onBackPressed();
    }


}
