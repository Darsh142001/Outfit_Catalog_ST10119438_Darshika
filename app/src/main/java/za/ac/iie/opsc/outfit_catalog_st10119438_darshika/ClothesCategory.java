package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ClothesCategory extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggleOnOff;
    private NavigationView navigationView;

    //Clothes category
    Button addCategory;
    EditText clothesCat;
    RecyclerView recycleView;

    //ArrayList to store category
    private ArrayList<ModelCategory> categoryArrayList;

    //Adapter
    private AdapterCategory adapterCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_with_nav_drawer);
        mAuth = FirebaseAuth.getInstance();

        //this method will load and display the category's that the user created in a recycler view.
        loadCategories();

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
        clothesCat = findViewById(R.id.categoryNameEt);
        recycleView = findViewById(R.id.categoryRecyclerV);
        addCategory = findViewById(R.id.addCategoryBtn);


    }

    //This button will add the users categories that they add to their profile, will be added into the database as well.
    public void addCategoryClick(View v)
    {
        validateDate();

    }

    private String newCategory;

    public void validateDate()
{
     newCategory = clothesCat.getText().toString().trim();
    //validate if not empty:
    //TextUtils.isEmpty(newCategory)
    //newCategory.isEmpty()
    if(TextUtils.isEmpty(newCategory)) {
        Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show();
    }else{
        addCategoryToFirebase();

    }
}

private void addCategoryToFirebase()
{
    //get timestamp.
    long timestamp = System.currentTimeMillis();

    //setup info to add in firebase db.
    /*
    The java.util.HashMap.put() method of HashMap is used to insert a mapping into a map.
    This means we can insert a specific key and the value it is mapping to into a particular map.
    If an existing key is passed then the previous value gets replaced by the new value.
    If a new pair is passed, then the pair gets inserted as a whole.
     */
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("id", ""+timestamp);
    hashMap.put("category", ""+newCategory);
    hashMap.put("timestamp", timestamp);
    hashMap.put("uid", ""+mAuth.getUid());

    //add to firebase db...Database root > Categories > categoryId > category info
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
    ref.child(""+timestamp)
            .setValue(hashMap)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    // category added successfully
                    Toast.makeText(ClothesCategory.this, "Category added successfully", Toast.LENGTH_SHORT).show();


                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Category failed to add.
                    Toast.makeText(ClothesCategory.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
}

private void loadCategories(){
        //init arraylist
    categoryArrayList = new ArrayList<>();

        //Get all categories from the firebase > Categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear arraylist before adding data into it
                categoryArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){ //A DataSnapshot instance contains data from a Firebase Database location. Any time you read Database data, you receive the data as a DataSnapshot.
                    ModelCategory modCategory = ds.getValue(ModelCategory.class);

                    //Add to arraylist
                    categoryArrayList.add(modCategory);
                }
                //setup adapter
                adapterCategory = new AdapterCategory(ClothesCategory.this, categoryArrayList);
                //set adapter to recyclerView
                recycleView.setAdapter(adapterCategory);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}

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
            case R.id.nav_addClothes:
                startActivity(new Intent(this, AddClothesToCategory.class));
                break;
            case R.id.nav_goal:
                startActivity(new Intent(this, SetUserGoals.class));
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
