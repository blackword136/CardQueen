package com.example.text;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Choose_Mode extends AppCompatActivity {
    public static Consumer<Game> mode_create = Choose_Mode::mode_0;
    private String mode_str = "";
    private Button sk = null;
    private TextView text_sk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_mode);
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


        Button bt_back = findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> { // 返回
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "close");
            startService(playIntent);
            Intent intent = new Intent(this, MainActivity.class);
//
//            if(sk_ch_bt.get(0) != null) intent.putExtra("sk_str_1", sk_ch_bt.get(0).getText());
//            if(sk_ch_bt.get(1) != null) intent.putExtra("sk_str_2", sk_ch_bt.get(1).getText());
//            if(sk_ch_bt.get(2) != null) intent.putExtra("sk_str_3", sk_ch_bt.get(2).getText());
//            if(sk_ch_bt.get(3) != null) intent.putExtra("sk_str_4", sk_ch_bt.get(3).getText());
//            intent.putExtra("Choose_Skills", "true");
            intent.putExtra("mode_str", mode_str);

            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            // 不清除本页面的情况下打开一个已经打开过的页面
        });
        text_sk = findViewById(R.id.text_sk);



        findViewById(R.id.mode_btn_0_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_0_1)));
        findViewById(R.id.mode_btn_1_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_1_1)));
        findViewById(R.id.mode_btn_2_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_2_1)));
        findViewById(R.id.mode_btn_2_2).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_2_2)));
        findViewById(R.id.mode_btn_6_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_6_1)));
        findViewById(R.id.mode_btn_10_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_10_1)));
        findViewById(R.id.mode_btn_11_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_11_1)));
        findViewById(R.id.mode_btn_12_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_12_1)));
        findViewById(R.id.mode_btn_12_2).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_12_2)));
        findViewById(R.id.mode_btn_13_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_13_1)));
        findViewById(R.id.mode_btn_13_2).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_13_2)));
        findViewById(R.id.mode_btn_14_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_14_1)));
        findViewById(R.id.mode_btn_14_2).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_14_2)));
        findViewById(R.id.mode_btn_16_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_16_1)));
        findViewById(R.id.mode_btn_17_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_17_1)));
        findViewById(R.id.mode_btn_17_2).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_17_2)));
        findViewById(R.id.mode_btn_18_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_18_1)));
        findViewById(R.id.mode_btn_21_1).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_21_1)));
        findViewById(R.id.mode_btn_21_2).setOnClickListener(v -> bt_click_choose(v, this.getString(R.string.mode_21_2)));

    }
    public static void mode_choose(Context context, String str){
        if(context.getString(R.string.sk_mode_0_1).equals(str)) mode_create = Choose_Mode::mode_0;
        else if(context.getString(R.string.sk_mode_1_1).equals(str)) mode_create = Choose_Mode::mode_1;
        else if(context.getString(R.string.sk_mode_2_1).equals(str)) mode_create = Choose_Mode::mode_2;
        else if(context.getString(R.string.sk_mode_2_2).equals(str)) mode_create = Choose_Mode::mode_2_2;
        else if(context.getString(R.string.sk_mode_6_1).equals(str)) mode_create = Choose_Mode::mode_6;
        else if(context.getString(R.string.sk_mode_10_1).equals(str)) mode_create = Choose_Mode::mode_10_1;
        else if(context.getString(R.string.sk_mode_11_1).equals(str)) mode_create = Choose_Mode::mode_11;
        else if(context.getString(R.string.sk_mode_12_1).equals(str)) mode_create = Choose_Mode::mode_12;
        else if(context.getString(R.string.sk_mode_12_2).equals(str)) mode_create = Choose_Mode::mode_12_2;
        else if(context.getString(R.string.sk_mode_13_1).equals(str)) mode_create = Choose_Mode::mode_13_1;
        else if(context.getString(R.string.sk_mode_13_2).equals(str)) mode_create = Choose_Mode::mode_13_2;
        else if(context.getString(R.string.sk_mode_14_1).equals(str)) mode_create = Choose_Mode::mode_14;
        else if(context.getString(R.string.sk_mode_14_2).equals(str)) mode_create = Choose_Mode::mode_14_2;
        else if(context.getString(R.string.sk_mode_16_1).equals(str)) mode_create = Choose_Mode::mode_16_1;
        else if(context.getString(R.string.sk_mode_17_1).equals(str)) mode_create = Choose_Mode::mode_17_1;
        else if(context.getString(R.string.sk_mode_17_2).equals(str)) mode_create = Choose_Mode::mode_17_2;
        else if(context.getString(R.string.sk_mode_18_1).equals(str)) mode_create = Choose_Mode::mode_18;
        else if(context.getString(R.string.sk_mode_21_1).equals(str)) mode_create = Choose_Mode::mode_21;
        else if(context.getString(R.string.sk_mode_21_2).equals(str)) mode_create = Choose_Mode::mode_21_2;
        else mode_create = null;
    }

    private void bt_click_choose(View v, String text){ // 右边技能按钮点击设置
        Button bt = (Button)v;
        if(sk == null){
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "ok2");
            startService(playIntent);
            button_change_background(bt, true);
            sk = bt;
            mode_str = sk.getText().toString();
//            mode_create = sk_suplier;
            text_sk.setText(sk.getText().toString() + " : " + text);
        }
        else if(sk.equals(bt)){
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "close");
            startService(playIntent);
            button_change_background(bt, false);
            sk = null;
            mode_str = "";
//            mode_create = null;
            text_sk.setText("");
        }
        else {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
            button_change_background(sk, false);
            button_change_background(bt, true);
            sk = bt;
            mode_str = sk.getText().toString();
//            mode_create = sk_suplier;
            text_sk.setText(sk.getText().toString() + " : " + text);
        }
    }
    private void button_change_background(Button bt, boolean bool){ // 根据是否能点击刷新显示状态
        int bgColor;
        if(bool){
            bgColor = ContextCompat.getColor(this, R.color.skills_background_cantouch);
        }
        else {
            bgColor = ContextCompat.getColor(this, R.color.transparent);
        }
        ColorStateList bgTintList = ColorStateList.valueOf(bgColor);
        bt.setBackgroundTintList(bgTintList);
    }
    public static void mode_0(Game main) {
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara11);
        main.startService(playIntent);
    }
    public static void mode_1(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara11);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara11_h);
        main.startService(playIntent);
        //1.拿到根布局View
