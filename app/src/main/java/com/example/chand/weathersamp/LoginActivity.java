package com.example.chand.weathersamp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity
{
    EditText editTextEmail;
    EditText editTextPassword;
    Button buttonSignOn;
    ConstraintLayout clLoginView;
    ProgressBar pbLoginProgress;
    DatabaseReference regIdRef;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean SignedIn = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = (EditText)findViewById(R.id.etEmail);
        editTextPassword = (EditText)findViewById(R.id.etPassword);
        buttonSignOn = (Button)findViewById(R.id.btSignOn);
        clLoginView = (ConstraintLayout)findViewById(R.id.clLoginLayout);
        pbLoginProgress = (ProgressBar)findViewById(R.id.pbLoginProgressBar);

        clLoginView.setVisibility(View.GONE);
        pbLoginProgress.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                clLoginView.setVisibility(View.GONE);
                pbLoginProgress.setVisibility(View.VISIBLE);
                if(user != null)
                {
                    if(SignedIn == false)
                    {
                        Log.d("AuthTest", "Login Success and the user is " + user.getUid());
                        SignedIn = true;
                        gotoMenu(user.getEmail(), user.getUid());
                    }
                }
                else
                {
                    Log.d("AuthTest", "Sign out Successful");
                    SignedIn = false;
                    clLoginView.setVisibility(View.VISIBLE);
                    pbLoginProgress.setVisibility(View.GONE);
                }
            }
        };
    }

    @Override
    protected  void onStart()
    {
        super.onStart();
        Log.d("AuthTest", "onStart() has been called");
        mAuth.addAuthStateListener(mAuthListener);
        Log.d("AuthTest", "mAuth.addAuthStateListener(mAuthListener) has been called");
        buttonSignOn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
            switch (view.getId()){
                case R.id.btSignOn:
                    signIn(editTextEmail.getText().toString(), editTextPassword.getText().toString());
                    break;
            }
            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("AuthTest", "onStop() has been called");
        if(mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            Log.d("AuthTest", "mAuth.removeAuthStateListener(mAuthListener) has been called");
        }
        SignedIn = false;
    }

    private void signIn(String email, String password)
    {
        if(!validateForm())
        {
            return;
        }

        Log.d("AuthTest", "signIn: " + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("AuthTest", "Login in with SignInWithEmailAndPassword is Successful");
                            FirebaseUser user  = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Authentication Passed.", Toast.LENGTH_LONG).show();
                            //Update UI based on User here
                        }
                        else
                        {
                            Log.d("AuthTest", "Login in with SignInWithEmailAndPassword is Failed");
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_LONG).show();
                            //Update UI based on Login Failure here
                        }
                    }
                });
    }

    private boolean validateForm()
    {
        boolean valid = true;

        String email = editTextEmail.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            editTextEmail.setError("Required.");
            valid = false;
        }
        else if(!email.contains("@") || !email.contains(".") || !(email.length() > 10) )
        {
            editTextEmail.setError("Invalid Email.");
            valid = false;
        }
        else
        {
            editTextEmail.setError(null);
        }

        String password = editTextPassword.getText().toString();
        if(TextUtils.isEmpty(password))
        {
            editTextPassword.setError("Required.");
            valid = false;
        }
        else if(password.length() < 7)
        {
            editTextPassword.setError("Password is too short.");
            valid = false;
        }
        else
        {
            editTextPassword.setError(null);
        }

        return valid;
    }

    private void gotoMenu(String email, String userId)
    {
        regIdRef = FirebaseDatabase.getInstance().getReference().child("regId").child(userId);
        regIdRef.setValue(FirebaseInstanceId.getInstance().getToken());
        Intent MenuIntent = new Intent(LoginActivity.this, MenuActivity.class);
        MenuIntent.putExtra("UserEmail",email);
        MenuIntent.putExtra("UserID",userId);
        startActivity(MenuIntent);
        finish();
    }

}
