package zzy.test.service;

import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "2";
    private ImageView imageView;

    private TextView get_text;
    private Button button;
    private SocketServerHelper server;
    private EditText edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.image_view);

        get_text = (TextView) findViewById(R.id.get_text);
        button = (Button) findViewById(R.id.button);
        edit_text = (EditText) findViewById(R.id.edit_text);


        server = new SocketServerHelper(12310);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**socket发送数据*/
                server.sendMessage(edit_text.getText().toString());
            }
        });
        /**socket服务端开始监听*/
        server.beginListen();

        /**socket收到消息线程*/
        SocketServerHelper.ServerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                get_text.setText("接收到的数据是："+msg.obj.toString());
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        /**socket服务端开始监听*/
        server.beginListen();
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationX", 0f, -300f, 300f, -400f, 500f, 0f);
        animator.setDuration(20000);
        animator.start();
    }

    ScreenShotService.SaveImageBinder saveImageBinder;
    boolean flag = false;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i(TAG, " service success ");
            // TODO Auto-generated method stub
            saveImageBinder = (ScreenShotService.SaveImageBinder) service;
            ScreenShotService service2 = saveImageBinder.getService();
            /**
             * 实现回调，开始截图
             */
            service2.setCallBack(new ScreenShotService.CallBack() {
                @Override
                public String callback() {
                    while (!flag) {
                        flag = true;
                        Log.i("zzy", "开始截图，并保存");
                        new SaveImageAsyncTask().execute();
                        Log.i("zzy", "开始截图，保存成功");
                        return imagePath;
                    }

                    return null;
                }
            });


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i(TAG, " service disconnected ");

        }
    };


    String imagePath;

    /**
     * 保存图片的子线程
     */
    private class SaveImageAsyncTask extends AsyncTask<View, Void, String> {

        @Override
        protected String doInBackground(View... views) {
            Bitmap screenShotBitmap = ScreenShotHelper.getBitmapFromView(views[0]);
            if (Environment.getExternalStorageState().
                    equals(Environment.MEDIA_MOUNTED)) {
                TimeString ts = new TimeString();

                String timeString = ts.getTimeString() + ".png";
                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "11" + File.separator + timeString;

                File file = new File(filePath);

                try {
                    FileOutputStream outputStream = new FileOutputStream(file);
                    screenShotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    //根据自己需求，如果外边对bitmp还有别的需求就不要recycle的
                    screenShotBitmap.recycle();
                    return filePath;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            imagePath = s;
            if (TextUtils.isEmpty(s)) {
                flag = false;
            }
        }
    }


}
