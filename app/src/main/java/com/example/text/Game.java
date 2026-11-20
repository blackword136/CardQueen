package com.example.text;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;


// 槽类型和信号触发位置{
// 回合开始发牌前，发牌后，点击回合结束按钮时，总回合结束时（不需要额外参数）
// 额外抽牌（游戏条件），出牌时，（需要参数card）
// 使用技能前，使用技能后，(需要参数，使用哪个技能)
// } // 有多少种类型，下面的东西就要多少种呢

// 信号触发器{ 上面的每有一种，就要有一个这玩意
// 按顺序触发自身携带的所有信号
// 链接（buff需要提供槽函数）
// 断开（）
// }

// 信号{ // 存储在信号触发器里面
// 槽函数
//
//}

// buff{
// 持续时间啦，不同槽函数所需的不同参数内存啦
// 链接槽和信号（考虑是否添加顺序），断开，
// 清除此buff，供持续时间结束时摧毁和槽函数使用
// buff消失时，应当自定断开
// 初始化，槽函数的赋值，实现槽的链接，槽函数所需参数的初始化，持续时间
// }
// 槽函数均在buff栏里面，
// 对于buff的创建初始化，需要尽可能简化实现可以通过tcp通讯

// 应当将技能和buff相存，因为buff一般只有技能会赋予包括场地技能，即使实现Buff的简化，也不能改变技能都存储在代码里面的结果
// 因此添加buff最好是在主函数写一个添加方法，传入信息位来调用对应技能的buff初始化，也方便tcp调用此方法
// 场地buff可以依托场地技能，因为buff终究和技能并没有关联

// buff分两种，一种是需要传递，添加给对面，不能在技能里面实现赋值，这种需要转为代码存储起来
// 一种是只会添加给自己，这种只需要对应的技能里面初始化即可

// 上诉的buff实际为函数，或者说信号，信号并不一定需要buff这个载体
// 不需要回合结束时就消失的信号，并且不需要参数，完全使用匿名函数即可
// 若需要参数，则调用技能的参数给予即可
// 但是添加Buff，则一定需要添加信号

public class Game extends AppCompatActivity implements TcpService.MsgCallback {
    public TcpService tcpService;
    private Handler mainHandler;
    private boolean bindOk = false;
    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // ✅ 连接成功，拿到Binder(TcpService.onBind 的返回值)，在这里获取Service实例
            TcpService.TcpBinder binder = (TcpService.TcpBinder) service;
            tcpService = binder.getService();
            tcpService.registerPage3(Game.this);
//
            // 延时2秒执行
            if(tcpService.currentState == TcpService.STATE_CLIENT) {
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 2秒后执行这里，可以Toast、修改控件
//                        msgSeq = 1;
                        game_send_smg(new ChatMsg(STATE_TOTAL_START));
                    }
                }, 2000);
            }
