package mytool.yixin.navinfo.com.navigation.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;


import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.text.DecimalFormat;

public class Utils {

    /**
     * 软件是否已升级
     *
     * @param context
     * @return
     */
    private static boolean isAppUpdate = true;

    public static boolean isAppUpdate(Context context) {
        return isAppUpdate;
    }



    public static String getMacAddr(Context context) // get macId as deviceId
    {
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String macString = info.getMacAddress() + "";
            return macString;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getAndroidID(Context context) {
        try {
            String m_androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            return m_androidId + "";
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAppVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String appVersion = packageInfo.versionName + ""; // appVersion
            return appVersion;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String appName = packageInfo.applicationInfo.loadLabel(packageManager).toString(); // appVersion
            return appName;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAppVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String appVersion = packageInfo.versionCode + ""; // appVersion
            return appVersion;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getScreenResolution() {
        try {
            DisplayMetrics dm = GlobalUtil.getResources().getDisplayMetrics();
            int screenHeight = dm.heightPixels;
            int screenWidth = dm.widthPixels;
            return screenHeight + "x" + screenWidth;

        } catch (Exception e) {
            return "";
        }
    }

    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * digitString.replaceAll("[^0-9]", "")
     *
     * @param digitString
     * @return
     */
    public static long parse2Long(String digitString) {
        try {
            return Long.parseLong(digitString.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 读取mainfest mate数据
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaIntValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = String.valueOf(metaData.getInt(metaKey));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return apiKey;
    }




    /**
     * 将字符串保存到文件中
     *
     * @param path
     * @param fileName
     * @param content
     */
    public static void saveData2File(String path, String fileName, String content) {

        FileOutputStream fos = null;
        try {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file, false);
            fos.write(content.getBytes());
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(fos);
        }
    }

    public static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void flushStrean(Flushable flushable) {
        if (flushable != null) {
            try {
                flushable.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void deleteCacheFile(Context context, String path, String fileName) {
        File file = new File(context.getCacheDir(), path + "/" + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 计算文字的高度
     *
     * @param p
     * @return 文字高度的像素值
     */
    public static float getTextHeight(Paint p) {
        FontMetrics fontMetrics = p.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    //TODO copy from old Utils by yuanyognchao

    /**
     * INFO 工具/将null及“null”转为“”
     *
     * @param str
     * @return
     */
    public static String formatStr(String str) {
        if (str == null || "null".equals(str)) {
            return "";
        }
        return str;
    }











    /**
     * 根据微信规则, 获取缩略图的 url, 兼容我们自己的规则
     *
     * <a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419317853&token=&lang=zh_CN">微信头像规则</>
     * @param source
     * @param size 对应的尺寸, 目前仅支持: 46, 96, 132
     * @return
     */
    public static String getThumbUrl(String source, int size) {
        if (TextUtils.isEmpty(source)) {
            return null;
        }

        Uri uri = Uri.parse(source);

        if (uri == null) {
            return null;
        }

        String path = uri.getPath();
        if (path.endsWith("/0")) {
            path = path.substring(0, path.length() - 1) + size;
        } else {
            if (!path.endsWith("/")) {
                path += "/";
            }
            path += size;
        }

        return uri.buildUpon().path(path).build().toString();
    }


    /**
     * 设置View的背景Drawable
     *
     * @param view     视图
     * @param drawable 图片Drawable
     */
    @UiThread
    public static void setViewBackGroundDrawable(@NonNull View view, Drawable drawable) {
        if (view == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }


    /**
     * 以（KB,MB,GB等形式）展示文件大小
     * @param size
     * @return
     */
    public static String getFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {

            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        }
        else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        }
        else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        }
        else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            }
            else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }
    /**
     * 得到bitmap的大小
     */
    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

}
