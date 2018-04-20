package zzy.test.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mytool.yixin.navinfo.com.screenshotserver.IImagePath;

public class ScreenShotService extends Service {
    public static final String TAG = ScreenShotService.class.getSimpleName();


    public interface CallBack {
        String callback();
    }

    CallBack mCallBack;

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }


    private ScheduledExecutorService executorService;
    private static final long initialDelay = 0;
    private static final long period = 1500;// 500 millseconds

    private BlockingQueue<ScheduledFuture> blockingQueue;

    private IImagePath.Stub binder = new IImagePath.Stub() {


        @Override
        public String getImagePath() throws RemoteException {
            Log.d(TAG, "call  getImagePath");


//            FutureTask<String> futureTask = new FutureTask<String>(new ShotScreenThread()) {
//                @Override
//                protected void done() {
//                    super.done();
//                    try {
//                        Log.d(TAG, "get:" + get());
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };


            ScheduledFuture<String> future = executorService.schedule(new ShotScreenThread(), initialDelay, TimeUnit.MILLISECONDS);
            blockingQueue.add(future);
            while (!blockingQueue.isEmpty()) {
                ScheduledFuture future2 = blockingQueue.poll();
                if (!future.isDone()) {
                    blockingQueue.add(future);
                } else {
                    try {
                        Log.d(TAG, "result" + future2.get());
                        Log.d(TAG, "call submite execute");
                        return  future2.get().toString();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }


//            try {
//                executorService.scheduleAtFixedRate(futureTask, initialDelay, period, TimeUnit.MILLISECONDS);
//
//            } catch (Exception e) {
//                Log.d(TAG, "exception");
//            }
            return "";

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
//        if (intent == null) {
//            return null;
//        }
//        String from = intent.getStringExtra("from");
//        if ("client".equals(from)){
//            return (IBinder) binder;
//        }else if("server".equals(from)){
//            return new SaveImageBinder();
//        }else {
//            return null;
//        }
        return binder;

    }


    @Override
    public void onCreate() {
        super.onCreate();
        int cpuCount = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newScheduledThreadPool(cpuCount);
        blockingQueue = new ArrayBlockingQueue<ScheduledFuture>(100, true);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 后台截图，保存图片线程
     */
    private class ShotScreenThread implements Callable<String> {

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
            Log.d(TAG, "sub thread call  method");
            return "shot mock";
        }
    }

    /**
     * 内部类继承Binder
     */
    public class SaveImageBinder extends Binder {
        /**
         * 声明方法返回值是MyService本身
         *
         * @return
         */
        public ScreenShotService getService() {
            return ScreenShotService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "call  onDestroy");
    }
}
