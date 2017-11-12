package com.example.chand.weathersamp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.example.chand.weathersamp.utils.MenuItem;
import com.example.chand.weathersamp.utils.RoutineStep;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.Duration;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by chand on 13-08-2017.
 */

public class InstallActivity extends AppCompatActivity //implements ZXingScannerView.ResultHandler
{
    Intent InstallIntent;
    TextView tvRoutineName;
    ImageView ivRoutineIcon;
    String sRoutineName;
    Integer iCurrentScanRow = 1;
    boolean doesnotified;

    LinearLayout llLocationLayout;
        LinearLayout llLocationStepEditLayout;
            AutoCompleteTextView actvLocationValueEdit;
        RelativeLayout rlLocationStepViewLayout;
            TextView tvLocationValueView;

    LinearLayout llNodeLayout;
        LinearLayout llNodeStepEditLayout;
            AutoCompleteTextView actvNodeValueEdit;
        RelativeLayout rlNodeStepViewLayout;
            TextView tvNodeValueView;

    LinearLayout llAssetLayout;
        LinearLayout llAssetStepEditLayout;
            AutoCompleteTextView actvAssetValueEdit;
        RelativeLayout rlAssetStepViewLayout;
            TextView tvAssetValueView;

    ImageView ivScanButton;

    DatabaseReference mInstallReference;
    DatabaseReference mInstallCheckReference;
    DatabaseReference mInstallLocationReference;
    DatabaseReference mInstallAssetReference;
    DatabaseReference mInstallInventoryOnHandReference;
    DatabaseReference mInstallInventoryInServiceReference;
    DatabaseReference mInstallLocationGPSReference;

    public Location CurrentUserLocation;
    com.example.chand.weathersamp.utils.Location CustomLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
        InstallIntent = this.getIntent();

        tvRoutineName = (TextView) findViewById(R.id.tvRoutineIcon_name);
        sRoutineName = InstallIntent.getStringExtra("IconText");
        tvRoutineName.setText(sRoutineName);

        ivRoutineIcon = (ImageView) findViewById(R.id.ivIcon_Image);
        ivRoutineIcon.setImageResource(InstallIntent.getIntExtra("IconId", R.drawable.install_icon));