//            if(tcpService.currentState == TcpService.STATE_CLIENT) {
//                game_send_smg(new ChatMsg(STATE_TOTAL_START));
////                game_send_smg(new ChatMsg(Game.this, STATE_TOTAL_START));
//            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
        // ❌ 服务意外断开（进程被杀、崩溃），主动unbind不会触发这个！
    };

    // --------------------------------------------------------------------------
    public Signal trun_end_sign = new Signal(); // 回合结束
    public Signal trun_begin_sign = new Signal(); // 回合开始
    public Signal card_begin_num_sign = new Signal(); // 每回合开始时发牌数
    public class buff_time_settle extends Signal.Slot {
//        public void invoke(Map<String, Object> data){
//            wait.accept(data);
//        }
        public boolean front = true; // 默认正面Buff
        public boolean clear = true; // 默认可以清除
        public int time;
//        Signal.Slot slot_time;
//        public void cation_default(Map<String, Object> data){
//            if(time > 0){
//                time--;
//                if(time == 0){
//                    .disconnect(slot_time);
//                    buff_time_settle_sign.disconnect(buff_time_settle.this);
//                }
//            }
//        }
//        public Consumer<Map<String, Object>> cation = this::cation_default;_default
        public void completion_default(Signal signal, Signal.Slot slot){
            signal.disconnect(slot);
            buff_time_settle_sign.disconnect(buff_time_settle.this);
        }
        public void wait_default(){
            if(time > 0){
                time--;
                if(time == 0) completion.run();
            }
        }
        public Runnable completion;
        public Consumer<Map<String, Object>> wait;
        public buff_time_settle(int time_num, Signal signal, Signal.Slot slot){
            super(null);
            //                      buff持续时间   绑定的信号       绑定的槽
            time = time_num;
//            completion = ()->{
//                signal.disconnect(slot);
//                buff_time_settle_sign.disconnect(buff_time_settle.this);
//            };
//            wait = (Map<String,Object> data)->{
//                if(time > 0){
//                    time--;
//                    if(time == 0) completion.run();
//                }
//            };
            completion = ()->completion_default(signal, slot);
            wait = v->wait_default();
            invoke = (Map<String,Object> data)->wait.accept(data);
        }
        public buff_time_settle(int time_num, Signal signal, Signal.Slot slot, boolean bool){
            this(time_num, signal, slot);
            front = bool;
        }
        public buff_time_settle(int time_num, Signal signal, Signal.Slot slot, boolean bool, boolean bool_clear){
            this(time_num, signal, slot);
            front = bool;
            clear = bool_clear;
        }
    }
    public Signal skills_trigger_sign = new Signal(); // 触发技能
    public void skills_ation_succes (Kilss_Button skills){ // 成功触发技能之后，需要执行的效果
        Map<String,Object> data = new HashMap<>();
        data.put("Kilss_Button", skills);
//        data.put("type","test");
        skills_trigger_sign.emitDirect(data); // 结算buff

        game_send_smg(new ChatMsg(this, STATE_SKILLS, "对方使用技能" + skills.name));
    }
    public Signal hit_card_sign = new Signal(); // 打牌时的数值计算
    // 10000: arr_bool[0]==1 mode14_1,mode10_1,mode13_1,mode13_2,mode2_2
    // 10000: arr_bool[3]=false mode13_2
    public Signal hit_card_succes_sign = new Signal(); // 成功打牌时
    // 500: arr_bool[3]=false mode11_1,mode13_2,mode21_1,mode17_2,mode16

    // 下面这两个是独立于上面的先行检测
    public Signal getHit_card_sign = new Signal(); // 这个是多次检测，不能做有次数的buff哦 这张牌是否可以打出
    // 10000: mode16_1
    public Signal addcard_sign = new Signal(); // 这个是多次检测，不能做有次数的buff哦 额外卡牌数量
    // 10000: 冻结 小型冻结，mode13_1
    // addcard_sign 和 hit_card_sign,hit_card_succes_sign 有时不一样的哦
    public Signal click_trunover_sign = new Signal(); // 点击回合结束按钮
    public Signal buff_time_settle_sign = new Signal(); // buff时间结算
    public Signal semd_sign = new Signal(); // 发送信息
    public Signal ondownload_sign = new Signal(); // 接受信息并刷新数据时
    public Signal btnendround_num_sign = new Signal(); // 回合结束每张进入弃牌库获得多少mp，不可小于0
    // --------------------------------------------------------------------------
    public Game get_main(){
        return Game.this;
    }
    public Hardcard_LinearLayout tagContainer; // 手牌 （布局）
    public Print layout_show; // 绝大多数界面数据计算和打印函数  除了手牌布局和5个技能布局，它们的打印自己解决
    public TextView show_text; // 轮次文字框
    private PopupWindow popupWindow;
    private Button btn_menu; //触发下拉的主按钮
    public ArrayList<Kilss_Button> btn_skill = new ArrayList<>(); // 场地技能加四个携带技能
//再写一个游戏开始一次性信号把
    // --------------------------------------------------------------------------
    public void choose_card_kills(Consumer<Card> callback){ // 这都有callback,回调函数
        choose_card_action = ()->{
//            if(tcpService.currentState == TcpService.STATE_IDLE) return; // 没网你打个锤子牌
//            else if(!allow_roune_strat()) return; // 没到回合

            Card View0 = tagContainer.get_choose_card();
            if(View0 == null) {
                Sound_sffects("SOUND", "close"); // 选择失败时触发的音效
                return;
            }
            if(choose_card_pop.isShowing()){
                choose_card_pop.dismiss();
            }
            callback.accept(View0);
        };
        choose_card_pop.showAtLocation(
                getWindow().getDecorView().findViewById(android.R.id.content),
                Gravity.CENTER,0,0);
    }
    public Runnable choose_card_action = null;
    private PopupWindow choose_card_pop;
    // --------------------------------------------------------------------------
    @Override
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
    //  得对Print的构造和Print的使用的顺序设计好

        mainHandler = new Handler(getMainLooper());

        //绑定后台服务
//        Intent intent = new Intent(this, TcpService.class);
        bindService(new Intent(this, TcpService.class), connection, Context.BIND_AUTO_CREATE);
        // BIND_AUTO_CREATE 如果 Service 没启动，则自动创建 Service（最常用）
        bindOk = true;

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // 横屏常用：只上下避让，左右不增加padding
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            v.setPadding(0, 0, systemBars.right, 0);
            return insets;
        });
//        **适配系统栏（状态栏、导航栏）**
//        自动给根布局 `main` 添加 padding，避免页面内容被手机顶部状态栏、底部虚拟导航栏挡住。
//        这是 Android Studio 新建项目默认自动生成的适配代码。

        // 注册返回拦截回调
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 这里空着，拦截系统返回（手势、返回键全部无效）
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        show_text = findViewById(R.id.show_text);
//        show_text.setRotation(45);
        tagContainer = findViewById(R.id.layout_tag_container);
        tagContainer.init(this::get_main);
        Card.show_last_card = "";