//        ViewGroup rootLayout = findViewById(R.id.root_layout);
//        view.setBackgroundResource(R.drawable.bg_pic);

        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character11);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        TextView view_card = new TextView(main);
        view_card.setId(View.generateViewId());
        main_Gravity.addView(view_card, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentHeight(view_card.getId(), 0.1f);
        constraintSet.setDimensionRatio(view_card.getId(),"1:1");
//        - `setRotation()`：中心点旋转（最常用）
//        - `setRotationX()`：绕水平轴 3D 翻转
//        - `setRotationY()`：绕垂直轴 3D 翻转

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
        drawable.setStroke(
                5,
                Color.parseColor("#dbe604")
        );
        view_card.setBackground(drawable);

        TextView view_num = new TextView(main);
        view_num.setId(View.generateViewId());
        view_num.setTextColor(Color.WHITE);
        view_num.setGravity(Gravity.CENTER); // 上下左右居中
        main_Gravity.addView(view_num, new ConstraintLayout.LayoutParams(0,0));
        view_num.setTextSize(18); // sp
//        constraintSet.constrainPercentHeight(view_card.getId(), 0.1f);
//        constraintSet.setDimensionRatio(view_num.getId(),"1:1");
        view_num.setText("");
//        Card.set_color(view_num, Card.color_x, Card.alpha_cant_hit);
//        view_card.setRotation(45); // android:rotation="270"

        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_num.getId(), ConstraintSet.START, view_card.getId(), ConstraintSet.START);
        constraintSet.connect(view_num.getId(), ConstraintSet.END, view_card.getId(), ConstraintSet.END);
        constraintSet.connect(view_num.getId(), ConstraintSet.TOP, view_card.getId(), ConstraintSet.TOP);
        constraintSet.connect(view_num.getId(), ConstraintSet.BOTTOM, view_card.getId(), ConstraintSet.BOTTOM);

        constraintSet.applyTo(main_Gravity);

        view_card.setRotation(45); // android:rotation="270"  先约束好宽高（正方形 / 百分比），再代码设置 rotation
        int[] card_num = {0};
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode1_1
//                String str = (String) data.get("text");
            int num = Card.get_number((String) data.get("text"));
            if(card_num[0] != 0 && card_num[0] == num){
                int[] arr = (int[]) data.get("arr");
                if(arr != null)
                    arr[0]++;
//                card_num[0] = 0;
//                view_num.setText("");
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode1_1
            String str = (String) data.get("text");
            int num = Card.get_number(str);
            if(card_num[0] != 0 && card_num[0] == num){
                main.Sound_sffects("SPECIAL", "element");
                card_num[0] = 0;
                view_num.setText("");
                drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
                view_card.setBackground(drawable);
            }
            if(num != -1 && 'x' == Card.type_action(str)){
                main.Sound_sffects("SOUND", "game_start");
                card_num[0] = num;
                view_num.setText(String.valueOf(num));
                drawable.setColor(Color.parseColor("#44dbe604"));
                view_card.setBackground(drawable);
            }
        }));


        Kilss_Button info = main.btn_skill.get(0);
        Choose_Skills.set_namempuse(info, R.string.sk_10_1_1);
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        // 切分不对，出来只有1，本身数值没减，多的牌只有h
        info.setOnClickListener(v->Choose_Skills.setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            char type = View0.type_action();
            if(num != -1 && type != '0') {
                if(num <= 1){
                    info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
                }
                else {
                    Random random = new Random();
                    int num0 = random.nextInt(num - 1) + 1;
                    num -= num0;
                    info.get_main.get().tagContainer.card_settext(View0, String.valueOf(num) + type);
                    info.get_main.get().tagContainer.addNewcard(String.valueOf(num0) + type).copy_bute(View0);
                }
            }
            else {
                info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
            }
        }));
        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_10_1_1));

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_1_1));
    }
    public static void mode_2(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara13);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara13_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character13);

        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode2_1
                int[] arr = (int[]) data.get("arr");
                if(arr != null && main.tagContainer.getChildCount() == 1) // 此时还没删除手牌
                    arr[0]+=3;
        }));

        Kilss_Button info = main.btn_skill.get(0);
        Choose_Skills.set_namempuse(info, R.string.sk_5_2_1);
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard(Card.square);
        }));
        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_5_2_1));

        for(int i=0;i<3;i++){
            main.tagContainer.list.add(i, Card.square);
        }

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_2_1));
    }
    public static void mode_2_2(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara13);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara13_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character13);

        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode2_1
            int[] arr = (int[]) data.get("arr");
            if(arr != null) { // 此时还没删除手牌
                if(main.tagContainer.getChildCount() == 1)
                    arr[0] += 5;
                if (Card.square.equals(data.get("text"))) { // 是方块
                        arr[0]++;
                        // 实际是14_2先设计出方块+1再搬到这里的哦
                }
            }
        }));

        Kilss_Button info = main.btn_skill.get(0);
        main.hit_card_sign.connect(new Signal.Slot((data)->{ // mode2_2
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && arr_bool[0] && main.tagContainer.getChildCount() == 1) {
                info.set_use_fre_add(info.use_fre_max);
            }
        }, 10000));

        Choose_Skills.set_namempuse(info, R.string.sk_5_2_1);
        info.set_need_mp(15); // 降低循环
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard(Card.square);
            info.get_main.get().tagContainer.list.add(Hardcard_LinearLayout.getRandomListstring()); // 并向牌库添加一张牌
        }));
        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_5_2_1) + "\n并向牌库添加一张牌");

        for(int i=0;i<3;i++){
            main.tagContainer.list.add(i, Card.square);
        }

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_2_2));
    }
    public static void mode_6(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara16_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character16);

        main.tagContainer.card_begin += 2;
        main.getHit_card_sign.connect(new Signal.Slot((data)->{ // mode6_1
            String text = (String) data.get("text");
            String str = (String) data.get("str");
            if(Card.type_action(text) != Card.type_action(str)){
                int num0 = Card.get_number(text);
                int num1 = Card.get_number(str);
                if(num0 != -1 && num1 != -1 && (num0 == num1 + 2 || num0 == num1 - 2)) {
                    boolean[] bool = (boolean[]) data.get("bool");
                    if(bool != null){
                        bool[0] = true;
                    }
                }
            }
        }));

        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode6_1
            String text = (String) data.get("text");
            String str = (String) data.get("str");
            if(Card.type_action(text) != Card.type_action(str)){
                int num0 = Card.get_number(text);
                int num1 = Card.get_number(str);
                if(num0 != -1 && num1 != -1 && (num0 == num1 + 2 || num0 == num1 - 2)) {
                    main.layout_show.text_number_add(
                            "play_technique",
                            -2 );
                    Card card = main.tagContainer.addNewcard(Card.square);
                    card.abandon = false;
                }
            }
        }));

        Kilss_Button info = main.btn_skill.get(0);
        info.set_name("弃疗");
        info.set_need_mp(0);
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        info.setOnClickListener(v->Choose_Skills.setOn_choosecard_sta(v, info, (View0)->{
            if(View0.is_special()) {
                info.get_main.get().tagContainer.addNewcard();
                info.get_main.get().tagContainer.addNewcard();
            }
            main.layout_show.text_number_add(
                    "play_hp",
                    15 );
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
        }));
        Kilss_Button.setonlongclick.accept(info, "选择一张手牌丢弃，并恢复15血量，若丢弃特殊卡牌，则额外抽取两张牌");

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_6_1));
    }

    public static void mode_10_1(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara11);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara11_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character11);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        ConstraintLayout layout = new ConstraintLayout(main);
        layout.setId(View.generateViewId());
        main_Gravity.addView(layout, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentWidth(layout.getId(), 0.4f);
        constraintSet.constrainPercentHeight(layout.getId(), 0.12f);

        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(12); // sp
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_text.getId(), 0.05f);

        constraintSet.connect(layout.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(layout.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);
        // 应用约束生效
        constraintSet.applyTo(main_Gravity);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
        drawable.setStroke(
                2,
                ContextCompat.getColor(main, R.color.blue)
        );
        layout.setBackground(drawable);

        Kilss_Button info = main.btn_skill.get(0);
        final char[] list_char = {'s', 'h', 'd', 'c', 'x'};
        ConstraintSet constraintSet_i = new ConstraintSet();
        constraintSet_i.clone(layout);// 克隆当前布局已有的约束
        TextView view_card_s = new TextView(main);
        TextView view_card_h = new TextView(main);
        TextView view_card_d = new TextView(main);
        TextView view_card_c = new TextView(main);
        TextView view_card_x = new TextView(main);
        List<TextView> view_card = new ArrayList<>(Arrays.asList(view_card_s,view_card_h,view_card_d,view_card_c,view_card_x));
        for(int i=0;i<5;i++) {
            TextView view = view_card.get(i);
            view.setText(String.valueOf(list_char[i]));
            view.setId(View.generateViewId());
            view.setTextColor(Color.WHITE);
            view.setSingleLine(true); // 不能换行
            view.setGravity(Gravity.CENTER); // 上下左右居中
            view.setTextSize(28); // sp
            switch (i) {
                case 0:
                    Card.set_color(view, Card.color_s, Card.alpha_cant_hit);
                    break;
                case 1:
                    Card.set_color(view, Card.color_h, Card.alpha_cant_hit);
                    break;
                case 2:
                    Card.set_color(view, Card.color_d, Card.alpha_cant_hit);
                    break;
                case 3:
                    Card.set_color(view, Card.color_c, Card.alpha_cant_hit);
                    break;
                case 4:
                    Card.set_color(view, Card.color_x, Card.alpha_cant_hit);
                    break;
            }
            layout.addView(view, new ConstraintLayout.LayoutParams(0,0));
            constraintSet_i.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet_i.connect(view.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet_i.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet_i.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        }
        for(int i=0;i<5;i++){
            if(i != 0)
                constraintSet_i.connect(view_card.get(i).getId(), ConstraintSet.START, view_card.get(i-1).getId(), ConstraintSet.END);
            if(i != 4)
                constraintSet_i.connect(view_card.get(i).getId(), ConstraintSet.END, view_card.get(i+1).getId(), ConstraintSet.START);
        }
        constraintSet_i.applyTo(layout);

//        boolean[] bool = {true, false};
        StringBuilder num_str = new StringBuilder();
        Runnable refresh_card = ()->{
            if(num_str.length() >= 5){
                main.Sound_sffects("SPECIAL", "firewall");
                num_str.setLength(0);
                info.set_use_fre_add(info.use_fre_max);
                view_text.setText("全元素爆发");
            }
            else view_text.setText("元素收集");
            for(int i=0;i<5;i++){
                if(num_str.indexOf(String.valueOf(list_char[i])) == -1){
                    switch (i) {
                        case 0:
                            Card.set_color(view_card.get(i), Card.color_s, Card.alpha_cant_hit);
                            break;
                        case 1:
                            Card.set_color(view_card.get(i), Card.color_h, Card.alpha_cant_hit);
                            break;
                        case 2:
                            Card.set_color(view_card.get(i), Card.color_d, Card.alpha_cant_hit);
                            break;
                        case 3:
                            Card.set_color(view_card.get(i), Card.color_c, Card.alpha_cant_hit);
                            break;
                        case 4:
                            Card.set_color(view_card.get(i), Card.color_x, Card.alpha_cant_hit);
                            break;
                    }
                }
                else {
                    switch (i) {
                        case 0:
                            Card.set_color(view_card.get(i), Card.color_s, Card.alpha_can_hit);
                            break;
                        case 1:
                            Card.set_color(view_card.get(i), Card.color_h, Card.alpha_can_hit);
                            break;
                        case 2:
                            Card.set_color(view_card.get(i), Card.color_d, Card.alpha_can_hit);
                            break;
                        case 3:
                            Card.set_color(view_card.get(i), Card.color_c, Card.alpha_can_hit);
                            break;
                        case 4:
                            Card.set_color(view_card.get(i), Card.color_x, Card.alpha_can_hit);
                            break;
                    }
                }
            }
        };

//        main.semd_sign.connect(new Signal.Slot((data)->{ // 发送信息
//            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
//            if(msg != null) { //
//                if(bool[1]){
//                    msg.memory.put("mode_10_2_num", num_str.toString());
//                    bool[1] = false;
//                }
//            }
//        }));
//        main.ondownload_sign.connect(new Signal.Slot((data)->{ // 接受信息并刷新数据时
//            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
//            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
//                String str = (String)msg.memory.get("mode_10_2_num");
//                if(str != null){
//                    num_str.setLength(0);
//                    num_str.append(str);
//                    view_text.setText("已刷新碎片");
//                    refresh_card.run();
//                }
//            }
//        }));
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode10_1
            char char_i = Card.type_action((String) data.get("text"));
            if(char_i != '0' && char_i == Card.type_action(Card.show_last_card)){
                if(num_str.indexOf(String.valueOf(char_i)) == -1) {
                    int[] arr = (int[]) data.get("arr");
                    if(arr != null)
                        arr[0] ++;
                }
            }
        }));

        main.hit_card_sign.connect(new Signal.Slot((data)->{  // mode10_1
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && arr_bool[0]) {
                char char_i = Card.type_action((String) data.get("text"));
                if (char_i != '0' && char_i == Card.type_action(Card.show_last_card) && num_str.indexOf(String.valueOf(char_i)) == -1) {
                    // 此时上一张牌还没刷新
                    for (int i = 0; i < 5; i++)
                        if (list_char[i] == char_i) {
//                        bool[1] = true;
                            main.Sound_sffects("SPECIAL", "element");
                            num_str.append(list_char[i]);
                            refresh_card.run();
//                        view_text.setText("碎片填入成功");
                            break;
                        }
                } else view_text.setText("");
            }
        }, 10000));
