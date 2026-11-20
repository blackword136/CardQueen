package com.example.text;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.util.Random;

// 链接不到使用外网的服务器
public class Tcpactivity extends AppCompatActivity  implements TcpService.MsgCallback {
    private TcpService tcpService;
    private boolean bindOk = false;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // ✅ 连接成功，拿到Binder(TcpService.onBind 的返回值)，在这里获取Service实例
            TcpService.TcpBinder binder = (TcpService.TcpBinder) service;
            tcpService = binder.getService();
            tcpService.regtcpact(Tcpactivity.this);
//            `onServiceConnected` 是异步！
//`bindService` 调用完**不会立刻拿到 service 对象**，不能紧接着直接调用 service 方法；
//            所有操作要放在 `onServiceConnected` 内部。
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
        // ❌ 服务意外断开（进程被杀、崩溃），主动unbind不会触发这个！
    };


    private int PORT = 50000;
    private TextView ip_view;
    private TextView showtcp_view;
//    private TextView show_view;
    private TextView poro_view;
    private Button btn_ip_refresh;
    private Button btnServer;
    private Button btnclient;
    private Button btn_server_over;
    private Button btn_poro;
    private TextView et_ip;
    private TextView et_poro;
    private String the_last_int;

    private Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        mainHandler = new Handler(getMainLooper());

        setContentView(R.layout.activity_tcpactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 注册返回拦截回调
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 这里空着，拦截系统返回（手势、返回键全部无效）
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        //绑定后台服务
//        Intent intent = new Intent(this, TcpService.class);
        bindService(new Intent(this, TcpService.class), connection, Context.BIND_AUTO_CREATE);
        // BIND_AUTO_CREATE 如果 Service 没启动，则自动创建 Service（最常用）
        bindOk = true;

        ip_view = findViewById(R.id.ip_view);
        ip_view.setText("ip: " + TcpService.getLocalIp());

        showtcp_view = findViewById(R.id.showtcp_view);
        poro_view = findViewById(R.id.poro_view);

        btn_ip_refresh = findViewById(R.id.btn_ip_refresh); // 刷新ip
        btn_ip_refresh.setOnClickListener(v -> {
            ip_view.setText("ip: " + TcpService.getLocalIp());
        });

        btnServer = findViewById(R.id.btn_server); // 房主的按钮
        btnServer.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
            if(tcpService == null){
                Toast.makeText(Tcpactivity.this,"tcpService还没建立完毕",Toast.LENGTH_SHORT).show();
                return;
            }
            tcpService.startServer(PORT);
        });

        btnclient = findViewById(R.id.btn_client); // 链接的按钮
        btnclient.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
            if(tcpService == null){
                Toast.makeText(Tcpactivity.this,"tcpService还没建立完毕",Toast.LENGTH_SHORT).show();
                return;
            }
            String ip = et_ip.getText().toString();
            if(TcpService.getLocalIp().equals(ip)){
                Toast.makeText(Tcpactivity.this,"不准链接自身ip",Toast.LENGTH_SHORT).show();
                return;
            }
            tcpService.connectClient(ip, PORT);
        });

        btn_server_over = findViewById(R.id.btn_server_over); // 关闭
        btn_server_over.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
//            if(tcpService == null){
//                Toast.makeText(Tcpactivity.this,"tcpService还没建立完毕",Toast.LENGTH_SHORT).show();
//                return;
//            }
//            else if(tcpService.working){
//                Toast.makeText(Tcpactivity.this,"tcpService正在建立",Toast.LENGTH_SHORT).show();
//                return;
//            }
            if(tcpService != null) {
                tcpService.cancelClientConnect();
            }
            else {
                Toast.makeText(Tcpactivity.this,"tcpService还没建立完毕",Toast.LENGTH_SHORT).show();
//                return;
            }
        });

        btn_poro = findViewById(R.id.btn_poro); // 刷新端口
        btn_poro.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
            try{
                String ip = et_poro.getText().toString();
                PORT = Integer.parseInt(ip);
                if(PORT <= 65535) poro_view.setText("当前端口:" + PORT);
                else {
                    mainHandler.post(() -> {
                        Toast.makeText(Tcpactivity.this,"端口数值过大",Toast.LENGTH_SHORT).show();
                    });
                }
            }catch (NumberFormatException e){
            }
        });

        et_ip = findViewById(R.id.et_ip);
