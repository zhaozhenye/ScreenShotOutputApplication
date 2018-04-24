package zzy.test.service;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/12/5.
 */

public class SocketServerHelper {
    public static final String TAG = ScreenShotHelper.class.getSimpleName();

    private ServerSocket serverSocket;
    private Socket mSocket;
    private InputStream in;
    private String str = null;
    public static Handler ServerHandler;
    private int port;
    private ScheduledExecutorService executorService;
    private BlockingQueue<ScheduledFuture> blockingQueue;

    /**
     * @param port 端口号
     * @steps bind();绑定端口号
     * @effect 初始化服务端
     */
    public SocketServerHelper(int port) {
        this.port = port;
        // 初始化线程池
        int cpuCount = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newScheduledThreadPool(cpuCount);
        blockingQueue = new ArrayBlockingQueue<ScheduledFuture>(100, true);

    }

    /**
     * @steps listen();
     * @effect socket监听数据
     */
    public void beginListen() {
        // 利用线程池直接开启一个线程 & 执行该线程
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    /**
                     * accept();
                     * 接受请求
                     * */
                    mSocket = serverSocket.accept();
                    while (mSocket.isConnected()) {
                        try {
                            /**得到输入流*/
                            in = mSocket.getInputStream();

                            //4.解析数据
                            InputStreamReader reader = new InputStreamReader(in);
                            BufferedReader bufferedReader = new BufferedReader(reader);
                            String s = null;
                            StringBuffer sb = new StringBuffer();
                            s = bufferedReader.readLine();
                            if ("shot".equals(s)) {
                                //接收到客户端的截屏请求后，执行截屏操作
                                ScheduledFuture<String> future = executorService.schedule(new ShotScreenThread(), 0, TimeUnit.MILLISECONDS);
                                blockingQueue.add(future);
                                while (!blockingQueue.isEmpty()) {
                                    ScheduledFuture future2 = blockingQueue.poll();
                                    if (!future.isDone()) {
                                        blockingQueue.add(future);
                                    } else {
                                        try {
                                            Log.d(TAG, "result" + future2.get());
                                            Log.d(TAG, "call submite execute");
                                            sendMessage(future2.get().toString());
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                            }

                            Log.d(TAG, "从服务端接收到的数据是" + s);
                            System.out.println("response;" + s);
                            returnMessage(s);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });
    }


    /**
     * @steps write();
     * @effect socket服务端发送信息
     */
    public void sendMessage(final String chat) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintWriter out = new PrintWriter(mSocket.getOutputStream());
                    out.print(chat + "\n");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * @steps read();
     * @effect socket服务端得到返回数据并发送到主界面
     */
    public void returnMessage(String chat) {
        Message msg = new Message();
        msg.obj = chat;
        ServerHandler.sendMessage(msg);
    }

}