//        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode10_1
//            char char_i = Card.type_action((String) data.get("text"));
//            if(char_i != '0' && char_i == Card.type_action(Card.show_last_card) && num_str.indexOf(String.valueOf(char_i)) == -1){
//                for(int i=0;i<5;i++) if(list_char[i] == char_i){
////                        bool[1] = true;
//                    num_str.append(list_char[i]);
//                    refresh_card.run();
////                        view_text.setText("碎片填入成功");
//                    break;
//                }
//            }
//            else view_text.setText("");
//        }));

        Choose_Skills.crat_sk_1_2_1(info);
        info.set_need_mp(15);
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
//            bool[0] = true;
            num_str.setLength(0);
            refresh_card.run();
            info.set_use_fre_add(info.use_fre_max);
        }));
        Kilss_Button.setonlongclick.accept(info, "选择一张手牌复制并加入手牌");

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_10_1));
    }
    public static void mode_11(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara11);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara11_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character11);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        TextView view_card = new TextView(main);
        view_card.setId(View.generateViewId());
        main_Gravity.addView(view_card, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentHeight(view_card.getId(), 0.1f);
        constraintSet.setDimensionRatio(view_card.getId(),"1:1");

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
        drawable.setStroke(
                5,
                Color.parseColor("#dbe604")
        );
        view_card.setBackground(drawable);

        TextView view_num = new TextView(main);
        view_num.setId(View.generateViewId());
        view_num.setTextColor(Color.WHITE);
        view_num.setGravity(Gravity.CENTER); // 上下左右居中
        main_Gravity.addView(view_num, new ConstraintLayout.LayoutParams(0,0));
        view_num.setTextSize(18); // sp

        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        view_text.setTextColor(Color.WHITE);
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        view_text.setTextSize(12); // sp
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentHeight(view_text.getId(), 0.05f);
        constraintSet.constrainPercentWidth(view_text.getId(), 0.3f);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
//        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, view_text.getId(), ConstraintSet.TOP);

        constraintSet.connect(view_num.getId(), ConstraintSet.START, view_card.getId(), ConstraintSet.START);
        constraintSet.connect(view_num.getId(), ConstraintSet.END, view_card.getId(), ConstraintSet.END);
        constraintSet.connect(view_num.getId(), ConstraintSet.TOP, view_card.getId(), ConstraintSet.TOP);
        constraintSet.connect(view_num.getId(), ConstraintSet.BOTTOM, view_card.getId(), ConstraintSet.BOTTOM);

        constraintSet.applyTo(main_Gravity);

        Kilss_Button info = main.btn_skill.get(0);
        view_card.setRotation(45); // android:rotation="270"  先约束好宽高（正方形 / 百分比），再代码设置 rotation
        int[] card_num = {0};
        boolean[] bool = {false}; // 下次发送信息时，顺便发送
        main.semd_sign.connect(new Signal.Slot((data)->{ // 发送信息
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { //
                if(bool[0]){
                    msg.memory.put("mode_11_num", card_num[0]);
                    bool[0] = false;
                }
            }
        }));
        main.ondownload_sign.connect(new Signal.Slot((data)->{ // 接受信息并刷新数据时
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
                Object obj = msg.memory.get("mode_11_num");
                if(obj instanceof Number){
                    card_num[0] = ((Number)obj).intValue();
                    main.Sound_sffects("SPECIAL", "element");
                    if(card_num[0] != 0){
                        view_num.setText(String.valueOf(card_num[0]));
                        drawable.setColor(Color.parseColor("#44dbe604"));
                        view_card.setBackground(drawable);
                    }
                    else{
                        view_num.setText("");
                        drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
                        view_card.setBackground(drawable);
                    }
                }
            }
        }));
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode11_1
//                String str = (String) data.get("text");
            int num = Card.get_number((String) data.get("text"));
            if(card_num[0] != 0 && card_num[0] == num){
                int[] arr = (int[]) data.get("arr");
                if(arr != null)
                    arr[0]++;
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode11_1
            String str = (String) data.get("text");
            int num = Card.get_number(str);
            boolean print = false;
            if(card_num[0] != 0 && card_num[0] == num){
                bool[0] = true;
                card_num[0] = 0;
                view_num.setText("");
                drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
                view_card.setBackground(drawable);
                print = true;
                main.Sound_sffects("SPECIAL", "element");
                if(num > 1) {
                    view_text.setText("触发交叉刻印，刷新场地技能");
                    info.set_use_fre_add(info.use_fre_max);
                }
                else {
                    view_text.setText("触发交叉刻印");
                }
            }
            if(num != -1 && 'x' == Card.type_action(str)){
                main.Sound_sffects("SOUND", "game_start");
                boolean[] arr_bool = (boolean[]) data.get("arr_bool");
                if(arr_bool != null) {
                    arr_bool[3] = false;
                    if(!print) {
                        print = true;
                        view_text.setText("刻印成功，可继续打牌");
                    }
                    else view_text.setText(view_text.getText().toString() + "，可继续打牌");
                }
                bool[0] = true;
                card_num[0] = num;
                view_num.setText(String.valueOf(num));
                drawable.setColor(Color.parseColor("#44dbe604"));
                view_card.setBackground(drawable);
            }
            if(!print) view_text.setText("");
        }, 500));


        Choose_Skills.set_namempuse(info, R.string.sk_10_1_1);
        info.set_need_mp(5);
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        info.setOnClickListener(v->Choose_Skills.setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            char type = View0.type_action();
            if(num != -1 && type != '0') {
                if(num <= 1){
                    info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
                }
                else {
                    Random random = new Random();
                    int num0 = random.nextInt(num - 1) + 1;
                    num -= num0;
                    info.get_main.get().tagContainer.card_settext(View0, String.valueOf(num) + type);
                    info.get_main.get().tagContainer.addNewcard(String.valueOf(num0) + type).copy_bute(View0);
                }
            }
            else {
                info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
            }
        }));
        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_10_1_1));

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_11_1));
    }
    public static void mode_12(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara12);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara12_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character12);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        // 创建水平ProgressBar
        ProgressBar progressBar = new ProgressBar(main, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setId(View.generateViewId());
        main_Gravity.addView(progressBar, new ConstraintLayout.LayoutParams(0,0));
        progressBar.setMax(3);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#dddd00")));
//        progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(R.color.blue));
//        progressBar.setBackgroundColor(R.color.blue);
        progressBar.setProgress(3);
        constraintSet.constrainPercentWidth(progressBar.getId(), 0.3f);
        constraintSet.constrainPercentHeight(progressBar.getId(), 0.1f);

        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(18); // sp
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        // shadowRadius=2f，dx=1f，dy=1f，黑色阴影
        view_text.setShadowLayer(2f, 1f, 1f, Color.BLACK);
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_text.getId(), 0.1f);

        constraintSet.connect(progressBar.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(progressBar.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, progressBar.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        // 应用约束生效
        constraintSet.applyTo(main_Gravity);

        boolean[] fradd_refresh = {true};
        int[] card_num = {0};
        Consumer<Integer> set_card_num = (i)->{
            if(i == -1) card_num[0]--;
            else card_num[0] = i;
//            if(card_num[0] < 0) card_num[0] = 0;
            if(card_num[0] == 0 && fradd_refresh[0]){
                fradd_refresh[0] = false;
                main.layout_show.text_number_setmax(
                        "play_att",
                        main.layout_show.play_att + 1 );
            }
            progressBar.setProgress(card_num[0]);
            view_text.setText(card_num[0] + " / 3");
        };
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode12_1
            String str = (String) data.get("text");
            if(card_num[0] > 0 && 'x' == Card.type_action(str)){
                int[] arr = (int[]) data.get("arr");
                if(arr != null)
                    arr[0]++;
//                card_num[0] = 0;
//                // 拥有电量打出x额外抽牌
//                view_num.setText("");
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode12_1
            String str = (String) data.get("text");
            if(card_num[0] > 0){
                main.layout_show.text_number_add(
                        "play_hp",
                        card_num[0] );
                if('x' == Card.type_action(str)){
                    main.Sound_sffects("SPECIAL", "tension");
                    set_card_num.accept(-1);
//                view_num.setText("");
                }
            }
            else if('x' == Card.type_action(str))
                main.Sound_sffects("SPECIAL", "no_battery");
//            int num = Card.get_number(str);
//            if(num != -1 && 'x' == Card.type_action(str)){
//                card_num[0] = num;
//                view_num.setText(String.valueOf(num));
//            }
        }));
        Kilss_Button info = main.btn_skill.get(0);
        Choose_Skills.set_namempuse(info, R.string.sk_10_1_2);
        info.set_name("超级涡轮");
        info.set_need_mp(30);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
            fradd_refresh[0] = true;
            set_card_num.accept(3);
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard();
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++) { // 手牌
                View item = main.tagContainer.getChildAt(i);
                if (item instanceof Card) {
                    Card View1 = (Card) item;
                    int num0 = View1.get_number();
                    if(num0 != -1)
                        main.tagContainer.card_settext(View1, num0 + "x");
                }
            }
            set_card_num.accept(3);
        }));
        Kilss_Button.setonlongclick.accept(info,
                "抽一张牌，并将所有卡牌花色变为x(黄色)\n并将电量充满");

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_12_1));
    }
    public static void mode_12_2(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara12_h);
//        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara12_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character12);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        // 创建水平ProgressBar
        ProgressBar progressBar = new ProgressBar(main, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setId(View.generateViewId());
        main_Gravity.addView(progressBar, new ConstraintLayout.LayoutParams(0,0));
        progressBar.setMax(5);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#dddd00")));
        progressBar.setProgress(5);
        constraintSet.constrainPercentWidth(progressBar.getId(), 0.3f);
        constraintSet.constrainPercentHeight(progressBar.getId(), 0.1f);

        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(18); // sp
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        // shadowRadius=2f，dx=1f，dy=1f，黑色阴影
        view_text.setShadowLayer(2f, 1f, 1f, Color.BLACK);
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_text.getId(), 0.1f);

        constraintSet.connect(progressBar.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(progressBar.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, progressBar.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        // 应用约束生效
        constraintSet.applyTo(main_Gravity);

        int[] card_num = {5};
        Consumer<Integer> set_card_num = (i)->{
            if(i == -1) card_num[0]--;
            else card_num[0] = i;
            progressBar.setProgress(card_num[0]);
            view_text.setText(card_num[0] + " / " + progressBar.getMax());
        };
//        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode12_2
//            String str = (String) data.get("text");
//            if(card_num[0] > 0 && 'x' == Card.type_action(str)){
//                int[] arr = (int[]) data.get("arr");
//                if(arr != null)
//                    arr[0]++;
//            }
//        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode12_2
            String str = (String) data.get("text");
            if('x' == Card.type_action(str)){
                if(card_num[0] > 0) {
                    main.Sound_sffects("SPECIAL", "tension");
                    main.layout_show.text_number_add(
                            "play_technique",
                            4);
//                    main.layout_show.text_number_add(
//                            "play_hp",
//                            card_num[0]);
                    set_card_num.accept(-1);
                }
                else {
                    main.Sound_sffects("SPECIAL", "no_battery");
                    if(main.layout_show.play_hp > progressBar.getMax()){
                        main.layout_show.text_number_add(
                                "play_hp",
                                -1 * progressBar.getMax());
                    }
                    else if(main.layout_show.play_hp > 1){
                        main.layout_show.text_number_add(
                                "play_hp",
                                main.layout_show.play_hp - 1);
                    }
                }
            }
        }));
        Kilss_Button info = main.btn_skill.get(0);
        Choose_Skills.set_namempuse(info, R.string.sk_10_1_2);
        info.set_name("超级涡轮");
        info.set_need_mp(30);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
//            fradd_refresh[0] = true;
            set_card_num.accept(progressBar.getMax());
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++) { // 手牌
                View item = main.tagContainer.getChildAt(i);
                if (item instanceof Card) {
                    Card View1 = (Card) item;
                    char type = View1.type_action();
                    if(type != 'x' && type != '0') {
                        int num0 = View1.get_number();
                        if (num0 != -1) {
                            main.tagContainer.card_settext(View1, num0 + "x");
                            info.get_main.get().tagContainer.addNewcard();
                        }
                    }
                }
            }
//            progressBar.setMax(progressBar.getMax() + max);
            set_card_num.accept(progressBar.getMax());
        }));
        Kilss_Button.setonlongclick.accept(info,
                "并将所有卡牌花色变为x(黄色)\n并将电量充满,且每使一张卡牌变成黄色，则抽取一张牌");

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_12_2));
    }

