package zzy.test.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;



/**
 * 截图的帮助类
 * @author zhaozy
 */
public class ScreenShotHelper {


//    /**
//     * 组装地图截图和其他View截图，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
//     *
//     * @param bitmap        地图截图回调返回的结果
//     * @param viewContainer MapView和其他要截图的View所在的父容器ViewGroup
//     * @param mapView       MapView控件
//     * @param views         其他想要在截图中显示的控件
//     */
//    public static Bitmap getMapAndViewScreenShot(Bitmap bitmap, ViewGroup viewContainer, MapView mapView, View... views) {
//        int width = viewContainer.getWidth();
//        int height = viewContainer.getHeight();
//        final Bitmap screenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(screenBitmap);
//        canvas.drawBitmap(bitmap, mapView.getLeft(), mapView.getTop(), null);
//        for (View view : views) {
//            view.setDrawingCacheEnabled(true);
//            canvas.drawBitmap(view.getDrawingCache(), view.getLeft(), view.getTop(), null);
//        }
//
//        return screenBitmap;
//    }

    public static List<View> getAllChildViews(View view) {
        List<View> allchildren = new ArrayList<View>();
        if (view instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) view;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View viewchild = vp.getChildAt(i);
                allchildren.add(viewchild);
                //再次 调用本身（递归）
                allchildren.addAll(getAllChildViews(viewchild));
            }
        }
        return allchildren;
    }

    public static Bitmap getBitmapFromView(View view) {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap viewBmp = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return viewBmp;
    }

    /**
     * 获取整个窗口的截图
     *
     * @param context
     * @return
     */
    public static Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();

        cv.setDrawingCacheEnabled(true);
        cv.buildDrawingCache();
        Bitmap bmp = cv.getDrawingCache();
        if (bmp == null) {
            return null;
        }

        bmp.setHasAlpha(false);
        return bmp;
    }


}
