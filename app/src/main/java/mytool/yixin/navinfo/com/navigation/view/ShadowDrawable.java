package mytool.yixin.navinfo.com.navigation.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import mytool.yixin.navinfo.com.navigation.R;
import mytool.yixin.navinfo.com.navigation.utils.BitmapUtil;
import mytool.yixin.navinfo.com.navigation.utils.GlobalUtil;


/**
 * INFO: SUB_VIEWER/把阴影画到 view 的外面（类似工具类）
 */
public class ShadowDrawable extends Drawable {

    public static final int DIRECTION_NONE = 0;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_TOP = 2;
    public static final int DIRECTION_RIGHT = 3;
    public static final int DIRECTION_BOTTOM = 4;
    private static Bitmap shadowBmp = BitmapUtil.decodeResourceWithHP(GlobalUtil.getResources(), R.drawable.bg_shadow);
    private Drawable original = null;
    /*private int d = LayoutUtils.dp2px(0);
    private int radius = LayoutUtils.dp2px(2);
    *//**
     * 距离
     *//*
    private int distance = d + radius;*/

    /**
     * 方向
     */
    private int direction = DIRECTION_NONE;

    /*private Paint paint = new Paint();

    {
        paint.setColor(Color.BLACK);
    }*/

    public static void update(View view, int direction) {
        Drawable background = view.getBackground();
        if (null == background || !(background instanceof ShadowDrawable)) {
            ShadowDrawable b = new ShadowDrawable();
            b.setOriginal(background);
            view.setBackgroundDrawable(b);
            background = b;
        }
        ((ShadowDrawable) background).setDirection(direction);
    }

    public void setOriginal(Drawable original) {
        this.original = original;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();

        if (null != original) {
            original.setBounds(bounds);
            original.draw(canvas);
        }

        /*Rect clipBounds = new Rect(bounds);
        Rect contentRect = new Rect(bounds);
        switch (direction) {
            case DIRECTION_LEFT:
                paint.setShadowLayer(radius, -d, 0, R.color.on_map_shadow);
                contentRect.top -= radius;
                contentRect.bottom += radius;
                clipBounds.right = clipBounds.left;
                clipBounds.left -= distance;
                break;
            case DIRECTION_TOP:
                paint.setShadowLayer(radius, 0, -d, R.color.on_map_shadow);
                contentRect.left -= radius;
                contentRect.right += radius;
                clipBounds.bottom = clipBounds.top;
                clipBounds.top -= distance;
                break;
            case DIRECTION_RIGHT:
                paint.setShadowLayer(radius, d, 0, R.color.on_map_shadow);
                contentRect.top -= radius;
                contentRect.bottom += radius;
                clipBounds.left = clipBounds.right;
                clipBounds.right += distance;
                break;
            case DIRECTION_BOTTOM:
                paint.setShadowLayer(radius, 0, d, R.color.on_map_shadow);
                contentRect.left -= radius;
                contentRect.right += radius;
                clipBounds.top = clipBounds.bottom;
                clipBounds.bottom += distance;
                break;
            case DIRECTION_NONE:
                return;
        }
        canvas.save();
        canvas.clipRect(clipBounds);
        canvas.drawRect(contentRect, paint);
        canvas.restore();*/

        Matrix matrix = new Matrix();
        switch (direction) {
            case DIRECTION_LEFT:
                // 以左上角旋转90度
                // 拉伸
                matrix.preScale(1f, (float) Math.ceil((double) bounds.height() / shadowBmp.getWidth()));
                matrix.preRotate(90f);
                break;
            case DIRECTION_TOP:
                // 平移
                // 以中心点旋转180度
                // 拉伸
                matrix.preScale((float) Math.ceil((double) bounds.width() / shadowBmp.getWidth()), 1f);
                matrix.preRotate(180f, shadowBmp.getWidth() / 2f, bounds.top - (shadowBmp.getHeight() / 2f));
                matrix.preTranslate(0f, bounds.top - shadowBmp.getHeight());
                break;
            case DIRECTION_RIGHT:
                // 以左上角旋转90度
                // 以中心点旋转180度
                // 平移, 拉伸
                matrix.preScale(1f, (float) Math.ceil((double) bounds.height() / shadowBmp.getWidth()));
                matrix.preTranslate(bounds.width() + shadowBmp.getHeight(), 0f);
                matrix.preRotate(180f, (bounds.left - shadowBmp.getHeight() / 2f), shadowBmp.getWidth() / 2f);
                matrix.preRotate(90f, bounds.left, bounds.top);
                break;
            case DIRECTION_BOTTOM:
                // 平移, 拉伸
                matrix.preScale((float) Math.ceil((double) bounds.width() / shadowBmp.getWidth()), 1f);
                matrix.preTranslate(0f, bounds.height());
                break;
            case DIRECTION_NONE:
                return;
        }
        canvas.save();
        canvas.drawBitmap(shadowBmp, matrix, null);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
