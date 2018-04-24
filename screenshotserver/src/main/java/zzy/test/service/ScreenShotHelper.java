package zzy.test.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiyi.qi on 2016/10/31.
 */

public class ScreenShotHelper {

    public static void mkDirectory(String path) {
        //新建一个File，传入文件夹目录
        File file = new File(path);
//判断文件夹是否存在，如果不存在就创建，否则不创建
        if (!file.exists()) {
            //通过file的mkdirs()方法创建
            file.mkdirs();
        }
    }

//    /**
//     * 组装地图截图和其他View截图，并且将截图存储在本地sdcard，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
//     *
//     * @param bitmap        地图截图回调返回的结果
//     * @param viewContainer MapView和其他要截图的View所在的父容器ViewGroup
//     * @param mapView       MapView控件
//     * @param views         其他想要在截图中显示的控件
//     */
//    public static void saveScreenShot(final Bitmap bitmap, final ViewGroup viewContainer, final MapView mapView, final View... views) {
//
//
//
//        new Thread() {
//            @Override
//            public void run() {
//
//                Bitmap screenShotBitmap = getMapAndViewScreenShot(bitmap, viewContainer, mapView, views);
//                if (Environment.getExternalStorageState().
//                        equals(Environment.MEDIA_MOUNTED)) {
//                    TimeString ts = new TimeString();
//
//                    String timeString = ts.getTimeString() + ".png";
//                    String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "11" + File.separator + timeString;
//
//                    File file = new File(filePath);
//
//                    try {
//                        FileOutputStream outputStream = new FileOutputStream(file);
//                        screenShotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//
//
//                        //根据自己需求，如果外边对bitmp还有别的需求就不要recycle的
//                        screenShotBitmap.recycle();
//                        bitmap.recycle();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        }.start();

//    }

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
//        bmp.prepareToDraw();
        return bmp;
    }


}
