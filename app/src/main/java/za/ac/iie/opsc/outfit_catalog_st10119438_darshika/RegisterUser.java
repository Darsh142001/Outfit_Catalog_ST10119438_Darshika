package za.ac.iie.opsc.outfit_catalog_st10119438_darshika;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText userFullName;
    EditText email;
    EditText password;
    Button register;

    TextView goLoginPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);
        mAuth = FirebaseAuth.getInstance();

        userFullName = findViewById(R.id.fullNameET);
        email = findViewById(R.id.emailET);
        password = findViewById(R.id.passwordET);

        register = findViewById(R.id.registerBtn);

        goLoginPage =(TextView) findViewById(R.id.loginPage);
        goLoginPage.setOnClickListener(this);
    }

    //This method is called if the user has already registered and just wants to sign in.
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


//If the user has not registered, then this button will register them once they filled in their details.
    public void onClickRegister(View view)
    {
        RegisterUser();
    }

    //This method will register the users.
    private void RegisterUser()
    {
       String fullName = userFullName.getText().toString().trim();
       String userEmail = email.getText().toString().trim();
       String userPassword = password.getText().toString().trim();
//The if statements will throw an exception if the user does not fill in everything or forgets to fill in one input.
        if(fullName.isEmpty()){
            userFullName.setError("Surname is required");
            userFullName.requestFocus();
            return;
        }
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
//If all inputs are filled in, this method will be called.
//It will take the users inputs and create the account.
        createAccount(fullName, userEmail, userPassword);

    }

    //This method will create the account. Once their name. email and password has been entered:
    //This method will save their details in the firebase database.
    private void createAccount(String fullName, String userEmail, String userPassword)
    {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        if(task.isSuccessful()){
                            User user = new User(fullName, userEmail);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(RegisterUser.this, "User has been successfully registered", Toast.LENGTH_LONG).show();
                                            }else{
                                                Toast.makeText(RegisterUser.this, "Failed to register! Try again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(RegisterUser.this, "Failed to register", Toast.LENGTH_LONG).show();
                        }
                        }

                });
    }



}