//        Button btnAdd = findViewById(R.id.btn_add);
        Button btnRemove = findViewById(R.id.btn_remove);

        //增加标签
//        btnAdd.setOnClickListener(v -> addNewTag());
        //删除标签
        btnRemove.setOnClickListener(v -> {
            // 只有按钮打牌才会触发自动选0
            if((tagContainer.choose < 0 || tagContainer.choose > tagContainer.getChildCount()) && tagContainer.getChildCount() > 0)
                tagContainer.set_choose(0);
            tagContainer.hit_card();
        });
        // ------------------------------------------------------------
        btn_skill.add(findViewById(R.id.btn_venve_skill));
        btn_skill.add(findViewById(R.id.btn_play_skill1));
        btn_skill.add(findViewById(R.id.btn_play_skill2));
        btn_skill.add(findViewById(R.id.btn_play_skill3));
        btn_skill.add(findViewById(R.id.btn_play_skill4));
        for(int i=0;i<5;i++){ // 技能按钮初始设置（必须）
            btn_skill.get(i).init(i, this::get_main);
        }
        for(int i=0;i<4;i++){
            if(Choose_Skills.sk_lier.get(i) != null)
                Choose_Skills.sk_lier.get(i).accept(btn_skill.get(i+1));
            else {
                Intent playIntent = new Intent(this, BgmService.class);
                playIntent.putExtra("CMD", "CHANGE");
                playIntent.putExtra("MUSIC_ID", R.raw.chara11);
                startService(playIntent);
            }
        }

        Button btnleft = findViewById(R.id.btn_move_left);
        btnleft.setOnClickListener(v->{ // 左移按钮
            int sum = tagContainer.getChildCount();
            if(sum == 0) return;
            Sound_sffects("SOUND", "touch");
            tagContainer.set_choose((tagContainer.choose + sum - 1) % sum);
        });
        Button btnright = findViewById(R.id.btn_move_right);
        btnright.setOnClickListener(v->{ // 右移按钮
            int sum = tagContainer.getChildCount();
            if(sum == 0) return;
            Sound_sffects("SOUND", "touch");
            tagContainer.set_choose((tagContainer.choose + 1) % sum);
        });

    // ------------------------------------------------------------
        layout_show = new Print(this::get_main);
         if(Choose_Mode.mode_create != null) // 场地初始化
             Choose_Mode.mode_create.accept(this);
