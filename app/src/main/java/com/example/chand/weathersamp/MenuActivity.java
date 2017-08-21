package com.example.chand.weathersamp;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chand.weathersamp.utils.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    Intent MenuIntent;
    ProgressBar MenuProgressBar;
    RelativeLayout MenuLayout;
    GridView gvmenu;
    TextView tvEmail;
    DatabaseReference mUserProfileReference;
    ArrayList<MenuItem> MenuList;
    ImageView ivLogOut;
    private FirebaseAuth mAuth;

    public static final int PERMISSION_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MenuIntent = this.getIntent();
        MenuLayout = (RelativeLayout)findViewById(R.id.rlMenuView);
        MenuProgressBar = (ProgressBar)findViewById(R.id.pbMenuProgress);
        ivLogOut = (ImageView)findViewById(R.id.ivLogout);

        mAuth = FirebaseAuth.getInstance();

        MenuLayout.setVisibility(View.GONE);
        MenuProgressBar.setVisibility(View.VISIBLE);

        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvEmail.setText(MenuIntent.getStringExtra("UserEmail"));

        gvmenu = (GridView)findViewById(R.id.gvMenu);

        gvmenu.setOnItemClickListener(this);

        ivLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mAuth.signOut();
                Intent LoginIntent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(LoginIntent);
                finish();
            }
        });

        mUserProfileReference = FirebaseDatabase.getInstance().getReference().child("users").child(MenuIntent.getStringExtra("UserID")).child("mobilityprofiles");
        mUserProfileReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                List userprofiles = dataSnapshot.getValue(t);
                MenuList = new ArrayList<MenuItem>();
                if(userprofiles == null)
                {
                    Log.d("AuthTest","No User Profiles set for this user");
                }
                else
                {
                    Log.d("AuthTest","No of User Profiles set for this user is " + userprofiles.size());
                    Log.d("AuthTest",userprofiles.get(3).toString());
                    for(int i=0;i<userprofiles.size();i++)
                    {
                        switch (userprofiles.get(i).toString())
                        {
                            case "install":
                                MenuItem MenuItem1 = new MenuItem(R.drawable.install_icon, "Install");
                                Log.d("AuthTest",userprofiles.get(i).toString());
                                MenuList.add(MenuItem1);
                                break;
                            case "uninstall":
                                MenuItem MenuItem2 = new MenuItem(R.drawable.uninstall_icon, "Uninstall");
                                Log.d("AuthTest",userprofiles.get(i).toString());
                                MenuList.add(MenuItem2);
                                break;
                            case "send":
                                MenuItem MenuItem3 = new MenuItem(R.drawable.send_icon, "Send");
                                Log.d("AuthTest",userprofiles.get(i).toString());
                                MenuList.add(MenuItem3);
                                break;
                            case "receive":
                                MenuItem MenuItem4 = new MenuItem(R.drawable.receive_icon, "Receive");
                                Log.d("AuthTest",userprofiles.get(i).toString());
                                MenuList.add(MenuItem4);
                                break;
                            case "swap":
                                MenuItem MenuItem5 = new MenuItem(R.drawable.swap_icon, "Swap");
                                Log.d("AuthTest",userprofiles.get(i).toString());
                                MenuList.add(MenuItem5);
                                break;
                        }
                    }
                }
                setAdapter(MenuList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void setAdapter(ArrayList<MenuItem> MenuList)
    {
        gvmenu.setAdapter(new MenuAdapter(this, MenuList));
        MenuProgressBar.setVisibility(View.GONE);
        MenuLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //Allow to proceed further if and only if the User allowed the Camera Permission
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
            return;
        }
        Intent InstallIntent = new Intent(this,InstallActivity.class);
        Log.d("AuthTest","On Click");
        MenuAdapter.viewHolder MenuViewHolder = (MenuAdapter.viewHolder) view.getTag();
        MenuItem ChosenMenu = (MenuItem) MenuViewHolder.ivRoutineIcon.getTag();
        InstallIntent.putExtra("IconId",ChosenMenu.iconId );
        InstallIntent.putExtra("IconText",ChosenMenu.iconName);
        startActivity(InstallIntent);
    }
}

class MenuAdapter extends BaseAdapter
{
    ArrayList<MenuItem> MenuList;
    Context context;
    MenuAdapter(Context context, ArrayList<MenuItem> MenuList)
    {
        this.context = context;
 /*       MenuList = new ArrayList<MenuItem>();
        MenuItem Menu1 = new MenuItem(R.drawable.install_icon, "Install");
        MenuItem Menu2 = new MenuItem(R.drawable.uninstall_icon, "Uninstall");
        MenuItem Menu3 = new MenuItem(R.drawable.send_icon, "Send");
        MenuItem Menu4 = new MenuItem(R.drawable.receive_icon, "Receive");
        MenuItem Menu5 = new MenuItem(R.drawable.swap_icon, "Swap");
        MenuList.add(Menu1);
        MenuList.add(Menu2);
        MenuList.add(Menu3);
        MenuList.add(Menu4);
        MenuList.add(Menu5);*/
        this.MenuList = MenuList;
    }
    @Override
    public int getCount() {
        return MenuList.size();
    }

    @Override
    public Object getItem(int i) {
        return MenuList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class viewHolder
    {
        ImageView ivRoutineIcon;
        TextView tvRoutineName;
        viewHolder(View v)
        {
            ivRoutineIcon = (ImageView)v.findViewById(R.id.ivRoutineIcon);
            tvRoutineName = (TextView)v.findViewById(R.id.tvRoutineName);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View MenuIconView = view;
        viewHolder MenuViewHolder=null;
        if(MenuIconView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            MenuIconView = inflater.inflate(R.layout.single_icon,viewGroup,false);
            MenuViewHolder = new viewHolder(MenuIconView);
            MenuIconView.setTag(MenuViewHolder);
        }
        else
        {
            MenuViewHolder = (viewHolder) MenuIconView.getTag();
        }
        MenuViewHolder.ivRoutineIcon.setImageResource(MenuList.get(i).iconId);
        MenuViewHolder.ivRoutineIcon.setTag(MenuList.get(i));
        MenuViewHolder.tvRoutineName.setText(MenuList.get(i).iconName);
        return MenuIconView;
    }

}