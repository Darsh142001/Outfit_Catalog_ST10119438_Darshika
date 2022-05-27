package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SetUserGoals extends AppCompatActivity implements View.OnClickListener{
//This class is will allow users to select their category and set a goal for the specific category they selected.
//So basically later on, it will allow users to set goals for each category.
//Example: Lets say user selects pants as the category, and the set goal is 20,
//In order for the user to reach their goal, they would have to have a list/recycler view
//of 20 pictures of pants saved.
    TextView pickedCategory;
    EditText goalSetNumber;
    RecyclerView displayGoalsRecyclerView;


    ArrayList<ModelCategory> categoryArrayList;
    AdapterGoalSet adapterGoalSet;

    ImageButton backToMain;

    private FirebaseAuth mAuth;

    //ArrayList to store goals
    private ArrayList<GoalSetForSelectedCategory> goalsSetArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_set_goals);
        mAuth = FirebaseAuth.getInstance();

        //This will load all the categories the user added earlier in the dropdown view.
        loadCategories();

        loadGoalSet(); //did not work

        backToMain = findViewById(R.id.backToMainBtn);
        backToMain.setOnClickListener(this);

        pickedCategory = findViewById(R.id.pickCategory);
        goalSetNumber = findViewById(R.id.goalNumberET);

        displayGoalsRecyclerView = findViewById(R.id.goalsRecyclerV);

    }
//If user does not want to set a goal, the back image button will take them back to the activity_main_with_nav_drawer page.
    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId()) {
            case R.id.backToMainBtn:
                startActivity(new Intent(this, ClothesCategory.class)); //This will go back to the activity with the nav bar.
                break;
        }
    }

    //This button will allow the user to select the items (categories created earlier) from the dropdown list
    public void pickCategoryClick(View V)
    {
        categoryPickDialog(); //When the user has selected the category they want, it will display in the textview.
    }

    //This method will basically bring back what is displayed in the database,
    // to be displayed in the activity_main_with_nav_bar activity in the recycler view.
    //Therefore, the user can see what categories they added.
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

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //This will take the data under Goals and display the category chosen and its set goal.
    //This method will take the selected category from the dropdown menu and display it in the textview.
    public void categoryPickDialog()
    {
        //get string array of categories from arraylist
        String [] categoriesArray = new String[categoryArrayList.size()];
        for(int i=0; i < categoryArrayList.size(); i++){
            categoriesArray[i] = categoryArrayList.get(i).getCategory();
        }

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Category")
                .setItems(categoriesArray, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        //handle item click
                        //get clicked item from list
                        String category = categoriesArray[which];
                        //set to category textview
                        pickedCategory.setText(category);

                        //Log.d(TAG, "onClick: Selected Category: "+category);
                    }
                })
                .show();
    }


    public void addSetGoalClick(View v)
    {
        validateDate();
    }

    private String selectedCategory, inputNumber;
    public void validateDate()
    {
        selectedCategory = pickedCategory.getText().toString().trim();
        inputNumber = goalSetNumber.getText().toString().trim();

        //validate if not empty:
        //TextUtils.isEmpty(newCategory)
        //newCategory.isEmpty()
        if(TextUtils.isEmpty(selectedCategory)) {
            Toast.makeText(this, "Please enter category", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(inputNumber)){
            Toast.makeText(this, "Please set goal number", Toast.LENGTH_SHORT).show();
        }
        else{
          addUserSetGoalToFirebase(); //This will add the selected category and the set number

        }
    }

    public void addUserSetGoalToFirebase()
    {
        //get timestamp.
        long timestamp = System.currentTimeMillis();
        FirebaseUser user = mAuth.getCurrentUser();
        //setup info to add in firebase db.
    /*
    The java.util.HashMap.put() method of HashMap is used to insert a mapping into a map.
    This means we can insert a specific key and the value it is mapping to into a particular map.
    If an existing key is passed then the previous value gets replaced by the new value.
    If a new pair is passed, then the pair gets inserted as a whole.
     */
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", ""+timestamp);
        hashMap.put("category", ""+selectedCategory);
        hashMap.put("SetGoal", ""+inputNumber);
        hashMap.put("timestamp", timestamp);
        hashMap.put("uid", ""+mAuth.getUid());
        hashMap.put("email", ""+user.getEmail());

        //add to firebase db...Database root > Categories > categoryId > category info
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Goals");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // category added successfully
                        Toast.makeText(SetUserGoals.this, "Goal set added successfully", Toast.LENGTH_SHORT).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Category failed to add.
                        Toast.makeText(SetUserGoals.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    //Load the goals set into the recycler view:
    private void loadGoalSet()
    {
        goalsSetArrayList = new ArrayList<>();
        FirebaseUser user = mAuth.getCurrentUser();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Goals");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //clear arraylist before adding data into it
                goalsSetArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){ //A DataSnapshot instance contains data from a Firebase Database location. Any time you read Database data, you receive the data as a DataSnapshot.
                    GoalSetForSelectedCategory goalCategory = ds.getValue(GoalSetForSelectedCategory.class);
                    if(user.getEmail().equals(goalCategory.getEmail()))
                    {
                        //Add to arraylist
                        goalsSetArrayList.add(goalCategory);
                    }
                    //I think the If statement should go here: We need to only bring back the categories for the user that is logged in.
                    //Therefore, only that specific user will see their categories that they added or add.

                }
                //setup adapter
                adapterGoalSet = new AdapterGoalSet(SetUserGoals.this, goalsSetArrayList);
                //set adapter to recyclerView
                displayGoalsRecyclerView.setAdapter(adapterGoalSet); //this will display the categories that the user added.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}
