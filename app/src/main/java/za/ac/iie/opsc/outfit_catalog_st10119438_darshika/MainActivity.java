package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //Going to need to use this url when it comes to authentication and adding users to realtime database:
    // https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4

    TextView registerPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerPage = (TextView) findViewById(R.id.registerPageTv);
        registerPage.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId())
        {
            case R.id.registerPageTv:
                startActivity(new Intent(this, RegisterUser.class));
                break;


        }
    }


}