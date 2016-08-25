package com.mrmo.mlocationlib;

/**
 * Created by moguangjian on 16/8/24 14:25.
 */
public interface MLocationAble {
    void start();

    void stop();

    void destroy();

    void setMLocationLister(OnMLocationListener onMLocationListener);
}
