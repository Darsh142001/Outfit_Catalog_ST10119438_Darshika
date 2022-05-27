package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewGoals extends AppCompatActivity {

    //RecyclerView displayGoals;
    RecyclerView displayGoalsRecyclerView;
    private FirebaseAuth mAuth;

    //RecyclerView displayGoalsRecyclerView;

    //ArrayList to store goals
    private ArrayList<GoalSetForSelectedCategory> goalsSetArrayList;
    AdapterGoalSet adapterGoalSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goals);

        //loadGoalSet();

        displayGoalsRecyclerView = findViewById(R.id.goalsRecyclerV);
    }


    //Load the goals set into the recycler view:
//NEED TO FIX!!!!!!!!!!!!
    //Probably need to check the activity_row_goals_category. Maybe the setup is incorrect.
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
                adapterGoalSet = new AdapterGoalSet(ViewGoals.this, goalsSetArrayList);
                //set adapter to recyclerView
                displayGoalsRecyclerView.setAdapter(adapterGoalSet); //this will display the categories that the user added.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
