package mytool.yixin.navinfo.com.navigation;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mytool.yixin.navinfo.com.navigation.bean.LaneModel;
import mytool.yixin.navinfo.com.navigation.bean.NaviDataChangeEventInfo;
import mytool.yixin.navinfo.com.navigation.bean.TmcSections;
import mytool.yixin.navinfo.com.navigation.controller.NaviController;
import mytool.yixin.navinfo.com.navigation.controller.RoadLineController;
import mytool.yixin.navinfo.com.navigation.manager.RoadLineManager;
import mytool.yixin.navinfo.com.navigation.utils.GlobalUtil;
import mytool.yixin.navinfo.com.navigation.utils.Utils;
import mytool.yixin.navinfo.com.navigation.view.ArLaneLineView;
import mytool.yixin.navinfo.com.navigation.view.MapTmcView;
import mytool.yixin.navinfo.com.navigation.view.NaviTitleView;
import mytool.yixin.navinfo.com.screenshotserver.IImagePath;

import static mytool.yixin.navinfo.com.navigation.utils.BitmapUtils.getDiskBitmap;

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
    private View btnApp;
    private NaviTitleView naviTitleView;
    private MapTmcView mapTmcView;
    ArLaneLineView laneLineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalUtil.setMainActivity(this);
        setContentView(R.layout.activity_main);
        initView();
        init();

    }

    private void initView() {
        naviTitleView = findViewById(R.id.naviTitleView);
        mapTmcView = findViewById(R.id.mapTmcView);
        laneLineView = findViewById(R.id.roadLineView);
        btnBindService = findViewById(R.id.btn_bind_service);
        btnUnbindService = findViewById(R.id.btn_unbind_service);
        btnApp = findViewById(R.id.btn_app);
        btnApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("wedrive.navigation:"));
