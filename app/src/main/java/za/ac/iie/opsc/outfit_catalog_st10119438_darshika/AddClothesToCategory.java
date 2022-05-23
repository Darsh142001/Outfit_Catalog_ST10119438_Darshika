package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddClothesToCategory extends AppCompatActivity implements View.OnClickListener{

    ImageButton backToMain;
    FloatingActionButton fab;
    ImageView imgCameraImage;

    private static final int REQUEST_IMAGE_CAPTURE =0;
    private static final int REQUEST_IMAGE_CAPTURE_PERMISSION =100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes_to_category_main);

        backToMain = findViewById(R.id.backToMainBtn);
        backToMain.setOnClickListener(this);

        fab = findViewById(R.id.photoFab);
        imgCameraImage = findViewById(R.id.img_cameraImage);

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


    public void fabBtnClick(View v)
    {
        if(ActivityCompat.checkSelfPermission(AddClothesToCategory.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            final String [] permissions = {Manifest.permission.CAMERA};

            ActivityCompat.requestPermissions(AddClothesToCategory.this, permissions, REQUEST_IMAGE_CAPTURE_PERMISSION);
        }else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //Check if we are receiving the result from the right request.
        //Also check whther teh data is null, meaning the user may have cancelled.
        if(requestCode == REQUEST_IMAGE_CAPTURE && data !=null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imgCameraImage.setImageBitmap(bitmap);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_IMAGE_CAPTURE_PERMISSION && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            takePhoto();
        }
    }

    private void takePhoto()
    {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_IMAGE_CAPTURE);
    }

}