//    private static void mode_13(Game main){
//
//    }
    public static void mode_13_1(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara13);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara13_h);
        main.startService(playIntent);
        // 四个框没有显示
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character13);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        ConstraintLayout layout = new ConstraintLayout(main);
        layout.setId(View.generateViewId());
        main_Gravity.addView(layout, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentWidth(layout.getId(), 0.4f);
        constraintSet.constrainPercentHeight(layout.getId(), 0.15f);

        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(12); // sp
//        view_text.setText("游戏开始时将所有x卡牌替换为方块\n违反规则会提升对方1点基础攻击力");
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_text.getId(), 0.05f);

        constraintSet.connect(layout.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(layout.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);
        // 应用约束生效
        constraintSet.applyTo(main_Gravity);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
        drawable.setStroke(
                2,
                ContextCompat.getColor(main, R.color.blue)
        );
        layout.setBackground(drawable);

        final char[] list_char = {'s', 'h', 'd', 'c'};
        ConstraintSet constraintSet_i = new ConstraintSet();
        constraintSet_i.clone(layout);// 克隆当前布局已有的约束
        TextView view_card_s = new TextView(main);
//        view_card_s.setText("s");
        TextView view_card_h = new TextView(main);
//        view_card_h.setText("h");
        TextView view_card_d = new TextView(main);
//        view_card_d.setText("d");
        TextView view_card_c = new TextView(main);
//        view_card_c.setText("c");
        List<TextView> view_card = new ArrayList<>(Arrays.asList(view_card_s,view_card_h,view_card_d,view_card_c));
        for(int i=0;i<4;i++) {
            TextView view = view_card.get(i);
            view.setText(String.valueOf(list_char[i]));
            view.setId(View.generateViewId());
            view.setTextColor(Color.WHITE);
            view.setSingleLine(true); // 不能换行
            view.setGravity(Gravity.CENTER); // 上下左右居中
            view.setTextSize(28); // sp
            switch (i) {
                case 0:
                    Card.set_color(view, Card.color_s, Card.alpha_cant_hit);
                    break;
                case 1:
                    Card.set_color(view, Card.color_h, Card.alpha_cant_hit);
                    break;
                case 2:
                    Card.set_color(view, Card.color_d, Card.alpha_cant_hit);
                    break;
                case 3:
                    Card.set_color(view, Card.color_c, Card.alpha_cant_hit);
                    break;
            }
            layout.addView(view, new ConstraintLayout.LayoutParams(0,0));
            constraintSet_i.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet_i.connect(view.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet_i.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet_i.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        }
        for(int i=0;i<4;i++){
            if(i != 0)
                constraintSet_i.connect(view_card.get(i).getId(), ConstraintSet.START, view_card.get(i-1).getId(), ConstraintSet.END);
            if(i != 3)
                constraintSet_i.connect(view_card.get(i).getId(), ConstraintSet.END, view_card.get(i+1).getId(), ConstraintSet.START);
        }
        constraintSet_i.applyTo(layout);

        boolean[] fradd_refresh = {true};
        StringBuilder num_str = new StringBuilder();

        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode13_1
//            String str = (String) data.get("text");
            if(fradd_refresh[0] && 'x' == Card.type_action((String) data.get("text"))){
                if(num_str.length() == 0) return;
                int[] arr = (int[]) data.get("arr");
                if(arr != null)
                    arr[0] += num_str.length();
            }
        }));
        main.hit_card_sign.connect(new Signal.Slot((data)->{ // mode13_1
            // 需要在添加手牌前，把冻结添加，否则打出x新增的牌没有新的额外手牌检测buff
            // 也要把碎片添加，否则新增卡牌也没有
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && arr_bool[0]) {
                char char_i = Card.type_action((String) data.get("text"));
                if('x' == char_i) {
                    if(!fradd_refresh[0]) {
                        view_text.setText("");
                        return;
                    }
                    main.Sound_sffects("SPECIAL", "element");
                    view_text.setText("进入冻结，不能额外抽牌");
                    fradd_refresh[0] = false;
                    num_str.setLength(0);
                    for(int i=0;i<4;i++){
                        switch (i) {
                            case 0:
                                Card.set_color(view_card.get(i), Card.color_s, Card.alpha_cant_hit);
                                break;
                            case 1:
                                Card.set_color(view_card.get(i), Card.color_h, Card.alpha_cant_hit);
                                break;
                            case 2:
                                Card.set_color(view_card.get(i), Card.color_d, Card.alpha_cant_hit);
                                break;
                            case 3:
                                Card.set_color(view_card.get(i), Card.color_c, Card.alpha_cant_hit);
                                break;
                        }
                    }
                    Signal.Slot slot = new Signal.Slot((data_i)->{
                        int[] arr = (int[]) data_i.get("arr");
                        if(arr != null)
                            arr[0] = 0;
                    }, 10000);
                    main.addcard_sign.connect(slot); // mode_13
                    main.buff_time_settle_sign.connect(
                            main. new buff_time_settle(1, main.addcard_sign, slot, false, false)
                    );
                }
                else {
                    if(num_str.indexOf(String.valueOf(char_i)) == -1) {
                        for (int i = 0; i < 4; i++) {
                            if (list_char[i] == char_i) {
                                main.Sound_sffects("SOUND", "game_start");
                                view_text.setText("碎片填入成功");
                                num_str.append(list_char[i]);
                                switch (i) {
                                    case 0:
                                        Card.set_color(view_card.get(i), Card.color_s, Card.alpha_can_hit);
                                        break;
                                    case 1:
                                        Card.set_color(view_card.get(i), Card.color_h, Card.alpha_can_hit);
                                        break;
                                    case 2:
                                        Card.set_color(view_card.get(i), Card.color_d, Card.alpha_can_hit);
                                        break;
                                    case 3:
                                        Card.set_color(view_card.get(i), Card.color_c, Card.alpha_can_hit);
                                        break;
                                }
                                return;
                            }
                        }
                    }
                    else{
                        if(fradd_refresh[0])
                            view_text.setText("");
                        else
                            view_text.setText("冻结中，不能额外抽牌");
                    }
                }
            }
        }, 10000));

        Kilss_Button info = main.btn_skill.get(0);
        info.set_need_mp(10);
        info.set_name("生成x");
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
            fradd_refresh[0] = true;
            view_text.setText("");
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard(Hardcard_LinearLayout.getRandomListstring("x"));
        }));
        Kilss_Button.setonlongclick.accept(info, "随机生成一张x类型卡牌加入手牌");

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_13_1));
    }
    public static void mode_13_2(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara13);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara13_h);
        main.startService(playIntent);
//        {
//            Intent intent_bgm = new Intent(main,BgmService.class);
//            intent_bgm.putExtra("CMD","NEXT");
//            main.startService(intent_bgm);
//        }
        // 四个框没有显示
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character13);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        ConstraintLayout layout = new ConstraintLayout(main);
        layout.setId(View.generateViewId());
        main_Gravity.addView(layout, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentWidth(layout.getId(), 0.4f);
        constraintSet.constrainPercentHeight(layout.getId(), 0.15f);

        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(12); // sp
//        view_text.setText("游戏开始时将所有x卡牌替换为方块\n违反规则会提升对方1点基础攻击力");
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_text.getId(), 0.05f);

        constraintSet.connect(layout.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(layout.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);
        // 应用约束生效
        constraintSet.applyTo(main_Gravity);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
        drawable.setStroke(
                3,
                ContextCompat.getColor(main, R.color.blue)
        );
        layout.setBackground(drawable);

        Kilss_Button info = main.btn_skill.get(0);
        final char[] list_char = {'s', 'h', 'd', 'c'};
        ConstraintSet constraintSet_i = new ConstraintSet();
        constraintSet_i.clone(layout);// 克隆当前布局已有的约束
        TextView view_card_s = new TextView(main);
        TextView view_card_h = new TextView(main);
        TextView view_card_d = new TextView(main);
        TextView view_card_c = new TextView(main);
        List<TextView> view_card = new ArrayList<>(Arrays.asList(view_card_s,view_card_h,view_card_d,view_card_c));
        for(int i=0;i<4;i++) {
            TextView view = view_card.get(i);
            view.setText(String.valueOf(list_char[i]));
            view.setId(View.generateViewId());
            view.setTextColor(Color.WHITE);
            view.setSingleLine(true); // 不能换行
            view.setGravity(Gravity.CENTER); // 上下左右居中
            view.setTextSize(28); // sp
            switch (i) {
                case 0:
                    Card.set_color(view, Card.color_s, Card.alpha_cant_hit);
                    break;
                case 1:
                    Card.set_color(view, Card.color_h, Card.alpha_cant_hit);
                    break;
                case 2:
                    Card.set_color(view, Card.color_d, Card.alpha_cant_hit);
                    break;
                case 3:
                    Card.set_color(view, Card.color_c, Card.alpha_cant_hit);
                    break;
            }
            layout.addView(view, new ConstraintLayout.LayoutParams(0,0));
            constraintSet_i.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet_i.connect(view.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
            constraintSet_i.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
            constraintSet_i.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        }
        for(int i=0;i<4;i++){
            if(i != 0)
                constraintSet_i.connect(view_card.get(i).getId(), ConstraintSet.START, view_card.get(i-1).getId(), ConstraintSet.END);
            if(i != 3)
                constraintSet_i.connect(view_card.get(i).getId(), ConstraintSet.END, view_card.get(i+1).getId(), ConstraintSet.START);
        }
        constraintSet_i.applyTo(layout);

        boolean[] bool = {true, false};
        StringBuilder num_str = new StringBuilder();
        Runnable refresh_card = ()->{
            for(int i=0;i<4;i++){
                if(num_str.indexOf(String.valueOf(list_char[i])) == -1){
                    switch (i) {
                        case 0:
                            Card.set_color(view_card.get(i), Card.color_s, Card.alpha_cant_hit);
                            break;
                        case 1:
                            Card.set_color(view_card.get(i), Card.color_h, Card.alpha_cant_hit);
                            break;
                        case 2:
                            Card.set_color(view_card.get(i), Card.color_d, Card.alpha_cant_hit);
                            break;
                        case 3:
                            Card.set_color(view_card.get(i), Card.color_c, Card.alpha_cant_hit);
                            break;
                    }
                }
                else {
                    switch (i) {
                        case 0:
                            Card.set_color(view_card.get(i), Card.color_s, Card.alpha_can_hit);
                            break;
                        case 1:
                            Card.set_color(view_card.get(i), Card.color_h, Card.alpha_can_hit);
                            break;
                        case 2:
                            Card.set_color(view_card.get(i), Card.color_d, Card.alpha_can_hit);
                            break;
                        case 3:
                            Card.set_color(view_card.get(i), Card.color_c, Card.alpha_can_hit);
                            break;
                    }
                }
            }
        };

        main.semd_sign.connect(new Signal.Slot((data)->{ // 发送信息
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { //
                if(bool[1]){
                    msg.memory.put("mode_13_2_num", num_str.toString());
                    bool[1] = false;
                }
            }
        }));
        main.ondownload_sign.connect(new Signal.Slot((data)->{ // 接受信息并刷新数据时
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
                String str = (String)msg.memory.get("mode_13_2_num");
                if(str != null){
                    num_str.setLength(0);
                    num_str.append(str);
                    view_text.setText("已刷新碎片");
                    refresh_card.run();
                }
            }
        }));
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode13_2
            if(bool[0] && 'x' == Card.type_action((String) data.get("text"))){
                if(num_str.length() == 0) return;
                int[] arr = (int[]) data.get("arr");
                if(arr != null)
                    arr[0] += num_str.length();
            }
        }));

        main.hit_card_sign.connect(new Signal.Slot((data)->{ // mode13_2
            // 需要在添加手牌前，把冻结添加，否则打出x新增的牌没有新的额外手牌检测buff
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && arr_bool[0]) {
                char char_i = Card.type_action((String) data.get("text"));
                if('x' == char_i) {
                    main.Sound_sffects("SPECIAL", "element");
                    if(!bool[0]) {
                        view_text.setText("冻结中，不能额外抽牌");
                        return;
                    }
                    bool[1] = true;
                    if(num_str.length() >= 4){
                        view_text.setText("完全解放，刷新场地技能");
                        info.set_use_fre_add(info.use_fre_max);
                        num_str.setLength(0);
                        refresh_card.run();
                    }
                    else {
                        view_text.setText("进入冻结，不能额外抽牌");
                        bool[0] = false;
                        num_str.setLength(0);
                        refresh_card.run();
                        Signal.Slot slot = new Signal.Slot((data_i) -> {
                            int[] arr = (int[]) data_i.get("arr");
                            if (arr != null)
                                arr[0] = 0;
                        }, 10000);
                        main.addcard_sign.connect(slot); // mode_13_2
                        main.buff_time_settle_sign.connect(
                                main.new buff_time_settle(1, main.addcard_sign, slot, false, false)
                        );
                    }
                }
                else {
                    if(num_str.indexOf(String.valueOf(char_i)) == -1){
                        for(int i=0;i<4;i++) if(list_char[i] == char_i){
                            bool[1] = true;
                            num_str.append(list_char[i]);
                            refresh_card.run();
                            main.Sound_sffects("SOUND", "game_start");
                            if(num_str.length() >= 4) {
//                                boolean[] arr_bool = (boolean[]) data.get("arr_bool");
//                                if (arr_bool != null) {
                                    arr_bool[3] = false;
                                    view_text.setText("碎片成功填满，可继续打牌");
                                    return;
//                                }
                            }
                            view_text.setText("碎片填入成功");
                            break;
                        }
                    }
                    else {
                        if(bool[0])
                            view_text.setText("");
                        else
                            view_text.setText("冻结中，不能额外抽牌");
                    }
                }
            }
        }, 10000));
//        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode13_2
//            char char_i = Card.type_action((String) data.get("text"));
//            if('x' != char_i)
//            {
//                if(num_str.indexOf(String.valueOf(char_i)) == -1){
//                    for(int i=0;i<4;i++) if(list_char[i] == char_i){
//                        bool[1] = true;
//                        num_str.append(list_char[i]);
//                        refresh_card.run();
//                        if(num_str.length() >= 4) {
//                            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
//                            if (arr_bool != null) {
//                                arr_bool[3] = false;
//                                view_text.setText("碎片成功填满，可继续打牌");
//                                return;
//                            }
//                        }
//                        view_text.setText("碎片填入成功");
//                        break;
//                    }
//               }
//               else {
//                    if(bool[0])
//                        view_text.setText("");
//                    else
//                        view_text.setText("冻结中，不能额外抽牌");
//                }
//            }
//        }, 500));
//        main.trun_begin_sign.connect(new Signal.Slot((data)->{
//        }));

        info.set_need_mp(10);
        info.set_name("生成x");
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            bool[0] = true;
            view_text.setText("");
            num_str.setLength(0);
            refresh_card.run();
            info.set_use_fre_add(info.use_fre_max);

