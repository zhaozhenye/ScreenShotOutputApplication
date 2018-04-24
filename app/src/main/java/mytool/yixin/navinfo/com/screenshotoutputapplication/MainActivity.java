package mytool.yixin.navinfo.com.screenshotoutputapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mytool.yixin.navinfo.com.screenshotserver.IImagePath;

import static mytool.yixin.navinfo.com.screenshotoutputapplication.BitmapUtils.getDiskBitmap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private ImageView imageView;
    private Button btnBindService;
    private Button btnUnbindService;
    private TextView textPath;
    // 连接 断开连接 发送数据到服务器 的按钮变量
    private Button btnConnect, btnDisconnect, btnSend;

    /**
     * 显示接收服务器消息 按钮
     */
    private TextView Receive;
    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private Socket socket;
    private BufferedReader br;
    /**
     * 接收服务器发送过来的消息
     */
    private String response;



    /**
     * 发送消息到服务器 变量
     */
    private OutputStream outputStream;

    public static final String IP = "localhost";
    public static final int PORT = 12310;

    private boolean isBind = false;

    private IImagePath mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);
        btnBindService = findViewById(R.id.btn_bind_service);
        btnUnbindService = findViewById(R.id.btn_unbind_service);
        textPath = findViewById(R.id.text_path);

        btnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService();
            }
        });
        btnUnbindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unBindService();
            }
        });

        init();

    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
                    String imagePath = bundle.getString("path");
                    Log.d(TAG, "imagePath:-->" + imagePath);
                    if (TextUtils.isEmpty(imagePath)) {
                        //停止截图
                        unBindService();
                    } else {
                        textPath.setText("图片地址是： " + imagePath);
                        Bitmap bitmap = getDiskBitmap(imagePath);
                        if (bitmap != null) {
                            imageView.setImageBitmap(bitmap);
                        } else {
                            Log.d(TAG, "bitmap" + bitmap);
                        }
                    }


                    break;
                case 1:
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    System.out.println("截图图片后的时间是：" + df.format(System.currentTimeMillis()));
                    textPath.setText("图片地址是： " + response);

                    if (TextUtils.isEmpty(response)) {
                        //停止截图
                        disconectSocket();
                    } else {
                        Bitmap bitmap2 = getDiskBitmap(response);
                        if (bitmap2 != null) {
                            imageView.setImageBitmap(bitmap2);
                        } else {
                            Log.d(TAG, "bitmap" + bitmap2);
                        }

                    }
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "请首先建立连接", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), "请确保开启服务端后，再次尝试连接", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                String imagePath = mBinder.getImagePath();
                Message msg = mHandler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putString("path", imagePath);// 将服务器返回的订单号传到Bundle中，，再通过handler传出
                msg.setData(bundle);
                msg.arg1 = 0;   // 0为获取验证码成功
                mHandler.sendMessage(msg);

            } catch (RemoteException e) {
                e.printStackTrace();
            }
            if (isBind) {
                //每隔1s循环执行run方法
                mHandler.postDelayed(this, 10);
            }
        }
    };

    /**
     * 初始化操作
     */
    private void init() {
        btnConnect = (Button) findViewById(R.id.connect);
        btnDisconnect = (Button) findViewById(R.id.disconnect);
        btnSend = (Button) findViewById(R.id.start_screen_shot);
        Receive = (Button) findViewById(R.id.btn_receive_message);

        mThreadPool = Executors.newCachedThreadPool();

        btnConnect.setOnClickListener(this);

        Receive.setOnClickListener(this);

        btnSend.setOnClickListener(this);

        btnDisconnect.setOnClickListener(this);


    }


    /**
     * 解绑服务
     */
    private void unBindService() {
        if (isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }


    }


    /**
     * 绑定服务
     */
    private void bindService() {

        Intent serviceIntent = new Intent().setComponent(new ComponentName("zzy.test.service", "zzy.test.service.ScreenShotService"));
        serviceIntent.putExtra("from", "client");
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBind = true;
            mBinder = IImagePath.Stub.asInterface(service);
            Log.i(TAG, "bind service success ");

            mHandler.postDelayed(runnable, 10);//延时100毫秒


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, " service disconnected ");

        }
    };


    /**
     * 建立连接的子线程
     */
    Runnable connectThread = new Runnable() {
        @Override
        public void run() {

            try {
                // 创建Socket对象 & 指定服务端的IP 及 端口号
                socket = new Socket(IP, PORT);


                // 判断客户端和服务器是否连接成功
                Log.d(TAG, "当前连接状态是：" + socket.isConnected());

            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(4);
            }

        }
    };

    /**
     * 发送消息-开始截图
     */
    Runnable sendMessageThread = new Runnable() {
        @Override
        public void run() {
            //
            if (socket == null || (!socket.isConnected())) {
                mHandler.sendEmptyMessage(3);
                return;
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("截图图片前的时间是：" + df.format(System.currentTimeMillis()));
            try {
                // 步骤1：从Socket 获得输出流对象OutputStream
                // 该对象作用：发送数据
                outputStream = socket.getOutputStream();

                // 步骤2：写入需要发送的数据到输出流对象中
                outputStream.write(("ScreenShot" + "\n").getBytes("utf-8"));
                // 特别注意：数据的结尾加上换行符才可让服务器端的readline()停止阻塞

                // 步骤3：发送数据到服务端
                outputStream.flush();

                mThreadPool.execute(receiveMessageThread);

                //每隔1s循环执行run方法
                mHandler.postDelayed(this, 100);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    /**
     * 接收服务器的数据
     */
    Runnable receiveMessageThread = new Runnable() {
        @Override
        public void run() {
            if (socket == null) {
                return;
            }

            try {
                while (socket.isConnected()) {
                    // 步骤1：创建输入流对象InputStream
                    InputStream is = socket.getInputStream();

                    // 步骤2：创建输入流读取器对象 并传入输入流对象
                    // 该对象作用：获取服务器返回的数据
                    InputStreamReader isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
                    response = br.readLine();
                    Log.d(TAG, "response:" + response);
                    // 步骤4:通知主线程,将接收的消息显示到界面
                    Message msg = Message.obtain();
                    msg.what = 1;
                    mHandler.sendMessage(msg);

                }


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            //建立连接
            case R.id.connect:
                mThreadPool.execute(connectThread);

                break;
            //断开连接
            case R.id.disconnect:
                disconectSocket();
                break;
            //发送消息给服务器
            case R.id.start_screen_shot:

                mThreadPool.execute(sendMessageThread);
                break;
            //接收消息
            case R.id.btn_receive_message:
                mThreadPool.execute(receiveMessageThread);

                break;
            default:
                break;
        }
    }

    /**
     * 断开客户端 & 服务器的连接
     */
    private void disconectSocket() {
        try {
            if (outputStream != null) {
                // 断开 客户端发送到服务器 的连接，即关闭输出流对象OutputStream
                outputStream.close();
            }
            if (br != null) {
                // 断开 服务器发送到客户端 的连接，即关闭输入流读取器对象BufferedReader
                br.close();

            }
            if (socket != null) {
                // 最终关闭整个Socket连接
                socket.close();


                // 判断客户端和服务器是否已经断开连接
                Log.d(TAG, "当前连接状态是：" + socket.isConnected());

            } else {
                Log.d(TAG, "socket:" + socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unBindService();
        disconectSocket();
        Log.d(TAG, "call  onDestroy");
    }
}
