package com.example.text;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Enumeration;

import com.google.gson.Gson;

// 可以考虑在这页代码新增一个传递给其他页面的方法

public class TcpService extends Service {
    // 新增Gson实例
    private final Gson gson = new Gson();
    public static final int STATE_IDLE = 0;
    public static final int STATE_SERVER = 1; // 作为房主（可以选择场景，模式）
    public static final int STATE_CLIENT = 2; // 链接别人（需要准备）
    public volatile int currentState = STATE_IDLE; // 当前联机状态
    private Socket tempSocket; // 客户端握手阶段临时socket，用于外部取消
    private Socket socket;
    private ServerSocket serverSocket = null;
    private OutputStreamWriter outWriter;
    private Thread serverThread;
    private Thread clientConnectThread;

    private Handler mainHandler; // getMainLooper() 要用this,所以要丢到构造函数初始化
     // Toast 不能在子线程运行，所以要转到主线程
     @Override
     public void onCreate() {
         super.onCreate();
         // onCreate 里面上下文已经准备好，在这里初始化Handler
         mainHandler = new Handler(getMainLooper());
     }

//    public class data{
//
//        protected void onCreate(int ){
//
//        }
//    }
    public interface MsgCallback {
        void onMessage(ChatMsg msg);
    }

    public class TcpBinder extends Binder {
        public TcpService getService() {
            return TcpService.this;
        }
    }
    private final TcpBinder binder = new TcpBinder();

    private MsgCallback tcpact;
    private MsgCallback page2Callback;
    private MsgCallback page3Callback;
    public void regtcpact(MsgCallback cb) {
        tcpact = cb;
    }
    public void registerPage2(MsgCallback cb) {
        page2Callback = cb;
    }

    public void registerPage3(MsgCallback cb) {
        page3Callback = cb;
    }
    private void notifyAllPage(ChatMsg chatMsg) {
        // 由于套娃，onMessage都不是主线程运行的
        if(page3Callback != null)
        {
            page3Callback.onMessage(chatMsg);
//            mainHandler.post(() -> {
//                Toast.makeText(TcpService.this,"page3Callback != null",Toast.LENGTH_SHORT).show();
//            });
        }
//        else{
//            mainHandler.post(() -> {
//                Toast.makeText(TcpService.this,"page3Callback != null",Toast.LENGTH_SHORT).show();
//            });
//        }
        if(page2Callback != null) page2Callback.onMessage(chatMsg);
        if(tcpact != null) {
            tcpact.onMessage(chatMsg);
        }
    }

    public boolean working = false;
    // ========== 客户端：主动连接对方 ==========
    public void connectClient(String ip, int port) {
        if(currentState != STATE_IDLE){
//            notifyAllPage("⚠不能操作！已经开启服务端/已有连接");
            //长时间显示(约3.5秒) LENGTH_LONG  / 短 LENGTH_SHORT 2秒
            mainHandler.post(() -> {
                Toast.makeText(TcpService.this,"⚠不能操作！已经开启服务端/已有连接",Toast.LENGTH_SHORT).show();
            });
            return;
        }
        currentState = STATE_CLIENT; // 链接别人
        clientConnectThread = new Thread(() -> {
            working = true;
            tempSocket = new Socket();
            try {
                // 设置连接超时，防止卡死
                tempSocket.connect(new java.net.InetSocketAddress(ip, port),4000); // 等待点
                socket = tempSocket;
                tempSocket = null; // 握手成功，临时socket置空
                outWriter = new OutputStreamWriter(socket.getOutputStream());
                mainHandler.post(() -> {
                    Intent playIntent = new Intent(this, BgmService.class);
                    playIntent.putExtra("CMD", "SOUND");
                    playIntent.putExtra("SE", "ok2");
                    startService(playIntent);
                    Toast.makeText(TcpService.this,"✅客户端连接成功",Toast.LENGTH_SHORT).show();
                });
                startReceiveLoop(socket);
            } catch (Exception e) {
                mainHandler.post(() -> {
                    Toast.makeText(TcpService.this,"❌连接失败/已取消:" + e.getMessage(),Toast.LENGTH_LONG).show();
                });
                try{
                    if(tempSocket != null) tempSocket.close(); // 释放 tempSocket
                }catch (Exception ignored){}
                currentState = STATE_IDLE;
            }
            working = false;
        });
        clientConnectThread.start();
    }

