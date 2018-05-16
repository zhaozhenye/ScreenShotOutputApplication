package mytool.yixin.navinfo.com.navigation.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

/**
 * 为了避免每次继承drawable都要实现setAlpha/setColorFilter/getOpacity方法，可以继承这个类
 * Created by liangxin on 2015/12/2.
 */
public class SimpleDrawable extends Drawable {
    protected TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
