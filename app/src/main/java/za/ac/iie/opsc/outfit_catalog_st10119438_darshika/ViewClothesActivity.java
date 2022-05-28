package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewClothesActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton backToMain;
    ArrayList<ModelCategory> categoryArrayList;
    FirebaseAuth firebaseAuth;

    TextView pickCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_clothes);
        firebaseAuth = FirebaseAuth.getInstance();

        loadCategories();

        pickCat = findViewById(R.id.pickCategoryToView);

        backToMain = findViewById(R.id.backToMainBtn);
        backToMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId()) {
            case R.id.backToMainBtn:
                startActivity(new Intent(this, ClothesCategory.class)); //This will go back to the activity with the nav bar.
                break;
        }
    }

    public void pickCategoryToViewClick(View v)
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
                        pickCat.setText(category);

                        //Log.d(TAG, "onClick: Selected Category: "+category);
                    }
                })
                .show();
    }

    public void loadCategories()
    {

        //log.d(TAG, "LoadClotheCategories: Loading clothes categories...");
        categoryArrayList = new ArrayList<>();
        FirebaseUser user = firebaseAuth .getCurrentUser();
        //db reference to load categories...db > Categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");

        ref.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                categoryArrayList.clear(); //clear before adding data.
                for(DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    ModelCategory modCategory = ds.getValue(ModelCategory.class);
                    //add to arraylist
                    if(user.getEmail().equals(modCategory.getEmail()))
                    {
                        //Add to arraylist
                        categoryArrayList.add(modCategory);
                    }
                    //categoryArrayList.add(modCategory);

                    //log.d(TAG, "onDataChanged: "+modCategory.getCategory());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });
    }



    @Override
    public void onBackPressed() //Therefore user cannot press the back button on their phone. they have to use the icon.
    {
        // super.onBackPressed();
    }

}
