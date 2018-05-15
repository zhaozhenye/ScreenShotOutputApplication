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
 * $截图的子线程$
 *
 * @author zhaozy
 * @date 2018/4/24
 */


public class ShotScreenThread implements Callable<Bitmap> {


    @Override
    public Bitmap call() throws Exception {
        Bitmap bitmap = null;
        Activity currentActivity = ScreenApplication.getInstance().getCurrentActivity();
        long beforeTime = System.currentTimeMillis();
        Bitmap screenShotBitmap = ScreenShotHelper.captureScreen(currentActivity);
        long screenEnd = System.currentTimeMillis();
        System.out.println("screen shot all time is:" + (screenEnd - beforeTime));
        bitmap = Utils.compressRGB565(screenShotBitmap);
        System.out.println("screen shot bitmap compress all time is:" + (System.currentTimeMillis() - screenEnd));


        System.out.println("screen shot bitmap size is：" + Utils.getNetFileSizeDescription(bitmap.getByteCount()));


        return bitmap;


//        saveImageToLocal(screenShotBitmap);

    }

    private String saveImageToLocal(Bitmap screenShotBitmap) {
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
