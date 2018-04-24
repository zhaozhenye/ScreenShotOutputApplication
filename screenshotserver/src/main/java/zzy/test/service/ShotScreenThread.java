package zzy.test.service;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/4/24
 */


public class ShotScreenThread implements Callable<String> {

    @Override
    public String call() throws Exception {
//            Thread.sleep(4000);
        Activity currentActivity = ScreenApplication.getInstance().getCurrentActivity();
        Log.d("pic","开始截图");
        Bitmap screenShotBitmap = ScreenShotHelper.captureScreen(currentActivity);
        Log.d("pic","截图完成"+screenShotBitmap);
        if (Environment.getExternalStorageState().
                equals(Environment.MEDIA_MOUNTED)) {
            TimeString ts = new TimeString();

            String timeString = ts.getTimeString() + ".png";
            //新建一个File，传入文件夹目录
            File ImagePathFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "11");
            //判断文件夹是否存在，如果不存在就创建，否则不创建
            if (!ImagePathFile.exists()) {
                //通过file的mkdirs()方法创建<span style="color:#FF0000;">目录中包含却不存在</span>的文件夹
                ImagePathFile.mkdirs();
            }
            String filePath = ImagePathFile.getAbsolutePath() + File.separator+ timeString;
            Log.d("pic", "filePath-->" + filePath);

            File imageFile = new File(filePath);
            Log.d("pic","图片开始保存");

            try {
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                screenShotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                //根据自己需求，如果外边对bitmp还有别的需求就不要recycle的
//                    screenShotBitmap.recycle();
                Log.d("pic","图片保存成功");
                return filePath;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "shot mock";
    }
}