//        Choose_Mode.mode_17(this);
    // ------------------------------------------------------------
        btn_menu = findViewById(R.id.btn_menu);
        View popView = LayoutInflater.from(this).inflate(R.layout.layout_pop_menu, null);
        //初始化PopupWindow，宽100dp，高度自适应
        popupWindow = new PopupWindow(popView,
                dip2px(this,100),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true); // true：获取焦点，点击外部自动关闭
        // 必须设置背景，否则点击外部无法关闭PopupWindow
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000)); // `0x80000000`：黑色，50% 透明

        // 面板内部按钮监听
        popView.findViewById(R.id.btn1).setOnClickListener(v->{
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SWITCH");
            startService(playIntent);
//            Toast.makeText(Game.this,"暂不支持喵",Toast.LENGTH_SHORT).show();
//            popupWindow.dismiss(); //点完自动关闭下拉框
        });
        popView.findViewById(R.id.btn2).setOnClickListener(v->{
            Intent intent = new Intent(Game.this, Tcpactivity.class);
            intent.putExtra("the_last_int", "Game");
// 对象转json字符串
            String msgJson = new Gson().toJson(the_last_send_msg);
            intent.putExtra("chat_msg_json", msgJson); // 将最近存储的msg发送给 Tcpactivity
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
//            Toast.makeText(Game.this,"设置联网",Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        popView.findViewById(R.id.btn3).setOnClickListener(v->{
            if(tcpService == null || tcpService.currentState == TcpService.STATE_IDLE){
                Intent intent_bgm = new Intent(getApplicationContext(),BgmService.class);
                intent_bgm.putExtra("CMD","CHANGE");
                intent_bgm.putExtra("MUSIC_ID",R.raw.menu);
                startService(intent_bgm);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                finish();
                startActivity(intent);
            }
            else
                Toast.makeText(Game.this,"断网才能退出",Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });
        popView.findViewById(R.id.btn4).setOnClickListener(v->{
            if(tcpService.currentState == TcpService.STATE_SERVER){ // 房主投降
                game_state = STATE_SURRENDER;
                show_text.setText("已投降");
                layout_show.play_hp = 0;
                game_send_smg(new ChatMsg(this));
                gameover();
            }
            else if(tcpService.currentState == TcpService.STATE_CLIENT){ // 玩家投降
                game_state = STATE_SURRENDER;
                show_text.setText("已投降");
                layout_show.play_hp = 0;
                game_send_smg(new ChatMsg(this));
                gameover();
            }
            else
                Toast.makeText(Game.this,"联机才能投降",Toast.LENGTH_SHORT).show();
            popupWindow.dismiss();
        });

        //主按钮点击，弹出下拉框，在按钮下方展示
        btn_menu.setOnClickListener(v->{
            if(popupWindow.isShowing()){
                popupWindow.dismiss();
            }else{
                // showAsDropDown：依附在按钮下方弹出，就是下拉效果
                popupWindow.showAsDropDown(v,0,0);


//                mPopupWindow.showAtLocation(popView, Gravity.FILL, 0, 0);   // 全屏弹出，Gravity.FILL
            }
        });

        // ------------------------------------------------------------
        View popView_cc = LayoutInflater.from(this).inflate(R.layout.choose_card, null);
        choose_card_pop = new PopupWindow(popView_cc,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                true); // true：获取焦点，点击外部自动关闭
        // 必须设置背景，否则点击外部无法关闭PopupWindow
        choose_card_pop.setBackgroundDrawable(new ColorDrawable(0x40000000)); // `0x80000000`：黑色，25% 透明
        popView_cc.setOnClickListener(v -> {
            // 点击弹窗空白区域
            if(choose_card_pop.isShowing()){
                choose_card_pop.dismiss();
            }
        });

        LinearLayout cc_layout_tag_container = popView_cc.findViewById(R.id.cc_layout_tag_container);

        cc_layout_tag_container.setOnTouchListener((v, event) ->{
            tagContainer.hardcard_touch.accept(event, choose_card_action);
            return true;
        });
        Button cc_btn_move_left = popView_cc.findViewById(R.id.cc_btn_move_left);
        cc_btn_move_left.setOnClickListener(v->{ // 左移按钮
            btnleft.performClick();
        });
        Button cc_btn_move_right = popView_cc.findViewById(R.id.cc_btn_move_right);
        cc_btn_move_right.setOnClickListener(v->{ // 右移按钮
            btnright.performClick();
        });
        Button cc_btn_remove = popView_cc.findViewById(R.id.cc_btn_remove);
        cc_btn_remove.setOnClickListener(v -> {
            choose_card_action.run();
        });
        Button cc_btn_menu = popView_cc.findViewById(R.id.cc_btn_menu);
        cc_btn_menu.setOnClickListener(v->{
            if(choose_card_pop.isShowing()){
                choose_card_pop.dismiss();
            }
        });

        // ------------------------------------------------------------
        layout_show = new Print(this::get_main);
        // ------------------------------------------------------------
        layout_show.btnendround.setOnClickListener(v -> { // 回合结束按钮
            if(!allow_roune_strat()) return;
//            layout_show.btnendround.setEnabled(false);// 按钮点击失效（按钮同时会变灰，视觉上提示不可点）
//            btn.setEnabled(true);// 恢复按钮点击
            Sound_sffects("NORMAL", "turnEnd");

            boolean[] arr_bool = {true};
            Map<String,Object> data = new HashMap<>();
            data.put("arr_bool", arr_bool);
            click_trunover_sign.emitDirect(data); // 打牌，计三个数
            if(arr_bool[0]){
                int num = tagContainer.getChildCount(), sum = 0;
                for(int i=0;i<num;i++){ // 清空手牌
                    View item = tagContainer.getChildAt(sum);
                    if(item instanceof Card){
                        Card View0 = (Card) item;
                        if(View0.giveup) sum++;
                        else tagContainer.removecard(View0);
//                        if(View0.abandon){
//                            abandon_card_string.add(View0.gettext()); // 添加弃牌堆
//                            get_main.get().layout_show.text_number_add("abandon", 1);
//                        }
                    }
                }
                sum = num - sum;
                num = tagContainer.get_btnendround_num();
                if(num > 0 && sum > 0)
                    layout_show.text_number_add("play_technique", num * sum);
            }
//            for(int i=tagContainer.getChildCount()-1;i>=0;i--){
//                View item1 = tagContainer.getChildAt(i);
//                if(item1 instanceof Card){
//                    Card View0 = (Card) item1;
//                    if(View0.abandon){
//                        tagContainer.abandon_card_string.add(View0.gettext()); // 添加弃牌堆
//                    }
//                }
//                tagContainer.removeViewAt(i);
//            }
//            layout_show.text_number_refresh(layout_show.head_card); // 刷新手牌数
//            layout_show.text_number_refresh(layout_show.abandon_card); // 刷新弃牌数
//            layout_show.text_number_set(layout_show.btnendround, 0); // "结束回合\nMP+0"

//            tagContainer.set_choose(-1);

            game_state = STATE_TURN_END; // 自身进入处于回合结束
            sk_use_show_refresh_all();
            show_text.setText("回合结束，等待对方回合结束");
            game_send_smg(new ChatMsg(this)); // STATE_TURN_END 发送处于回合结束的信息
        });

//        Button btn_tcp = findViewById(R.id.btn_tcp);
//        btn_tcp.setOnClickListener(v->{
//            TextView et_poro = findViewById(R.id.et_poro);
//            try{
//                String ip = et_poro.getText().toString();
//                int num = Integer.parseInt(ip);
//                game_send_smg(new ChatMsg(this, num));
//            }catch (NumberFormatException e){
//            }
//        });

    }

    public void sk_use_show_refresh_all(){ // 刷新所有技能的显示
    //    每次修改技力时
        for(int i=0;i<5;i++){ // 技能按钮初始设置（必须）
            btn_skill.get(i).use_show_refresh.run();
        }
    }
    public void Sound_sffects(String type, String str){
        Intent playIntent = new Intent(this, BgmService.class);
        playIntent.putExtra("CMD", type);
        playIntent.putExtra("SE", str);
        startService(playIntent);
    }
    public void Sound_sffects(String type, String str, int time){
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent playIntent = new Intent(Game.this, BgmService.class);
                playIntent.putExtra("CMD", type);
                playIntent.putExtra("SE", str);
                startService(playIntent);
            }
        }, time);
    }


    private boolean trun_all_end(){
        show_text.setText("结算伤害");
        layout_show.text_number_set("combo", 0); // COMBO: 0

        if(tcpService.currentState == TcpService.STATE_SERVER){ // 是房主

            if(layout_show.play_att_sum > layout_show.enemy_att_sum){
                layout_show.text_number_add("enemy_hp", -1*layout_show.play_att_sum);
            }
            else if(layout_show.play_att_sum < layout_show.enemy_att_sum){
                layout_show.text_number_add("play_hp", -1*layout_show.enemy_att_sum);
            }
            layout_show.text_number_set("play_att", 0);
            layout_show.text_number_set("enemy_att", 0);

//牌库数量不对的bug好像没有解决
            tagContainer.list.addAll(tagContainer.abandon_card_string); // 将弃牌堆的牌加回牌库
            tagContainer.abandon_card_string.clear(); // 清空弃牌
            layout_show.text_number_refresh("library"); // 打印牌库
            layout_show.text_number_refresh("abandon"); // 打印弃牌
            tagContainer.showlast_card_refresh(""); // 刷新下一张
            layout_show.text_number_add("turn", 1); // 伦次加1

            game_state = STATE_INTEGRATE;
            game_send_smg(new ChatMsg(this)); // STATE_INTEGRATE 发送数据和 下阶段

        }

        for(int i=0;i<5;i++) btn_skill.get(i).cd_add(-1);

        Map<String,Object> data_bufftime = new HashMap<>();
//        data_bufftime.put("type","test");
        buff_time_settle_sign.emitDirect(data_bufftime); // 结算buff

        Map<String,Object> data = new HashMap<>();
//        data.put("type","test");
        trun_end_sign.emitDirect(data); // 回合结束

        if((double) layout_show.play_hp / layout_show.play_hp_max + (double) layout_show.enemy_hp / layout_show.enemy_hp_max <= 1.5){
            Intent intent_bgm = new Intent(this,BgmService.class);
            intent_bgm.putExtra("CMD","NEXT");
            startService(intent_bgm);
        }

        if(layout_show.play_hp <= 0){
            gameover();
        }
        else if(layout_show.enemy_hp <= 0){
            gameover();
        }
        else return true;

        return false;
    }
    public int msgSeq = 0; // 0~2 -1表示例外不能用
    private ChatMsg the_last_send_msg = null;
    public void game_send_smg(ChatMsg msgObj){
        msgObj.msgSeq = msgSeq;
        // ChatMsg此时已经生成，也就是Game已经复制了，此时在semd_sign里面修改Game不会同步到msg，等效白写
        Map<String,Object> data = new HashMap<>();
        data.put("ChatMsg",msgObj);
        semd_sign.emitDirect(data); //

        the_last_send_msg = msgObj;
        tcpService.sendChatMsg(msgObj);
    }
    @Override
    public void onMessage(ChatMsg msg) {
        if(msg.game_state >= 100) return;
        mainHandler.post(() -> {
//            Toast.makeText(Game.this,"onMessage",Toast.LENGTH_LONG).show();
//        runOnUiThread(() -> tvLog.append(text + "\n"));
            ondownload(msg); // 刷新信息

            if(msg.game_state == STATE_SKILLS){ // 收到对方使用技能
                if(msg.skills_show_text != null)
                    show_text.setText(msg.skills_show_text);
                Sound_sffects("NORMAL", "skill_activation");
                return;
            }
            else if(msg.game_state == STATE_REFRESH){ // 只刷新
                if(msg.skills_show_text != null)
                    show_text.setText(msg.skills_show_text);
                return;
            }
//        每次接受后加1，实现标记后接受的
            msgSeq = (msgSeq + 1) % 3; // 每次接受信息加1
            switch (msg.game_state){
                case STATE_SERVER_START:{ // (允许)房主打牌
                    if(game_state == STATE_TURN_END){ // 自身处于回合结束
                        game_send_smg(new ChatMsg(STATE_TURN_END)); // STATE_TURN_END
                        break;
                    }
                    else if(tcpService.currentState == TcpService.STATE_SERVER){ // 是房主
                        game_state = STATE_SERVER_START;
                        sk_use_show_refresh_all(); // 刷新所有技能的显示
                        Sound_sffects("NORMAL", "turnStart");
                        show_text.setText("轮到你打牌(房主)");
                    }
                    break;
                }
                case STATE_CLIENT_START:{ // (允许)玩家打牌
                    if(game_state == STATE_TURN_END){ // 自身处于回合结束
                        game_send_smg(new ChatMsg(STATE_TURN_END)); // STATE_TURN_END
                        break;
                    }
                    else if(tcpService.currentState == TcpService.STATE_CLIENT){ // 是玩家
                        game_state = STATE_CLIENT_START;
                        sk_use_show_refresh_all(); // 刷新所有技能的显示
                        Sound_sffects("NORMAL", "turnStart");
                        show_text.setText("轮到你打牌(玩家)");
                    }
                    break;
                }
                case STATE_TURN_END:{ // 收到回合结束
                    if(game_state == STATE_TURN_END){ // 自身也是处于回合结束
                        // 进入双方均回合结束
                        if(tcpService.currentState == TcpService.STATE_SERVER){
                            trun_all_end();
                        }
                        else if(tcpService.currentState == TcpService.STATE_CLIENT){
//                        game_state = STATE_ALL_END;
                            game_send_smg(new ChatMsg(this)); // STATE_TURN_END
                        }
//                    game_state = STATE_ALL_END;
//                    game_send_smg(new ChatMsg(this)); // STATE_ALL_END
//                    trun_all_end();
                    }
//                    Sound_sffects("NORMAL", "turnEnd");
                    else if(tcpService.currentState == TcpService.STATE_SERVER){
                        game_state = STATE_SERVER_START;
                        sk_use_show_refresh_all(); // 刷新所有技能的显示
                        show_text.setText("对方回合结束，可肆意打牌(房主)");
                    }
                    else if(tcpService.currentState == TcpService.STATE_CLIENT){
                        game_state = STATE_CLIENT_START;
                        sk_use_show_refresh_all(); // 刷新所有技能的显示
                        show_text.setText("对方回合结束，可肆意打牌(玩家)");
                    }
                    break;
                }

                case STATE_TOTAL_START:{ // 收到总回合开始
//                Toast.makeText(Game.this,"STATE_TOTAL_START:{ // 收到总回合开始",Toast.LENGTH_LONG).show();
                    if(tcpService.currentState == TcpService.STATE_SERVER){
//                    Toast.makeText(Game.this,"已收到开始信息",Toast.LENGTH_LONG).show();
                        game_state = STATE_TOTAL_START; // 返回开始

                        Map<String,Object> data = new HashMap<>();
//                      data.put("type","test");
                        trun_begin_sign.emitDirect(data);

                        Sound_sffects("NORMAL", "turnStart");

                        Random random = new Random();
                        if(random.nextInt(2) == 1){
                            tagContainer.turn_begin(); // 抽牌
                            game_send_smg(new ChatMsg(this, STATE_CLIENT_GETCARD));
                        }
                        else {
                            show_text.setText("轮到对方打牌");
                            game_send_smg(new ChatMsg(this, STATE_CLIENT_FRIST));
                        }
                    }
//                    else if(tcpService.currentState == TcpService.STATE_CLIENT){
//                    }
                    break;
                }
                case STATE_CLIENT_GETCARD:{ // 玩家抽牌
                    if(tcpService.currentState == TcpService.STATE_CLIENT){ // 是玩家
                        tagContainer.turn_begin(); // 抽牌
                        game_send_smg(new ChatMsg(this, STATE_SERVER_START)); // (允许)房主打牌
                        show_text.setText("轮到对方打牌");
                    }
                    break;
                }
                case STATE_CLIENT_FRIST:{ // 玩家先抽牌
                    if(tcpService.currentState == TcpService.STATE_CLIENT){ // 是玩家
                        tagContainer.turn_begin(); // 抽牌
                        game_send_smg(new ChatMsg(this, STATE_SERVER_GETCARD));
                    }
                    break;
                }
                case STATE_SERVER_GETCARD:{ // 房主抽牌
                    if(tcpService.currentState == TcpService.STATE_SERVER){ // 是房主
                        tagContainer.turn_begin(); // 抽牌
                        game_send_smg(new ChatMsg(this, STATE_CLIENT_START)); // (允许)玩家打牌
                    }
                    break;
                }

//            game_state = STATE_ALL_END; // 进入双方均回合结束
//            game_send_smg(new ChatMsg(this)); // STATE_ALL_END
//            trun_all_end();
//            case STATE_ALL_END:{// 收到总回合结束
//                game_state = STATE_ALL_END; // 进入双方均回合结束
//                trun_all_end();
//                break;
//            }
                case STATE_INTEGRATE:{// 信息整合
                    if(tcpService.currentState == TcpService.STATE_SERVER){
//                    game_state = STATE_INTEGRATE;
//                    game_state = STATE_TOTAL_START; // 返回开始
//                    game_send_smg(new ChatMsg(this)); // 让玩家接受信息并且也返回开始
                    }
                    else if(tcpService.currentState == TcpService.STATE_CLIENT){
                        game_state = STATE_INTEGRATE;

                        if(trun_all_end()) {
                            mainHandler.postDelayed(() -> {
                                // 这里是同一个对象使用线程，因此这个会在外层执行完后再执行这个线程
                                // 若不是使用同一个个对象，那么会在这里同步进行
                                // 由于这里是函数尾，所以是哪种情况都不影响
                                // 2秒后执行这里，可以Toast、修改控件
                                game_state = STATE_TOTAL_START; // 返回开始
                                Map<String,Object> data = new HashMap<>();
//                                  data.put("type","test");
                                trun_begin_sign.emitDirect(data);

                                Sound_sffects("NORMAL", "turnStart");

                                game_send_smg(new ChatMsg(this, STATE_TOTAL_START)); // STATE_INTEGRATE 发送数据和 下阶段
                            }, 2000);
                            // 首回合玩家的首回合信号没有触发，需要解决这个问题。
                            // 因为如果首回合玩家先触发再发送回合开始，那么这个回合开始信号没有游戏数据，因为游戏数据是房主加载初始化的
                        }
                    }
                    break;
                }
                case STATE_SURRENDER: {
                    if(tcpService.currentState == TcpService.STATE_SERVER){
                        game_state = STATE_SURRENDER;
                        show_text.setText("对方已投降");
                        gameover();
                    }
                    else if(tcpService.currentState == TcpService.STATE_CLIENT){
                        game_state = STATE_SURRENDER;
                        show_text.setText("对方已投降");
                        gameover();
                    }
                    break;
                }
            }
        });
    }
    public int game_state = STATE_TOTAL_START; // 游戏状态
    public static final int STATE_TOTAL_START = 0; // 总回合开始
    // 房主接受STATE_TOTAL_START
    // 房主生成随机数，决定谁先
    public static final int STATE_CLIENT_FRIST = 1; // 玩家先拿牌
    public static final int STATE_SERVER_GETCARD = 2; // 房主拿牌
    public static final int STATE_CLIENT_GETCARD = 3; // 玩家拿牌

    // 并由先拿牌先进入 STATE_server_START / STATE_client_START
    public static final int STATE_SERVER_START = 4; // (允许)房主打牌
    // 若自身出完牌允许对方出牌，则需要发对方的 STATE
    public static final int STATE_CLIENT_START = 5; // (允许)玩家打牌
    // 只有处于打牌期间才能释放技能(一般情况下)
    public static final int STATE_TURN_END = 10; // 回合结束（点击了回合结束按钮）
    // 将牌全部扔进弃牌库，并根据弃牌张数获得对应的mp
    // 等待另一方也回合结束 期间收到打牌时返回回合结束
    // 若自身结束且收到对方结束，{
    // 若为玩家，则进入STATE_ALL_END，并发送STATE_ALL_END给房主转下面
    // 若为房主，调用trun_all_end并进入STATE_INTEGRATE
    // }
