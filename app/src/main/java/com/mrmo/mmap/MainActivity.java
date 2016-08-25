package com.mrmo.mmap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mrmo.mlocationlib.MLocationAble;
import com.mrmo.mlocationlib.MLocationGaode;
import com.mrmo.mlocationlib.MLocationModel;
import com.mrmo.mlocationlib.OnMLocationListener;

public class MainActivity extends AppCompatActivity {

    private MLocationAble mLocationAble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLocation();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationAble.stop();
        mLocationAble.destroy();
    }

    private void initLocation() {
        mLocationAble = new MLocationGaode(this);
        mLocationAble.setMLocationLister(new OnMLocationListener() {
            @Override
            public void onLocation(MLocationModel mLocationModel) {

                mLocationAble.stop();
            }
        });
        mLocationAble.start();
    }
}
