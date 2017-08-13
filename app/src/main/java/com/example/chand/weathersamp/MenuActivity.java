package com.example.chand.weathersamp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.TextView;

import com.example.chand.weathersamp.utils.MenuItem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    Intent MenuIntent;
    GridView gvmenu;
    TextView tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MenuIntent = this.getIntent();
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvEmail.setText(MenuIntent.getStringExtra("UserEmail"));
        gvmenu = (GridView)findViewById(R.id.gvMenu);
        gvmenu.setAdapter(new MenuAdapter(this));
        gvmenu.setOnItemClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    Intent InstallIntent = new Intent(this,InstallActivity.class);
        Log.d("AuthTest","On Click");
        MenuAdapter.viewHolder MenuViewHolder = (MenuAdapter.viewHolder) view.getTag();
        MenuItem ChoosenMenu = (MenuItem) MenuViewHolder.ivRoutineIcon.getTag();
        InstallIntent.putExtra("IconId",ChoosenMenu.iconId );
        InstallIntent.putExtra("IconText", ChoosenMenu.iconName);
        startActivity(InstallIntent);
    }
}

class MenuAdapter extends BaseAdapter
{
    ArrayList<MenuItem> MenuList;
    Context context;
    MenuAdapter(Context context)
    {
        this.context = context;
        MenuList = new ArrayList<MenuItem>();
        MenuItem Menu1 = new MenuItem(R.drawable.install_icon, "Install");
        MenuItem Menu2 = new MenuItem(R.drawable.uninstall_icon, "Uninstall");
        MenuItem Menu3 = new MenuItem(R.drawable.send_icon, "Send");
        MenuItem Menu4 = new MenuItem(R.drawable.receive_icon, "Receive");
        MenuItem Menu5 = new MenuItem(R.drawable.swap_icon, "Swap");
        MenuList.add(Menu1);
        MenuList.add(Menu2);
        MenuList.add(Menu3);
        MenuList.add(Menu4);
        MenuList.add(Menu5);
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