//        et_ip.setText("10.132.98.19");
        et_ip.setText("10.140.103.203");
        et_poro = findViewById(R.id.et_poro);


        Button bt_back = findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "close");
            startService(playIntent);
            if("MainActivity".equals(the_last_int)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // 不清除本页面的情况下打开一个已经打开过的页面
            }
            else if("Game".equals(the_last_int)) {
                Intent intent = new Intent(this, Game.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                // 不清除本页面的情况下打开一个已经打开过的页面
            }
//            else finish();
        });

        Button btn_tcp_view_refresh = findViewById(R.id.btn_tcp_view_refresh);
        btn_tcp_view_refresh.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
            if(tcpService != null) {
                if(tcpService.working){
                    showtcp_view.setText("tcpService正在建立");
                }
                else {
                    switch (tcpService.currentState) {
                        case TcpService.STATE_IDLE: { // 0
                            showtcp_view.setText("暂未链接到玩家");
                            break;
                        }
                        case TcpService.STATE_SERVER: { // 1 作为房主（可以选择场景，模式）
                            showtcp_view.setText("房间创建成功");
                            break;
                        }
                        case TcpService.STATE_CLIENT: {  // 2 链接别人（需要准备）
                            showtcp_view.setText("链接成功");
//                        show_view.setText("输入对方ip");
                            break;
                        }
                    }
                }
            }
        });

        Intent intent = getIntent();
        the_last_int = intent.getStringExtra("the_last_int");
        String jsonStr = intent.getStringExtra("chat_msg_json");
        if(jsonStr != null){
//            ChatMsg the_last_msg = new Gson().fromJson(jsonStr, ChatMsg.class);
            // 在这里拿到解析好的ChatMsg，执行业务逻辑
            // 例如：ondownload(msg);
        }
        else the_last_send_msg = null;

        Button btn_game_start = findViewById(R.id.btn_game_start);
        btn_game_start.setOnClickListener(v -> {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
            if(tcpService.currentState != TcpService.STATE_IDLE) {
                if(the_last_send_msg == null || msgSeq == -1){
                    Toast.makeText(Tcpactivity.this,"请在开启游戏后按此按钮",Toast.LENGTH_SHORT).show();
                }
                else {
                    ChatMsg msgObj = new ChatMsg(100); // 100表示检测
                    msgObj.msgSeq = msgSeq;
                    tcpService.sendChatMsg(msgObj);
                }
            }
            else {
                Toast.makeText(Tcpactivity.this,"请在联网情况下按此按钮",Toast.LENGTH_SHORT).show();
            }
        });

//        TextView ip_view = findViewById(R.id.ip_view);

//        //方案1：GONE（推荐，不占位置+不可点击）
//        view.setVisibility(View.GONE);
//
////方案2：INVISIBLE + 关掉点击（保留位置，但点不动）
//        view.setVisibility(View.INVISIBLE);
//        view.setClickable(false);

    }

    // 页面已经存在，再次被调到前台，新intent会走到这里！
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 必须更新当前页面的intent引用
        setIntent(intent);
//        Intent intent = getIntent();
        the_last_int = intent.getStringExtra("the_last_int");
        String jsonStr = intent.getStringExtra("chat_msg_json");
        if(jsonStr != null){
            the_last_send_msg = new Gson().fromJson(jsonStr, ChatMsg.class);
            // 在这里拿到解析好的ChatMsg
        }
        else the_last_send_msg = null;

        // 这里修改UI完全没问题
        if(tcpService != null) {
            switch (tcpService.currentState) {
                case TcpService.STATE_IDLE: { // 0
                    showtcp_view.setText("暂未链接到玩家");

//                et_ip.setVisibility(View.GONE);
//                show_view.setVisibility(View.GONE);

                    break;
                }
                case TcpService.STATE_SERVER: { // 1 作为房主（可以选择场景，模式）
                    showtcp_view.setText("房间创建成功");

//                et_ip.setVisibility(View.VISIBLE);
//                show_view.setVisibility(View.VISIBLE);
//                show_view.setText("");

                    break;
                }
                case TcpService.STATE_CLIENT: {  // 2 链接别人（需要准备）
                    showtcp_view.setText("链接成功");

//                et_ip.setVisibility(View.VISIBLE);
//                show_view.setVisibility(View.VISIBLE);
//                    show_view.setText("输入对方ip");

                    break;
                }
            }
        }
    }

//    private TextView tvLog;
    //服务回调收到消息，主线程刷新UI

    // 在Tcpact这里，针对game做收到检测，具体体现为
    // 由于收到信息时所有页面都有收到，所以在这里检测game是否收到
    // 若未收到，则将原数据发送回去一次，让其重新发送一次
    // 若在己方未收到的情况下对方再发一次，则旧数据需要抛弃
    // 对于对方，若收到要求重新发送，但此时要发送新数据，则也需要将旧数据抛弃

    // 此中断重连仅针对游戏开始后
    // 对于tcp中断重连，只要使最后的发送中断重新发送即可
    // 不需要最近的接受的整个msg，只要获取顺序号即可

    private ChatMsg the_last_send_msg = null; // 存储上一个发送的msg
