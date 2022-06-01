package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
   //This class will allow the user to login after they have registered.
    //(see #1 Login and registration android app tutorial using firebase authentication-create user - CodeWithMazn, 2020).

    private FirebaseAuth mAuth;

    EditText email;
    EditText password;

    TextView registerPage;
    TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.emailLoginET);
        password = findViewById(R.id.passwordLoginET);

        registerPage = (TextView) findViewById(R.id.registerPageTv);
        registerPage.setOnClickListener(this);

        forgetPassword = (TextView) findViewById(R.id.forgetPasswordTv);
        forgetPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) //https://www.youtube.com/watch?v=Z-RE1QuUWPg&list=PL65Ccv9j4eZJ_bg0TlmxA7ZNbS8IMyl5i&index=4
    {
        switch(v.getId())
        {
            case R.id.registerPageTv:
                startActivity(new Intent(this, RegisterUser.class)); //This will direct the user back to the register page if they haven't registered.
                break;
            case R.id.forgetPasswordTv:
                startActivity(new Intent(this, ForgetPasswordActivity.class)); //This will direct the user to the forget password activity page so that they can reset their password.
                break;
        }
    }

    public void loginClick(View view)
    {
        LoginUser();

    }

    public void LoginUser() //this method will first ensure that the user had entered in all the fields before proceeding.
    {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();

        if(userEmail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
            email.setError("Please provide a valid email!");
            email.requestFocus();
            return;
        }
        if(userPassword.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }
        if(userPassword.length() < 6){
            password.setError("Min password length should be 6 characters ");
            password.requestFocus();
            return;
        }

        signIn(userEmail, userPassword); //they they filled in all the fields correctly, they will be logged in.
    }

    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            //Check if email has been verified:
                            FirebaseUser user = mAuth.getCurrentUser();

                            if(user.isEmailVerified()){
                                //redirect user to Menu activity.
                                startActivity(new Intent(MainActivity.this, ClothesCategory.class)); //check AndroidManifest.
                                Toast.makeText(MainActivity.this, "user is: "+user.getEmail(), Toast.LENGTH_LONG).show();

                            }else{
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this, "Check your email to verify your account!", Toast.LENGTH_LONG).show();
                            }

                        }else{
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() //Therefore user cannot press the back button on their phone. they have to use the icon.
    {
        // super.onBackPressed();
    }



}