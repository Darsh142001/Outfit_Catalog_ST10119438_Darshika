package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class AddClothesToCategory extends AppCompatActivity implements View.OnClickListener{
//The purpose of this java class, is to allow users, after entering the name, description, selecting the category,
//from the dropdown, and taken a picture, it must store this in firebase. The picture must be stored in the
//storage section of the firebase. This is created so that when another user uses this app, they can't see
//what other clothing collections other users have.

    ImageButton backToMain;
    FloatingActionButton fab;
    ImageView imgCameraImage;

    TextView pickCategory;

    private static final int REQUEST_IMAGE_CAPTURE =0;
    private static final int REQUEST_IMAGE_CAPTURE_PERMISSION =100;


    FirebaseAuth firebaseAuth;
    FirebaseStorage mStorage;
    //private FirebaseAuth mAuth;
    ArrayList<ModelCategory> categoryArrayList;

    EditText nameOfClothes;
    EditText descriptionOfClothes;

    private Uri imageUri = null;
    Bitmap bitmap;

    private  boolean hasImageBeenSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_to_category_main);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();

        loadClotheCategories();

        backToMain = findViewById(R.id.backToMainBtn);
        backToMain.setOnClickListener(this);

        fab = findViewById(R.id.photoFab);
        imgCameraImage = findViewById(R.id.img_cameraImage);

        pickCategory = findViewById(R.id.pickCategory);

        nameOfClothes = findViewById(R.id.clothingNameET);
        descriptionOfClothes = findViewById(R.id.descriptionET);

    }

    //This method is called only when the user wants to go back to the activity_main_with_nav_bar.
    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId()) {
            case R.id.backToMainBtn:
                startActivity(new Intent(this, ClothesCategory.class)); //This will go back to the activity with the nav bar.
                break;
        }
    }

    //This click method is used for the textview where the user has to select the category they would be adding
    //clothes too.
    public void pickCategoryClick(View V)
    {
        categoryPickDialog(); //When the user has selected the category they want, it will display in the textview.
    }



    //This method will bring all the categories that the user added and display it as a dropdown menu.
    public void loadClotheCategories()
    {
        //log.d(TAG, "LoadClotheCategories: Loading clothes categories...");
        categoryArrayList = new ArrayList<>();
        FirebaseUser user = firebaseAuth .getCurrentUser();
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

    //This method will take the selected category from the dropdown menu and display it in the textview.
    public void categoryPickDialog()
    {
        //get string array of categories from arraylist
        String [] categoriesArray = new String[categoryArrayList.size()];
        for(int i=0; i < categoryArrayList.size(); i++){
            categoriesArray[i] = categoryArrayList.get(i).getCategory();
        }

        //alert dialog
        /*
        Javapoint, (2021) mentions that Android AlertDialog can be used to display the dialog message with OK and Cancel buttons.
        It can be used to interrupt and ask the user about his/her choice to continue or discontinue.
        Android AlertDialog is composed of three regions: title, content area and action buttons.
        Android AlertDialog is the subclass of Dialog class.
         */
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

                        //Log.d(TAG, "onClick: Selected Category: "+category);
                    }
                })
                .show();
    }

//These methods including takePhoto, are allow the user to give permission and then taking a picture of their clothing.
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
        //Also check whether the data is null, meaning the user may have cancelled.
        if(requestCode == REQUEST_IMAGE_CAPTURE && data !=null){
            bitmap = (Bitmap) data.getExtras().get("data");
            imgCameraImage.setImageBitmap(bitmap);
             hasImageBeenSet=true;
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




    //Need to upload the picture with the name, description and category to the database.
    private String clothingName="" , description="", category="";

    public void uploadPicClick(View v)
    {

        clothingName = nameOfClothes.getText().toString().trim();
        description = descriptionOfClothes.getText().toString().trim();
        category = pickCategory.getText().toString().trim();

        if(clothingName.isEmpty())
        {
            nameOfClothes.setError("Name of clothing is required");
            nameOfClothes.requestFocus();
            return;
        }
        if(description.isEmpty())
        {
            descriptionOfClothes.setError("Description is required");
            descriptionOfClothes.requestFocus();
            return;
        }
        if(category.isEmpty())
        {
            pickCategory.setError("Category is required");
            pickCategory.requestFocus();
            return;
        }

        if(!hasImageBeenSet){
            Toast.makeText(AddClothesToCategory.this, "Need to take a picture", Toast.LENGTH_SHORT).show();
            return;
        }
        long timestamp = System.currentTimeMillis();

        clothingName = nameOfClothes.getText().toString().trim();
        StorageReference ref = mStorage.getReferenceFromUrl("gs://outfitcatalog-c22ae.appspot.com");
        StorageReference clothesRef = ref.child(clothingName+".jpg"); //pass name of the image + .jpg //"pants.jpg"
        StorageReference clothesImageRef = ref.child("Images/"+clothesRef);
        //StorageReference clothesImageRef = ref.child("images/pants.jpg"); //constant folder: call it images
       // String child = "images/"+ imagename;
        clothesRef.getName().equals(clothesImageRef.getName()); //true
        clothesRef.getPath().equals(clothesImageRef.getPath()); //false

        imgCameraImage.setDrawingCacheEnabled(true);
        imgCameraImage.buildDrawingCache();
        bitmap =((BitmapDrawable) imgCameraImage.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArray);
        byte[] data = byteArray.toByteArray();

        UploadTask uploadTask = clothesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddClothesToCategory.this,"Failed", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //When the image has successfully uploaded, we get its download url. (Uri can only be used on saved content.)
              // Uri uri = taskSnapshot.getDownloadUrl();//
               // clothesRef.getDownloadUrl();
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while(!uriTask.isSuccessful());
                String uploadPictureUrl = ""+ uriTask.getResult();

                //upload to firebase db
                uploadPicInfoToDb(uploadPictureUrl, timestamp);

                Toast.makeText(AddClothesToCategory.this,"Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public String selectedCategory, enteredDescription;

    public void uploadPicInfoToDb(String uploadPictureUrl, long timestamp)
    {
        //Step 3: Upload pic info to firebase db
        selectedCategory = pickCategory.getText().toString().trim();
        enteredDescription = descriptionOfClothes.getText().toString().trim();

        String uid = firebaseAuth.getUid();

        //setup data to upload
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", ""+uid);
        hashMap.put("id", ""+timestamp);
        hashMap.put("name", ""+clothingName);
        hashMap.put("description", ""+enteredDescription);
        hashMap.put("category", ""+selectedCategory);
        hashMap.put("url", ""+uploadPictureUrl);
        hashMap.put("timestamp", +timestamp);

        //db reference: DB > Clothes
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Clothes");
        ref.child(""+timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddClothesToCategory.this, "Successfully uploaded...", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddClothesToCategory.this, "Failed to upload to db due to "+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void onBackPressed() //Therefore user cannot press the back button on their phone. they have to use the icon.
    {
        // super.onBackPressed();
    }


}
