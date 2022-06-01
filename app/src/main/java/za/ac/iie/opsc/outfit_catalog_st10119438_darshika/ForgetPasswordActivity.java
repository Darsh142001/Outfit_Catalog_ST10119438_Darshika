package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    //This class will allow the user to reset their password if they forgot.
    //It will prompt the user for their email, once they click the reset button, an email will be sent.
    //User must check their emails, either under inbox or spam folder, once they found the email...
    //they must click on the link and the user will be able to reset their pasword.
    //(see #1 Login and registration android app tutorial using firebase authentication-create user - CodeWithMazn, 2020).
    EditText email;
    Button resetPassword;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpassword);

        email = findViewById(R.id.emailResetPwdEt);
        resetPassword = findViewById(R.id.resetPasswordBtn);

        auth = FirebaseAuth.getInstance();

        resetPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                resetPassword();
            }
        });
    }

    private void resetPassword()
    {
        String userEmail = email.getText().toString().trim();

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

        reset(userEmail);
    }

    public void reset(String email)
    {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgetPasswordActivity.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgetPasswordActivity.this, "Try again! Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
