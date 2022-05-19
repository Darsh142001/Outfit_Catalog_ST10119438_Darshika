package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class ClothesCategory extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {



    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleOnOff;
    private NavigationView navigationView;

    //Clothes category
    private ImageView img_pants;
    private ImageView img_shirts;
    private ImageView img_tie;
    private ImageView img_hoodie;
    private ImageView img_dress;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_nav_drawer);

        //logout = findViewById(R.id.logoutBtn);

        toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggleOnOff = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggleOnOff);
        toggleOnOff.syncState();

        //Navigation Bar.
        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        //Clothing category.
        img_pants = findViewById(R.id.img_pants);
        img_shirts = findViewById(R.id.img_tshirt);
        img_hoodie = findViewById(R.id.img_hoodie);
        img_tie = findViewById(R.id.img_tie);
        img_dress = findViewById(R.id.img_dress);

    }
/*
    public void logoutClick(View v)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ClothesCategory.this, FrontCoverActivity.class));
    }
*/
    //The menu appears. But if the back button is pressed, the menu isn't closed as you would expect.
    //Instead, the app is exited. This onBackPressed will fix this issue.
    @Override
    public void onBackPressed()
    {
        //if drawer is opened, close is.
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            //otherwise, let the super class handle it
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,  FrontCoverActivity.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
