package com.example.chand.weathersamp;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by chand on 13-08-2017.
 */

public class SwapActivity extends AppCompatActivity //implements ZXingScannerView.ResultHandler
{
    Intent SwapIntent;
    TextView tvSwapRoutineName;
    ImageView ivSwapRoutineIcon;
    String sSwapRoutineName;
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


    LinearLayout llSwappedAssetLayout;
    LinearLayout llSwappedAssetStepEditLayout;
    AutoCompleteTextView actvSwappedAssetValueEdit;
    RelativeLayout rlSwappedAssetStepViewLayout;
    TextView tvSwappedAssetValueView;

    ImageView ivScanButton;

    DatabaseReference mSwapReference;
    DatabaseReference mSwapCheckReference;
    DatabaseReference mSwapLocationReference;
    DatabaseReference mSwapAssetReference;
    DatabaseReference mSwapInventoryOnHandReference;
    DatabaseReference mSwapInventoryInServiceReference;
    DatabaseReference mSwapLocationGPSReference;
    DatabaseReference mSwapedAssetReference;

    public Location CurrentUserLocation;
    com.example.chand.weathersamp.utils.Location CustomLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap);
        SwapIntent = this.getIntent();

        tvSwapRoutineName = (TextView)findViewById(R.id.tvSwapRoutineIcon_name);
        sSwapRoutineName = SwapIntent.getStringExtra("IconText");
        tvSwapRoutineName.setText(sSwapRoutineName);

        ivSwapRoutineIcon = (ImageView)findViewById(R.id.ivSwapIcon_Image);
        ivSwapRoutineIcon.setImageResource(SwapIntent.getIntExtra("IconId", R.drawable.swap_icon));

        Bundle bundle = SwapIntent.getExtras();
        CurrentUserLocation = bundle.getParcelable("CurrentUserLocation");
        if(CurrentUserLocation != null) {
            Toast.makeText(getApplicationContext(), "Swap Lat : " + CurrentUserLocation.getLatitude() + " | Long : " + CurrentUserLocation.getLongitude() + " | Aqr : " + CurrentUserLocation.getAccuracy() + " | Alt : " + CurrentUserLocation.getAltitude(), Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(),"No GPS Location Found",Toast.LENGTH_LONG).show();
        }

        llLocationLayout = (LinearLayout)findViewById(R.id.llSwapLocationStepOuterLayout);
        llLocationLayout.setVisibility(View.VISIBLE);
        llLocationStepEditLayout = (LinearLayout)findViewById(R.id.llSwapLocationStepEditLayout);
        llLocationStepEditLayout.setVisibility(View.VISIBLE);
        actvLocationValueEdit = (AutoCompleteTextView)findViewById(R.id.actvSwapLocationValueEditField);
        actvLocationValueEdit.setText("");
        rlLocationStepViewLayout = (RelativeLayout) findViewById(R.id.rlSwapLocationStepViewLayout);
        rlLocationStepViewLayout.setVisibility(View.GONE);
        tvLocationValueView = (TextView)findViewById(R.id.tvSwapLocationStepValueView);
        tvLocationValueView.setText("");

        llNodeLayout = (LinearLayout)findViewById(R.id.llSwapNodeStepOuterLayout);
        llNodeLayout.setVisibility(View.GONE);
        llNodeStepEditLayout = (LinearLayout)findViewById(R.id.llSwapNodeStepEditLayout);
        llNodeStepEditLayout.setVisibility(View.GONE);
        actvNodeValueEdit = (AutoCompleteTextView)findViewById(R.id.actvSwapNodeValueEditField);
        actvNodeValueEdit.setText("");
        rlNodeStepViewLayout = (RelativeLayout) findViewById(R.id.rlSwapNodeStepViewLayout);
        rlNodeStepViewLayout.setVisibility(View.GONE);
        tvNodeValueView = (TextView)findViewById(R.id.tvSwapNodeStepValueView);
        tvNodeValueView.setText("");

        llAssetLayout = (LinearLayout)findViewById(R.id.llSwapAssetStepOuterLayout);
        llAssetLayout.setVisibility(View.GONE);
        llAssetStepEditLayout = (LinearLayout)findViewById(R.id.llSwapAssetStepEditLayout);
        llAssetStepEditLayout.setVisibility(View.GONE);
        actvAssetValueEdit = (AutoCompleteTextView)findViewById(R.id.actvSwapAssetValueEditField);
        actvAssetValueEdit.setText("");
        rlAssetStepViewLayout = (RelativeLayout) findViewById(R.id.rlSwapAssetStepViewLayout);
        rlAssetStepViewLayout.setVisibility(View.GONE);
        tvAssetValueView = (TextView)findViewById(R.id.tvSwapAssetStepValueView);
        tvAssetValueView.setText("");


        llSwappedAssetLayout = (LinearLayout)findViewById(R.id.llSwapedAssetStepOuterLayout);
        llSwappedAssetLayout.setVisibility(View.GONE);
        llSwappedAssetStepEditLayout = (LinearLayout)findViewById(R.id.llSwapedAssetStepEditLayout);
        llSwappedAssetStepEditLayout.setVisibility(View.GONE);
        actvSwappedAssetValueEdit = (AutoCompleteTextView)findViewById(R.id.actvSwapedAssetValueEditField);
        actvSwappedAssetValueEdit.setText("");
        rlSwappedAssetStepViewLayout = (RelativeLayout) findViewById(R.id.rlSwapedAssetStepViewLayout);
        rlSwappedAssetStepViewLayout.setVisibility(View.GONE);
        tvSwappedAssetValueView = (TextView)findViewById(R.id.tvSwapedAssetStepValueView);
        tvSwappedAssetValueView.setText("");

        iCurrentScanRow = 1;

        ivScanButton = (ImageView)findViewById(R.id.ivSwapScan);
        Log.v("Victor","before");
