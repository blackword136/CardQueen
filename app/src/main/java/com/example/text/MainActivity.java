package com.example.text;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.LinearLayout;
import android.widget.TextView;

import android.graphics.Color;
import android.view.Gravity; // 文字排版
import android.util.Log; // 打印
import android.widget.HorizontalScrollView;
import android.view.ViewTreeObserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import android.view.MotionEvent; // totouch
import android.view.ViewGroup; // ViewGroup
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements TcpService.MsgCallback {
    private TcpService tcpService;
    private boolean bindOk = false;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // ✅ 连接成功，拿到Binder(TcpService.onBind 的返回值)，在这里获取Service实例
            TcpService.TcpBinder binder = (TcpService.TcpBinder) service;
            tcpService = binder.getService();
            tcpService.registerPage2(MainActivity.this);
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
        // ❌ 服务意外断开（进程被杀、崩溃），主动unbind不会触发这个！
    };
    private Handler mainHandler;
    private TextView tcp_end;
    private TextView view_mode;
    private TextView prepare_game;
    private Button game_start;
    private String mode_str = null; // 需要不处于玩家联机期间收到信息时，一直处于null

    private boolean client_prepare = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定后台服务
//        Intent intent = new Intent(this, TcpService.class);
        bindService(new Intent(this, TcpService.class), connection, Context.BIND_AUTO_CREATE);
        // BIND_AUTO_CREATE 如果 Service 没启动，则自动创建 Service（最常用）
        bindOk = true;

        mainHandler = new Handler(getMainLooper());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // 横屏常用：只上下避让，左右不增加padding
            v.setPadding(0, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 播放背景音乐
        Intent playIntent_cmd = new Intent(this, BgmService.class);
        playIntent_cmd.putExtra("CMD", "PLAY");
        startService(playIntent_cmd);

        tcp_end = findViewById(R.id.tcp_end);
        view_mode = findViewById(R.id.view_mode);
        prepare_game = findViewById(R.id.prepare_game);

        // 在做投降后，做链接时目标不在游戏内的情况判断
        Button game_tcp = findViewById(R.id.game_tcp);
        game_tcp.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "ok2");
            startService(playIntent);

            Intent intent = new Intent(MainActivity.this, Tcpactivity.class);
            intent.putExtra("the_last_int", "MainActivity");

            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        game_start = findViewById(R.id.game_start);
        game_start.setOnClickListener(v -> {
            // 还得做一个检测模式是否相等
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "game_start");
            startService(playIntent);

            if(tcpService.currentState == TcpService.STATE_SERVER) {
                if(!client_prepare) {
                    Toast.makeText(MainActivity.this,"请催对方点击准备",Toast.LENGTH_SHORT).show();
                }
                else {
                    client_prepare = false;
                    prepare_game.setText("等待对方准备游戏开始");
                    tcpService.sendChatMsg(new ChatMsg(main_state_OK));
                    // 跳转页面
                    Intent intent = new Intent(MainActivity.this, Game.class);
                    startActivity(intent);
                }
            }
            else if(tcpService.currentState == TcpService.STATE_CLIENT) {
                client_prepare = !client_prepare;
                if(client_prepare) {
                    prepare_game.setText("已经准备完毕");
                    game_start.setText("取消准备");
                    tcpService.sendChatMsg(new ChatMsg(main_state_prepare)); // (玩家)链接段准备完毕
                }
                else {
                    game_start.setText("准备游戏");
                    prepare_game.setText("尚未准备游戏开始");
                    tcpService.sendChatMsg(new ChatMsg(main_state_prepare_not)); // (玩家)链接段取消准备完毕
                }
            }
        });
        Button choose_skills = findViewById(R.id.choose_skills);
        choose_skills.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "ok2");
            startService(playIntent);
                // 跳转页面
            Intent intent = new Intent(MainActivity.this, Choose_Skills.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        Button choose_mode = findViewById(R.id.choose_mode);
        choose_mode.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "ok2");
            startService(playIntent);

            if(tcpService == null || tcpService.currentState == TcpService.STATE_CLIENT){
                Toast.makeText(MainActivity.this,"作为玩家设置模式没有用哦",Toast.LENGTH_SHORT).show();
            }
            // 跳转页面
            Intent intent = new Intent(MainActivity.this, Choose_Mode.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        Button bt_bgm = findViewById(R.id.bt_bgm);
        bt_bgm.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SWITCH");
            startService(playIntent);


        });

    }
