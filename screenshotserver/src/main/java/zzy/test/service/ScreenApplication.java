package zzy.test.service;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;


/**
 * $Application$
 *
 * @author zhaozy
 * @date 2018/4/20
 */


public class ScreenApplication extends Application {

    private static ScreenApplication mInstance;
    private Activity app_activity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initGlobeActivity();
    }


    private void initGlobeActivity() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                app_activity = activity;
                Log.e("onActivityCreated===", app_activity + "");
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                app_activity = activity;
                Log.e("onActivityDestroyed===", app_activity + "");
            }

            /** Unused implementation **/
            @Override
            public void onActivityStarted(Activity activity) {
                app_activity = activity;
                Log.e("onActivityStarted===", app_activity + "");
            }

            @Override
            public void onActivityResumed(Activity activity) {
                app_activity = activity;
                Log.e("onActivityResumed===", app_activity + "");
            }

            @Override
            public void onActivityPaused(Activity activity) {
                app_activity = activity;
                Log.e("onActivityPaused===", app_activity + "");
            }

            @Override
            public void onActivityStopped(Activity activity) {
                app_activity = activity;
                Log.e("onActivityStopped===", app_activity + "");
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
        });
    }

    /**
     * 获取实例
     * @return
     */
    public static ScreenApplication getInstance() {
        return mInstance;
    }

    /**
     * 公开方法，外部可通过 MyApplication.getInstance().getCurrentActivity() 获取到当前最上层的activity
     */
    public Activity getCurrentActivity() {
        return app_activity;
    }
}
