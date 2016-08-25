/**
 *
 */
package com.mrmo.mlocationlib;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 辅助工具类
 *
 * @author hongming.wang
 * @创建时间： 2015年11月24日 上午11:46:50
 * @项目名称： AMapLocationDemo2.x
 * @文件名称: Utils.java
 * @类型名称: Utils
 */
public class MGaodeUtil {
    /**
     * 根据定位结果返回定位信息的字符串
     *
     * @param location
     * @return
     */
    public synchronized static String getLocationStr(AMapLocation location) {
        if (null == location) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if (location.getErrorCode() == 0) {
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
                //定位完成的时间
                sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss:sss") + "\n");
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:sss") + "\n");
        return sb.toString();
    }

    private static SimpleDateFormat sdf = null;

    public synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        if (l <= 0l) {
            l = System.currentTimeMillis();
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }

    /**
     * 将坐标转换高德坐标
     *
     * @param context
     * @param coordType CoordType.BAIDU:百度坐标/CoordType.MAPBAR:图吧坐标/CoordType.MAPABC:图盟坐标/CoordType.SOSOMAP:搜搜坐标/CoordType.ALIYUN:阿里云坐标/CoordType.GOOGLE:谷歌坐标/CoordType.GPS ： GPS坐标
     * @param lat
     * @param lng
     * @return 0:lat 1:lng
     */
    public static double[] convert(Context context, CoordinateConverter.CoordType coordType, double lat, double lng) {
        double[] point = new double[]{-1, -1};
        try {
            CoordinateConverter converter = new CoordinateConverter(context);
            converter.from(coordType);
            DPoint dPoint = new DPoint(lat, lng);//设置需要转换的坐标
            converter.coord(dPoint);

            DPoint destPoint = converter.convert();//转换成高德坐标
            if (null != destPoint) {
                point[0] = destPoint.getLatitude();
                point[1] = destPoint.getLongitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return point;
    }

    /**
     * 判断坐标是否高德地图可用
     *
     * @param context
     * @param lat
     * @param lng
     * @return
     */
    public static boolean checkIsAvaliableGaode(Context context, double lat, double lng) {
        CoordinateConverter converter = new CoordinateConverter(context);
        boolean result = converter.isAMapDataAvailable(lat, lng);
        if (result) {
            return true;
        } else {
            return false;
        }
    }
}
