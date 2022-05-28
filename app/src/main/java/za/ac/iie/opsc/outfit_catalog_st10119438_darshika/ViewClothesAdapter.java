package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewClothesAdapter extends RecyclerView.Adapter<ViewClothesAdapter.MyViewHolder> {

    Context context;
    ArrayList<ViewClothesCategory> clothesArrayList;

    public ViewClothesAdapter(Context context, ArrayList<ViewClothesCategory> clothesArrayList) {
        this.context = context;
        this.clothesArrayList = clothesArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_selected_category,parent, false); //activity_row_goals_category
        return new ViewClothesAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewClothesAdapter.MyViewHolder holder, int position) {


        ViewClothesCategory clothesCat = clothesArrayList.get(position);
        String id = clothesCat.getId();
        String category = clothesCat.getCategory();
        String name = clothesCat.getName();
        String description = clothesCat.getDescription();
       //String url = clothesCat.getUrl();
        String uid = clothesCat.getUid();
        long timestamp = clothesCat.getTimestamp();


        //Set data
        Picasso.with(context)
        .load(clothesCat.getUrl())
        .fit()
        .centerCrop()
                .into(holder.imageOfClothing);

        holder.nameOfClothing.setText(name);
        holder.descriptionOfClothing.setText(description);

        //Handle click delete
        holder.deleteClothes.setOnClickListener(new View.OnClickListener(){
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
                                deleteCategory(clothesCat, holder);

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

    private void deleteCategory(ViewClothesCategory clothesCat, ViewClothesAdapter.MyViewHolder holder){
        //get id of category to delete
        String id = clothesCat.getId();
        //Firebase DB > Categories > categoryId
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Clothes");
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
        return clothesArrayList.size();
    }


    //View holder class to hold UI views for row_category.xml.
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageOfClothing;
        TextView nameOfClothing;
        TextView descriptionOfClothing;
        ImageButton deleteClothes;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            //init UI Views.
            imageOfClothing = itemView.findViewById(R.id.image);
            nameOfClothing = itemView.findViewById(R.id.clothesName);
            descriptionOfClothing = itemView.findViewById(R.id.clothesDescrip);
            deleteClothes = itemView.findViewById(R.id.deleteClothesBtn);
        }
    }

}
