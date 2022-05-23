package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddClothesToCategory extends AppCompatActivity implements View.OnClickListener{

    ImageButton backToMain;
    FloatingActionButton fab;
    ImageView imgCameraImage;

    TextView pickCategory;

    private static final int REQUEST_IMAGE_CAPTURE =0;
    private static final int REQUEST_IMAGE_CAPTURE_PERMISSION =100;

    FirebaseAuth firebaseAuth;

    ArrayList<ModelCategory> categoryArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_to_category_main);

        firebaseAuth = FirebaseAuth.getInstance();
        loadClotheCategories();

        backToMain = findViewById(R.id.backToMainBtn);
        backToMain.setOnClickListener(this);

        fab = findViewById(R.id.photoFab);
        imgCameraImage = findViewById(R.id.img_cameraImage);

        pickCategory = findViewById(R.id.pickCategory);

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

    public void pickCategoryClick(View V)
    {
        categoryPickDialog();
    }

    public void loadClotheCategories()
    {
        //log.d(TAG, "LoadClotheCategories: Loading clothes categories...");
        categoryArrayList = new ArrayList<>();
        //db reference to load categories...db > Categories
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");

        ref.addListenerForSingleValueEvent(new ValueEventListener (){
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                categoryArrayList.clear(); //clear before adding data.
                for(DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    ModelCategory modCategory = ds.getValue(ModelCategory.class);
                    //add to arraylist
                    categoryArrayList.add(modCategory);

                    //log.d(TAG, "onDataChanged: "+modCategory.getCategory());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });

    }

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
                        pickCategory.setText(category);
                    }
                })
                .show();
    }


    public void fabBtnClick(View v)
    {
        //Check if we have camera permission.
        if(ActivityCompat.checkSelfPermission(AddClothesToCategory.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            final String [] permissions = {Manifest.permission.CAMERA};
        //Request permission - this is asynchronous.
            ActivityCompat.requestPermissions(AddClothesToCategory.this, permissions, REQUEST_IMAGE_CAPTURE_PERMISSION);
        }else{
            //we have permission, so take the photo
            takePhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if we are receiving the result from the right request.
        //Also check whether teh data is null, meaning the user may have cancelled.
        if(requestCode == REQUEST_IMAGE_CAPTURE && data !=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgCameraImage.setImageBitmap(bitmap);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_IMAGE_CAPTURE_PERMISSION && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            takePhoto(); // We have permission, so take the photo.
        }
    }

    private void takePhoto()
    {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

}
