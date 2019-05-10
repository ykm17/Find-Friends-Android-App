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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    EditText email, password;
    Button sendButton,registerButton, mPhoneVerifyButton;
    String mEmail, mPassword;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "-------------------------------------------mAuth: "+mAuth);
        if (mAuth.getCurrentUser()!=null){

            FirebaseUser User = mAuth.getCurrentUser();
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("userid",User.getUid());
            startActivity(intent);
            finish();

        }

        email = (EditText) findViewById(R.id.emailText);
        password = (EditText) findViewById(R.id.passwordText);
        sendButton = (Button) findViewById(R.id.LoginButton);
        registerButton = findViewById(R.id.gotoSignup);
        mPhoneVerifyButton = findViewById(R.id.phoneNumber);

        mPhoneVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,PhoneActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmail = email.getText().toString().trim();
                mPassword = password.getText().toString().trim();

                if(mEmail.equals("") || mPassword.equals("")){
                    Log.d(TAG,"Please fill proper details");
                    Toast.makeText(MainActivity.this,"Please fill proper details",Toast.LENGTH_SHORT).show();


                }
                else{
                    Toast.makeText(MainActivity.this, "Verifying", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"Proper details, Signing In");
                    signIn(mEmail, mPassword);

                }


            }
        });
        Log.d(TAG, "signIn:" + email);
    }

    void signIn(final String email, String password) {
        Log.d(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(MainActivity.this, "Logging you in...", Toast.LENGTH_SHORT).show();

                            FirebaseUser User = mAuth.getCurrentUser();
                            final String UserId = User.getUid();

                            //Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                            intent.putExtra("userid", UserId);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            finish();


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Incorrect Details", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    long back_pressed=0;
    @Override
    public void onBackPressed() {
        if (back_pressed + 1000 > System.currentTimeMillis()){


            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        back_pressed = System.currentTimeMillis();
    }

}

