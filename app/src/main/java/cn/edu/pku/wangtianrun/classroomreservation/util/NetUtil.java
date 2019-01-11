package cn.edu.pku.wangtianrun.classroomreservation.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
    public static final int NETWORN_NONE=0;
    public static final int NETWORN_WIFI=1;
    public static final int NETWORN_MOBILE=2;
    /*
     *获得网络状态方法
     */
    public static int getNetworkState(Context context){
        ConnectivityManager connManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);//创建ConnectivityManager类的实例connManager，通过调用.getActiveNetworkInfo（）方法来获取网络状态
        NetworkInfo networkInfo=connManager.getActiveNetworkInfo();
        if (networkInfo==null){
            return NETWORN_NONE;//无网络连接
        }
        int nType=networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE){
            return NETWORN_MOBILE;//移动网络连接
        }else if(nType==ConnectivityManager.TYPE_WIFI){
            return NETWORN_WIFI;//WIFI网络连接
        }
        return NETWORN_NONE;
    }
}
