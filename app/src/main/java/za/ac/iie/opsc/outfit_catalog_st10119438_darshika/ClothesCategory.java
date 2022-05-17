package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ClothesCategory extends AppCompatActivity {

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_category_main);

        logout = findViewById(R.id.logoutBtn);

    }

    public void logoutClick(View v)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ClothesCategory.this, FrontCoverActivity.class));
    }

}
