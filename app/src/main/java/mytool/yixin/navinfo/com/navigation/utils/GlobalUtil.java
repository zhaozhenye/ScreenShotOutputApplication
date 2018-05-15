package mytool.yixin.navinfo.com.navigation.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

import mytool.yixin.navinfo.com.navigation.listener.Listener;
import mytool.yixin.navinfo.com.navigation.listener.WeakSuccinctListeners;
import mytool.yixin.navinfo.com.navigation.log.Log;
import mytool.yixin.navinfo.com.navigation.log.LogTag;

public class GlobalUtil {

    private static int sUID;

    private static int statusBarHeight;

    private static Handler handler;

    private static Context context;

    private static Activity mainActivity;
    //
    private static String resPackageName;

    private static WeakSuccinctListeners backGroundListeners = new WeakSuccinctListeners();

    private static ForegroundTaskListener foregroundTaskListener = new ForegroundTaskListener();
    /**
     * 系统原始的dpi
     */
    private static float originalDpi = 1;
    /**
     * 当前是否处于后台运行
     */
    private static boolean isBackGround;

    private static boolean isDebugMode;


    static {
        addBackGroundListener(foregroundTaskListener);
    }

    /**
     * 是否是 debug 模式,此 debug 模式为 Android 原生的那种;
     * //NOTE:谨记:使用此方法的前提是 在 App Module 的 AndroidManifest 中不能主动设置 android:debuggable
     */
    public static boolean isDebugMode() {
        return isDebugMode;
    }

    /**
     * 判断应用是否处于后台运行
     */
    public static boolean isBackGround() {
        return isBackGround;
    }

    /**
     * 设置应用的前后台状态
     */
    public static void setIsBackGround(boolean isBackGround) {
        if (Log.isLoggable(LogTag.ACTIVITY_CONFIG, Log.DEBUG)) {
            Log.d(LogTag.ACTIVITY_CONFIG, " -->> 当前处于" + (isBackGround ? "后台" : "前台") + "运行");
        }
        GlobalUtil.isBackGround = isBackGround;
        backGroundListeners.conveyEvent();
    }

    public static void addBackGroundListener(Listener.SuccinctListener l) {
        backGroundListeners.add(l);
    }

    /**
     * 传入一个必须在前台执行的任务，该任务会立即执行（当软件已经处于前台）或者从后台转入前台后开始执行（如：切换页面）
     */
    public static void runInForeground(Runnable foregroundTask) {
        if (GlobalUtil.isBackGround()) {
            foregroundTaskListener.addTask(foregroundTask);
//            backGroundListener = new Listener.SuccinctListener() {
//                @Override
//                public void onEvent() {
//                    if (!GlobalUtil.isBackGround()) {
//                        exitOrFinish(finish);
//                    }
//                    backGroundListener = null;
//                }
//            };
//            GlobalUtil.addBackGroundListener(backGroundListener);
        } else {
            foregroundTask.run();
        }
    }

    public static Context getContext() {
        return GlobalUtil.context;
    }

    public static void setContext(Application context) {
        GlobalUtil.context = context;
        GlobalUtil.handler = new Handler(Looper.getMainLooper());
        GlobalUtil.isDebugMode = context.getApplicationInfo() != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

    }



    public static Resources getResources() {
        Context context = getMainActivity();
        if (context == null) {
            context = getContext();
        }
        return context.getResources();
    }

    public static Configuration getConfiguration() {
        return getResources().getConfiguration();
    }

    public static Handler getHandler() {
        return GlobalUtil.handler;
    }

    public static String getFromAssets(Context context, String fileName) throws IOException {
        BufferedReader bufReader = null;
        try {
            bufReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(fileName)));
            String line = "";
            StringBuilder result = new StringBuilder();
            while (null != (line = bufReader.readLine())) {
                result.append(line);
            }
            return result.toString();
        } finally {
            if (bufReader != null) {
                bufReader.close();
            }
        }
    }

    public static Activity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(Activity mainActivity) {
        GlobalUtil.mainActivity = mainActivity;
    }

    public static float getOriginalDpi() {
        return getContext().getResources().getDisplayMetrics().density;
    }

    /**
     * 获取桌面状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        if (0 == GlobalUtil.statusBarHeight) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                GlobalUtil.statusBarHeight = GlobalUtil.context.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        return GlobalUtil.statusBarHeight;
    }

    public static int getUnixUID() {
        if (sUID == 0) {
            try {
                sUID = mainActivity.getPackageManager().getPackageInfo(resPackageName, 0).applicationInfo.uid;
            } catch (Throwable e) {
//
            }
        }
        return sUID;
    }

    public static boolean isKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isM() {
        return Build.VERSION.SDK_INT >= 23 || "MNC".equals(Build.VERSION.CODENAME);
    }

    public static boolean isNougat() {
        return Build.VERSION.SDK_INT >= 24 || "N".equals(Build.VERSION.CODENAME);
    }

    /**
     * 判断当前线程是否非UI线程
     *
     * @return
     */
    public static boolean isNotUIThread() {
        return Looper.myLooper() != Looper.getMainLooper();
    }

    /**
     * INFO 工具/收起软键盘
     * 调用该方法可以收起页面中的软键盘
     */
    public static void hideKeyboard() {
        //收起软键盘
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mainActivity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static String getResPackageName() {
        return resPackageName;
    }

    /**
     * 确保该包名同AndroidManifest中的package一致
     */
    public static void setResPackageName(String resPackageName) {
        GlobalUtil.resPackageName = resPackageName;
    }

    //获取手机对应的api  level
    public static int getMobileApiLevel() {
        return Build.VERSION.SDK_INT;
    }

    public static class ForegroundTaskListener implements Listener.SuccinctListener {

        private ArrayList<Runnable> foregroundTasks = new ArrayList<>();
        private boolean execute = false;

        public void addTask(Runnable foregroundTask) {
            if (execute) {
                return;
            }
            if (!foregroundTasks.contains(foregroundTask)) {
                foregroundTasks.add(foregroundTask);
            }
        }

        public void execute() {
            execute = true;
            while (foregroundTasks.size() != 0 && !isBackGround()) {
                foregroundTasks.remove(0).run();
            }
            execute = false;
        }

        @Override
        public void onEvent() {
            if (!isBackGround()) {
                execute();
            }
        }
    }
}