//    private ChatMsg the_last_get_msg = null; // 存储上一个接受的msg
    public int msgSeq = -1; // 0~2 -1表示例外不能用
    @Override
    public void onMessage(ChatMsg msg) {
        mainHandler.post(() -> {
            if(msg.game_state == 100) {
                if(tcpService.currentState == TcpService.STATE_IDLE || the_last_send_msg == null || msgSeq == -1){
                    return;
                }
                else if(msgSeq == msg.msgSeq + 1 || msgSeq + 2 == msg.msgSeq){
                    tcpService.sendChatMsg(the_last_send_msg);
                    //可能需要做一个刷新检测网络的方法按钮
                }
                else tcpService.sendChatMsg(new ChatMsg(100));
                return;
            }

//        Gson gson = new Gson();// 对象 -> json字符串 -> 新对象
//        the_last_get_msg = gson.fromJson(gson.toJson(msg), ChatMsg.class);
            msgSeq = msg.msgSeq; // 每次接受信息刷新

//        if(tcpact instanceof Tcpactivity){
//            Tcpactivity msg = (Tcpactivity) tcpact;
//            msg.msg_send = gson.fromJson(gson.toJson(chatMsg), ChatMsg.class);
//            // 访问game_state等ChatMsg独有成员
//        }
            if(msg.game_state > 100) msgSeq = -1;
        });


//        switch (msg.game_state){
//            case Game.STATE_SERVER_START:{ // (允许)房主打牌
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_SERVER_START",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            case Game.STATE_CLIENT_START:{ // (允许)玩家打牌
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_CLIENT_START",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            case Game.STATE_TURN_END:{ // 收到回合结束
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_TURN_END",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//
//            case Game.STATE_TOTAL_START:{ // 收到总回合开始
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_TOTAL_START",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            case Game.STATE_CLIENT_GETCARD:{ // 玩家抽牌
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_CLIENT_GETCARD",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            case Game.STATE_CLIENT_FRIST:{ // 玩家先抽牌
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_CLIENT_FRIST",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            case Game.STATE_SERVER_GETCARD:{ // 房主抽牌
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_SERVER_GETCARD",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            case Game.STATE_ALL_END:{// 收到总回合结束
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_ALL_END",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            case Game.STATE_INTEGRATE:{// 信息整合
//                mainHandler.post(() -> {
//                    Toast.makeText(Tcpactivity.this,"STATE_INTEGRATE",Toast.LENGTH_SHORT).show();
//                });
//                break;
//            }
//            default:
//                msgSeq = -1;
////            default:the_last_get_msg = null;
//        }
    }
//    public int tcp_state;
//
//    public static final int main_state_OK = 100;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bindOk) {
            unbindService(connection);
        }
    }
}