//Intent intent_bgm = new Intent(main,BgmService.class);
//intent_bgm.putExtra("CMD","NEXT");
//main.startService(intent_bgm);
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard(Hardcard_LinearLayout.getRandomListstring("x"));
        }));
        Kilss_Button.setonlongclick.accept(info, "随机生成一张x类型卡牌加入手牌");

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_13_2));
    }

    public static void mode_14(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara14);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara14_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character14);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        TextView view_card = new TextView(main);
        view_card.setId(View.generateViewId());
        main_Gravity.addView(view_card, new ConstraintLayout.LayoutParams(0,0));
        view_card.setTextSize(18); // sp
        view_card.setText("");
        view_card.setTextColor(Color.WHITE);
        view_card.setSingleLine(true); // 不能换行
        view_card.setShadowLayer(5f, 2f, 2f, 0x80000000);
        view_card.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_card.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_card.getId(), 0.1f);

//        TextView view_text = new TextView(main);
//        view_text.setId(View.generateViewId());
//        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
//        view_text.setTextSize(12); // sp
//        view_text.setText("游戏开始时将所有x卡牌替换为方块\n违反规则会提升对方1点基础攻击力");
//        view_text.setTextColor(Color.WHITE);
//        view_text.setSingleLine(true); // 不能换行
//        view_text.setGravity(Gravity.CENTER); // 上下左右居中
//        constraintSet.constrainPercentWidth(view_text.getId(), 0.2f);

        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

//        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
//        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, view_card.getId(), ConstraintSet.BOTTOM);
//        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.applyTo(main_Gravity);

        char[] chars = {'0', '0'};
        Consumer<Integer> att_add = (i)->{
            main.layout_show.text_number_setmax(
                    "enemy_att",
                    main.layout_show.enemy_att + 1 );
            if(view_card.getText().toString().isEmpty()) {
                main.Sound_sffects("SPECIAL", "rule_alert");
                view_card.setText("违反规则: " + i);
            }
            else view_card.setText(view_card.getText().toString() + " ," + i);
        };
        main.hit_card_sign.connect(new Signal.Slot((data)->{ // mode14_1
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && arr_bool[0]) {
                view_card.setText("");
                String text = (String) data.get("text");
                int num_str = Card.get_number(text);
                if(num_str != -1){
                    if(Card.get_number(Card.show_last_card) > num_str)
                        att_add.accept(1);   // \n1.不可打出比上一张更小的数字
                }
                if(main.layout_show.play_technique >= 50){
                    att_add.accept(3);   // \n3.mp达到50及以上禁止出牌
                }
                if(main.tagContainer.getChildCount() >= 8){
                    att_add.accept(4);   // \n4.手牌达到8张禁止出牌
                }
                char char_i = Card.type_action(text);
                if(char_i != '0') {
                    if(chars[0] == char_i){
                        if(chars[1] == char_i){
                            att_add.accept(5); // \n2.不可连续打出三张同色卡牌
                        }
                        else {
                            chars[1] = char_i;
                        }
                    }
                    else {
                        chars[0] = char_i;
                        chars[1] = '0';
                    }
                }
                else {
                    chars[0] = '0';
                    chars[1] = '0';
                }
            }
        }, 10000));

        main.click_trunover_sign.connect(new Signal.Slot((data)->{ // mode14_1
            view_card.setText("");
            if(main.layout_show.combo % 4 != 0){
                att_add.accept(5);   // \n5.只能在Combo被4整除时回合结束
            }
        }));

//        int sum = main.tagContainer.list.size();
        for(int i=0;i<main.tagContainer.list.size();i++){
            if(Card.type_action(main.tagContainer.list.get(i)) == 'x') main.tagContainer.list.set(i, "方块");
        }

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_14_1));
//        text.setText("\n1.不可打出比上一张更小的数字\n2.不可连续打出三张同色卡牌\n3.mp达到50及以上禁止出牌\n4.手牌达到8张禁止出牌\n5.只能在Combo被4整除时回合结束");
    }
    // 连打三张同色不生效
    public static void mode_14_2(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara14);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara14_h);
        main.startService(playIntent);
        main.tagContainer.card_begin += 1;
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character14);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        TextView view_card = new TextView(main);
        view_card.setId(View.generateViewId());
        main_Gravity.addView(view_card, new ConstraintLayout.LayoutParams(0,0));
        view_card.setTextSize(18); // sp
        view_card.setText("");
        view_card.setTextColor(Color.WHITE);
        view_card.setSingleLine(true); // 不能换行
        view_card.setShadowLayer(5f, 2f, 2f, 0x80000000);
        view_card.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_card.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_card.getId(), 0.1f);

        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(12); // sp
        view_text.setText("");
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        view_text.setShadowLayer(5f, 2f, 2f, 0x80000000);
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_text.getId(), 0.1f);

        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, view_card.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.applyTo(main_Gravity);

        char[] chars = {'0', '0'};
        boolean[] sk_refresh = {false, false}; // [0] 为刷新对方场地技能和添加方块次数 [1] 为自己的添加方块次数是否可触发
        Kilss_Button info = main.btn_skill.get(0);
        Consumer<Integer> att_add = (i)->{
            main.layout_show.text_number_setmax(
                    "enemy_att",
                    main.layout_show.enemy_att + 1 );
            if(view_card.getText().toString().isEmpty()) {
                main.Sound_sffects("SPECIAL", "rule_alert");
                view_card.setText("违反规则: " + i);
            }
            else view_card.setText(view_card.getText().toString() + " ," + i);
        };
        main.semd_sign.connect(new Signal.Slot((data)->{ // 发送信息
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { //
                if(sk_refresh[0]){
                    msg.memory.put("mode_14_2_bool", sk_refresh[0]);
                    sk_refresh[0] = false;
                }
            }
        }));
        main.ondownload_sign.connect(new Signal.Slot((data)->{ // 接受信息并刷新数据时
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
                Boolean bool = (Boolean)msg.memory.get("mode_14_2_bool");
                if(bool != null){
//                    main.Sound_sffects("SPECIAL", "rule_alert");
                    view_text.setText("对方场地效果添加方块冷却中");
                    info.set_use_fre_add(info.use_fre_max);
                    sk_refresh[1] = true;
                }
            }
        }));
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode14_2
            if(Card.square.equals(data.get("text"))){ // 是方块
                int[] arr = (int[]) data.get("arr");
                if(arr != null)
                    arr[0] ++;
            }
        }));
        main.hit_card_sign.connect(new Signal.Slot((data)->{ // mode14_2
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && arr_bool[0]) {
                boolean bool = false;
                view_card.setText("");
                String text = (String) data.get("text");
                int num_str = Card.get_number(text);
                if(num_str != -1){
                    if(Card.get_number(Card.show_last_card) > num_str){
                        bool = true;
                        att_add.accept(1);   // \n1.不可打出比上一张更小的数字
                    }
                }
                if(main.layout_show.play_technique >= 50){
                    bool = true;
                    att_add.accept(3);   // \n3.mp达到50及以上禁止出牌
                }
                if(main.tagContainer.getChildCount() >= 8){
                    bool = true;
                    att_add.accept(4);   // \n4.手牌达到8张禁止出牌
                }
                char char_i = Card.type_action(text);
                if(char_i != '0') {
                    if(chars[0] == char_i){
                        if(chars[1] == char_i){
                            att_add.accept(5); // \n2.不可连续打出三张同色卡牌
                        }
                        else {
                            chars[1] = char_i;
                        }
                    }
                    else {
                        chars[0] = char_i;
                        chars[1] = '0';
                    }
                }
                else {
                    chars[0] = '0';
                    chars[1] = '0';
                }
                if(bool && sk_refresh[1]){
                    view_text.setText("场地效果添加方块冷却中");
                    sk_refresh[1] = false;
                    sk_refresh[0] = true;
                    info.set_use_fre_add(info.use_fre_max);
                    Card card = main.tagContainer.addNewcard(Card.square);
                    card.abandon = false;
                }
            }
        }, 10000));

        main.click_trunover_sign.connect(new Signal.Slot((data)->{ // mode14_2
            view_card.setText("");
            if(main.layout_show.combo % 4 != 0){
                att_add.accept(5);   // \n5.只能在Combo被4整除时回合结束
            }
            if(main.layout_show.combo <= main.layout_show.turn){
                att_add.accept(6);   // \n6回合结束时Combo不能小于回合数
            }
        }));

        Choose_Skills.set_namempuse(info, R.string.sk_5_2_1);
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
            sk_refresh[0] = true;
            sk_refresh[1] = true;
            view_text.setText("");
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            Card card = info.get_main.get().tagContainer.addNewcard(Card.square);
            card.abandon = false;
        }));
        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_5_2_1) + "\n该方块打出后不会进入弃牌库");

        for(int i=0;i<8;i++){
            main.tagContainer.list.add("方块");
        }

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_14_2));
    }
    public static void mode_16_1(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara16);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara16_h);
        main.startService(playIntent);
        {
            ArrayList<String> list = main.tagContainer.list;
            for (int i = 1; i <= 8; i++) list.add(i + "x");
            for (int i = 1; i <= 8; i++) list.add(i + "s");
            for (int i = 1; i <= 8; i++) list.add(i + "d");
            for (int i = 1; i <= 8; i++) list.add(i + "c");
            for (int i = 1; i <= 8; i++) list.add(i + "h");
        }

        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character16);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        TextView view_card = new TextView(main);
        view_card.setId(View.generateViewId());
        main_Gravity.addView(view_card, new ConstraintLayout.LayoutParams(0,0));
        view_card.setTextSize(18); // sp
        view_card.setText("");
        view_card.setTextColor(Color.WHITE);
        view_card.setSingleLine(true); // 不能换行
        view_card.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_card.getId(), 0.3f);
        constraintSet.constrainPercentHeight(view_card.getId(), 0.1f);

        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.applyTo(main_Gravity);

        boolean[] reversal = {true, true}; // 正反面(否是翻转) [1]开关