    // ========== 开启服务端 ==========
    public void startServer(int port) {
        if(currentState != STATE_IDLE){
            mainHandler.post(() -> {
                Toast.makeText(TcpService.this,"⚠不能操作！已经作为客户端连接别人",Toast.LENGTH_SHORT).show();
            });
            return;
        }
        currentState = STATE_SERVER; // 作为房主
        serverThread = new Thread(() -> {
            working = true;
            try{
                serverSocket = new ServerSocket(port);
//                serverSocket.setSoTimeout(5000); // 每5秒超时一次
                mainHandler.post(() -> {
                    Toast.makeText(TcpService.this,"服务端启动成功，等待接入",Toast.LENGTH_SHORT).show();
                });
                // 阻塞等待客户端接入
                socket = serverSocket.accept(); // 等待点 close应该可以中断
                // `accept()` 只执行一次，所以是**单客户端服务端**
                outWriter = new OutputStreamWriter(socket.getOutputStream());
                // 从 Socket 中拿到输出流，包装成字符流，用于后续发送 JSON 消息。
                mainHandler.post(() -> {
                    Intent playIntent = new Intent(this, BgmService.class);
                    playIntent.putExtra("CMD", "SOUND");
                    playIntent.putExtra("SE", "ok2");
                    startService(playIntent);
                    Toast.makeText(TcpService.this,"✅客户端已接入",Toast.LENGTH_SHORT).show();
                });
                startReceiveLoop(socket);
            }catch (Exception e){
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                    }
                } catch (Exception ignored) {}
                mainHandler.post(() -> {
                    Toast.makeText(TcpService.this,"服务端已停止，等待被取消" + e.getMessage(),Toast.LENGTH_SHORT).show();
                });
                currentState = STATE_IDLE;
            }
            working = false;
        });
        serverThread.start();
    }

    private void startReceiveLoop(Socket s) {
        // 这是新开一个线程，因此原可以使working正常进入false
        new Thread(() -> {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    // line 就是收到的JSON字符串
                    ChatMsg chatMsg = gson.fromJson(line, ChatMsg.class);
                    // 取出消息内容，通知页面
//                    String showText = "📩收到：" + chatMsg.content
//                            + " | 类型:" + chatMsg.msgType
//                            + " | 时间:" + chatMsg.time;
                    notifyAllPage(chatMsg);
                }
            } catch (Exception e) {
                mainHandler.post(() -> {
                    Toast.makeText(TcpService.this,"连接断开" + e.getMessage(),Toast.LENGTH_LONG).show();
                });
            } finally {
                try {
                    if(reader != null) reader.close();
                    s.close();
                } catch (Exception ignored) {}
                currentState = STATE_IDLE;
            }
        }).start();
    }
    // 新增锁对象
    private final Object sendLock = new Object();
    // 由于tcpact对于game的收到检测，存在同时发信息的可能性，因此需要做锁防止outWriter串通
    public void sendChatMsg(ChatMsg msgObj) {
        new Thread(() -> {
            synchronized (sendLock) { // 加锁，同一时间只有一个线程进入
                try {
                    if (outWriter != null) {
                        String jsonStr = gson.toJson(msgObj);
                        outWriter.write(jsonStr + "\n");
                        outWriter.flush();
//                    没看懂
//                    notifyAllPage("📤发送：" + jsonStr);
//                    notifyAllPage(msgObj);
                    } else {
                        mainHandler.post(() -> {
                            Toast.makeText(TcpService.this,"⚠没有有效连接",Toast.LENGTH_LONG).show();
                        });
                    }
                } catch (Exception e) {
                    mainHandler.post(() -> {
                        Toast.makeText(TcpService.this,"❌连接失败/已取消:" + e.getMessage(),Toast.LENGTH_LONG).show();
                    });
                }
            }
        }).start();
    }
    //按钮点击事件
//btnSend.setOnClickListener(v -> {
//        ChatMsg msg = new ChatMsg("你好，TCP+Gson消息",0, System.currentTimeMillis());
//        tcpService.sendChatMsg(msg);
//    });


    // ===================== 【新增：中断/关闭接口】=====================
//    public void stopServer() {
//        if(serverSocket != null && !serverSocket.isClosed()){
//            try {
//                serverSocket.close();
//            } catch (Exception ignored) {}
//        }
//    }

    public void cancelClientConnect() {
//        if (currentState == STATE_CLIENT) {
            // 正在客户端握手阶段：关闭临时socket终止connect
            try {
                if (tempSocket != null && !tempSocket.isClosed()) {
                    tempSocket.close();
                }
            } catch (Exception ignored) {}
//        } else if (currentState == STATE_SERVER) {
            // 服务端：关闭serverSocket打断accept
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                }
            } catch (Exception ignored) {}
//        }
        // 如果已经有客户端接入，关闭通信socket
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception ignored) {}
        serverThread = null;
        clientConnectThread = null;
        // 统一重置状态
        currentState = STATE_IDLE;
    }

    //统一关闭全部
    public void closeAllConnection() {
//        stopServer();
        cancelClientConnect();
//        try {
//            if(socket != null) socket.close();
//        } catch (Exception ignored) {}
//        currentState = STATE_IDLE;
    }

    public static String getLocalIp() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netIf : Collections.list(interfaces)) {
                Enumeration<InetAddress> addrs = netIf.getInetAddresses();
                for (InetAddress addr : Collections.list(addrs)) {
                    if (!addr.isLoopbackAddress() && addr.getAddress().length == 4) {
                        return addr.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "获取失败";
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeAllConnection();
    }
}

