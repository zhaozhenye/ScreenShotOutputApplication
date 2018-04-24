package zzy.test.service;

import android.app.Service;
import android.content.Intent;
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

/**
 * 后台服务
 *
 * @author zhaozy
 */
public class ScreenShotService extends Service {
    public static final String TAG = ScreenShotService.class.getSimpleName();

    /**
     * 线程池
     */
    private ScheduledExecutorService executorService;
    /**
     * 延迟处理
     */
    private static final long INITIAL_DELAY = 0;
    /**
     * 间隔处理
     */
    private static final long period = 1500;
    /**
     * 阻塞队列
     */
    private BlockingQueue<ScheduledFuture> blockingQueue;

    /**
     * 通讯接口
     */
    private IImagePath.Stub binder = new IImagePath.Stub() {


        @Override
        public String getImagePath() throws RemoteException {
            Log.d(TAG, "call  getImagePath");
            ScheduledFuture<String> future = executorService.schedule(new ShotScreenThread(), INITIAL_DELAY, TimeUnit.MILLISECONDS);
            blockingQueue.add(future);
            while (!blockingQueue.isEmpty()) {
                ScheduledFuture future2 = blockingQueue.poll();
                if (!future.isDone()) {
                    blockingQueue.add(future);
                } else {
                    try {
                        Log.d(TAG, "result" + future2.get());
                        Log.d(TAG, "call submite execute");
                        return future2.get().toString();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
            return "";

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "call  onDestroy");
    }
}
