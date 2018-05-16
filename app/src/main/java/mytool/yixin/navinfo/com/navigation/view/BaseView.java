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
    /**
     * 当前为横屏还是竖屏 true为横屏模式
     */
    public boolean isExpandWidet = true;

    public BaseView(Context context) {
        super(context);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isExpandWidet() {
        return isExpandWidet;
    }

    public void setExpandWidet(boolean expandWidet) {
        isExpandWidet = expandWidet;
    }
}
