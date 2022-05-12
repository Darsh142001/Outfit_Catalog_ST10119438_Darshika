package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    TextView goLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        goLoginPage =(TextView) findViewById(R.id.loginPage);
        goLoginPage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId())
        {
            case R.id.loginPage:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

}
