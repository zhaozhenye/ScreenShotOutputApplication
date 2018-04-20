package mytool.yixin.navinfo.com.screenshotoutputapplication;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import mytool.yixin.navinfo.com.screenshotserver.IImagePath;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private ImageView imageView;
    private Button btnBindService;
    private Button btnUnbindService;
    private TextView textPath;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {


            switch (msg.what) {
                case 0:
                    Bundle bundle = msg.getData();
                    String imagePath = bundle.getString("path");
                    Log.d(TAG,"imagePath:-->"+imagePath);
                    textPath.setText("图片地址是： "+imagePath);

                    Bitmap bitmap = getDiskBitmap(imagePath);
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Log.d(TAG, "bitmap" + bitmap);
                    }


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
            //do omething
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
            //每隔1s循环执行run方法
            mHandler.postDelayed(this, 5000);
        }
    };


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

    }

    private boolean isBind = false;

    /**
     * 解绑服务
     */
    private void unBindService() {
        if (isBind) {
            unbindService(serviceConnection);
            isBind = false;
        }


    }

    private IImagePath mBinder;

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

            mHandler.postDelayed(runnable, 100);//延时100毫秒


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, " service disconnected ");

        }
    };

    //    Bitmap bitmap = getDiskBitmap(Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"test1.png");
    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return bitmap;
    }
}
