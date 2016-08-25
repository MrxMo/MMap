package com.mrmo.mlocationlib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 高德定位
 * Created by moguangjian on 16/8/24 14:39.
 */
public class MLocationGaode implements MLocationAble {

    private static final String TAG = MLocationGaode.class.getSimpleName();
    private Context context;
    private OnMLocationListener onMLocationListener;
    private AMapLocationClient aMapLocationClient;
    private AMapLocationClientOption aMapLocationClientOption;

    protected String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };

    private static final int REQUEST_CODE_LOCATION = 1215;

    public MLocationGaode(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        aMapLocationClientOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        aMapLocationClientOption.setHttpTimeOut(30 * 1000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        aMapLocationClientOption.setInterval(2 * 1000);//可选，设置定位间隔。默认为2秒
        aMapLocationClientOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        aMapLocationClientOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        aMapLocationClientOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用

        aMapLocationClient = new AMapLocationClient(context);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (null != aMapLocation) {
                    String result = MGaodeUtil.getLocationStr(aMapLocation);
                    if (aMapLocation.getErrorCode() == 0) {
                        MLocationModel mLocationModel = new MLocationModel();
                        mLocationModel.setCountry(aMapLocation.getCountry());
                        mLocationModel.setLat(aMapLocation.getLatitude());
                        mLocationModel.setLng(aMapLocation.getLongitude());
                        mLocationModel.setProvince(aMapLocation.getProvince());
                        mLocationModel.setCity(aMapLocation.getCity());
                        mLocationModel.setDistrict(aMapLocation.getDistrict());
                        mLocationModel.setStreet(aMapLocation.getStreet());
                        mLocationModel.setAddress(aMapLocation.getAddress());

                        if (onMLocationListener != null) {
                            onMLocationListener.onLocation(mLocationModel);
                        }
                        Log.i(TAG, "定位成功:");
                        Log.i(TAG, "" + result);
                    } else {
                        Log.e(TAG, "定位失败:");
                        Log.e(TAG, "" + result);
                    }

                } else {
                    Log.e(TAG, "定位失败，aMapLocation is null");
                }
            }
        });
    }

    @Override
    public void start() {
        if (!checkPermission()) {
            Toast.makeText(context, "没有足够权限", Toast.LENGTH_SHORT).show();
            return;
        }

        if (aMapLocationClient != null) {
            aMapLocationClient.startLocation();
        }

    }

    @Override
    public void stop() {
        if (aMapLocationClient != null) {
            aMapLocationClient.stopLocation();
        }
    }

    @Override
    public void destroy() {
        if (aMapLocationClient != null) {
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
        }
    }

    @Override
    public void setMLocationLister(OnMLocationListener onMLocationListener) {
        this.onMLocationListener = onMLocationListener;
    }

    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) context, needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]), REQUEST_CODE_LOCATION);
        }
    }

    private boolean checkPermission() {
        List list = findDeniedPermissions(needPermissions);
        return list.isEmpty();
    }

    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                needRequestPermissonList.add(perm);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, perm)) {
                    needRequestPermissonList.add(perm);
                }
            }
        }
        return needRequestPermissonList;
    }

    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] paramArrayOfInt) {
//        if (requestCode == REQUEST_CODE_LOCATION) {
//            if (!verifyPermissions(paramArrayOfInt)) {
//                // no permission
//            }
//        }


}
