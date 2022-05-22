package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder> {
//Must check
    Context context;
    ArrayList<ModelCategory> categoryList;

    public AdapterCategory(Context context, ArrayList<ModelCategory> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_row_category,parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ModelCategory modCategory = categoryList.get(position);
        String id = modCategory.getId();
        String category = modCategory.getCategory();
        String uid = modCategory.getUid();
        long timestamp = modCategory.getTimestamp();

        //Set data
        holder.categoryName.setText(category);
        //holder.categoryName.setText(modCategory.getCategory());

        //Handle click delete
        holder.deleteCategory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //confirm delete dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this category")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                            public void onClick(DialogInterface dialog, int which){
                            //begin delete
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                            deleteCategory(modCategory, holder);

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

    private void deleteCategory(ModelCategory modCategory, MyViewHolder holder){
        //get id of category to delete
        String id = modCategory.getId();
        //Firebase DB > Categories > categoryId
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
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
        return categoryList.size();
    }

    //View holder class to hold UI views for row_category.xml.
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView categoryName;
        ImageButton deleteCategory;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            //init UI Views.
            categoryName = itemView.findViewById(R.id.displayCategoryTv);
            deleteCategory = itemView.findViewById(R.id.deleteBtn);
        }
    }

}
