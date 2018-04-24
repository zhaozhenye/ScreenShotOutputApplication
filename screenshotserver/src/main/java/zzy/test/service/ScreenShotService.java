package zzy.test.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
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
