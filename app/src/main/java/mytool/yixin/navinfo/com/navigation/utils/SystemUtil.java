package mytool.yixin.navinfo.com.navigation.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;

import java.util.List;

/**
 * Created by zhangying2 on 2014/12/23.
 */
public class SystemUtil {
    private static String channel = "";



    /**
     * 判断是否为前台进程
     *
     * @param context
     * @return
     */
    public static boolean isAppOnForeground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);

        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();

        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (!appProcess.processName.equals(packageName)) {
                continue;
            }
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检查系统的GPS开关是否开启
     */
    public static boolean isGPSOpen() {
        try {
            LocationManager alm = (LocationManager) GlobalUtil.getContext().getSystemService(Context.LOCATION_SERVICE);
            return alm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            // 兼容某些手机(如金立), 用户禁止权限之后, 访问 gps 权限崩溃的异常
            e.printStackTrace();

//            Toast.makeText("请在系统设置中允许"  GlobalUtil.getResources().getString(R.string.app_name) + "获取位置权限!").show();
        }
        return false;
    }




}