//
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("nav:%s,%f,%f", "天安门", 116.397399, 39.908870)));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ArrayList<String> viaPois = new ArrayList<>(3);
                viaPois.add(String.format("%s,%f,%f", "复兴门", 116.3565444946, 39.9071685451));
                viaPois.add(String.format("%s,%f,%f", "王府井", 116.4115619659, 39.9080244523));
                viaPois.add(String.format("%s,%f,%f", "前门", 116.3979578018, 39.8999587488));
                intent.putStringArrayListExtra("viaPois", viaPois);
                intent.setComponent(new ComponentName("com.mapbar.android.mapbarmap", "com.mapbar.android.MainActivity"));
                startActivity(intent);
            }
        });
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

    }

    private static final String FIELD_COMMAND = "command";

    private JSONObject test(String welinkStateStr) {
        //整体对象
        JSONObject stateJson = null;
        try {
            stateJson = new JSONObject(welinkStateStr);
            //command 对象
            JSONObject jsonObject = stateJson.optJSONObject(FIELD_COMMAND);
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stateJson;

    }


    private static final String ACTION_SEND_NAVI_DATA = "com.wedrive.action.NAVI_COMMAND_RESULT";
    private static final String ACTION_SEND_TMC_DATA = "com.wedrive.action.TMC_COMMAND_RESULT";
    private static final String ACTION_SEND_ROAD_LINE_DATA = "com.wedrive.action.ROAD_LINE_COMMAND_RESULT";
    private static final String EXTRA_WELINK = "com.wedrive.extra.COMMAND_DATA";
    private static final String EXTRA_SHOW_ROAD_LINE = "com.wedrive.extra.SHOW_ROAD_LINE";



    NaviController naviController = NaviController.getInstance();
    RoadLineManager roadLineManager = RoadLineManager.getInstance();

    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(ACTION_SEND_NAVI_DATA);
        registerReceiver(naviDataReceiver, iFilter);
        // 注册广播
        IntentFilter iFilter2 = new IntentFilter();
        iFilter2.addAction(ACTION_SEND_TMC_DATA);
        registerReceiver(tmcDataReceiver, iFilter2);
        super.onResume();
        // 注册广播
        IntentFilter iFilter3 = new IntentFilter();
        iFilter3.addAction(ACTION_SEND_ROAD_LINE_DATA);
        registerReceiver(roadLineDataReceiver, iFilter3);

    }



    BroadcastReceiver naviDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(ACTION_SEND_NAVI_DATA)) {
                Bundle extras = intent.getExtras();
                String data = (String) extras.get(EXTRA_WELINK);
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    String moduleName = jsonObject.optString("moduleName");
                    String version = jsonObject.optString("version");
                    JSONObject commandJson = jsonObject.optJSONObject("command");
                    String method = commandJson.optString("method");
                    JSONObject extDataJson = commandJson.optJSONObject("extData");
                    if (extDataJson != null) {
                        System.out.println(extDataJson.toString());
                        NaviDataChangeEventInfo naviDataChangeEventInfo = wrapData(extDataJson);
                        naviController.setNaviDataInfo(naviDataChangeEventInfo);
                        naviTitleView.update();
                        mapTmcView.updateUI();
                    } else {
                        naviTitleView.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "恭喜到达目的地", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    };


    BroadcastReceiver roadLineDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("接收到Roadline的广播");
            String action = intent.getAction();
            if (action.equalsIgnoreCase(ACTION_SEND_ROAD_LINE_DATA)) {
                RoadLineController roadLineController = RoadLineController.getInstance();
                Bundle extras = intent.getExtras();
                String data = (String) extras.get(EXTRA_WELINK);
                boolean showRoadLine = (Boolean) extras.get(EXTRA_SHOW_ROAD_LINE);
                if (showRoadLine) {
                    LaneModel mLaneModel = new Gson().fromJson(data, LaneModel.class);
                    System.out.println("mLaneModel 的值 ：" + mLaneModel.toString());

                    RoadLineManager.LaneType[] laneTypes = roadLineManager.parser(mLaneModel);

                    //设置数据
                    roadLineController.setmShowRoadLine(true);
                    roadLineController.setLaneTypes(laneTypes);
                    roadLineController.update();
                    //更新UI
                    laneLineView.notifyLaneLineChange();
                    laneLineView.setVisibility(View.VISIBLE);
                } else {
                    roadLineController.setmShowRoadLine(false);
                    laneLineView.setVisibility(View.GONE);
                }


            }
        }
    };
    BroadcastReceiver tmcDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("接收到Tmc的广播");
            String action = intent.getAction();
            if (action.equalsIgnoreCase(ACTION_SEND_TMC_DATA)) {
                Bundle extras = intent.getExtras();
                String data = (String) extras.get(EXTRA_WELINK);
                TmcSections tmcSections = new Gson().fromJson(data, TmcSections.class);
                System.out.println("tmcSections 的值 ：" + tmcSections.toString());
                naviController.setTmcSections(tmcSections);
                mapTmcView.updateUI();

            }
        }
    };

    /**
     * 封装数据
     */
    private NaviDataChangeEventInfo wrapData(JSONObject extDataJson) {
        String name = extDataJson.optString("name");
        String nextName = extDataJson.optString("nextName");
        String distanceToCurrPoint = extDataJson.optString("distanceToCurrPoint");
        String direction = extDataJson.optString("direction");
        int icon = extDataJson.optInt("icon");
        int totalDistance = extDataJson.optInt("totalDistance");
        int distanceToEnd = extDataJson.optInt("distanceToEnd");
        String remainDistance = extDataJson.optString("remainDistance");
        String remainTime = extDataJson.optString("remainTime");
        String currSpeed = extDataJson.optString("currSpeed");
        double percentToCurrPoint = extDataJson.optDouble("percentToCurrPoint");
        NaviDataChangeEventInfo naviDataChangeEventInfo = new NaviDataChangeEventInfo();
        naviDataChangeEventInfo.setName(name);
        naviDataChangeEventInfo.setNextName(nextName);
        naviDataChangeEventInfo.setDistanceToCurrPoint(distanceToCurrPoint);
        naviDataChangeEventInfo.setDirection(direction);
        naviDataChangeEventInfo.setIcon(icon);
        naviDataChangeEventInfo.setRemainDistance(remainDistance);
        naviDataChangeEventInfo.setRemainTime(remainTime);
        naviDataChangeEventInfo.setCurrSpeed(currSpeed);
        naviDataChangeEventInfo.setPercentToCurrPoint(percentToCurrPoint);
        naviDataChangeEventInfo.setTotalDistance(totalDistance);
        naviDataChangeEventInfo.setDistanceToEnd(distanceToEnd);
        return naviDataChangeEventInfo;
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
//                    String imagePath = bundle.getString("path");
//                    byte[] imageBytes = bundle.getByteArray("path");
                    Bitmap bitmap = (Bitmap) bundle.get("transferBitmap");
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    }