/*
        mSwapLocationReference = FirebaseDatabase.getInstance().getReference().child("locations");
        mSwapLocationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>() {};
                List SwapLocations = dataSnapshot.getValue(t);
                String[] ArraySwapLocations = new String[SwapLocations.size()];
                for(int i=0;i<SwapLocations.size();i++)
                {
                    ArraySwapLocations[i] = SwapLocations.get(i).toString();
                }

                ArrayAdapter<String> locationAdaptor = new ArrayAdapter<String>(SwapActivity.this,android.R.layout.simple_list_item_1, ArraySwapLocations);
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

        mSwapLocationGPSReference = FirebaseDatabase.getInstance().getReference().child("locations_gps");
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
                //submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString());
            }
        });

        actvSwappedAssetValueEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setSwappedAssetAndNext(actvSwappedAssetValueEdit.getText().toString());
                submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString(),tvSwappedAssetValueView.getText().toString());
            }
        });
                Log.v("Victor","after");

        if(SwapIntent.getStringExtra("SwapLocationName")!=null)
        {
            setLocationAndNext(SwapIntent.getStringExtra("SwapLocationName"));
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void scan(View view)
    {
        Intent intent = new Intent(SwapActivity.this, ScanActivity.class);
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
                    //submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString());
                    //System.out.println(tvLocationValueView.getText());
                    break;
                case 4:
                    //Scanned when Asset field is active
                    setSwappedAssetAndNext(data.getStringExtra("scannedText"));
                    submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString(),tvSwappedAssetValueView.getText().toString());
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

        mSwapAssetReference = FirebaseDatabase.getInstance().getReference().child("install").child(actvLocationValueEdit.getText().toString()).child(actvNodeValueEdit.getText().toString());
        mSwapAssetReference.addValueEventListener(new ValueEventListener() {

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

                String[] ArraySwapableAssets = new String[AssetList.size()];
                for(int i=0;i<AssetList.size();i++)
                {
                    ArraySwapableAssets[i] = AssetList.get(i).toString();
                }
                ArrayAdapter<String> AssetAdaptor = new ArrayAdapter<String>(SwapActivity.this,android.R.layout.simple_list_item_1, ArraySwapableAssets);
                actvAssetValueEdit.setAdapter(AssetAdaptor);
                actvAssetValueEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        setAssetAndNext(actvAssetValueEdit.getText().toString());
                        //submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setAssetAndNext(String AssetCode)
    {
        actvAssetValueEdit.setText(AssetCode);
        iCurrentScanRow = 4;
        tvAssetValueView.setText(AssetCode);
        llAssetStepEditLayout.setVisibility(View.GONE);
        rlAssetStepViewLayout.setVisibility(View.VISIBLE);
        llSwappedAssetLayout.setVisibility(View.VISIBLE);
        llSwappedAssetStepEditLayout.setVisibility(View.VISIBLE);

        mSwapedAssetReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(actvLocationValueEdit.getText().toString()).child("On Hand");
        mSwapedAssetReference.addValueEventListener(new ValueEventListener() {

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

                String[] ArraySwapableAssets = new String[AssetList.size()];
                for(int i=0;i<AssetList.size();i++)
                {
                    ArraySwapableAssets[i] = AssetList.get(i).toString();
                }
                ArrayAdapter<String> AssetAdaptor = new ArrayAdapter<String>(SwapActivity.this,android.R.layout.simple_list_item_1, ArraySwapableAssets);
                actvSwappedAssetValueEdit.setAdapter(AssetAdaptor);
                actvSwappedAssetValueEdit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        setSwappedAssetAndNext(actvSwappedAssetValueEdit.getText().toString());
                        submitRoutine(tvLocationValueView.getText().toString(),tvNodeValueView.getText().toString(),tvAssetValueView.getText().toString(),tvSwappedAssetValueView.getText().toString());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setSwappedAssetAndNext(String SwappedAssetName)
    {
        actvSwappedAssetValueEdit.setText(SwappedAssetName);
        iCurrentScanRow = 5;
        //Initiate Call for Asset Validation and Ensure True ??? Shall be added later
        tvSwappedAssetValueView.setText(SwappedAssetName);
        llSwappedAssetStepEditLayout.setVisibility(View.GONE);
        rlSwappedAssetStepViewLayout.setVisibility(View.VISIBLE);
        //llAssetLayout.setVisibility(View.VISIBLE);
        //llAssetStepEditLayout.setVisibility(View.VISIBLE);
    }

    public void submitRoutine(final String LocationName, final String NodeName, String AssetCode, String SwappedAssetCode)
    {
        doesnotified= false;
        iCurrentScanRow = 3;
        mSwapReference = FirebaseDatabase.getInstance().getReference().child("install").child(LocationName).child(NodeName).child(AssetCode);
        mSwapReference.removeValue();
        mSwapReference = FirebaseDatabase.getInstance().getReference().child("install").child(LocationName).child(NodeName).child(SwappedAssetCode);
        mSwapReference.setValue("In Service");
        mSwapInventoryInServiceReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(LocationName).child("In Service").child(AssetCode);
        mSwapInventoryInServiceReference.removeValue();
        mSwapInventoryInServiceReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(LocationName).child("In Service").child(SwappedAssetCode);
        mSwapInventoryInServiceReference.setValue("Y");
        mSwapInventoryOnHandReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(LocationName).child("Swapped").child(AssetCode);
        mSwapInventoryOnHandReference.setValue("Y");
        mSwapInventoryOnHandReference = FirebaseDatabase.getInstance().getReference().child("inventory").child(LocationName).child("On Hand").child(SwappedAssetCode);
        mSwapInventoryOnHandReference.removeValue();
        nextAsset();
        mSwapCheckReference = FirebaseDatabase.getInstance().getReference().child("install").child(LocationName).child(NodeName);
        mSwapCheckReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String ChildName = dataSnapshot.getKey();
                String ChildValue = dataSnapshot.getValue(String.class);
                //if(!doesnotified) {
                Toast.makeText(getApplicationContext(), ChildName + " has been Swaped with " + tvSwappedAssetValueView.getText().toString() + " in the Node " + NodeName + " of Location " + LocationName + " in " + ChildValue + " status!", Toast.LENGTH_LONG).show();
                //nextAsset();
                //doesnotified = true;
                //}
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
        tvSwappedAssetValueView.setText("");
        rlSwappedAssetStepViewLayout.setVisibility(View.GONE);
        llSwappedAssetStepEditLayout.setVisibility(View.GONE);
        actvSwappedAssetValueEdit.setText("");
        llSwappedAssetLayout.setVisibility(View.GONE);
    }


    //Location Picklist Helper Functions
    void getGlobalLocationPickList()
    {
        mSwapLocationGPSReference.orderByChild("Location_Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<com.example.chand.weathersamp.utils.Location>> t = new GenericTypeIndicator<List<com.example.chand.weathersamp.utils.Location>>() {
                };
                Log.v("Victor", dataSnapshot.toString());
                List SwapGPSLocationsRaw = dataSnapshot.getValue(t);
                com.example.chand.weathersamp.utils.Location TempGPSLocation1;
                List<com.example.chand.weathersamp.utils.Location> SwapGPSLocations = new ArrayList<com.example.chand.weathersamp.utils.Location>();
                for (int i = 0; i < SwapGPSLocationsRaw.size(); i++) {
                    if (SwapGPSLocationsRaw.get(i) != null) {
                        TempGPSLocation1 = (com.example.chand.weathersamp.utils.Location) SwapGPSLocationsRaw.get(i);
                        SwapGPSLocations.add(TempGPSLocation1);
                    }
                }
                if (SwapGPSLocations.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Location available", Toast.LENGTH_LONG).show();
                    return;
                }
                String[] ArraySwapLocations = new String[SwapGPSLocations.size()];
                for (int i = 0; i < SwapGPSLocations.size(); i++) {

                    ArraySwapLocations[i] =  SwapGPSLocations.get(i).getLocation_Name();
                }

                ArrayAdapter<String> locationAdaptor = new ArrayAdapter<String>(SwapActivity.this, android.R.layout.simple_list_item_1, ArraySwapLocations);
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

        mSwapLocationGPSReference.orderByChild("Latitude").startAt(Latitude-0.01).endAt(Latitude+0.01).addValueEventListener(new ValueEventListener() {
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
                //List SwapGPSLocationsRaw = dataSnapshot.getValue(t);
                List<com.example.chand.weathersamp.utils.Location> SwapGPSLocationsRaw = new ArrayList<com.example.chand.weathersamp.utils.Location>();
                for(DataSnapshot child: dataSnapshot.getChildren())
                {
                    if(child != null)
                    {
                        SwapGPSLocationsRaw.add(child.getValue(com.example.chand.weathersamp.utils.Location.class));
                    }
                }
                com.example.chand.weathersamp.utils.Location TempGPSLocation1;
                List<com.example.chand.weathersamp.utils.Location> SwapGPSLocations = new ArrayList<com.example.chand.weathersamp.utils.Location>();
                for (int i = 0; i < SwapGPSLocationsRaw.size(); i++) {
                    if (SwapGPSLocationsRaw.get(i) != null) {
                        TempGPSLocation1 = (com.example.chand.weathersamp.utils.Location) SwapGPSLocationsRaw.get(i);
                        if (TempGPSLocation1.getLongitude() > (Longitude-0.01) && TempGPSLocation1.getLongitude() < (Longitude+0.01)) {
                            SwapGPSLocations.add(TempGPSLocation1);
                        }
                    }
                }
                if (SwapGPSLocations.size() == 0) {
                    Toast.makeText(getApplicationContext(), "No Matching Location for Current GPS Location", Toast.LENGTH_LONG).show();
                    getGlobalLocationPickList();
                    return;
                }
                String[] ArraySwapLocations = new String[SwapGPSLocations.size()];
                for (int i = 0; i < SwapGPSLocations.size(); i++)
                {
                    ArraySwapLocations[i] = SwapGPSLocations.get(i).getLocation_Name();
                }
                actvLocationValueEdit.setText(ArraySwapLocations[0]);
                setLocationAndNext(ArraySwapLocations[0]);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}