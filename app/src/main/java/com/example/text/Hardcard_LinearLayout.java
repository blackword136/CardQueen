package com.example.text;

//import com.example.text.Card;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Hardcard_LinearLayout extends LinearLayout {
    private Supplier<Game> get_main;
    public int choose = -1;
//    private boolean touch;
    private long time_start;
    public ArrayList<String> abandon_card_string; //弃牌堆
    private boolean astion_sorting = false; // ture为比例（>=5），falsh为固定(<5)
//    Supplier<String> getRandomListItemstring; // 从牌库获取一张牌
    public ArrayList<String> list; // 牌库
    private TextView hit_show_card;
    private TextView hit_add_card;
    private TextView hit_giveup;

    private TextView showlast_card; // 上一张牌

    public Hardcard_LinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    // XML加载 + 应用主题默认样式（你问的这个）
    public Hardcard_LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void set_choose(int i){
//        if(i == choose) {
//            return;
//        }
        if(i != choose)
            Translation(choose, false);
        if(Translation(i, true)){
            choose = i; // 唯一赋值成功的路径
            View item = this.getChildAt(i);
            if(item instanceof Card){
                Card View0 = (Card) item;
                hit_show_card.setText(View0.gettext());

//                IntBoolResult data = get_card_data(View0);
                int num = get_addcard_num(View0);
                if(num > 0){
                    hit_show_card.setTextColor(Color.BLACK);
                    hit_add_card.setText("+ " + num);
                }
                else{
                    hit_show_card.setTextColor(Color.WHITE);
                    hit_add_card.setText("");
                }
                boolean bool = get_card_hit(View0);
                if(bool)
                    Card.set_color(
                            hit_show_card,
                            Card.get_color(Card.type_action(hit_show_card.getText().toString())),
                            Card.alpha_can_hit);
                else
                    Card.set_color(
                            hit_show_card,
                            Card.get_color(Card.type_action(hit_show_card.getText().toString())),
                            Card.alpha_cant_hit);
                if(View0.giveup)
                    hit_giveup.setVisibility(View.VISIBLE);
                else
                    hit_giveup.setVisibility(View.INVISIBLE);
                return;
            }
        }
        choose = -1; // 唯一赋值失败的路径
        hit_show_card.setText("");
        hit_add_card.setText("");
        Card.set_color(hit_show_card, R.color.transparent);
    }
    public Card get_choose_card(){
        if(choose >= this.getChildCount() || choose < 0) {
//            if(!set_choose(0))
            return null;
        }
        View item = this.getChildAt(choose);
//        Card View0;
        if(item instanceof Card){
            return  (Card) item;
        }
        return null;
    }
    public final BiConsumer<MotionEvent, Runnable> hardcard_touch = (event, callback)->{
        int x = (int)event.getX();      //相对于控件左上角的X
        int sum = this.getChildCount(); // 0~(sum - 1)
        if(sum < 5) sum = 5;
        int i = x/show_width(sum);
        if(i >= this.getChildCount()) return;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN: // 按下
//                if(this.getChildCount() <= 0) return; // 没牌你点个锤子
//                touch = true;
                set_choose(i);
                time_start = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                set_choose(i);
                break;
            case MotionEvent.ACTION_UP: // 松开
                set_choose(i);
//                touch = false;
                if(System.currentTimeMillis() - time_start <= 500){ // 短按
                    callback.run();
                }
                break;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    public void init(Supplier<Game> game_main){
        get_main = game_main;
        hit_show_card = get_main.get().findViewById(R.id.hit_show_card); // 看来不是每个findViewById都可以找组件呢
        showlast_card = get_main.get().findViewById(R.id.showlast_card);
        hit_add_card = get_main.get().findViewById(R.id.hit_add_card);
        hit_giveup = get_main.get().findViewById(R.id.hit_giveup);
        abandon_card_string = new ArrayList<>();
        list = new ArrayList<>();
//            float y = event.getY();      //相对于控件左上角的Y
//            float rawX = event.getRawX();//相对于整个手机屏幕左上角X
//            float rawY = event.getRawY();//相对于整个手机屏幕左上角Y

        this.setOnTouchListener((v, event) ->{
            hardcard_touch.accept(event, this::hit_card); // 包装，
            return true;
        });
        listinit();
    }
    public boolean showlast_card_set(String itemstr){ // 刷新上一张的文字背景
//        if(itemstr.equals(Card.show_last_card)) return false;
        Card.show_last_card = itemstr;
        showlast_card.setText(itemstr);
        if(itemstr.isEmpty())
            Card.set_color(showlast_card, R.color.transparent);
        else
            Card.set_color(showlast_card, Card.get_color(Card.type_action(Card.show_last_card)), Card.alpha_can_hit);
        return true;
    }

    public Card addNewcard(){
        String itemstr = getRandomListItemstring();
        if(itemstr.isEmpty()) return null; // 获取""说明没牌，不加了

        return addNewcard(itemstr);
    }
    public Card addNewcard(String itemstr){
        // 每次向布局添加手牌都要经过这一步
//        String itemstr = getRandomListItemstring();
//        if(itemstr.isEmpty()) return; // 获取""说明没牌，不加了

        int childNum = this.getChildCount();
        Card newTag = new Card(this.getContext(), itemstr, childNum, show_width(5));
        // 从这里开始接上Card构造函数后续函数
        this.addView(newTag); // 显示在手牌
        card_settext(newTag, itemstr);

        if(childNum >= 4 & !astion_sorting){  // 若手牌大于5张(此时那张额外childNum还没加)// 若当前为固定长度排序
            astion_sorting = true;
            for(int i=0;i<childNum;i++){
                View itemi= this.getChildAt(i);
//                    Log.d("itemi","i:" + i + "  X:" + itemi.getX() + "  Y:" + itemi.getY());
                LinearLayout.LayoutParams lpi = (LinearLayout.LayoutParams) itemi.getLayoutParams();
                lpi.width = 0;
                lpi.weight = 1; // 改为比例排序
                itemi.setLayoutParams(lpi);
            }
        }
        get_main.get().layout_show.text_number_set("btnendround", get_btnendround_num() * (childNum + 1)); // "结束回合\nMP+0"
        get_main.get().layout_show.text_number_refresh("head_card"); // 手牌刷新打印

        return newTag;
    }
    public boolean Translation(int i, boolean bool){ // 改变选中特效
        if(i >= this.getChildCount() || i < 0) return false;
        View item = this.getChildAt(i);
        if(item instanceof Card){
            Card View0 = (Card) item;
            View0.special_effects(bool);
            return true;
        }
        return false;
    }

    private boolean hit_true = false; // 不清楚会不会，但以防万一，做一个锁
    public void hit_card(){ // 尝试打出当前选择的牌

        Game main = get_main.get();
        if(main.tcpService.currentState == TcpService.STATE_IDLE) return; // 没网你打个锤子牌
        else if(!main.allow_roune_strat()) return; // 没到回合

        Card View0 = get_choose_card();
        if(View0 == null) return;

//        IntBoolResult result = get_card_data(View0);
        int[] arr = {1, 1, main.layout_show.play_att, get_addcard_num(View0)}; // 打牌时的数值计算
        boolean[] arr_bool = {get_card_hit(View0), true, true, !Card.square.equals(View0.gettext())};
    //                        是否能打牌        是否刷新上一张  是否删除该牌          (判断是否为方块)是否结束本回合
        Map<String,Object> data = new HashMap<>();
        data.put("text", View0.gettext());
        data.put("arr", arr);
        data.put("str", Card.show_last_card);
        data.put("arr_bool", arr_bool);
        data.put("card", View0);
        get_main.get().hit_card_sign.emitDirect(data); // 打牌，计三个数
//    Log.d("show_last_card", Card.show_last_card);
//    Log.d("hit_card", View0.gettext());
        if(!arr_bool[0]) return; // 是否可以打出

        if(hit_true) return; // 上面有return 只能放这里了
        hit_true = true;

        get_main.get().Sound_sffects("NORMAL", "combo");

        main.layout_show.text_number_add("combo", 1); // combo自加打印

        main.layout_show.text_number_add("play_technique", arr[0]); // 每打一张牌加1技力
        main.layout_show.text_number_add("enemy_technique", arr[1]); // 每打一张牌加1技力
        // 没做低于0检测估计是为了mode16做的，也可能是是否低于0由buff自己决定
        main.layout_show.text_number_add("play_att", arr[2]); // 每打一张牌增加累计攻击力


        if(arr[3] == 1)
            get_main.get().Sound_sffects("NORMAL", "draw");
        else if(arr[3] > 1)
            get_main.get().Sound_sffects("NORMAL", "drawSpecial");
//        else if(arr[3] > 1)

        for(int i=0;i<arr[3];i++) {
            addNewcard(); // 是否额外抽牌
        }
        if(arr_bool[1]) showlast_card_refresh(View0.gettext()); // 刷新上一张

        if(arr_bool[2]) removecard(choose); // 删除手牌


//        Map<String,Object> data = new HashMap<>();
//        data.put("type","test");
        main.hit_card_succes_sign.emitDirect(data);


        if(arr_bool[3]) {
            if (main.tcpService.currentState == TcpService.STATE_SERVER) {
                main.game_state = Game.STATE_CLIENT_START;
                main.sk_use_show_refresh_all(); // 刷新所有技能的显示
                main.game_send_smg(new ChatMsg(main));
                if(!main.show_text.getText().toString().equals("对方回合结束，可肆意打牌(房主)"))
                    main.show_text.setText("轮到对方打牌");
            } else if (main.tcpService.currentState == TcpService.STATE_CLIENT) {
                main.game_state = Game.STATE_SERVER_START;
                main.sk_use_show_refresh_all(); // 刷新所有技能的显示
                main.game_send_smg(new ChatMsg(main));
                if(!main.show_text.getText().toString().equals("对方回合结束，可肆意打牌(玩家)"))
                    main.show_text.setText("轮到对方打牌");
            }
        }
        else{ // 只刷新 不知道会不会有bug
            get_main.get().Sound_sffects("NORMAL", "critical");
            main.game_send_smg(new ChatMsg(main, Game.STATE_REFRESH));
        }

//        hit_add_card.setText("");
//        Card.set_color(
//            hit_show_card,
//            Card.get_color(Card.type_action(hit_show_card.getText().toString())),
//            Card.alpha_can_hit
//        );

        checkall_addcard(); // 多放几个这玩意把，我实在受不了了
        hit_true = false;
    }
    public void showlast_card_refresh(String str){ // 刷新上一张 同时刷新手牌展示
        if(this.showlast_card_set(str)){
            checkall_addcard();
        }
    }
    public void removecard(Card card){ // 清除手牌中的一张牌 有返回弃牌库判断
        if(this.indexOfChild(card) == -1) return;
        if(card.abandon){
            abandon_card_string.add(card.gettext()); // 添加弃牌堆
            get_main.get().layout_show.text_number_add("abandon", 1);
        }
        destroycard(this.indexOfChild(card));
    }
    public void removecard(int i0){ // 清除手牌中的一张牌 有返回弃牌库判断
        int childNum = this.getChildCount();
        if(i0 >= childNum) return;

        View item = this.getChildAt(i0);
        if(item instanceof Card){
            Card View0 = (Card) item;
            if(View0.abandon){
                abandon_card_string.add(View0.gettext()); // 添加弃牌堆
                get_main.get().layout_show.text_number_add("abandon", 1);
            }
        }

        destroycard(i0);
    }
    public void destroycard(int i0){ // 销毁手牌中的一张牌 没有返回弃牌库判断
        int childNum;
//        = this.getChildCount();
//        if(i0 >= childNum) return;

        this.removeViewAt(i0); // 清除手牌第i0张
        childNum = this.getChildCount(); // 清除后重新获取个数
        if(choose == i0)
            set_choose(-1);

        get_main.get().layout_show.text_number_set("btnendround", get_btnendround_num() * childNum); // "结束回合\nMP+0"

        get_main.get().layout_show.text_number_add("head_card", -1); // 手牌自减打印

        if(childNum < 5 && astion_sorting){  // 若手牌小于5张// 若当前为比例排序
            astion_sorting = false;
            for(int i=0;i<childNum;i++){
                View itemi= this.getChildAt(i);
                LinearLayout.LayoutParams lpi = (LinearLayout.LayoutParams) itemi.getLayoutParams();
                lpi.width = show_width(5);
                lpi.weight = 0; // 改为固定长度排序
                itemi.setLayoutParams(lpi);
            }
        }
    }
    public int get_btnendround_num(){
        int[] arr = {5}; // 增加5倍剩余手牌mp
        Map<String,Object> data = new HashMap<>();
        data.put("arr", arr);
        get_main.get().btnendround_num_sign.emitDirect(data);
        if(arr[0] < 0) arr[0] = 0;
        return arr[0];
    }

    public int card_begin = 5;
    public void turn_begin(){
        int[] arr = {card_begin};

        Map<String,Object> data = new HashMap<>();
        data.put("arr", arr);
        get_main.get().card_begin_num_sign.emitDirect(data); // 每回合开始时发牌数

        for(int i=0;i<arr[0];i++) addNewcard();

        checkall_addcard(); // 多放几个这玩意把，我实在受不了了
    }
    public int show_width(int sum){ // 给card使用
        if(sum == 0) return 0;
        return this.getWidth() / sum;
    }
    public Card card_settext(Card card, String str){
        card.settext(str);
//        IntBoolResult data = get_card_data(card);
        card.add_effects(get_addcard_num(card));
        card.set_color_begin(get_card_hit(card));
//        card.add_effects(data.intVal);
//        card.set_color_begin(data.boolVal);
        return card;
    }
//    private static boolean HIT = true;
    public static boolean checkture_hit(String str){
        return checkture_hit(str, Card.show_last_card);
    }
    public static boolean checkture_hit(String str_0, String str_1){ // 检测这张牌否可以打出
//        if(!HIT) return false;
//        HIT = false;

        if(Card.triangle.equals(str_0)) return false;
        if(str_0.isEmpty()) return false;
        if(str_1.isEmpty()) return true; // str.length() == 0

        if(Card.square.equals(str_0) || Card.square.equals(str_1)) return true; // 为方块

        int num = Card.get_number(str_1), mun0 = Card.get_number(str_0);
        if(num == -1 || mun0 == -1) return false;

        if(Card.type_action(str_1) == Card.type_action(str_0))
            return true; // 类型相同
        else if( mun0 >= num - 1 && mun0 <= num + 1)
            return true; // 差值1

        return false;
    }
    public void checkall_addcard(){ // 对所有手牌进行 打出 额外抽牌 检测
//        if(Card.show_last_card.isEmpty()) return; // == "" 没牌

        int sum = this.getChildCount();
        for(int i=0;i<sum;i++){
            View child = this.getChildAt(i);
            if(child instanceof Card){
                Card View0 = (Card) child;
//                IntBoolResult data = get_card_data(View0);
                View0.add_effects(get_addcard_num(View0));
                View0.set_color_begin(get_card_hit(View0));
            }
        }
    }
//    public static class IntBoolResult{
//        public int intVal;
//        public boolean boolVal;
//
//        public IntBoolResult(int intVal, boolean boolVal){
//            this.intVal = intVal;
//            this.boolVal = boolVal;
//        }
//    }
//    public IntBoolResult get_card_data(Card card){
//        return get_card_data(card, Card.show_last_card);
//    }
//    public IntBoolResult get_card_data(String text, String str){
//        int[] arr = {0};
//        boolean[] bool = {checkture_hit(text, str)};
//        if(Card.increment(text, str)) arr[0] = 1;
//        Map<String,Object> data = new HashMap<>();
//        data.put("text", text);
//        data.put("str", str);
//        data.put("arr", arr);
//        data.put("bool", bool);
////        data.put("card", card);
//        get_main.get().addcard_sign.emitDirect(data);
//        return new IntBoolResult(arr[0], bool[0]);
//    }
//    public IntBoolResult get_card_data(Card card, String str){ //
//        return get_card_data(card.gettext(), str);
//    }
    public boolean get_card_hit(Card card){
        return get_card_hit(card, Card.show_last_card);
    }
    public boolean get_card_hit(Card card, String str){ // 是否能打牌
        boolean[] bool = {checkture_hit(card.gettext(), str)};
        Map<String,Object> data = new HashMap<>();
        data.put("text", card.gettext());
        data.put("str", str);
        data.put("bool", bool);
        data.put("card", card);
        get_main.get().getHit_card_sign.emitDirect(data);
        return bool[0];
    }
//    public boolean get_card_hit(String text, String str){ // 是否能打牌
////        if(this.indexOfChild(card) == -1) return 0;
//
////        int[] arr = {0};
//        boolean[] bool = {checkture_hit(text, str)};
////        if(card.increment()) arr[0] = 1;
//        Map<String,Object> data = new HashMap<>();
//        data.put("text", text);
//        data.put("str", str);
//        data.put("bool", bool);
//        get_main.get().getHit_card_sign.emitDirect(data);
//        return bool[0];
//    }
    public int get_addcard_num(Card card){
        return get_addcard_num(card, Card.show_last_card);
    }
    public int get_addcard_num(Card card, String str){ // 额外抽牌
        int[] arr = {0};
        if(card.increment()) arr[0] = 1;
        Map<String,Object> data = new HashMap<>();
        data.put("text", card.gettext());
        data.put("arr", arr);
        data.put("str", str);
        data.put("card", card);
        get_main.get().addcard_sign.emitDirect(data);
        return arr[0];
    }
    public int get_addcard_num(String text, String str){ // 额外抽牌 说难听的，专门为mode17做的
        int[] arr = {0};
        if(Card.increment(text, str)) arr[0] = 1;
        Map<String,Object> data = new HashMap<>();
        data.put("text", text);
        data.put("arr", arr);
        data.put("str", str);
//        data.put("card", card);
        get_main.get().addcard_sign.emitDirect(data);
        return arr[0];
    }

    public String getRandomListItemstring(){ // 从牌库随机返回一张牌
        //先判空，防止崩溃
        if(list == null || list.isEmpty()){
            return "";
        }
        Random random = new Random();
        int i = random.nextInt(list.size());
        String sitemstr = list.get(i);
        list.remove(i); // 清除牌库的一张牌
        get_main.get().layout_show.text_number_refresh("library"); // 刷新打印牌库
        return sitemstr;
    }
    public static String getRandomListstring(){ // 随机返回一张牌
        Random random = new Random();
//        int i = 1 + random.nextInt(8);
        String sitemstr = String.valueOf(1 + random.nextInt(8));
        switch (random.nextInt(5)){
            case 0: sitemstr = sitemstr + "x";break;
            case 1: sitemstr = sitemstr + "s";break;
            case 2: sitemstr = sitemstr + "d";break;
            case 3: sitemstr = sitemstr + "c";break;
            case 4: sitemstr = sitemstr + "h";break;
        }
        return sitemstr;
    }
    public static String getRandomListstring(int i){ // 随机返回一张牌 但固定数字
        Random random = new Random();
//        int i = 1 + random.nextInt(8);
        String sitemstr = String.valueOf(i);
        switch (random.nextInt(5)){
            case 0: sitemstr = sitemstr + "x";break;
            case 1: sitemstr = sitemstr + "s";break;
            case 2: sitemstr = sitemstr + "d";break;
            case 3: sitemstr = sitemstr + "c";break;
            case 4: sitemstr = sitemstr + "h";break;
        }
        return sitemstr;
    }
    public static String getRandomListstring(String str){ // 从随机返回一张牌 但固定类型
        Random random = new Random();
//        int i = 1 + random.nextInt(8);
        String sitemstr = String.valueOf(1 + random.nextInt(8));
        return sitemstr + str;
    }
    private void listinit(){ // list牌库初始化
        list.clear();
// x黄色 s蓝色 d紫色 c绿色 h红色
        for(int i=1;i<=8;i++) list.add(i + "x");
        for(int i=1;i<=8;i++) list.add(i + "s");
        for(int i=1;i<=8;i++) list.add(i + "d");
        for(int i=1;i<=8;i++) list.add(i + "c");
        for(int i=1;i<=8;i++) list.add(i + "h");
    }
}