//# 完整时序梳理：从页面打开 → Service 创建 → TCP 通道就绪 → 断开
//
//>
//        > 两个层级，**一定要分开，很多 bug 都源于混淆这两个就绪**
//        > ① Service 实例就绪（Service 对象创建、拿到引用，可以注册回调、调用开启服务端 / 连接接口）
//        > ② TCP 通道就绪（Socket 成功建立、outWriter 初始化完成，**真正可以收发 ChatMsg 消息**）
//
//        ## 阶段 1：Activity 启动，bindService（页面刚创建）
//
//        1. Activity onCreate 执行 `bindService()`
//        2. Android 系统收到绑定请求，启动 TcpService，执行 `TcpService.onCreate()`
//        - Service 成员变量初始化，标记 `isServiceCreated = true`
//        - **此时：Service 对象存在，但 TCP 还没任何动作，Socket=null，outWriter=null**
//        3. Service 创建完成后，系统回调 Activity 的 `ServiceConnection.onServiceConnected()`
//        - 页面拿到 `TcpService` 实例引用
//   - 触发 `onServiceReady`，页面执行 `registerCallback(this)`
//
//        ✅ 当前状态：**Service 就绪**
//        ✅ 允许：调用开启服务端、发起连接、注册 / 注销回调、查询状态
//❌ 禁止：直接调用 `sendChatMsg`，outWriter 仍然是 null，发送会失败
//
//>
//        > 一句话：能操控 Service，但网络通道还没建立，不能发业务消息。
//
//        ## 阶段 2：二选一，开启【服务端模式】或者【客户端连接模式】
//
//        ### 分支 A：作为服务端（等待别人连进来）
//
//        1. 页面调用 `tcpService.startServer(port)`，Service 内部新开线程，创建`ServerSocket`开始监听端口
//>
//        > ServerSocket 监听成功 ≠ 通道就绪！只是在等对方接入
//2. 阻塞等待客户端 Socket 接入
//3. **远端设备成功连接进来** → ServerSocket.accept () 返回 Socket
//4. Service 拿到 Socket，初始化：
//        - `InputStream`、`BufferedReader` 接收线程 `startReceiveLoop()` 启动
//   - `OutputStream`、`PrintWriter(outWriter)` 创建成功
//   - 设置标记 `isTcpConnected = true`
//        - 调用 `notifyState("✅TCP已连接")`，分发状态回调给所有注册页面
//
//👉 **到这里，TCP 通道正式就绪，可以收发消息**
//
//        ### 分支 B：作为客户端（主动连接对方 IP 端口）
//
//        1. 页面调用 `tcpService.connect(ip,port)`，Service 新开线程执行 `socket.connect(addr,timeout)`
//        2. 网络握手：三次握手建立 Socket
//3. connect 成功返回，拿到 Socket
//4. 同样初始化 reader、outWriter，启动接收线程
//5. `isTcpConnected=true`，推送连接成功状态回调
//
//👉 **到此，TCP 通道就绪，可以收发消息**
//
//        >
//        > ⚠️ 重点：`connect()` 只是尝试握手，超时 / IP 错误会直接失败，不会进入就绪状态。
//
//        ## 阶段 3：通道就绪后，正常收发消息
//
//1. 发送：页面调用 `sendChatMsg`，内部加锁，Gson 转 JSON，追加`\n`写入 outWriter
//2. 接收：后台`readLine()`循环持续读取一行 JSON → Gson 解析 ChatMsg → 分发到所有注册页面回调
//3. 只要 Socket 不断开：随时可以收发，多页面并发发送由 sendLock 排队，避免报文穿插
//
//## 阶段 4：通道断开（任意一种断开场景）
//
//触发断开的场景：
//
//        - 远端主动关闭 Socket
//- 网络断开、WiFi 切走
//- 手动调用关闭连接代码
//- 读写流异常抛出异常
//
//1. 接收循环捕获异常，进入 catch 块
//2. 关闭 reader、Socket，`outWriter = null`
//        3. `isTcpConnected = false`
//        4. 推送状态回调 `notifyState("🔴连接断开")`
//
//        ✅ Service 实例还在，仍然可以注册回调；
//        ❌ TCP 通道失效，不能发送消息，调用 sendChatMsg 会提示发送失败。
//
//        >
//        > 此时你可以重新调用 startServer 或者 connect，重建 TCP 通道，不需要重新 bindService。
//
//        ## 阶段 5：页面退出 / Service 销毁
//
//1. Activity finish，onDestroy 执行 unRegisterCallback、unbindService
//   - 解绑只是解除页面和 Service 的引用，**Service 不一定立刻销毁，TCP 通道还可能保留**
//        2. 所有页面全部解绑后，Service 才会被系统回收，执行`onDestroy`
//        3. Service onDestroy：主动关闭 Socket、释放所有流，TCP 通道彻底销毁
//
//# 关键时间点对照表
//
//        表格
//
//| 时间点 | isServiceCreated | isTcpConnected | outWriter | 可以做什么 | 不能做 |
//        | --- | --- | --- | --- | --- | --- |
//        | bindService 之后，onServiceConnected 前 | true（Service 已创建） | false | null | 无（页面拿不到 service 引用） | 任何调用 |
//        | onServiceConnected 回调后 | true | false | null | 注册回调、开启服务端 / 发起连接、查询状态 | sendChatMsg 发业务消息 |
//        | ServerSocket 监听成功，还没有客户端接入 | true | false | null | 关闭服务端 | 收发消息 |
//        | Socket 握手成功，流初始化完成 | true | true | 非 null | ✅正常收发 ChatMsg | - |
//        | Socket 断开，Service 还存活 | true | false | null | 重连、注册回调 | 发送消息 |
//        | Service onDestroy 销毁 | false | false | null | 全部资源释放 | 任何操作 |
//
//        # 容易踩坑的时序 bug
//
//1. **拿到 Service 实例，立刻发消息**
//onServiceConnected 只代表 Service 对象就绪，Socket 还没建立 → outWriter=null，发送失败。
//        ✅ 正确：等待收到 `onConnectionStateChange("✅TCP已连接")` 之后再启用发送按钮。
//        2. **服务端开启成功，就认为通道就绪**
//startServer 成功只是开启监听，没人连接时，没有 Socket，通道不存在。
//必须等到远端接入，拿到 Socket，才是就绪。
//        3. **连接断开后，Service 还在，误以为通道还能用**`isTcpConnected`变为 false，outWriter 置空；页面需要根据状态，禁用发送按钮。
//        4. 页面在收到`✅TCP已连接`回调前，点击发送按钮
//建议 UI 层面：发送按钮默认禁用，收到连接成功回调后，再启用按钮；断开时禁用。