//                    if (imageBytes == null) {
//                        //停止截图
//                        unBindService();
//                    } else {
//                        textPath.setText("图片字节流是： " + imageBytes);
////                        Bitmap transferBitmap = getDiskBitmap(imagePath);
//
//                        long beforeTime = System.currentTimeMillis();
//                        System.out.println("set image start time is:" + beforeTime);
//                        Bitmap transferBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//                        if (transferBitmap != null) {
//                            imageView.setImageBitmap(transferBitmap);
//                            long afterTime = System.currentTimeMillis();
//                            System.out.println("set image end time is:" + afterTime);
//                            System.out.println("set image all time is:" + (afterTime - beforeTime));
//                        } else {
//                            Log.d(TAG, "transferBitmap" + transferBitmap);
//                        }
//                    }


                    break;
                case 1:


                    textPath.setText("图片地址是： " + response);

                    if (TextUtils.isEmpty(response)) {
                        //停止截图
                        disconectSocket();
                    } else {
                        Bitmap bitmap2 = getDiskBitmap(response);
                        if (bitmap2 != null) {
                            imageView.setImageBitmap(bitmap2);
                        } else {
                            Log.d(TAG, "transferBitmap" + bitmap2);
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

        private Bitmap transferBitmap;

        @Override
        public void run() {
            try {
//                String imagePath = mBinder.getImagePath();

//                String bitmapStr = mBinder.getBitmapStr();
//                int length = bitmapStr.getBytes().length;
//                System.out.println("bitmapStr is:" + bitmapStr);
//                System.out.println("bitmapStr length is:" + length);
//                System.out.println("imageBytes size is:" + getNetFileSizeDescription(length));


//                byte[] imageBytes = mBinder.getImageBytes();
//                Bitmap bitmap1 = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//                int byteCount = bitmap1.getByteCount();
//                System.out.println("imageBytes length is:" + getNetFileSizeDescription(byteCount));

                int naviState = mBinder.getNaviState();
                switch (naviState) {
                    case 0:
                        break;
                    case 1:
                        long beforeTime = System.currentTimeMillis();
                        transferBitmap = mBinder.getBitmap();
                        System.out.println("transfer all time is:" + (System.currentTimeMillis() - beforeTime));
                        if (transferBitmap == null) {
                            System.out.println("transferBitmap is null");

                            return;
                        } else {
                            System.out.println("transferBitmap is not null");
                        }
                        int bitmapSize = getBitmapSize(transferBitmap);
                        String size = Utils.getFileSizeDescription(bitmapSize);
                        System.out.println("bitmapSize is:" + size);

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(transferBitmap);
                                mHandler.postDelayed(this, 16);
                            }
                        });
                        System.out.println("transfer and set transferBitmap time is :" + (System.currentTimeMillis() - beforeTime));
                        break;
                    case 2:
                        //导航结束
                        imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background));
                        break;
                    default:
                        break;
                }
                if (isBind) {
                    //每隔1s循环执行run方法
                    mHandler.postDelayed(this, 40);
                }


            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
    };

    public int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)   //API 19

        {
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)  //API 12

        {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }


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

        Intent serviceIntent = new Intent().setComponent(new ComponentName("com.mapbar.basedemo", "com.mapbar.basedemo.shot.ScreenShotService"));
        serviceIntent.putExtra("from", "client");
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBind = true;
            mBinder = IImagePath.Stub.asInterface(service);
            Log.i(TAG, "bind service success ");

            mHandler.post(runnable);


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
//                    // 步骤1：创建输入流对象InputStream
//                    InputStream is = socket.getInputStream();
//
//                    // 步骤2：创建输入流读取器对象 并传入输入流对象
//                    // 该对象作用：获取服务器返回的数据
//                    InputStreamReader isr = new InputStreamReader(is);
//                    br = new BufferedReader(isr);
//                    // 步骤3：通过输入流读取器对象 接收服务器发送过来的数据
//                    response = br.readLine();
//                    Log.d(TAG, "response:" + response);
//                    // 步骤4:通知主线程,将接收的消息显示到界面
//                    Message msg = Message.obtain();
//                    msg.what = 1;
//                    mHandler.sendMessage(msg);

                    InputStream in = socket.getInputStream();
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    System.err.println("start");
                    while ((len = in.read(buffer)) != -1) {
                        output.write(buffer, 0, len);
                    }
                    System.err.println("end");
                    byte[] bytes = output.toByteArray();
                    System.err.println(new String(output.toByteArray()));


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
        unregisterReceiver(naviDataReceiver);
        unregisterReceiver(tmcDataReceiver);
        unregisterReceiver(roadLineDataReceiver);
        unBindService();
        disconectSocket();
        Log.d(TAG, "call  onDestroy");
    }
}