//        String[] card_set = {""};
        int[] ints = {0, 1};

        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode16_1
            int[] arr = (int[]) data.get("arr");
            if(arr != null && main.tagContainer.getChildCount() == 1) // 此时还没删除手牌
                arr[0]+=3;
        }));
        main.getHit_card_sign.connect(new Signal.Slot((data)->{ // mode16_1
            boolean[] bool = (boolean[]) data.get("bool");
            if (bool != null && reversal[1]) { // 开关
                Card card = (Card) data.get("card");
                if(card != null){
                    GradientDrawable drawable = new GradientDrawable();
                    drawable.setShape(GradientDrawable.RECTANGLE);
                    drawable.setColor(ContextCompat.getColor(main, R.color.transparent));
                    if(bool[0]){
                        drawable.setStroke(
                                10,
                                Color.parseColor("#B633FF")
                        );
                    }
                    else {
                        drawable.setStroke(
                                10,
                                Color.parseColor("#FFFFFFFF")
                        );
                    }
                    card.setBackground(drawable);
                }
                if(reversal[0]) {
                    bool[0] = true;
                }
                else {
                    bool[0] = !bool[0];
                }
            }
        }, 10000));
        main.hit_card_sign.connect(new Signal.Slot((data)->{ // mode16_1
            if(!reversal[0]){
                int[] arr = (int[]) data.get("arr");
                if(arr != null){
                    arr[0] -= 2;
                    arr[1] -= 2;
                }
            }
        }));

        Kilss_Button info = main.btn_skill.get(0);
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode_16_1
            if(reversal[0]) { // 未处于反转
                Card card = (Card) data.get("card");
                String str = (String) data.get("str");
                if (card != null) {
                    reversal[1] = false;
                    if (!main.tagContainer.get_card_hit(card, str)) {
                        reversal[0] = false;
                        view_card.setText("反转中");
                        info.set_name("转化三角");
                        main.Sound_sffects("SPECIAL", "cyan_reverse");
                    }
                    reversal[1] = true;
                }
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode_16_1
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(Card.triangle.equals(data.get("text")) && arr_bool != null) { // 打出三角时可继续打牌
                arr_bool[3] = false;
            }
        }, 500));

        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
            reversal[0] = true;
            view_card.setText("");
            info.set_name("转化方块");
            main.Sound_sffects("SPECIAL", "cyan_normal");
        }));

        Choose_Skills.set_namempuse(info, R.string.sk_10_2_1);
        info.set_need_mp(0);
        info.setOnClickListener(v->Choose_Skills.setOn_choosecard_sta(v, info, (View0)->{
            if(reversal[0])
                info.get_main.get().tagContainer.card_settext(View0, Card.square);
            else
                info.get_main.get().tagContainer.card_settext(View0, Card.triangle);
        }));

        Kilss_Button.setonlongclick.accept(info, "选择一张手牌变为方块,若为反转状态下则是变为三角");

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_16_1));
    }
    private static void mode_17(Game main){
        main.tagContainer.card_begin += 2;
        {
            ArrayList<String> list = main.tagContainer.list;
            for (int i = 1; i <= 8; i++) list.add(i + "x");
            for (int i = 1; i <= 8; i++) list.add(i + "s");
            for (int i = 1; i <= 8; i++) list.add(i + "d");
            for (int i = 1; i <= 8; i++) list.add(i + "c");
            for (int i = 1; i <= 8; i++) list.add(i + "h");
        }
    }
    public static void mode_17_1(Game main){ // 克蕾儿
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara31);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara31_h);
        main.startService(playIntent);
        main.tagContainer.card_begin += 2;
        {
            ArrayList<String> list = main.tagContainer.list;
            for (int i = 1; i <= 8; i++) list.add(i + "x");
            for (int i = 1; i <= 8; i++) list.add(i + "s");
            for (int i = 1; i <= 8; i++) list.add(i + "d");
            for (int i = 1; i <= 8; i++) list.add(i + "c");
            for (int i = 1; i <= 8; i++) list.add(i + "h");
        }
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character31);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        TextView view_card = new TextView(main);
        view_card.setId(View.generateViewId());
        main_Gravity.addView(view_card, new ConstraintLayout.LayoutParams(0,0));
        view_card.setTextSize(24); // sp

        view_card.setSingleLine(true); // 不能换行
        view_card.setTextColor(Color.WHITE);// 设置文字颜色 白色
        view_card.setGravity(Gravity.CENTER); // 上下左右居中

        // 创建水平ProgressBar
        ProgressBar progressBar = new ProgressBar(main, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setId(View.generateViewId());
        main_Gravity.addView(progressBar, new ConstraintLayout.LayoutParams(0,0));
        progressBar.setMax(15);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#dddd00")));
//        progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(R.color.blue));
//        progressBar.setBackgroundColor(R.color.blue);
        progressBar.setProgress(0);

        TextView cishu = new TextView(main);
        cishu.setId(View.generateViewId());
        main_Gravity.addView(cishu, new ConstraintLayout.LayoutParams(
                0,
                0
        ));
//        constraintSet.constrainWidth(cishu.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(cishu.getId(), ConstraintSet.WRAP_CONTENT);

        cishu.setTextSize(12); // sp
        cishu.setSingleLine(true); // 不能换行
        cishu.setTextColor(Color.WHITE);// 设置文字颜色 白色
        cishu.setGravity(Gravity.CENTER); // 上下左右居中
        cishu.setText("15 / 0 累计倍率: 100%");
        Card.set_color(cishu, R.color.color_gray_information);
//        constraintSet.constrainPercentWidth(cishu.getId(), 0.15f);


//        Card.set_color(cishu, R.color.transparent);

        // 1. 设置宽度模式为 percent
//        constraintSet.constrainWidth(view_card.getId(), ConstraintSet.WRAP_CONTENT);
// 设置百分比
        constraintSet.constrainPercentWidth(R.id.layout_venve, 0.3f);
        constraintSet.constrainPercentWidth(view_card.getId(), 0.15f);
        constraintSet.constrainPercentHeight(view_card.getId(), 0.15f);
        constraintSet.constrainPercentWidth(progressBar.getId(), 0.15f);
        // 高度同理 set.setHeightPercent(viewId,0.5f)

        // ========= 设置四边约束：子view的上下左右 绑定父布局的上下左右 =========
        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(progressBar.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.TOP, view_card.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(progressBar.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(cishu.getId(), ConstraintSet.START, view_card.getId(), ConstraintSet.END);
        constraintSet.connect(cishu.getId(), ConstraintSet.END, R.id.turn_view, ConstraintSet.START);
//        constraintSet.connect(cishu.getId(), ConstraintSet.TOP, R.id.turn_view, ConstraintSet.TOP);
        constraintSet.connect(cishu.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        // 应用约束生效
        constraintSet.applyTo(main_Gravity);


        Consumer<String> view_card_settext = (str)->{
            view_card.setText(str);
            if(str.isEmpty())
                Card.set_color(view_card, R.color.transparent);
            else
                Card.set_color(view_card, Card.get_color(Card.type_action(str)));
        };
        view_card_settext.accept("");
//        Card.set_color(view_card, Card.get_color(Card.type_action(str)));

//        this.settext(itemstr); // 文字 和 颜色

        boolean[] bool = {false, false, false}; // 下次发送信息时，顺便发送刷新场地卡牌
//        String[] card_set = {""};
        int[] ints = {0, 1};
        main.semd_sign.connect(new Signal.Slot((data)->{ // 发送信息
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) {
                if(bool[0]){
                    msg.memory.put("mode_17_str", view_card.getText().toString());
                    bool[0] = false;
                }
                if(bool[1]){
                    msg.memory.put("mode_17_int0", String.valueOf(ints[0]));
                    msg.memory.put("mode_17_int1", String.valueOf(ints[1]));
                    bool[1] = false;
                    if(bool[2]){
                        msg.memory.put("mode_17_bool", bool[2]);
                        bool[2] = false;
                    }
                }
            }
        }));
        main.ondownload_sign.connect(new Signal.Slot((data)->{ // 接受信息并刷新数据时
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
                String str = (String)msg.memory.get("mode_17_str");
                if(str != null){
                    main.Sound_sffects("SPECIAL", "interrupt", 50);
                    view_card_settext.accept(str);
                }
                String int0 = (String) msg.memory.get("mode_17_int0");
                String int1 = (String) msg.memory.get("mode_17_int1");
                try{
                    if(int0 != null)
                        ints[0] = Integer.parseInt(int0);
                    if(int1 != null)
                        ints[1] = Integer.parseInt(int1);
                    progressBar.setProgress(ints[0]);
                    cishu.setText("15 / " + ints[0] +"累计倍率: "  + ints[1] + "00%");
                }catch (NumberFormatException ignored){
                }
//                Object obj = msg.memory.get("mode_17_bool");
                if(msg.memory.get("mode_17_bool") != null){
                    main.Sound_sffects("SPECIAL", "thunder", 250);
                }
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode_17_1
            String text = (String) data.get("text");
            if(text != null) { //
                ints[0]++;
                if(ints[0] >= 15){
                    ints[1]++;
                    ints[0]-=15;
                }
                progressBar.setProgress(ints[0]);
                cishu.setText("15 / " + ints[0] +"累计倍率: "  + ints[1] + "00%");
                bool[1] = true;
                String str = view_card.getText().toString();
                if(str.isEmpty()) return;
                if(main.tagContainer.get_addcard_num(view_card.getText().toString(), text) > 0){ // 可以触发额外抽卡
                    bool[0] = true;
                    bool[2] = true;
                    main.Sound_sffects("SPECIAL", "interrupt", 50);
                    main.Sound_sffects("SPECIAL", "thunder", 250);
                    main.tagContainer.abandon_card_string.add(str);
                    int num = Card.get_number(str);
                    if(num != -1){
                        if(num * ints[1] < main.layout_show.enemy_hp)
                            main.layout_show.text_number_add("enemy_hp", -1 * num * ints[1]);
                        else if(main.layout_show.enemy_hp > 1)
                            main.layout_show.text_number_add("enemy_hp", -1 * main.layout_show.enemy_hp + 1);
                    }
                    main.tagContainer.showlast_card_refresh(str);
                    String set = main.tagContainer.getRandomListItemstring();
                    if(set.isEmpty()){
                        view_card_settext.accept("");
//                        card_set[0] = "";
                    }
                    else {
                        view_card_settext.accept(set);
//                        card_set[0] = set;
                    }
                }
                else if(Hardcard_LinearLayout.checkture_hit(str, text)){ // 可以打牌
                    bool[0] = true;
                    main.Sound_sffects("SPECIAL", "interrupt", 50);
                    main.tagContainer.abandon_card_string.add(str);
                    main.tagContainer.showlast_card_refresh(str);
                    String set = main.tagContainer.getRandomListItemstring();
                    if(set.isEmpty()){
                        view_card_settext.accept("");
//                        card_set[0] = "";
                    }
                    else {
                        view_card_settext.accept(set);
//                        card_set[0] = set;
                    }
                }
            }
        }));

        Kilss_Button info = main.btn_skill.get(0);
        Choose_Skills.set_namempuse(info, R.string.sk_10_2_2);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
            if(main.tcpService.currentState == TcpService.STATE_SERVER) {// 限定房主回合开始刷新场地卡牌
                String str = view_card.getText().toString();
                if(!str.isEmpty()) {
//                        main.tagContainer.abandon_card_string.add(str);
                    main.tagContainer.list.add(str);
                    main.layout_show.text_number_refresh("library"); // 打印牌库
//                    view_card_settext.accept("");
//                    msg.memory.put("mode_17_str", "");
                }
                String set = main.tagContainer.getRandomListItemstring();
                bool[0] = true;
                if (set.isEmpty()) {
                    view_card_settext.accept("");
//                    card_set[0] = "";
                } else {
                    view_card_settext.accept(set);
//                    card_set[0] = set;
                }
                bool[1] = true;
//            ints[0] = 0;
            }
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
//                Game main = info.get_main.get();
            main.layout_show.text_number_add(
                    "play_hp",
                    main.tagContainer.abandon_card_string.size());
            main.tagContainer.list.addAll(main.tagContainer.abandon_card_string); // 将弃牌堆的牌加回牌库
            main.tagContainer.abandon_card_string.clear(); // 清空弃牌
            main.layout_show.text_number_refresh("library"); // 打印牌库
            main.layout_show.text_number_refresh("abandon"); // 打印弃牌
            main.tagContainer.showlast_card_refresh("");
        }));

        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_10_2_2) +
                "\n每次出牌后，累计1点技力，每累计15点则消耗并提升场地100%伤害倍率");

        // ==========关键：只清除某一边约束==========
//        set.clear(viewId, ConstraintSet.BOTTOM);  // 移除底部约束

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_17_1));

//        text.setText("抽牌+2, 场地会抽取一张牌\n触发额外抽牌时,对对方造成卡牌数值的伤害\n场地卡牌可以打出时,场地卡牌会切换一张,并累计电力\n每累计15点则消耗并使伤害增加");
    }
    public static void mode_17_2(Game main){ // 克蕾儿
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara31);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara31_h);
        main.startService(playIntent);
        main.tagContainer.card_begin += 2;
        {
            ArrayList<String> list = main.tagContainer.list;
            for (int i = 1; i <= 8; i++) list.add(i + "x");
            for (int i = 1; i <= 8; i++) list.add(i + "s");
            for (int i = 1; i <= 8; i++) list.add(i + "d");
            for (int i = 1; i <= 8; i++) list.add(i + "c");
            for (int i = 1; i <= 8; i++) list.add(i + "h");
        }
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character31);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        TextView view_card = new TextView(main);
        view_card.setId(View.generateViewId());
        main_Gravity.addView(view_card, new ConstraintLayout.LayoutParams(0,0));
        view_card.setTextSize(24); // sp

        view_card.setSingleLine(true); // 不能换行
        view_card.setTextColor(Color.WHITE);// 设置文字颜色 白色
        view_card.setGravity(Gravity.CENTER); // 上下左右居中

        // 创建水平ProgressBar
        ProgressBar progressBar = new ProgressBar(main, null, android.R.attr.progressBarStyleHorizontal);
        progressBar.setId(View.generateViewId());
        main_Gravity.addView(progressBar, new ConstraintLayout.LayoutParams(0,0));
        progressBar.setMax(15);
        progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#dddd00")));