// 不能发送默认模式，需要额外做一个

    // 页面已经存在，再次被调到前台，新intent会走到这里！
    @Override
    protected void onNewIntent(Intent intent) { // 只有使用 startActivity 才会触发这个哦
        super.onNewIntent(intent);
        // 必须更新当前页面的intent引用
        setIntent(intent);



        String str = null;
        if(intent.getStringExtra("Choose_Skills") != null){ // 是从选择技能页面返回
            String str1 = intent.getStringExtra("sk_str_1");
            if(str1 == null) ((TextView)findViewById(R.id.view_sk_1)).setText("");
                else ((TextView)findViewById(R.id.view_sk_1)).setText(str1);
            String str2 = intent.getStringExtra("sk_str_2");
            if(str2 == null) ((TextView)findViewById(R.id.view_sk_2)).setText("");
                else ((TextView)findViewById(R.id.view_sk_2)).setText(str2);
            String str3 = intent.getStringExtra("sk_str_3");
            if(str3 == null) ((TextView)findViewById(R.id.view_sk_3)).setText("");
                else ((TextView)findViewById(R.id.view_sk_3)).setText(str3);
            String str4 = intent.getStringExtra("sk_str_4");
            if(str4 == null) ((TextView)findViewById(R.id.view_sk_4)).setText("");
                else ((TextView)findViewById(R.id.view_sk_4)).setText(str4);
        }

        if(mode_str == null) {
            str = intent.getStringExtra("mode_str");
            if (str != null) {
                if (str.isEmpty()) view_mode.setText("当前为默认模式");
                else view_mode.setText("当前模式: " + str);
            }
//                else if (Choose_Mode.mode_create == null)
//                    view_mode.setText("当前为默认模式");
        }
        else {
            if (mode_str.isEmpty()) view_mode.setText("当前为默认模式");
            else view_mode.setText("当前模式: " + mode_str);
        }
        // 这里修改UI完全没问题
        if(tcpService != null) {
            if(tcpService.working){
                mode_str = null;
                tcp_end.setText("tcpService正在建立");
//                Toast.makeText(MainActivity.this,"tcpService正在建立",Toast.LENGTH_SHORT).show();
            }
            else {
                switch (tcpService.currentState) {
                    case TcpService.STATE_IDLE: { // 0
                        mode_str = null;
                        client_prepare = false;
                        game_start.setText("游戏开始");
                        tcp_end.setText("暂未链接到玩家");
                        prepare_game.setText("");
                        break;
                    }
                    case TcpService.STATE_SERVER: { // 1 作为房主（可以选择场景，模式）
                        tcp_end.setText("房间创建成功");
                        game_start.setText("游戏开始");
                        if(client_prepare)
                            prepare_game.setText("对方准备完毕");
                        else
                            prepare_game.setText("等待对方准备游戏开始");

                        if(str != null && !str.isEmpty()) {
                            Choose_Mode.mode_choose(this, str);
                            ChatMsg msg = new ChatMsg(main_set_mode);
                            msg.memory.put("main_set_mode", str);
                            tcpService.sendChatMsg(msg);
//                            if (Choose_Mode.mode_str.isEmpty())
//                                view_mode.setText("当前为默认模式");
//                            else
//                                view_mode.setText("当前模式: " + Choose_Mode.mode_str);
//                            mode_str = null;
                        }
                        break;
                    }
                    case TcpService.STATE_CLIENT: {  // 2 玩家端
                        tcp_end.setText("链接成功");
                        if(client_prepare){
                            game_start.setText("取消准备");
                            prepare_game.setText("已经准备完毕");
                        }
                        else{
                            game_start.setText("准备游戏");
                            prepare_game.setText("尚未准备游戏开始");
                        }

//                        if(Choose_Mode.mode_str.isEmpty())
//                            view_mode.setText("当前为默认模式");
//                        else
//                            view_mode.setText("当前模式: " + Choose_Mode.mode_str);

//                        show_view.setText("输入对方ip");
                        break;
                    }
                }
            }
        }
        else mode_str = null;
    }



    // 链接完成后，玩家只能等待房主点击开始
    @Override
    public void onMessage(ChatMsg msg) {
        mainHandler.post(()->{
            switch (msg.game_state){
                case main_state_OK:{
                    if(tcpService.currentState == TcpService.STATE_SERVER){
                    }
                    else if(tcpService.currentState == TcpService.STATE_CLIENT){
                        client_prepare = false;
                        game_start.setText("准备游戏");
                        prepare_game.setText("尚未准备游戏开始");
                        // 跳转页面
                        Intent intent = new Intent(MainActivity.this, Game.class);
                        startActivity(intent); // 进入游戏，由房主发送信息触发
                    }
                    break;
                }
                case main_set_mode:{
                    if(tcpService.currentState == TcpService.STATE_SERVER){
                    }
                    else if(tcpService.currentState == TcpService.STATE_CLIENT){
                        String str = (String)msg.memory.get("main_set_mode");
                        if(str != null) {
                            Choose_Mode.mode_choose(this, str);
                            if(isPageForeground) {
                                if(str.isEmpty())
                                    view_mode.setText("当前为默认模式");
                                else
                                    view_mode.setText("当前模式: " + str);
                            }
                            mode_str = str;
                        }

    //                    Intent intent = new Intent(MainActivity.this, Game.class);
    //                    startActivity(intent);
                    }
                    break;
                }
                case main_state_prepare: {  // (玩家)链接段准备完毕  只有玩家发，房主收
                    client_prepare = true;
                    if(isPageForeground) {
                        prepare_game.setText("对方准备完毕");
                    }
                    Toast.makeText(MainActivity.this,"对方准备完毕",Toast.LENGTH_SHORT).show();
                    break;
                }
                case main_state_prepare_not: { // (玩家)链接段取消准备完毕  只有玩家发，房主收
                    client_prepare = false;
                    if(isPageForeground){
                        prepare_game.setText("等待对方准备游戏开始");
                    }
                    Toast.makeText(MainActivity.this,"等待对方准备游戏开始",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });
    }
//    public int main_state;

    public static final int main_state_OK = 100; // OK
    public static final int main_state_prepare = 101; // (玩家)链接段准备完毕
    public static final int main_state_prepare_not = 102; // (玩家)链接段取消准备完毕
    public static final int main_set_mode = 110; // set_mode


    // 房主点击开始游戏后，创建game并向玩家发送可以了的信息
    // 玩家创建game后，在里面并向房主发送 STATE_TOTAL_START 的信息

    private boolean isPageForeground = false; // 当前页面双方处于最上层

    @Override
    protected void onResume() {
        super.onResume();
        isPageForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPageForeground = false;
    }

    @Override
    protected void onDestroy() {
        if(bindOk) {
            unbindService(connection);
        }
        super.onDestroy();
    }
}
