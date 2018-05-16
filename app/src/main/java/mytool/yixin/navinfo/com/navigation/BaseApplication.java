package mytool.yixin.navinfo.com.navigation;

import android.app.Application;

import mytool.yixin.navinfo.com.navigation.utils.GlobalUtil;


/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/16
 */


public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        GlobalUtil.setContext(this);
        super.onCreate();
    }
}