//        progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(R.color.blue));
//        progressBar.setBackgroundColor(R.color.blue);
        progressBar.setProgress(0);

        TextView cishu = new TextView(main);
        cishu.setId(View.generateViewId());
        main_Gravity.addView(cishu, new ConstraintLayout.LayoutParams(
                0,
                0
        ));
//        constraintSet.constrainWidth(cishu.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainHeight(cishu.getId(), ConstraintSet.WRAP_CONTENT);

        cishu.setTextSize(12); // sp
        cishu.setSingleLine(true); // 不能换行
        cishu.setTextColor(Color.WHITE);// 设置文字颜色 白色
        cishu.setGravity(Gravity.CENTER); // 上下左右居中
        cishu.setText("15 / 0 累计倍率: 100%");
        Card.set_color(cishu, R.color.color_gray_information);
//        constraintSet.constrainPercentWidth(cishu.getId(), 0.15f);


//        Card.set_color(cishu, R.color.transparent);

        // 1. 设置宽度模式为 percent
//        constraintSet.constrainWidth(view_card.getId(), ConstraintSet.WRAP_CONTENT);
// 设置百分比
        constraintSet.constrainPercentWidth(R.id.layout_venve, 0.3f);
        constraintSet.constrainPercentWidth(view_card.getId(), 0.15f);
        constraintSet.constrainPercentHeight(view_card.getId(), 0.15f);
        constraintSet.constrainPercentWidth(progressBar.getId(), 0.15f);
        // 高度同理 set.setHeightPercent(viewId,0.5f)

        // ========= 设置四边约束：子view的上下左右 绑定父布局的上下左右 =========
        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(progressBar.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(progressBar.getId(), ConstraintSet.TOP, view_card.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(progressBar.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(cishu.getId(), ConstraintSet.START, view_card.getId(), ConstraintSet.END);
        constraintSet.connect(cishu.getId(), ConstraintSet.END, R.id.turn_view, ConstraintSet.START);
//        constraintSet.connect(cishu.getId(), ConstraintSet.TOP, R.id.turn_view, ConstraintSet.TOP);
        constraintSet.connect(cishu.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        // 应用约束生效
        constraintSet.applyTo(main_Gravity);


        Consumer<String> view_card_settext = (str)->{
            view_card.setText(str);
            if(str.isEmpty())
                Card.set_color(view_card, R.color.transparent);
            else
                Card.set_color(view_card, Card.get_color(Card.type_action(str)));
        };
        view_card_settext.accept("");

        boolean[] bool = {false, false, false, true}; // 下次发送信息时，顺便发送刷新场地卡牌
//        String[] card_set = {""};
        int[] ints = {0, 1};
        main.semd_sign.connect(new Signal.Slot((data)->{ // 发送信息
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) {
                if(bool[0]){
                    msg.memory.put("mode_17_str", view_card.getText().toString());
                    bool[0] = false;
                }
                if(bool[1]){
                    msg.memory.put("mode_17_int0", String.valueOf(ints[0]));
                    msg.memory.put("mode_17_int1", String.valueOf(ints[1]));
                    bool[1] = false;
                    if(bool[2]){
                        msg.memory.put("mode_17_bool", true);
                        bool[2] = false;
                    }
                }
            }
        }));
        main.ondownload_sign.connect(new Signal.Slot((data)->{ // 接受信息并刷新数据时
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
                String str = (String)msg.memory.get("mode_17_str");
                if(str != null){
                    main.Sound_sffects("SPECIAL", "interrupt", 50);
                    view_card_settext.accept(str);
                }
                String int0 = (String) msg.memory.get("mode_17_int0");
                String int1 = (String) msg.memory.get("mode_17_int1");
                try{
                    if(int0 != null)
                        ints[0] = Integer.parseInt(int0);
                    if(int1 != null)
                        ints[1] = Integer.parseInt(int1);
                    progressBar.setProgress(ints[0]);
                    cishu.setText("15 / " + ints[0] +"累计倍率: "  + ints[1] + "00%");
                }catch (NumberFormatException ignored){
                }
//                Object obj = msg.memory.get("mode_17_bool");
                if(msg.memory.get("mode_17_bool") != null){
                    main.Sound_sffects("SPECIAL", "thunder", 250);
                }
            }
        }));
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode17_2
//            if(bool[3]) {
//                bool[3] = false;
//                String text = (String) data.get("text");
//                if (text != null && main.tagContainer.get_addcard_num(view_card.getText().toString(), text) > 0){
//                    int[] arr = (int[]) data.get("arr");
//                    if (arr != null) // 此时还没删除手牌
//                        arr[0]++;
//                }
//                bool[3] = true;
//            }
            String text = (String) data.get("text");
            if (text != null && Card.increment(view_card.getText().toString(), text)){
                int[] arr = (int[]) data.get("arr");
                if (arr != null) // 此时还没删除手牌
                    arr[0]++;
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode_17_2
            String text = (String) data.get("text");
            if(text != null) { //
                ints[0]++;
                if(ints[0] >= 15){
                    ints[1]++;
                    ints[0]-=15;
                }
                progressBar.setProgress(ints[0]);
                cishu.setText("15 / " + ints[0] +"累计倍率: "  + ints[1] + "00%");
                bool[1] = true;
                String str = view_card.getText().toString();
                if(str.isEmpty()) return;
//                bool[3] = false;
                if(main.tagContainer.get_addcard_num(view_card.getText().toString(), text) > 1){ // 可以触发额外抽卡
                    bool[0] = true;
                    bool[2] = true;
                    main.Sound_sffects("SPECIAL", "interrupt", 50);
                    main.Sound_sffects("SPECIAL", "thunder", 250);
                    main.tagContainer.abandon_card_string.add(str);
                    boolean[] arr_bool = (boolean[]) data.get("arr_bool");
                    if (arr_bool != null) {
                        arr_bool[3] = false;
                    }
                    int num = Card.get_number(str);
                    if(num != -1){
                        if(num * ints[1] < main.layout_show.enemy_hp)
                            main.layout_show.text_number_add("enemy_hp", -1 * num * ints[1]);
                        else if(main.layout_show.enemy_hp > 1)
                            main.layout_show.text_number_add("enemy_hp", -1 * main.layout_show.enemy_hp + 1);
                    }
                    main.tagContainer.showlast_card_refresh(str);
                    String set = main.tagContainer.getRandomListItemstring();
                    if(set.isEmpty()){
                        view_card_settext.accept("");
                    }
                    else {
                        view_card_settext.accept(set);
                    }
//                    bool[3] = true;
                }
                else if(Hardcard_LinearLayout.checkture_hit(str, text)){ // 可以打牌
                    bool[0] = true;
                    main.Sound_sffects("SPECIAL", "interrupt", 50);
                    main.tagContainer.abandon_card_string.add(str);
                    main.tagContainer.showlast_card_refresh(str);
                    String set = main.tagContainer.getRandomListItemstring();
                    if(set.isEmpty()){
                        view_card_settext.accept("");
//                        card_set[0] = "";
                    }
                    else {
                        view_card_settext.accept(set);
//                        card_set[0] = set;
                    }
                }
            }
        }, 500));

        Kilss_Button info = main.btn_skill.get(0);
        Choose_Skills.set_namempuse(info, R.string.sk_10_2_2);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
            if(main.tcpService.currentState == TcpService.STATE_SERVER) {// 限定房主回合开始刷新场地卡牌
                String str = view_card.getText().toString();
                if(!str.isEmpty()) {
//                        main.tagContainer.abandon_card_string.add(str);
                    main.tagContainer.list.add(str);
                    main.layout_show.text_number_refresh("library"); // 打印牌库
//                    view_card_settext.accept("");
//                    msg.memory.put("mode_17_str", "");
                }
                String set = main.tagContainer.getRandomListItemstring();
                bool[0] = true;
                if (set.isEmpty()) {
                    view_card_settext.accept("");
                } else {
                    view_card_settext.accept(set);
                }
                bool[1] = true;
            }
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
//                Game main = info.get_main.get();
            main.layout_show.text_number_add(
                    "play_hp",
                    main.tagContainer.abandon_card_string.size());
            main.tagContainer.list.addAll(main.tagContainer.abandon_card_string); // 将弃牌堆的牌加回牌库
            main.tagContainer.abandon_card_string.clear(); // 清空弃牌
            main.layout_show.text_number_refresh("library"); // 打印牌库
            main.layout_show.text_number_refresh("abandon"); // 打印弃牌
            main.tagContainer.showlast_card_refresh("");
        }));

        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_10_2_2) +
                "\n每次出牌后，累计1点技力，每累计15点则消耗并提升场地100%伤害倍率");

        // ==========关键：只清除某一边约束==========
//        set.clear(viewId, ConstraintSet.BOTTOM);  // 移除底部约束

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_17_2));
    }
    public static void mode_18(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara51);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara51_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character51);

        Kilss_Button info = main.btn_skill.get(0);
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode18_1
            int[] arr = (int[]) data.get("arr");
            if(arr != null && main.tagContainer.getChildCount() == 1) // 此时还没删除手牌
                arr[0]+=3;
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode_18_1
//            String text = (String) data.get("text");
            if(Card.type_action((String) data.get("text")) == 'x')
                info.set_use_fre_add(info.use_fre_max);

            int num = main.tagContainer.getChildCount(), sum = 0; // 此时选择的那张手牌已经被删
            int[] arr = (int[]) data.get("arr");
            if(arr != null)
                num -= arr[3]; // 不要对新增的卡牌减少数值
            for(int i=0;i<num;i++) { // 手牌
                View item = main.tagContainer.getChildAt(i);
                if (item instanceof Card) {
                    Card View1 = (Card) item;
                    if(Card.triangle.equals(View1.gettext())){ // 寻找三角
                        sum++;
                        continue;
                    }
                    char type = View1.type_action();
                    int num0 = View1.get_number();
                    if(type != '0') {
                        if (num0 > 1)
                            main.tagContainer.card_settext(View1, String.valueOf(num0 - 1) + type);
                        else if (num0 == 1)
                            main.tagContainer.card_settext(View1, Card.triangle);
                    }
                }
            }
            if(sum > 0){ // 减少相等于三角的血量
                if(main.layout_show.play_hp > sum)
                    main.layout_show.text_number_add(
                            "play_hp",
                            -1 * sum );
                else if(main.layout_show.play_hp > 0)
                    main.layout_show.text_number_add(
                            "play_hp",
                            main.layout_show.play_hp - 1);
            }
        }));

        info.set_need_mp(0);
        info.set_name("熔炉");
        info.use_fre_max = 1;
        info.set_use_fre(1);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++) { // 手牌
                View item = main.tagContainer.getChildAt(i);
                if (item instanceof Card) {
                    Card View1 = (Card) item;
                    if(Card.triangle.equals(View1.gettext())){ // 是三角
                        main.tagContainer.card_settext(View1, Card.square);
                        main.tagContainer.list.add(Hardcard_LinearLayout.getRandomListstring()); // "\n每替换一张就向牌库增加一张牌"
                        main.layout_show.text_number_refresh("library"); // 打印牌库
                    }
                }
            }
        }));
        Kilss_Button.setonlongclick.accept(info, "将手牌中所有“三角”替换为方块\n每替换一张就向牌库增加一张牌");

        // ==========关键：只清除某一边约束==========
