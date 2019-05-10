package com.example.ykm.findfriends;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ykm.find.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    EditText Name, Password, Email;
    Button sendButton,goToLogin;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;

    private String mName, mEmail, mPassword;

    private boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Name = (EditText) findViewById(R.id.name);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        sendButton = (Button) findViewById(R.id.submitButton);
        goToLogin = findViewById(R.id.GoToLoginButton);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase  = FirebaseDatabase.getInstance();

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String name = Name.getText().toString().trim();

                if(email.equals("") || password.equals("") || name.equals("")){
                    Log.d(TAG,"Please fill proper details");
                    Toast.makeText(SignUpActivity.this,"Please fill proper details",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Registering", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Registering");
                    createAccount(name,email,password);

                }

            }
        });

    }

    private void createAccount(final String name, final String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        myRef = mFirebaseDatabase.getReference("users");
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser User = mAuth.getCurrentUser();
                            String UserId = User.getUid();

                            //myRef = mFirebaseDatabase.getReference("users").child(UserId);
                           /* myRef.child("Name").setValue(name);

                            myRef.child("latitude").setValue("0");

                            myRef.child("longitude").setValue("0");
*/                              User user = new User( name,0,  0,00000 , email);

                            myRef.child(UserId).setValue(user);


                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(SignUpActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intent);
                            mAuth.signOut();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            Name.setText("");
                            Email.setText("");
                            Password.setText("");

                        }



                    }
                });
    }

    long back_pressed = 0;

    @Override
    public void onBackPressed() {

        if (back_pressed + 1000 > System.currentTimeMillis()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }
}