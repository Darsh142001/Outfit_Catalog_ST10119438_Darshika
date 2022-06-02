package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ClothesCategory extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
//@color/purple_500
    private FirebaseAuth mAuth;

    //Nav bar
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
        setTitle("O:Catalague");
        //getSupportActionBar().setTitle(Html.fromHtml("<font color=\"white\">" + getString(R.string.app_name) + "</font>"));


        //this method will load and display the category's that the user created in a recycler view.
        //(see Book app firebase | 03 Add book category | Android studio | java - Atif Pervaiz, 2021)
        loadCategories();

        //Navigation bar.
        toolbar = findViewById(R.id.nav_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = findViewById(R.id.drawer_layout);
        toggleOnOff = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        validateDate(); //Before the categories can be added, it has to be validated first.

    }

    private String newCategory; //declare new variable.

    public void validateDate()
{
     newCategory = clothesCat.getText().toString().trim();
    //validate if not empty:
    if(TextUtils.isEmpty(newCategory)) {
        Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show();
    }else{
        addCategoryToFirebase(); //If the input box is not empty, then proceed to adding the category to firebase.

    }
}
    //this method will add the category the user created to firebase and be stored under Categories.
   //(see Book app firebase | 03 Add book category | Android studio | java - Atif Pervaiz, 2021)
    private void addCategoryToFirebase()
    {
        FirebaseUser user = mAuth.getCurrentUser();
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
        hashMap.put("email", ""+user.getEmail()); //This will display the email of the logged in user. This will show which user added which category.

        //add to firebase db...Database root > Categories > categoryId > category info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories"); //All the categories created by the user will be stored under this path.
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

    //This method will basically bring back what is displayed in the database,
    // to be displayed in the activity_main_with_nav_bar activity in the recycler view.
    //Therefore, the user can see what categories they added.
    //Need to load only the categories of the logged in user (see Book app firebase | 03 Add book category | Android studio | java - Atif Pervaiz, 2021).
    private void loadCategories(){
        //init arraylist
        categoryArrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();

        //Get all categories from the firebase > Categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear arraylist before adding data into it
                categoryArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){ //A DataSnapshot instance contains data from a Firebase Database location. Any time you read Database data, you receive the data as a DataSnapshot.
                    ModelCategory modCategory = ds.getValue(ModelCategory.class);
                    if(user.getEmail().equals(modCategory.getEmail()))
                    {
                        //Add to arraylist
                        categoryArrayList.add(modCategory);
                    }
                    //I think the If statement should go here: We need to only bring back the categories for the user that is logged in.
                    //Therefore, only that specific user will see their categories that they added or add.

                }
                //setup adapter
                adapterCategory = new AdapterCategory(ClothesCategory.this, categoryArrayList);
                //set adapter to recyclerView
                recycleView.setAdapter(adapterCategory); //this will display the categories that the user added.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //The menu appears. But if the back button is pressed, the menu isn't closed as you would expect.
    //Instead, the app is exited. This onBackPressed will fix this issue.
    //(The Independent Institute of Education, 2022).
    @Override
    public void onBackPressed()
    {
        //if drawer is opened, close is.
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            //otherwise, let the super class handle it
            //super.onBackPressed();
        }
    }

    //This method used called only when the items from the nav bar are clicked on.
    //It will direct the user to the correct activity (The Independent Institute of Education, 2022).
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                //Logout of the app.
                Intent frontCOver = new Intent(this,FrontCoverActivity.class);
                frontCOver.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(frontCOver);
                break;
            case R.id.nav_addClothes:
                //Take them to the activity that will allow them to add clothes and select the category they would like to save that picture to.
                startActivity(new Intent(this, AddClothesToCategory.class));
                break;
            case R.id.nav_goal:
                /*Take them to the activity that will allow the user to give a set goal to the categories they created.
                //They will select the category from the dropdown that they would like to set a goal for,
                //Then they can set a number for that specific category. Example: They select pants as the category,
                //Then they need to give a goal, let's say 20. Therefore in order to reach their goal, they would have to
                //Take pictures of pants and store it under this category which will be displayed in a recycler view.
                //If the have 20 pictures of pants that they collected, then they reached their goal.
                 */
                startActivity(new Intent(this, SetUserGoals.class));
                break;
            case R.id.nav_viewClothes:
                startActivity(new Intent(this, ViewClothesActivity.class));
                break;

            case R.id.nav_profile:
                startActivity(new Intent(this, UserProfileActivity.class));
                break;
            case R.id.nav_game:
                startActivity(new Intent(this, GameActivity.class));
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START); //If they tap anywhere else on the screen, the nav bar will close.
        return true;
    }


}