        Bundle bundle = InstallIntent.getExtras();
        CurrentUserLocation = bundle.getParcelable("CurrentUserLocation");
        if(CurrentUserLocation != null) {
            Toast.makeText(getApplicationContext(), "Install Lat : " + CurrentUserLocation.getLatitude() + " | Long : " + CurrentUserLocation.getLongitude() + " | Aqr : " + CurrentUserLocation.getAccuracy() + " | Alt : " + CurrentUserLocation.getAltitude(), Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No GPS Location Found",Toast.LENGTH_LONG).show();
        }

        llLocationLayout = (LinearLayout) findViewById(R.id.llInstallLocationStepOuterLayout);
        llLocationLayout.setVisibility(View.VISIBLE);
        llLocationStepEditLayout = (LinearLayout) findViewById(R.id.llInstallLocationStepEditLayout);
        llLocationStepEditLayout.setVisibility(View.VISIBLE);
        actvLocationValueEdit = (AutoCompleteTextView) findViewById(R.id.actvInstallLocationValueEditField);
        actvLocationValueEdit.setText("");
        rlLocationStepViewLayout = (RelativeLayout) findViewById(R.id.rlInstallLocationStepViewLayout);
        rlLocationStepViewLayout.setVisibility(View.GONE);
        tvLocationValueView = (TextView) findViewById(R.id.tvInstallLocationStepValueView);
        tvLocationValueView.setText("");

        llNodeLayout = (LinearLayout) findViewById(R.id.llInstallNodeStepOuterLayout);
        llNodeLayout.setVisibility(View.GONE);
        llNodeStepEditLayout = (LinearLayout) findViewById(R.id.llInstallNodeStepEditLayout);
        llNodeStepEditLayout.setVisibility(View.GONE);
        actvNodeValueEdit = (AutoCompleteTextView) findViewById(R.id.actvInstallNodeValueEditField);
        actvNodeValueEdit.setText("");
        rlNodeStepViewLayout = (RelativeLayout) findViewById(R.id.rlInstallNodeStepViewLayout);
        rlNodeStepViewLayout.setVisibility(View.GONE);
        tvNodeValueView = (TextView) findViewById(R.id.tvInstallNodeStepValueView);
        tvNodeValueView.setText("");

        llAssetLayout = (LinearLayout) findViewById(R.id.llInstallAssetStepOuterLayout);
        llAssetLayout.setVisibility(View.GONE);
        llAssetStepEditLayout = (LinearLayout) findViewById(R.id.llInstallAssetStepEditLayout);
        llAssetStepEditLayout.setVisibility(View.GONE);
        actvAssetValueEdit = (AutoCompleteTextView) findViewById(R.id.actvInstallAssetValueEditField);
        actvAssetValueEdit.setText("");
        rlAssetStepViewLayout = (RelativeLayout) findViewById(R.id.rlInstallAssetStepViewLayout);
        rlAssetStepViewLayout.setVisibility(View.GONE);
        tvAssetValueView = (TextView) findViewById(R.id.tvInstallAssetStepValueView);
        tvAssetValueView.setText("");

        iCurrentScanRow = 1;

        ivScanButton = (ImageView) findViewById(R.id.ivScan);
        Log.v("Victor", "before");

/*
    mInstallLocationReference = FirebaseDatabase.getInstance().getReference().child("locations");
    mInstallLocationReference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {
            };
            List InstallLocations = dataSnapshot.getValue(t);
            String[] ArrayInstallLocations = new String[InstallLocations.size()];
            for (int i = 0; i < InstallLocations.size(); i++) {
                ArrayInstallLocations[i] = InstallLocations.get(i).toString();
            }

            ArrayAdapter<String> locationAdaptor = new ArrayAdapter<String>(InstallActivity.this, android.R.layout.simple_list_item_1, ArrayInstallLocations);
            actvLocationValueEdit.setAdapter(locationAdaptor);
            actvLocationValueEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    setLocationAndNext(actvLocationValueEdit.getText().toString());
                }
            });
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
*/

    mInstallLocationGPSReference = FirebaseDatabase.getInstance().getReference().child("locations_gps");
        if(CurrentUserLocation==null)
        {
            getGlobalLocationPickList();
        }
        else
        {
            setLocationBasedOnGPS();
        }

        actvNodeValueEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    setNodeAndNext(actvNodeValueEdit.getText().toString());
                    return true;
                }
                return false;
            }
        });

        actvAssetValueEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setAssetAndNext(actvAssetValueEdit.getText().toString());
                submitRoutine(tvLocationValueView.getText().toString(), tvNodeValueView.getText().toString(), tvAssetValueView.getText().toString());
            }
        });
        Log.v("Victor", "after");

        if(InstallIntent.getStringExtra("InstallLocationName")!=null)
        {
            setLocationAndNext(InstallIntent.getStringExtra("InstallLocationName"));
        }

    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void scan(View view)
    {
        Intent intent = new Intent(InstallActivity.this, ScanActivity.class);
        startActivityForResult(intent, iCurrentScanRow);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            switch(requestCode)
            {
                case 1:
                    //Scanned when Location field is active
                    setLocationAndNext(data.getStringExtra("scannedText"));
                    break;
                case 2:
                    //Scanned when Node field is active
                    setNodeAndNext(data.getStringExtra("scannedText"));
                    break;
                case 3:
                    //Scanned when Asset field is active
                    setAssetAndNext(data.getStringExtra("scannedText"));
                    submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString());
                    //System.out.println(tvLocationValueView.getText());
                    break;
            }

            //Toast.makeText(getApplicationContext(), data.getStringExtra("scannedText"),Toast.LENGTH_LONG).show();
        }
    }

    public void setLocationAndNext(String LocationName)
    {
        actvLocationValueEdit.setText(LocationName);
        iCurrentScanRow = 2;
        //Initiate Call for Location Validation and Ensure True ??? Shall be added later
        tvLocationValueView.setText(LocationName);
        llLocationStepEditLayout.setVisibility(View.GONE);
        rlLocationStepViewLayout.setVisibility(View.VISIBLE);
        llNodeLayout.setVisibility(View.VISIBLE);
        llNodeStepEditLayout.setVisibility(View.VISIBLE);
    }

    public void setNodeAndNext(String NodeName)
    {
        actvNodeValueEdit.setText(NodeName);
        iCurrentScanRow = 3;
        //Initiate Call for Node Validation and Ensure True ??? Shall be added later
        tvNodeValueView.setText(NodeName);
        llNodeStepEditLayout.setVisibility(View.GONE);
        rlNodeStepViewLayout.setVisibility(View.VISIBLE);
        llAssetLayout.setVisibility(View.VISIBLE);
        llAssetStepEditLayout.setVisibility(View.VISIBLE);

        mInstallAssetReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(actvLocationValueEdit.getText().toString()).child("On Hand");
        mInstallAssetReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("Victor","Asset on Data changed");
                Iterable<DataSnapshot> snapshotIterator = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterator = snapshotIterator.iterator();
                List<String> AssetList = new ArrayList<String>();
                String AssetCode;
                while(iterator.hasNext())
                {
                    // iterator.next().getKey().toString();
                    AssetCode = iterator.next().getKey();
                    Log.v("Victor",AssetCode);
                    AssetList.add(AssetCode);
                }

                String[] ArrayInstallableAssets = new String[AssetList.size()];
                for(int i=0;i<AssetList.size();i++)
                {
                    ArrayInstallableAssets[i] = AssetList.get(i).toString();
                }
                ArrayAdapter<String> AssetAdaptor = new ArrayAdapter<String>(InstallActivity.this,android.R.layout.simple_list_item_1, ArrayInstallableAssets);
                actvAssetValueEdit.setAdapter(AssetAdaptor);
                actvAssetValueEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        setAssetAndNext(actvAssetValueEdit.getText().toString());
                        submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setAssetAndNext(String AssetName)
    {
        actvAssetValueEdit.setText(AssetName);
        iCurrentScanRow = 3;
        //Initiate Call for Asset Validation and Ensure True ??? Shall be added later
        tvAssetValueView.setText(AssetName);
        llAssetStepEditLayout.setVisibility(View.GONE);
        rlAssetStepViewLayout.setVisibility(View.VISIBLE);
        //llAssetLayout.setVisibility(View.VISIBLE);
        //llAssetStepEditLayout.setVisibility(View.VISIBLE);
    }

    public void submitRoutine(final String LocationName, final String NodeName, String AssetCode)
    {
        doesnotified= false;
        mInstallReference = FirebaseDatabase.getInstance().getReference().child("install").child(LocationName).child(NodeName).child(AssetCode);
        mInstallReference.setValue("In Service");
        mInstallInventoryInServiceReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(LocationName).child("In Service").child(AssetCode);
        mInstallInventoryInServiceReference.setValue("Y");
        mInstallInventoryOnHandReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(LocationName).child("On Hand").child(AssetCode);
        mInstallInventoryOnHandReference.removeValue();
        mInstallCheckReference = FirebaseDatabase.getInstance().getReference().child("install").child(LocationName).child(NodeName);
        mInstallCheckReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String ChildName = dataSnapshot.getKey();
                String ChildValue = dataSnapshot.getValue(String.class);
                //if(!doesnotified) {
                    Toast.makeText(getApplicationContext(), ChildName + " has been added to the Node " + NodeName + " of Location " + LocationName + " in " + ChildValue + " status!", Toast.LENGTH_LONG).show();
                    nextAsset();
                    //doesnotified = true;
                //}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void nextAsset()
    {
     tvAssetValueView.setText("");
        rlAssetStepViewLayout.setVisibility(View.GONE);
        llAssetStepEditLayout.setVisibility(View.VISIBLE);
        actvAssetValueEdit.setText("");
    }

    //Location Picklist Helper Functions
    void getGlobalLocationPickList()
    {
        mInstallLocationGPSReference.orderByChild("Location_Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<com.example.chand.weathersamp.utils.Location>> t = new GenericTypeIndicator<List<com.example.chand.weathersamp.utils.Location>>() {
                };
                Log.v("Victor", dataSnapshot.toString());
                List InstallGPSLocationsRaw = dataSnapshot.getValue(t);
                com.example.chand.weathersamp.utils.Location TempGPSLocation1;
                List<com.example.chand.weathersamp.utils.Location> InstallGPSLocations = new ArrayList<com.example.chand.weathersamp.utils.Location>();
                for (int i = 0; i < InstallGPSLocationsRaw.size(); i++) {
                    if (InstallGPSLocationsRaw.get(i) != null) {
                        TempGPSLocation1 = (com.example.chand.weathersamp.utils.Location) InstallGPSLocationsRaw.get(i);
                        InstallGPSLocations.add(TempGPSLocation1);
                    }
                }
                if (InstallGPSLocations.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Location available", Toast.LENGTH_LONG).show();
                    return;
                }
                String[] ArrayInstallLocations = new String[InstallGPSLocations.size()];
                for (int i = 0; i < InstallGPSLocations.size(); i++) {

                    ArrayInstallLocations[i] =  InstallGPSLocations.get(i).getLocation_Name();
                }

                ArrayAdapter<String> locationAdaptor = new ArrayAdapter<String>(InstallActivity.this, android.R.layout.simple_list_item_1, ArrayInstallLocations);
                actvLocationValueEdit.setAdapter(locationAdaptor);
                actvLocationValueEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        setLocationAndNext(actvLocationValueEdit.getText().toString());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void setLocationBasedOnGPS()
    {
        Double Latitude = CurrentUserLocation.getLatitude();

        mInstallLocationGPSReference.orderByChild("Latitude").startAt(Latitude-0.01).endAt(Latitude+0.01).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double Longitude = CurrentUserLocation.getLongitude();
                GenericTypeIndicator<List<com.example.chand.weathersamp.utils.Location>> t = new GenericTypeIndicator<List<com.example.chand.weathersamp.utils.Location>>() {
                };
                Log.v("Victor", dataSnapshot.toString());
                if(dataSnapshot.getValue() == null)
                {
                    Toast.makeText(getApplicationContext(), "No Matching Location for Current GPS Location", Toast.LENGTH_LONG).show();
                    getGlobalLocationPickList();
                    return;
                }
                //List InstallGPSLocationsRaw = dataSnapshot.getValue(t);
                List<com.example.chand.weathersamp.utils.Location> InstallGPSLocationsRaw = new ArrayList<com.example.chand.weathersamp.utils.Location>();
                for(DataSnapshot child: dataSnapshot.getChildren())
                {
                    if(child != null)
                    {
                        InstallGPSLocationsRaw.add(child.getValue(com.example.chand.weathersamp.utils.Location.class));
                    }
                }
                com.example.chand.weathersamp.utils.Location TempGPSLocation1;
                List<com.example.chand.weathersamp.utils.Location> InstallGPSLocations = new ArrayList<com.example.chand.weathersamp.utils.Location>();
                for (int i = 0; i < InstallGPSLocationsRaw.size(); i++) {
                    if (InstallGPSLocationsRaw.get(i) != null) {
                        TempGPSLocation1 = (com.example.chand.weathersamp.utils.Location) InstallGPSLocationsRaw.get(i);
                        if (TempGPSLocation1.getLongitude() > (Longitude-0.01) && TempGPSLocation1.getLongitude() < (Longitude+0.01)) {
                            InstallGPSLocations.add(TempGPSLocation1);
                        }
                    }
                }
                if (InstallGPSLocations.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Matching Location for Current GPS Location", Toast.LENGTH_LONG).show();
                    getGlobalLocationPickList();
                    return;
                }
                String[] ArrayInstallLocations = new String[InstallGPSLocations.size()];
                for (int i = 0; i < InstallGPSLocations.size(); i++)
                {
                    ArrayInstallLocations[i] = InstallGPSLocations.get(i).getLocation_Name();
                }
                actvLocationValueEdit.setText(ArrayInstallLocations[0]);
                setLocationAndNext(ArrayInstallLocations[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}