//    public static final int STATE_ALL_END = 15; // 双方均回合结束

    // 随后双方均执行以下{ trun_all_end()
    // 结算回合制buff
    // 结算技能cd或者技能信息数据
    // }
    // 房主 结算双方伤害，将弃牌库的牌放回手牌
    public static final int STATE_INTEGRATE = 20; // 信息整合
    // 房主 进入 STATE_INTEGRATE 随后发送数据和 STATE_INTEGRATE 给玩家（决定是否开始下回合）
    // 玩家接受信息返回开始，发送开始信息 STATE_TOTAL_START 给房主
    public static final int STATE_SURRENDER = 21; // 投降

    public static final int STATE_SKILLS = 6; // 收到对方使用技能
    public static final int STATE_REFRESH = 7; // 只刷新
    // 大体流程实际以双方回合制轮流，当一方在行动时，另一方尽量不要做行动
    public void ondownload(ChatMsg msg) { // 根据收到的信息刷新界面信息 （需要收到的信息要有效哦）
        Map<String,Object> data = new HashMap<>();
        data.put("ChatMsg",msg);
        ondownload_sign.emitDirect(data); //
        // 接受
        if(!msg.ondownload) return;

        tagContainer.list = new ArrayList<>(msg.list); // 牌库
        layout_show.text_number_refresh("library"); // 刷新牌库文字框
        tagContainer.abandon_card_string = new ArrayList<>(msg.abandon_card_string); // 弃牌
        layout_show.text_number_refresh("abandon"); // 刷新弃牌库的文字框

        tagContainer.showlast_card_refresh(msg.show_last_card); // 刷新上一张的文字背景

        if(layout_show.turn != msg.turn) { // 轮次
//            layout_show.turn = msg.turn;
            layout_show.text_number_set("turn", msg.turn);
        }
//        layout_show.combo = msg.combo; // combo

        // 由于对称性，接受信息时血量，技力，伤害的赋值应当反过来
        if(layout_show.play_technique_max != msg.enemy_technique_max) { // 玩家技力上限
            layout_show.text_number_setmax("play_technique", msg.enemy_technique_max);
        }
        if(layout_show.play_technique != msg.enemy_technique) { //   玩家技力
            layout_show.text_number_set("play_technique", msg.enemy_technique);
        }
        if(layout_show.play_hp_max != msg.enemy_hp_max) { // 玩家血量上限
            layout_show.text_number_setmax("play_hp", msg.enemy_hp_max);
        }
        if(layout_show.play_hp != msg.enemy_hp) { // 玩家血量
            layout_show.text_number_set("play_hp", msg.enemy_hp);
        }

        if(layout_show.enemy_technique_max != msg.play_technique_max) { // 敌人技力上限
            layout_show.text_number_setmax("enemy_technique", msg.play_technique_max);
        }
        if(layout_show.enemy_technique != msg.play_technique) { //   敌人技力
            layout_show.text_number_set("enemy_technique", msg.play_technique);
        }
        if(layout_show.enemy_hp_max != msg.play_hp_max) { // 敌人血量上限
            layout_show.text_number_setmax("enemy_hp", msg.play_hp_max);
        }
        if(layout_show.enemy_hp != msg.play_hp) { // 敌人血量
            layout_show.text_number_set("enemy_hp", msg.play_hp);
        }

        if(layout_show.play_att_sum != msg.enemy_att_sum) { // 玩家累计伤害值
            layout_show.text_number_set("play_att", msg.enemy_att_sum);
        }
        if(layout_show.play_att != msg.enemy_att) { // 玩家攻击力
            layout_show.text_number_setmax("play_att", msg.enemy_att);
        }

        if(layout_show.enemy_att_sum != msg.play_att_sum) { // 敌人累计伤害值
            layout_show.text_number_set("enemy_att", msg.play_att_sum);
        }
        if(layout_show.enemy_att != msg.play_att) { // 敌人攻击力
            layout_show.text_number_setmax("enemy_att", msg.play_att);
        }
    }
    public boolean allow_roune_strat(){ // 检测是否处于自身回合
        if(tcpService == null) return false; // tcp还未构造完成时，会用到这个
        if(tcpService.currentState == TcpService.STATE_SERVER){
            if(game_state == Game.STATE_SERVER_START) return true;
        }
        else if(tcpService.currentState == TcpService.STATE_CLIENT){
            if(game_state == Game.STATE_CLIENT_START) return true;
        }
        return false;
    }
    //动态新增标签
