package com.example.chand.weathersamp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.example.chand.weathersamp.utils.MenuItem;
import com.example.chand.weathersamp.utils.RoutineStep;
import com.google.zxing.Result;

import org.w3c.dom.Text;

import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by chand on 13-08-2017.
 */

public class InstallActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{

    Intent InstallIntent;
    TextView tvRoutineName;
    ImageView ivRoutineIcon;
    ListView lvRoutineSteps;
    String sRoutineName;

    private ZXingScannerView zXingScannerView;

    boolean CameraOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
        InstallIntent = this.getIntent();

        tvRoutineName = (TextView)findViewById(R.id.tvRoutineIcon_name);
        sRoutineName = InstallIntent.getStringExtra("IconText");
        tvRoutineName.setText(sRoutineName);

        ivRoutineIcon = (ImageView)findViewById(R.id.ivIcon_Image);
        ivRoutineIcon.setImageResource(InstallIntent.getIntExtra("IconId", R.drawable.install_icon));

        lvRoutineSteps = (ListView)findViewById(R.id.lvRoutineSteps);
        lvRoutineSteps.setAdapter(new RoutineStepsAdapter(this));
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    public void scan(View view)
    {
        zXingScannerView = new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
        CameraOn = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(CameraOn == true) {
            zXingScannerView.stopCamera();
            CameraOn = false;
        }
    }

    @Override
    public void handleResult(Result result) {
        Toast.makeText(getApplicationContext(), result.getText(),Toast.LENGTH_LONG).show();
        if(CameraOn == true) {
            zXingScannerView.stopCamera();
            CameraOn = false;
        }
        finish();
    }
}

class RoutineStepsAdapter extends BaseAdapter
{
    Context context;
    ArrayList<RoutineStep> RoutineStepList;
    RoutineStepsAdapter(Context context)
    {
        this.context = context;
        this.RoutineStepList = new ArrayList<RoutineStep>();
        RoutineStep Step1 = new RoutineStep("Location", "BSNL-DEL-5789231",'V');
        RoutineStep Step2 = new RoutineStep("Node", "NOD7890456",'V');
        RoutineStep Step3 = new RoutineStep("AssetCode", "",'E');
        RoutineStep Step4 = new RoutineStep("Install Confirmation", "",'H');
        RoutineStepList.add(Step1);
        RoutineStepList.add(Step2);
        RoutineStepList.add(Step3);
        RoutineStepList.add(Step4);
    }
    @Override
    public int getCount() {
        return RoutineStepList.size();
    }

    @Override
    public Object getItem(int i) {
        return RoutineStepList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class viewHolder
    {
        ImageView ivRoutineIcon;
        TextView tvRoutineName;
        LinearLayout llRoutineStepEditLayout;
        TextView tvRoutineStepNameEdit;
        RelativeLayout rlRoutineValueEditLayout;
        ImageView ivScan;
        AutoCompleteTextView actvRoutineStepValueEdit;
        RelativeLayout rlRoutineStepViewLayout;
        TextView tvRoutineStepNameView;
        TextView tvRoutineStepValueView;

        viewHolder(View v)
        {
            llRoutineStepEditLayout = (LinearLayout)v.findViewById(R.id.llRoutineStepEditLayout);
            tvRoutineStepNameEdit = (TextView)v.findViewById(R.id.tvRoutineStepNameEdit);
            rlRoutineValueEditLayout = (RelativeLayout)v.findViewById(R.id.rlRoutineValueEditLayout);
            ivScan = (ImageView) v.findViewById(R.id.ivScan);
            actvRoutineStepValueEdit = (AutoCompleteTextView)v.findViewById(R.id.actvRoutineStepValueEdit);
            rlRoutineStepViewLayout = (RelativeLayout)v.findViewById(R.id.rlRoutineStepViewLayout);
            tvRoutineStepNameView = (TextView)v.findViewById(R.id.tvRoutineStepNameView);
            tvRoutineStepValueView = (TextView)v.findViewById(R.id.tvRoutineStepValueView);
        }
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View RoutineStepView = view;
        viewHolder RoutineStepViewHolder=null;
        if(RoutineStepView==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            RoutineStepView = inflater.inflate(R.layout.single_routinerow,viewGroup,false);
            RoutineStepViewHolder = new viewHolder(RoutineStepView);
            RoutineStepView.setTag(RoutineStepViewHolder);
        }
        else
        {
            RoutineStepViewHolder = (viewHolder) RoutineStepView.getTag();
        }

        if(RoutineStepList.get(i).StepFlag == 'E')
        {
            RoutineStepViewHolder.llRoutineStepEditLayout.setVisibility(View.VISIBLE);
            RoutineStepViewHolder.tvRoutineStepNameEdit.setText("Enter " + RoutineStepList.get(i).StepName);
            RoutineStepViewHolder.actvRoutineStepValueEdit.setHint("Scan or Enter " + RoutineStepList.get(i).StepName);
            RoutineStepViewHolder.rlRoutineStepViewLayout.setVisibility(View.GONE);
        }
        else if (RoutineStepList.get(i).StepFlag == 'V')
        {
            RoutineStepViewHolder.llRoutineStepEditLayout.setVisibility(View.GONE);
            RoutineStepViewHolder.rlRoutineStepViewLayout.setVisibility(View.VISIBLE);
            RoutineStepViewHolder.tvRoutineStepNameView.setText(RoutineStepList.get(i).StepName);
            RoutineStepViewHolder.tvRoutineStepValueView.setText(RoutineStepList.get(i).StepValue);
        }
        else
        {
            RoutineStepViewHolder.llRoutineStepEditLayout.setVisibility(View.GONE);
            RoutineStepViewHolder.rlRoutineStepViewLayout.setVisibility(View.GONE);
        }

        return RoutineStepView;
    }

}