//        set.clear(viewId, ConstraintSet.BOTTOM);  // 移除底部约束

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_18_1));
    }
    public static void mode_21(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara54);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara54_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character54);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        LinearLayout layout = new LinearLayout(main);
        layout.setId(View.generateViewId());
        main_Gravity.addView(layout, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentWidth(layout.getId(), 0.4f);
        constraintSet.constrainPercentHeight(layout.getId(), 0.1f);
        layout.setOrientation(LinearLayout.HORIZONTAL); // 横


        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(12); // sp
//        view_text.setText("游戏开始时将所有x卡牌替换为方块\n违反规则会提升对方1点基础攻击力");
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.2f);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(layout.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
//        constraintSet.connect(layout.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.applyTo(main_Gravity);

        for(int i=0;i<8;i++){
            GradientDrawable drawable_i = new GradientDrawable();
            drawable_i.setShape(GradientDrawable.RECTANGLE);
            drawable_i.setColor(ContextCompat.getColor(main, R.color.transparent));
            drawable_i.setStroke(
                    2,
                    ContextCompat.getColor(main, R.color.blue)
            );
            TextView view = new TextView(main);
            view.setBackground(drawable_i);
            view.setTextSize(20); // sp
            view.setText(String.valueOf(i+1));
//            Card.set_color(view, R.color.transparent);
            view.setTextColor(Color.WHITE);
//            view.setSingleLine(true); // 不能换行
            view.setGravity(Gravity.CENTER); // 上下左右居中
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1));
            layout.addView(view);
//            view_list.add(view);
        }

        StringBuilder num_str = new StringBuilder();
        Consumer<String> num_str_refresh = (str)->{
            num_str.setLength(0);
            num_str.append(str);
//    view_text.setText(str);
            for(int i=0;i<8;i++){
                TextView view = (TextView)layout.getChildAt(i);
                // 获取view现在的背景
                Drawable bg = view.getBackground();
                if(bg instanceof GradientDrawable){
                    GradientDrawable gd = (GradientDrawable) bg;
                    if(num_str.indexOf(String.valueOf(i+1)) != -1){
                        gd.setColor(Color.parseColor("#882196F3"));
                    }
                    else {
                        gd.setColor(ContextCompat.getColor(main, R.color.transparent));
                    }
                }
            }
        };
        num_str_refresh.accept("");

        Kilss_Button info = main.btn_skill.get(0);
        boolean[] bool = {false}; // 下次发送信息时，顺便发送刷新
        main.semd_sign.connect(new Signal.Slot((data)->{  // mode_21_1// 发送信息
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) {
                if(bool[0]){
                    msg.memory.put("mode_20_str", num_str.toString());
                    bool[0] = false;
                }
            }
        }));
        main.ondownload_sign.connect(new Signal.Slot((data)->{  // mode_21_1// 接受信息并刷新数据时
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
                String str = (String)msg.memory.get("mode_20_str");
                if(str != null){
                    main.Sound_sffects("SPECIAL", "purify");
                    num_str_refresh.accept(str);
                    if(str.isEmpty()) {
                        // 双方共享同调，且双方回合结束自己把同调清0，因此传输""时为完全同调
                        main.Sound_sffects("SPECIAL", "full_synchro");
                    }
                }
            }
        }));
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode21_1
            int num = Card.get_number((String) data.get("text"));
            int[] arr = (int[]) data.get("arr");
            if(arr != null && num > 0 && num <= 8 && main.layout_show.combo % 10 == num - 1) { // combo还没加
                if(num_str.indexOf(String.valueOf(num)) == -1){
                    arr[0]++;
                }
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode_21_1
            int num = Card.get_number((String) data.get("text"));
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && num > 0 && num <= 8 && main.layout_show.combo % 10 == num) { // combo已经加了
                if(num_str.indexOf(String.valueOf(num)) == -1){
                    main.Sound_sffects("SPECIAL", "purify");
                    num_str.append(num);
                    bool[0] = true;
                    arr_bool[3] = false;
                    if(num_str.length() == 8){
                        main.Sound_sffects("SPECIAL", "full_synchro");
                        view_text.setText("触发完全同调！");
                        num_str_refresh.accept("");
                        info.set_use_fre_add(info.use_fre_max); // 刷新场地技能
                    }
                    else {
                        view_text.setText("触发同调，可继续打牌");
                        num_str_refresh.accept(num_str.toString());
                    }
                }
                else view_text.setText("");
            }
            else view_text.setText("");
        }, 500));

        Choose_Skills.set_namempuse(info, R.string.sk_10_3_1);
        info.set_need_mp(20);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        main.trun_end_sign.connect(new Signal.Slot((data)->{ // mode21_1
            num_str_refresh.accept("");
        }));
        info.setOnClickListener(v->Choose_Skills.setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
            for(int i=0;i<num;i++){ // 玩家端获取方块个数不对
                Card newTag = info.get_main.get().tagContainer.addNewcard(Card.square);
                newTag.abandon = false;
            }
        }));
        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_10_3_1));

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_21_1));
    }
    public static void mode_21_2(Game main){
        Intent playIntent = new Intent(main, BgmService.class);
        playIntent.putExtra("CMD", "CHANGE");
        playIntent.putExtra("MUSIC_ID", R.raw.chara54);
        playIntent.putExtra("SECOND_MUSIC_ID", R.raw.chara54_h);
        main.startService(playIntent);
        ConstraintLayout main_Gravity = main.findViewById(R.id.main);
        main_Gravity.setBackgroundResource(R.drawable.character54);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(main_Gravity);// 克隆当前布局已有的约束

        main.tagContainer.card_begin += 2;
        LinearLayout layout = new LinearLayout(main);
        layout.setId(View.generateViewId());
        main_Gravity.addView(layout, new ConstraintLayout.LayoutParams(0,0));
        constraintSet.constrainPercentWidth(layout.getId(), 0.4f);
        constraintSet.constrainPercentHeight(layout.getId(), 0.1f);
        layout.setOrientation(LinearLayout.HORIZONTAL); // 横


        TextView view_text = new TextView(main);
        view_text.setId(View.generateViewId());
        main_Gravity.addView(view_text, new ConstraintLayout.LayoutParams(0,0));
        view_text.setTextSize(12); // sp
    //        view_text.setText("游戏开始时将所有x卡牌替换为方块\n违反规则会提升对方1点基础攻击力");
        view_text.setTextColor(Color.WHITE);
        view_text.setSingleLine(true); // 不能换行
        view_text.setGravity(Gravity.CENTER); // 上下左右居中
        constraintSet.constrainPercentWidth(view_text.getId(), 0.2f);

        constraintSet.connect(view_text.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(view_text.getId(), ConstraintSet.TOP, layout.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(view_text.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.connect(layout.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.END, R.id.view_segmentation, ConstraintSet.START);
        constraintSet.connect(layout.getId(), ConstraintSet.TOP, R.id.show_text, ConstraintSet.BOTTOM);
    //        constraintSet.connect(layout.getId(), ConstraintSet.BOTTOM, R.id.layout_information_middle, ConstraintSet.TOP);

        constraintSet.applyTo(main_Gravity);

        for(int i=0;i<8;i++){
            GradientDrawable drawable_i = new GradientDrawable();
            drawable_i.setShape(GradientDrawable.RECTANGLE);
            drawable_i.setColor(ContextCompat.getColor(main, R.color.transparent));
            drawable_i.setStroke(
                    2,
                    ContextCompat.getColor(main, R.color.blue)
            );
            TextView view = new TextView(main);
            view.setBackground(drawable_i);
            view.setTextSize(20); // sp
            view.setText(String.valueOf(i+1));
    //            Card.set_color(view, R.color.transparent);
            view.setTextColor(Color.WHITE);
    //            view.setSingleLine(true); // 不能换行
            view.setGravity(Gravity.CENTER); // 上下左右居中
            view.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1));
            layout.addView(view);
    //            view_list.add(view);
        }

        StringBuilder num_str = new StringBuilder();
        Consumer<String> num_str_refresh = (str)->{
            num_str.setLength(0);
            num_str.append(str);
    //    view_text.setText(str);
            for(int i=0;i<8;i++){
                TextView view = (TextView)layout.getChildAt(i);
                // 获取view现在的背景
                Drawable bg = view.getBackground();
                if(bg instanceof GradientDrawable){
                    GradientDrawable gd = (GradientDrawable) bg;
                    if(num_str.indexOf(String.valueOf(i+1)) != -1){
                        gd.setColor(Color.parseColor("#882196F3"));
                    }
                    else {
                        gd.setColor(ContextCompat.getColor(main, R.color.transparent));
                    }
                }
            }
        };
        num_str_refresh.accept("");

        Kilss_Button info = main.btn_skill.get(0);
        boolean[] bool = {false}; // 下次发送信息时，顺便发送刷新
        main.semd_sign.connect(new Signal.Slot((data)->{  // mode_21_1// 发送信息
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) {
                if(bool[0]){
                    msg.memory.put("mode_20_str", num_str.toString());
                    bool[0] = false;
                }
            }
        }));
        main.ondownload_sign.connect(new Signal.Slot((data)->{  // mode_21_1// 接受信息并刷新数据时
            ChatMsg msg = (ChatMsg) data.get("ChatMsg");
            if(msg != null) { // 每次接受信息都要检测是否刷新场地卡牌
                String str = (String)msg.memory.get("mode_20_str");
                if(str != null){
                    main.Sound_sffects("SPECIAL", "purify");
                    num_str_refresh.accept(str);
                    if(str.isEmpty()) {
                        // 双方共享同调，且双方回合结束自己把同调清0，因此传输""时为完全同调
                        main.Sound_sffects("SPECIAL", "full_synchro");
                        info.set_use_fre_add(info.use_fre_max);
                    }
                }
            }
        }));
        main.addcard_sign.connect(new Signal.Slot((data)->{ // mode21_1
            int num = Card.get_number((String) data.get("text"));
            int[] arr = (int[]) data.get("arr");
            if(arr != null && num > 0 && num <= 8 && main.layout_show.combo % 10 == num - 1) { // combo还没加
                if(num_str.indexOf(String.valueOf(num)) == -1){
                    arr[0]++;
                }
            }
        }));
        main.hit_card_succes_sign.connect(new Signal.Slot((data)->{ // mode_21_1
            int num = Card.get_number((String) data.get("text"));
            boolean[] arr_bool = (boolean[]) data.get("arr_bool");
            if(arr_bool != null && num > 0 && num <= 8 && main.layout_show.combo % 10 == num) { // combo已经加了
                if(num_str.indexOf(String.valueOf(num)) == -1){
                    main.Sound_sffects("SPECIAL", "purify");
                    num_str.append(num);
                    bool[0] = true;
                    arr_bool[3] = false;
                    if(num_str.length() == 8){
                        main.Sound_sffects("SPECIAL", "full_synchro");
                        view_text.setText("触发完全同调！");
                        num_str_refresh.accept("");
                        info.set_use_fre_add(info.use_fre_max); // 刷新场地技能
                        main.layout_show.text_number_add("play_att", main.layout_show.enemy_att_sum);
                    }
                    else {
                        view_text.setText("触发同调，可继续打牌");
                        num_str_refresh.accept(num_str.toString());
                    }
                }
                else view_text.setText("");
            }
            else view_text.setText("");
        }, 500));

        Choose_Skills.set_namempuse(info, R.string.sk_10_3_1);
        info.set_need_mp(20);
        main.trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        main.trun_end_sign.connect(new Signal.Slot((data)->{ // mode21_1
            num_str_refresh.accept("");
        }));
        info.setOnClickListener(v->Choose_Skills.setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
            for(int i=0;i<num;i++){ // 玩家端获取方块个数不对
                Card newTag = info.get_main.get().tagContainer.addNewcard(Card.square);
                newTag.abandon = false;
            }
        }));
        Kilss_Button.setonlongclick.accept(info, main.getString(R.string.view_10_3_1));

        TextView text = main.findViewById(R.id.text_venve);
        text.setText(main.getString(R.string.mode_21_2));
    }
}
