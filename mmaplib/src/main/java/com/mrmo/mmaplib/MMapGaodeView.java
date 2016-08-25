package com.mrmo.mmaplib;

import android.content.Context;
import android.util.AttributeSet;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.MapView;

/**
 * Created by moguangjian on 16/8/24 17:17.
 */
public class MMapGaodeView extends MapView implements MMapAble{

    public MMapGaodeView(Context context) {
        super(context);
        init();
    }

    public MMapGaodeView(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
        init();
    }

    public MMapGaodeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMapGaodeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
    }

}
