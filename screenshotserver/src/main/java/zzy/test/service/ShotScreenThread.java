package zzy.test.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * $截图的子线程$
 *
 * @author zhaozy
 * @date 2018/4/24
 */


public class ShotScreenThread implements Callable<String> {

    @Override
    public String call() throws Exception {
        Activity currentActivity = ScreenApplication.getInstance().getCurrentActivity();
        View cv = currentActivity.getWindow().getDecorView();
        List<View> allChildViews = ScreenShotHelper.getAllChildViews(cv);
        Bitmap screenShotBitmap = null;
        for (View view : allChildViews) {
            if (view.getId() == R.id.image_view) {
                Log.d("pic", "开始截图");
//                screenShotBitmap = ScreenShotHelper.getBitmapFromView(view);
                Log.d("pic", "截图完成" + screenShotBitmap);
            }
        }


        screenShotBitmap = ScreenShotHelper.captureScreen(currentActivity);
        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)) {
            TimeUtils ts = new TimeUtils();

            String timeString = ts.getTimeString() + ".png";
            //新建一个File，传入文件夹目录
            File ImagePathFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "11");
            //判断文件夹是否存在，如果不存在就创建，否则不创建
            if (!ImagePathFile.exists()) {
                ImagePathFile.mkdirs();
            }
            String filePath = ImagePathFile.getAbsolutePath() + File.separator + timeString;
            Log.d("pic", "filePath-->" + filePath);

            File imageFile = new File(filePath);
            Log.d("pic", "图片开始保存");

            try {
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                screenShotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                //根据自己需求，如果外边对bitmp还有别的需求就不要recycle的
//                    screenShotBitmap.recycle();
                Log.d("pic", "图片保存成功");
                return filePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "shot mock";
    }
}
