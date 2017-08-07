package com.example.chand.weathersamp;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    TextView mConditionTextView;
    EditText mEnterEditText;
    Button mButtonSend;
    String strMessage;
    Typeface type;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mConditionRef = mRootRef.child("condition");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConditionTextView = (TextView)findViewById(R.id.textviewCondition);
        mEnterEditText = (EditText)findViewById(R.id.editTextEnter);
        mButtonSend = (Button)findViewById(R.id.buttonSend);
        type = Typeface.createFromAsset(getAssets(), "fonts/TrebuchetMS.ttf");
    }

    @Override
    protected  void onStart()
    {
        super.onStart();
        mConditionRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mConditionTextView.setTypeface(type);
                mConditionTextView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mEnterEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEnterEditText.setTypeface(type);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                strMessage = mEnterEditText.getText().toString();
                mConditionRef.setValue(strMessage);
            }
        });

       mButtonSend.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
                mConditionRef.setValue(strMessage);
           }
       });


    }
}