//    private void addNewTag(){
//        tagContainer.addNewcard();
//    }


    public void gameover(){
        if(layout_show.play_hp <= 0){
            Sound_sffects("NORMAL", "lose", 500);
            show_text.setText("你已失败");
        }
        else if(layout_show.enemy_hp <= 0){
            show_text.setText("你已胜利");
        }
        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 由于音频需要xontext，但是这个页面要摧毁了，只能在main页面重新播放音乐
//                Context appContext = getApplicationContext();
                Intent intent = new Intent(getApplicationContext(),BgmService.class);
                intent.putExtra("CMD","CHANGE");
                intent.putExtra("MUSIC_ID",R.raw.menu);
                startService(intent);
                finish();
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bindOk) {
            unbindService(connection);
        }
    }
    //dp转px工具方法
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

//# 如何判断是不是 Activity 重建？
//        `onCreate`里面判断 Bundle：
//        ```
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    if(savedInstanceState != null){
//        Log.d("test","Activity刚刚被系统重建");
//    }else{
//        Log.d("test","第一次打开");
//    }
//}
//```

//AndroidManifest 对应 Activity 标签添加：

//<activity
//android:name=".Choose_Mode"
//android:configChanges="orientation|screenSize|screenLayout|uiMode|fontScale" >
//        ```
//
//        ✅ 加上之后，旋转屏幕、切换深色模式、字体大小变化**不会重建 Activity**，只会回调`onConfigurationChanged()`。

//        tagContainer.getRandomListItemstring = this::getRandomListItemstring;

//        HorizontalScrollView hsv = findViewById(R.id.scroll_view);
//
//        hsv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            //放里面是因为要等画完在获取数据
//            @Override
//            public void onGlobalLayout() {
//                // 获取控件本身在屏幕上显示的宽高 不包含超出屏幕
//                int realW = hsv.getWidth();
//                int realH = hsv.getHeight();
//
//                Log.d("尺寸", "HorizontalScrollView 宽:"+realW + " 高:"+realH);
//
//                hsv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//            }
//        });