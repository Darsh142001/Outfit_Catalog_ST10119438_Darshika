package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterGoalSet extends RecyclerView.Adapter<AdapterGoalSet.MyViewHolder> {
    //This class will bring back the data from the firebase and display the data in a recycler view so that user
    //can see the goal they set for each category.
    //(see Book app firebase | 03 Add book category | Android studio | java - Atif Pervaiz, 2021)

    Context context;
    ArrayList<GoalSetForSelectedCategory>goalSetArrayList;

    public AdapterGoalSet(Context context, ArrayList<GoalSetForSelectedCategory> goalSetArrayList) {
        this.context = context;
        this.goalSetArrayList = goalSetArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_row_goals_category,parent, false); //activity_row_goals_category
        return new MyViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        GoalSetForSelectedCategory goalCategory = goalSetArrayList.get(position);
        String id = goalCategory.getId();
        String category = goalCategory.getCategory();
        String SetGoal = goalCategory.getSetGoal();
        String uid = goalCategory.getUid();
        long timestamp = goalCategory.getTimestamp();
        String email = goalCategory.getEmail();

        //Set data
        holder.selectedCategory.setText(category);
        holder.inputGoalNumber.setText(SetGoal);

        //Handle click delete
        holder.deleteGoalSet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //confirm delete dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this Goal")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                //begin delete
                                Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                                deleteCategory(goalCategory, holder);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                            }
                        })
                        .show();


            }
        });

    }


    private void deleteCategory(GoalSetForSelectedCategory goalCategory, AdapterGoalSet.MyViewHolder holder){
        //get id of category to delete
        String id = goalCategory.getId();
        //Firebase DB > Categories > categoryId
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Goals");
        ref.child(id)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //delete successfully
                        Toast.makeText(context, "Successfully deleted...", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed to delete
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public int getItemCount() {
        return goalSetArrayList.size();
    }


    //View holder class to hold UI views for row_category.xml.
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView selectedCategory;
        TextView inputGoalNumber;
        ImageButton deleteGoalSet;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            //init UI Views.
            selectedCategory = itemView.findViewById(R.id.categoryNameTv);
            inputGoalNumber = itemView.findViewById(R.id.setGoalNumberTV);
            deleteGoalSet = itemView.findViewById(R.id.deleteBtn);
        }
    }

}
