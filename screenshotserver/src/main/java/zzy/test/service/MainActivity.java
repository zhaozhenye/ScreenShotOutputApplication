package zzy.test.service;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "2";
    private ImageView imageView;

    private TextView get_text;
    private Button button;
    private SocketServerHelper server;
    private EditText edit_text;
    public static final int SOCKET_PORT = 12310;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initServerSocket();






    }


    /**
     * 初始化socket
     */
    private void initServerSocket() {

        server = new SocketServerHelper(SOCKET_PORT);

        /**socket服务端开始监听*/
        server.beginListen();

        /**socket收到消息线程*/
        SocketServerHelper.ServerHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                get_text.setText("接收到的数据是：" + msg.obj.toString());
            }
        };


    }

    /**
     * 初始化view
     */
    private void initView() {
        imageView = findViewById(R.id.image_view);
        get_text = (TextView) findViewById(R.id.get_text);
        button = (Button) findViewById(R.id.button);
        edit_text = (EditText) findViewById(R.id.edit_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**socket发送数据*/
                server.sendMessage(edit_text.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationX", 0f, -300f, 300f, -400f, 500f, 0f);
        animator.setDuration(5000);
        animator.setRepeatCount(100);
        animator.start();
    }


}
