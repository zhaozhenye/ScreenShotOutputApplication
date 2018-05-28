package mytool.yixin.navinfo.com.navigation.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/16
 */


public class BaseView extends View {

    public boolean landMode = true;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isLandMode() {
        return landMode;
    }

    public void setLandMode(boolean landMode) {
        this.landMode = landMode;
    }
}
