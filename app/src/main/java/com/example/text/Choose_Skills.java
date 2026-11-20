package com.example.text;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.Map;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
//Runnable：无参、无返回
//Supplier<T>：无参、有返回
//Consumer<T>：一个入参、无返回
//Function<T,R>：一个入参、有返回
//BiConsumer<T,T>
public class Choose_Skills extends AppCompatActivity {
    // 四个匿名函数Consumer
//    public ArrayList<String> sk_str = new ArrayList<>();
    private ArrayList<Button> sk_bt = new ArrayList<>(); // 左边四个 存储的的xml的组件，不修改指向
    private ArrayList<Button> sk_ch_bt = new ArrayList<>(Arrays.asList(null,null,null,null)); // 选择的四个右边 存储的是指向 可以为null
    public static ArrayList<Consumer<Kilss_Button>> sk_lier = new ArrayList<>(Arrays.asList(null,null,null,null)); // 存的匿名函数 上一个的跟屁虫
    // 设为全局，不要传递了，反正这个页面一般也不清除
    public static ArrayList<String> skills_detailde = new ArrayList<>(Arrays.asList("","","",""));
    private TextView text_sk;
    private Button btn_show;
    private int color = Kilss_Button.type_blue;
//    public Supplier<Kilss_Button> get_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_skills);
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

        text_sk = findViewById(R.id.text_sk);
        btn_show = findViewById(R.id.btn_show);

        sk_bt.add(findViewById(R.id.btn_ski1));
        sk_bt.add(findViewById(R.id.btn_ski2));
        sk_bt.add(findViewById(R.id.btn_ski3));
        sk_bt.add(findViewById(R.id.btn_ski4));
        for(int i=0;i<4;i++){
//            sk_str.add("");
            int finalI = i;
            sk_bt.get(i).setOnClickListener(v -> bt_click_bechoose(v, finalI)); // 设置左侧按钮点击
//            sk_ch_bt.add(null);
//            sk_lier.add(null);
//            skills_detailde.add("");
        }

        Button bt_back = findViewById(R.id.btn_back);
        bt_back.setOnClickListener(v -> { // 返回
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "close");
            startService(playIntent);
            Intent intent = new Intent(this, MainActivity.class);

            if(sk_ch_bt.get(0) != null) intent.putExtra("sk_str_1", sk_ch_bt.get(0).getText());
            if(sk_ch_bt.get(1) != null) intent.putExtra("sk_str_2", sk_ch_bt.get(1).getText());
            if(sk_ch_bt.get(2) != null) intent.putExtra("sk_str_3", sk_ch_bt.get(2).getText());
            if(sk_ch_bt.get(3) != null) intent.putExtra("sk_str_4", sk_ch_bt.get(3).getText());
            intent.putExtra("Choose_Skills", "true");
//            intent.putExtra("the_last_int", "MainActivity");

            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            // 不清除本页面的情况下打开一个已经打开过的页面
        });
// ----------------------------------------------------------------------------------------------------

        ConstraintLayout page1 = findViewById(R.id.page1);
        ConstraintLayout page2 = findViewById(R.id.page2);
        ConstraintLayout page3 = findViewById(R.id.page3);
        ConstraintLayout page4 = findViewById(R.id.page4);
        ConstraintLayout page5 = findViewById(R.id.page5);
        ConstraintLayout page6 = findViewById(R.id.page6);
        ConstraintLayout page10 = findViewById(R.id.page10);
        ConstraintLayout page11 = findViewById(R.id.page11);
        ArrayList<ConstraintLayout> page = new ArrayList<>();
        page.add(page1);
        page.add(page2);
        page.add(page3);
        page.add(page4);
        page.add(page5);
        page.add(page6);
        page.add(page10);
        page.add(page11);
        Button btn_chooseview_1 = findViewById(R.id.btn_chooseview_1);
        btn_chooseview_1.setBackgroundTintList(ColorStateList.valueOf( ContextCompat.getColor(this, R.color.skills_background_cantouch)));
        Button btn_chooseview_2 = findViewById(R.id.btn_chooseview_2);
        Button btn_chooseview_3 = findViewById(R.id.btn_chooseview_3);
        Button btn_chooseview_4 = findViewById(R.id.btn_chooseview_4);
        Button btn_chooseview_5 = findViewById(R.id.btn_chooseview_5);
        Button btn_chooseview_6 = findViewById(R.id.btn_chooseview_6);
        Button btn_chooseview_10 = findViewById(R.id.btn_chooseview_10);
        Button btn_chooseview_11 = findViewById(R.id.btn_chooseview_11);
        ArrayList<Button> btn_chooseview = new ArrayList<>();
        btn_chooseview.add(btn_chooseview_1);
        btn_chooseview.add(btn_chooseview_2);
        btn_chooseview.add(btn_chooseview_3);
        btn_chooseview.add(btn_chooseview_4);
        btn_chooseview.add(btn_chooseview_5);
        btn_chooseview.add(btn_chooseview_6);
        btn_chooseview.add(btn_chooseview_10);
        btn_chooseview.add(btn_chooseview_11);
        final int red_color_order = page.size() - 2;
//
        TextView text_other = findViewById(R.id.text_other);
        Consumer<Integer> Click = num->{
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "touch");
            startService(playIntent);
            if(num >= red_color_order) {
                color = Kilss_Button.type_red;
                text_other.setText("红色技能使用后使其他未处于cd的红色技能进入1cd");
            }
            else {
                color = Kilss_Button.type_blue;
                text_other.setText("");
            }
            for(int i=0;i<page.size();i++){
                if(i == num){
                    page.get(i).setVisibility(View.VISIBLE);
                    // 暂时想不到方便修改红色技能的方式
                    if(color == Kilss_Button.type_red)
                        btn_chooseview.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.skills_red_background_cantouch));
                    else
                        btn_chooseview.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.skills_background_cantouch));
                }
                else {
                    page.get(i).setVisibility(View.GONE);
                    btn_chooseview.get(i).setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
                }
            }
        };
        btn_chooseview_1.setOnClickListener(v->Click.accept(0));
        btn_chooseview_2.setOnClickListener(v->Click.accept(1));
        btn_chooseview_3.setOnClickListener(v->Click.accept(2));
        btn_chooseview_4.setOnClickListener(v->Click.accept(3));
        btn_chooseview_5.setOnClickListener(v->Click.accept(4));
        btn_chooseview_6.setOnClickListener(v->Click.accept(5));

        btn_chooseview_10.setOnClickListener(v->Click.accept(red_color_order));
        btn_chooseview_11.setOnClickListener(v->Click.accept(red_color_order + 1));

//        text_sk.setText(this.getString(R.string.view_1_1_1));
        // 匿名函数可以使用类函数带入，若类函数参数有多余的是(否)可以使用常数替代实现带入匿名函数
        // 这里直接链接对应按钮对应哪个技能
        findViewById(R.id.btn_1_1_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_1_1, this.getString(R.string.view_1_1_1)));
        findViewById(R.id.btn_1_1_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_1_2, this.getString(R.string.view_1_1_2)));
        findViewById(R.id.btn_1_1_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_1_3, this.getString(R.string.view_1_1_3)));
        findViewById(R.id.btn_1_1_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_1_4, this.getString(R.string.view_1_1_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_1_2_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_2_1, this.getString(R.string.view_1_2_1)));
        findViewById(R.id.btn_1_2_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_2_2, this.getString(R.string.view_1_2_2)));
        findViewById(R.id.btn_1_2_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_2_3, this.getString(R.string.view_1_2_3)));
        findViewById(R.id.btn_1_2_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_2_4, this.getString(R.string.view_1_2_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_1_3_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_3_1, this.getString(R.string.view_1_3_1)));
        findViewById(R.id.btn_1_3_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_3_2, this.getString(R.string.view_1_3_2)));
        findViewById(R.id.btn_1_3_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_3_3, this.getString(R.string.view_1_3_3)));
        findViewById(R.id.btn_1_3_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_1_3_4, this.getString(R.string.view_1_3_4)));

// -----------------------------------------------------------------------

        findViewById(R.id.btn_2_1_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_1_1, this.getString(R.string.view_2_1_1)));
        findViewById(R.id.btn_2_1_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_1_2, this.getString(R.string.view_2_1_2)));
        findViewById(R.id.btn_2_1_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_1_3, this.getString(R.string.view_2_1_3)));
        findViewById(R.id.btn_2_1_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_1_4, this.getString(R.string.view_2_1_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_2_2_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_2_1, this.getString(R.string.view_2_2_1)));
        findViewById(R.id.btn_2_2_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_2_2, this.getString(R.string.view_2_2_2)));
        findViewById(R.id.btn_2_2_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_2_3, this.getString(R.string.view_2_2_3)));
        findViewById(R.id.btn_2_2_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_2_4, this.getString(R.string.view_2_2_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_2_3_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_3_1, this.getString(R.string.view_2_3_1)));
        findViewById(R.id.btn_2_3_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_3_2, this.getString(R.string.view_2_3_2)));
        findViewById(R.id.btn_2_3_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_3_3, this.getString(R.string.view_2_3_3)));
        findViewById(R.id.btn_2_3_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_2_3_4, this.getString(R.string.view_2_3_4)));

// -----------------------------------------------------------------------

        findViewById(R.id.btn_3_1_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_1_1, this.getString(R.string.view_3_1_1)));
        findViewById(R.id.btn_3_1_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_1_2, this.getString(R.string.view_3_1_2)));
        findViewById(R.id.btn_3_1_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_1_3, this.getString(R.string.view_3_1_3)));
        findViewById(R.id.btn_3_1_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_1_4, this.getString(R.string.view_3_1_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_3_2_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_2_1, this.getString(R.string.view_3_2_1)));
        findViewById(R.id.btn_3_2_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_2_2, this.getString(R.string.view_3_2_2)));
        findViewById(R.id.btn_3_2_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_2_3, this.getString(R.string.view_3_2_3)));
        findViewById(R.id.btn_3_2_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_2_4, this.getString(R.string.view_3_2_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_3_3_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_3_1, this.getString(R.string.view_3_3_1)));
        findViewById(R.id.btn_3_3_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_3_2, this.getString(R.string.view_3_3_2)));
        findViewById(R.id.btn_3_3_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_3_3, this.getString(R.string.view_3_3_3)));
        findViewById(R.id.btn_3_3_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_3_3_4, this.getString(R.string.view_3_3_4)));

// -----------------------------------------------------------------------

        findViewById(R.id.btn_4_1_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_1_1, this.getString(R.string.view_4_1_1)));
        findViewById(R.id.btn_4_1_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_1_2, this.getString(R.string.view_4_1_2)));
        findViewById(R.id.btn_4_1_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_1_3, this.getString(R.string.view_4_1_3)));
        findViewById(R.id.btn_4_1_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_1_4, this.getString(R.string.view_4_1_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_4_2_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_2_1, this.getString(R.string.view_4_2_1)));
        findViewById(R.id.btn_4_2_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_2_2, this.getString(R.string.view_4_2_2)));
        findViewById(R.id.btn_4_2_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_2_3, this.getString(R.string.view_4_2_3)));
//        findViewById(R.id.btn_4_2_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_2_4, this.getString(R.string.view_4_2_4)));
//// -----------------------------------------------------------------------
//        findViewById(R.id.btn_4_3_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_3_1, this.getString(R.string.view_4_3_1)));
//        findViewById(R.id.btn_4_3_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_3_2, this.getString(R.string.view_4_3_2)));
//        findViewById(R.id.btn_4_3_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_3_3, this.getString(R.string.view_4_3_3)));
//        findViewById(R.id.btn_4_3_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_4_3_4, this.getString(R.string.view_4_3_4)));

// -----------------------------------------------------------------------

        // 这里直接链接对应按钮对应哪个技能
        findViewById(R.id.btn_5_1_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_1_1, this.getString(R.string.view_5_1_1)));
        findViewById(R.id.btn_5_1_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_1_2, this.getString(R.string.view_5_1_2)));
        findViewById(R.id.btn_5_1_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_1_3, this.getString(R.string.view_5_1_3)));
        findViewById(R.id.btn_5_1_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_1_4, this.getString(R.string.view_5_1_4)));
//// -----------------------------------------------------------------------
        findViewById(R.id.btn_5_2_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_2_1, this.getString(R.string.view_5_2_1)));
        findViewById(R.id.btn_5_2_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_2_2, this.getString(R.string.view_5_2_2)));
        findViewById(R.id.btn_5_2_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_2_3, this.getString(R.string.view_5_2_3)));
//        findViewById(R.id.btn_5_2_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_2_4, this.getString(R.string.view_5_2_4)));
        findViewById(R.id.btn_5_3_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_3_1, this.getString(R.string.view_5_3_1)));
        findViewById(R.id.btn_5_3_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_3_2, this.getString(R.string.view_5_3_2)));
        findViewById(R.id.btn_5_3_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_3_3, this.getString(R.string.view_5_3_3)));
//        findViewById(R.id.btn_5_3_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_5_3_4, this.getString(R.string.view_5_3_4)));



        findViewById(R.id.btn_10_1_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_1_1, this.getString(R.string.view_10_1_1)));
        findViewById(R.id.btn_10_1_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_1_2, this.getString(R.string.view_10_1_2)));
        findViewById(R.id.btn_10_1_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_1_3, this.getString(R.string.view_10_1_3)));
        findViewById(R.id.btn_10_1_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_1_4, this.getString(R.string.view_10_1_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_10_2_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_2_1, this.getString(R.string.view_10_2_1)));
        findViewById(R.id.btn_10_2_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_2_2, this.getString(R.string.view_10_2_2)));
        findViewById(R.id.btn_10_2_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_2_3, this.getString(R.string.view_10_2_3)));
        findViewById(R.id.btn_10_2_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_2_4, this.getString(R.string.view_10_2_4)));
// -----------------------------------------------------------------------
        findViewById(R.id.btn_10_3_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_3_1, this.getString(R.string.view_10_3_1)));
        findViewById(R.id.btn_10_3_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_3_2, this.getString(R.string.view_10_3_2)));
        findViewById(R.id.btn_10_3_3).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_3_3, this.getString(R.string.view_10_3_3)));
        findViewById(R.id.btn_10_3_4).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_10_3_4, this.getString(R.string.view_10_3_4)));

        findViewById(R.id.btn_11_1_1).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_11_1_1, this.getString(R.string.view_11_1_1)));
        findViewById(R.id.btn_11_1_2).setOnClickListener(v -> bt_click_choose(v, Choose_Skills::crat_sk_11_1_2, this.getString(R.string.view_11_1_2)));

    }
    private void button_change_background(Button bt, boolean bool){ // 根据是否能点击刷新显示状态
        // btn_show 上面 sk_bt 左边四个 sk_ch_bt 右边四个指针{分为红色 和 蓝色}
        int bgColor;
        if(sk_ch_bt.contains(bt)){ // 是否为右边的
            int id = bt.getId();
            if(id != View.NO_ID){
                String idName = getResources().getResourceEntryName(id);
                // idName 就是xml里写的名字，例如 "play_hp_number"
//            if("btn_10".equals(idName.substring(0,6))){
                if(idName.length() > "btn_1_1_1".length()){ // 红色技能
                    if(bool){
                        bgColor = ContextCompat.getColor(this, R.color.skills_red_background_cantouch);
                    }
                    else{
                        bgColor = ContextCompat.getColor(this, R.color.skills_red_background_notcan);
                    }
//                    ColorStateList bgTintList = ColorStateList.valueOf(bgColor);
//                    bt.setBackgroundTintList(bgTintList);
//                    return;
                }
                else { // 蓝色技能
                    if(bool){
                        bgColor = ContextCompat.getColor(this, R.color.skills_background_cantouch);
                    }
                    else{
                        bgColor = ContextCompat.getColor(this, R.color.skills_background_notcan);
                    }
                }
            }
            else return; // 没写xml id 或者 java没有调用 `setId(xxx)`
        }
        else { // 左边四个或者上面的那个
            if(bool){
                if(color == Kilss_Button.type_red) { // 当前界面是选红色的
                    bgColor = ContextCompat.getColor(this, R.color.skills_red_background_cantouch);
                }
                else { // 当前界面是选蓝色的
                    bgColor = ContextCompat.getColor(this, R.color.skills_background_cantouch);
                }
            }
            else {
                bgColor = ContextCompat.getColor(this, R.color.skills_background_notcan);
            }
        }
//        if(bool){
//            if(color == Kilss_Button.type_red) {
//                bgColor = ContextCompat.getColor(this, R.color.skills_red_background_cantouch);
//            }
//            else {
//                bgColor = ContextCompat.getColor(this, R.color.skills_background_cantouch);
//            }
//        }
//        else{
//            if(sk_ch_bt.contains(bt)) bgColor = ContextCompat.getColor(this, R.color.skills_background_notcan);
//            else bgColor = ContextCompat.getColor(this, R.color.transparent);
//        }

        ColorStateList bgTintList = ColorStateList.valueOf(bgColor);
        bt.setBackgroundTintList(bgTintList);
    }
    private void bt_click_choose(View v, Consumer<Kilss_Button> sk_suplier, String text){ // 右边技能按钮点击设置
        Button bt = (Button)v;
        text_sk.setText(text);
        button_change_background(btn_show, true);
        btn_show.setText(bt.getText());
        for(int i=0;i<4;i++){ // 寻找当前技能已经被选
            if(bt.equals(sk_ch_bt.get(i))){ // 有找到，把左边选的去除，右边自身还原
                Intent playIntent = new Intent(this, BgmService.class);
                playIntent.putExtra("CMD", "SOUND");
                playIntent.putExtra("SE", "unequip");
                startService(playIntent);
//                btn_show.setText("");
//                button_change_background(btn_show, false);
//                text_sk.setText("");

                button_change_background(sk_bt.get(i), false);
                button_change_background(bt, true);
                //同时修改左边和本身的显示

//                sk_str.set(i, "");
                sk_bt.get(i).setText("");
                skills_detailde.set(i, "");
                sk_ch_bt.set(i, null); // 修改指向
                sk_lier.set(i, null);
                return;
            }
        }
//        Button bt;
        for(int i=0;i<4;i++){
            if(sk_ch_bt.get(i) == null){ //  把左边选的改为选中，右边自身设置已选
                Intent playIntent = new Intent(this, BgmService.class);
                playIntent.putExtra("CMD", "SOUND");
                playIntent.putExtra("SE", "equip");
                startService(playIntent);
//                text_sk.setText(text);
//                button_change_background(btn_show, true);
//                btn_show.setText(bt.getText());

                button_change_background(sk_bt.get(i), true);
                button_change_background(bt, false);
                //同时修改左边和本身的显示

//                sk_str.set(i, sk_name);
                sk_bt.get(i).setText(bt.getText());
                skills_detailde.set(i, text);
                sk_ch_bt.set(i, (Button)v); // 修改指向
                sk_lier.set(i, sk_suplier);
                return;
            }
        }
    }
    private void bt_click_bechoose(View v, int i){ // 左边被选技能按钮点击设置
        text_sk.setText("");
        button_change_background(btn_show, false);
        btn_show.setText("");
        if(sk_ch_bt.get(i) != null) {
            Intent playIntent = new Intent(this, BgmService.class);
            playIntent.putExtra("CMD", "SOUND");
            playIntent.putExtra("SE", "unequip");
            startService(playIntent);
            skills_detailde.set(i, "");

            button_change_background(sk_bt.get(i), false);
            sk_bt.get(i).setText("");

            button_change_background(sk_ch_bt.get(i), true);
            sk_ch_bt.set(i, null);

            sk_lier.set(i, null);
        }
    }
    // -------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------
    public static void setOnClick_sta(View v, Kilss_Button info, Runnable h){ // 点击就触发形技能
        // 点击布局触发的逻辑
        if(info.use_true.get()) { // 是否可触发
            info.set_use_fre_add(-1); // 减少使用次数
            info.get_main.get().layout_show.text_number_add( // 扣蓝量
                    "play_technique",
                    -1 * info.need_mp
            );

            info.get_main.get().Sound_sffects("NORMAL", "skill");
            h.run();

            info.get_main.get().skills_ation_succes(info);
            info.use_show_refresh.run(); // 刷新使用显示
        }
    }
    public static void setOn_choosecard_sta(View v, Kilss_Button info, Consumer<Card> h){ // 需要选一张牌的技能
        // 点击布局触发的逻辑
        if(info.use_true.get()) { // 是否可触发
            info.get_main.get().Sound_sffects("NORMAL", "ok");
            info.get_main.get().choose_card_kills((View0)->{ // 有选择卡牌，View0为Card的牌
                info.set_use_fre_add(-1); // 减少使用次数
                info.get_main.get().layout_show.text_number_add( // 扣蓝量
                        "play_technique",
                        -1 * info.need_mp
                );

                info.get_main.get().Sound_sffects("NORMAL", "skill");
                h.accept(View0);

                info.get_main.get().skills_ation_succes(info);
                info.use_show_refresh.run(); // 刷新使用显示
            });
        }
    }
//    private static String get_name(String str){
//        int be = str.indexOf("\n") + 1, ed = str.lastIndexOf("\n");
//        if(be == 0 || be >= ed) return "";
//        else return str.substring(be, ed);
//    }
//    private static int get_mp(String str){
//        int ed = str.indexOf("\n");
//        if(ed == -1) return 99;
//        try{
//            return Integer.parseInt(str.substring(4, ed));
//        }catch (NumberFormatException e){
//            return 99;
//        }
//    }
//    private static int get_use(String str){
//        int be = str.lastIndexOf("\n") + 1;
//        if(be == 0) return 0;
//        try{
//            return Integer.parseInt(str.substring(be, str.length()));
//        }catch (NumberFormatException e){
//            return 0;
//        }
//    }
    public static void set_namempuse(Kilss_Button info, int id){
        String str = info.getContext().getString(id);
        int be,ed = str.indexOf("\n");
        if(ed == -1) info.set_need_mp(99); // 所需蓝量
        try{
            info.set_need_mp(Integer.parseInt(str.substring(3, ed))); // mp__
        }catch (NumberFormatException e){
            info.set_need_mp(99); // 所需蓝量
        }

        be = ed + 1;
        ed = str.lastIndexOf("\n");
        if(be == 0 || be >= ed) info.set_name("");
        else info.set_name(str.substring(be, ed));

        be = ed + 1;
        if(be == 0) info.use_fre_max = 0;
        try{
            info.use_fre_max = Integer.parseInt(str.substring(be, str.length()));  // 使用次数上限
        }catch (NumberFormatException e){
            info.use_fre_max = 0;
        }
        info.set_use_fre(info.use_fre_max); // 使用次数
    }
    public static void set_namempuse_redcolor(Kilss_Button info, int id){
        set_namempuse(info, id);
        info.type = Kilss_Button.type_red;
        info.get_main.get().trun_begin_sign.connect(new Signal.Slot((data)->{
            info.set_use_fre_add(info.use_fre_max);
        }));
        // 这里颜色赋值慢了，要么调整颜色初始化时机，要么这里再次颜色赋值
        info.color_background_cantouch = R.color.skills_red_background_cantouch;
        info.color_background_notcan = R.color.skills_red_background_notcan;
        info.color_stroke_cantouch = R.color.skills_red_stroke_cantouch;
        info.color_stroke_notcan = R.color.skills_red_stroke_notcan;
        info.set_background(false); // 刷新一次背景
        info.get_main.get().skills_trigger_sign.connect(new Signal.Slot((data)->{
            Kilss_Button sk = (Kilss_Button) data.get("Kilss_Button");
            if (info.equals(sk)) {
                for(Kilss_Button sk0:info.get_main.get().btn_skill)
                    if(sk0.type == Kilss_Button.type_red && sk0.cd == 0 && !info.equals(sk)){
                        sk0.cd_add(1);
                    }
            }
        }));
    }
    // 暂时先把技能实现函数放在这里
    public static void crat_sk_1_1_1(Kilss_Button info){ // 抽牌2
//    <string name="view_1_1_1">抽两张牌\n仅能使用一次</string>
        set_namempuse(info, R.string.sk_1_1_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
        }));
    }

    public static void crat_sk_1_1_2(Kilss_Button info){ // 图书馆
//    <string name="view_1_1_2">仅Combo为2或以下才能使用\n抽牌直到手牌达到5牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_1_2);
        info.use_true = ()->{ // 修改可以使用判定
            if(info.use_true_default()){ // 原可以使用判定
                if(info.get_main.get().layout_show.combo <= 2) return true;
                // 仅限 combo 小于等于2时使用
            }
            return false;
        };
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            int num = 5 - info.get_main.get().tagContainer.getChildCount();
            if(num > 0)
                for(int i=0;i<num;i++)
                    info.get_main.get().tagContainer.addNewcard();
        }));
    }

    public static void crat_sk_1_1_3(Kilss_Button info){ // 滚雪球
//    <string name="view_1_1_3">抽取 (5 - 剩余使用次数) 张牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_1_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            for(int i=0;i<4-info.use_fre;i++)
                info.get_main.get().tagContainer.addNewcard();
        }));
    }

    public static void crat_sk_1_1_4(Kilss_Button info){ // 堆叠
//    <string name="view_1_1_4">抽一张牌，且之后抽牌数+1</string>
        set_namempuse(info, R.string.sk_1_1_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            main.tagContainer.addNewcard(); // 先拿一张牌
            Signal.Slot slot = new Signal.Slot((data)->{
                int[] arr = (int[]) data.get("arr");
                if (arr != null) {
                    arr[0]++;
                }
            });
            main.card_begin_num_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.card_begin_num_sign, slot)
            ); // 绑定buff时间
        }));
    }
    public static void crat_sk_1_2_1(Kilss_Button info){ // 镜中镜
//    <string name="view_1_2_1">选择一张手牌复制并加入手牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_2_1);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
        }));
    }
    public static void crat_sk_1_2_2(Kilss_Button info){ // 生成
        //    <string name="view_1_2_2">生成三张数值随机，花色为 x 的卡牌加入手牌</string>
        set_namempuse(info, R.string.sk_1_2_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard(Hardcard_LinearLayout.getRandomListstring("x"));
            info.get_main.get().tagContainer.addNewcard(Hardcard_LinearLayout.getRandomListstring("x"));
            info.get_main.get().tagContainer.addNewcard(Hardcard_LinearLayout.getRandomListstring("x"));
        }));
    }
    public static void crat_sk_1_2_3(Kilss_Button info){ // 循环
        //    <string name="view_1_2_3">选择一张手牌加入弃牌区，下回合开始时额外抽取两张牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_2_3);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                int[] arr = (int[]) data.get("arr");
                if (arr != null) {
                    arr[0] += 2;
                }
            });
            main.card_begin_num_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(2, main.card_begin_num_sign, slot)
            ); // 绑定buff时间
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
        }));
    }
    public static void crat_sk_1_2_4(Kilss_Button info){ // 三色牌
        //    <string name="view_1_2_4">选择一张手牌加入弃牌区，生成三张数值相同，花色随机的卡牌并加入手牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_2_4);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            int view_num = View0.get_number();
            if(view_num == -1){
                info.get_main.get().tagContainer.addNewcard();
                info.get_main.get().tagContainer.addNewcard();
                info.get_main.get().tagContainer.addNewcard();
            }
            else {
                Random random = new Random();
                String view_char = String.valueOf(View0.type_action());
                ArrayList<String> i_char = new ArrayList<>(Arrays.asList("x","s","d","c","h"));
                for(int i=0;i<3;i++){ // 写这个随机写得相似
                    int num = random.nextInt(4);
                    if(i_char.get(num).equals(view_char))
                        info.get_main.get().tagContainer.addNewcard(view_num + i_char.get(4));
                    else
                        info.get_main.get().tagContainer.addNewcard(view_num + i_char.get(num));
                }
            }
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
        }));
    }
    public static void crat_sk_1_3_1(Kilss_Button info){ // 重抽
        //    <string name="view_1_3_1">仅Combo为0才能使用\n将手牌放回牌库，然后随机抽取相同数量的手牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_3_1);
        info.use_true = ()->{ // 修改可以使用判定
            if(info.use_true_default()){
                if(info.get_main.get().layout_show.combo == 0) return true;
            }
            return false;
        };
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++){ // 清空手牌
                View item = main.tagContainer.getChildAt(0);
                if(item instanceof Card){
                    Card View0 = (Card) item;
                    if(View0.abandon){
                        main.tagContainer.list.add(View0.gettext());
                    }
                }
                main.tagContainer.removecard(0);
            }
            for(int i=0;i<num;i++)
                info.get_main.get().tagContainer.addNewcard();
        }));
    }
    public static void crat_sk_1_3_2(Kilss_Button info){ // 冲刺
        //    <string name="view_1_3_2">仅Combo为0才能使用\n抽三张牌</string>
        set_namempuse(info, R.string.sk_1_3_2);
        info.use_true = ()->{ // 修改可以使用判定
            if(info.use_true_default()){
                if(info.get_main.get().layout_show.combo == 0) return true;
            }
            return false;
        };
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
        }));
    }
    public static void crat_sk_1_3_3(Kilss_Button info){ // 置换
        //    <string name="view_1_3_3">选择一张手牌加入弃牌区，抽取一张牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_3_3);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
        }));
    }
    public static void crat_sk_1_3_4(Kilss_Button info){ // 根除
        //    <string name="view_1_3_4">选择一张手牌加入弃牌区，抽取三张牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_1_3_4);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
        }));
    }
    public static void crat_sk_2_1_1(Kilss_Button info){ // 繁茂
//    <string name="view_2_1_1">获得buff:每次出牌后mp+1</string>
        set_namempuse(info, R.string.sk_2_1_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                int[] arr = (int[]) data.get("arr");
                if (arr != null) {
                    arr[0]++;
                }
            });
            main.hit_card_succes_sign.connect(slot); // 繁茂
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.hit_card_succes_sign, slot)
            );
        }));
    }
    public static void crat_sk_2_1_2(Kilss_Button info){ // 种子
        //    <string name="view_2_1_2">mp+15</string>
        set_namempuse(info, R.string.sk_2_1_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add("play_technique", 15);
        }));
    }
    public static void crat_sk_2_1_3(Kilss_Button info){ // 烈焰
//    <string name="view_2_1_3">选择一张手牌加入弃牌区，hp,hp上限,mp,mp上限 增加卡牌的数值\n可使用三次 选择特殊卡牌没有效果</string>
        set_namempuse(info, R.string.sk_2_1_3);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
            int num = View0.get_number();
            if(num != -1) {
                info.get_main.get().layout_show.text_number_setmax(
                        "play_hp",
                        info.get_main.get().layout_show.play_hp_max + num
                );
                info.get_main.get().layout_show.text_number_setmax(
                        "play_technique",
                        info.get_main.get().layout_show.play_technique_max + num
                );
                info.get_main.get().layout_show.text_number_add(
                        "play_hp",
                        num );
                info.get_main.get().layout_show.text_number_add(
                        "play_technique",
                        num );
            }
        }));
    }
    public static void crat_sk_2_1_4(Kilss_Button info) { // 血之抽牌
//    <string name="view_2_1_4">将血量上限降低为当前数值，按10，11递增最多减20\n每减少一次抽取一张牌</string>
        set_namempuse(info, R.string.sk_2_1_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            int sum = info.get_main.get().layout_show.play_hp_max - info.get_main.get().layout_show.play_hp;
            info.get_main.get().layout_show.text_number_setmax(
                    "play_hp",
                    info.get_main.get().layout_show.play_hp
            );

            for(int i=10;i<21;i++){
                if(sum > i){
                    sum -= i;
                    info.get_main.get().tagContainer.addNewcard();
                }
                else return;
            }
            for(int i=0;i<sum/20;i++){
                info.get_main.get().tagContainer.addNewcard();
            }
        }));
    }
    public static void crat_sk_2_2_1(Kilss_Button info) { // 再平衡
//    <string name="view_2_2_1">扣除mp后将mp和mp上限取为两者的平均值</string>
        set_namempuse(info, R.string.sk_2_2_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            int num = (
                    info.get_main.get().layout_show.play_technique_max +
                            info.get_main.get().layout_show.play_technique) / 2;

            info.get_main.get().layout_show.text_number_setmax(
                    "play_technique",
                    num
            );
            info.get_main.get().layout_show.text_number_set(
                    "play_technique",
                    num
            );
        }));
    }
    public static void crat_sk_2_2_2(Kilss_Button info) { // 弹跳
//    <string name="view_2_2_2">获得buff:每回合开始时mp+25</string>
        set_namempuse(info, R.string.sk_2_2_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                info.get_main.get().layout_show.text_number_add(
                        "play_technique",
                        25);
            });
            main.trun_begin_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.trun_begin_sign, slot)
            );
        }));
    }
    public static void crat_sk_2_2_3(Kilss_Button info) { // 馈赠
//    <string name="view_2_2_3">mp+40</string>
        set_namempuse(info, R.string.sk_2_2_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_technique",
                    40);
        }));
    }
    public static void crat_sk_2_2_4(Kilss_Button info) {
//    <string name="view_2_2_4">回合结束手牌放回弃牌库时，每放回一张额外获得2mp</string>
        set_namempuse(info, R.string.sk_2_2_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                int[] arr = (int[]) data.get("arr");
                if (arr != null) {
                    arr[0]+=2;
                }
            });
            main.btnendround_num_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.btnendround_num_sign, slot)
            );
        }));
    }
    public static void crat_sk_2_3_1(Kilss_Button info) {
//    <string name="view_2_3_1">获得buff:每次使用技能后，mp+5</string>
        set_namempuse(info, R.string.sk_2_3_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                info.get_main.get().layout_show.text_number_add(
                        "play_technique",
                        5);
            });
            main.skills_trigger_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.skills_trigger_sign, slot)
            );
        }));
    }
    public static void crat_sk_2_3_2(Kilss_Button info) {
//    <string name="view_2_3_2">mp+40,并获得buff:回合开始时，mp-5</string>
        set_namempuse(info, R.string.sk_2_3_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            info.get_main.get().layout_show.text_number_add(
                    "play_technique",
                    40);
            Signal.Slot slot = new Signal.Slot((data)->{
                info.get_main.get().layout_show.text_number_add(
                        "play_technique",
                        -5);
            });
            main.skills_trigger_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.skills_trigger_sign, slot, false)
            );
        }));
    }
    public static void crat_sk_2_3_3(Kilss_Button info) {
//    <string name="view_2_3_3">减少hp至1，每减少3hp则mp+1</string>
        set_namempuse(info, R.string.sk_2_3_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            int num = (info.get_main.get().layout_show.play_hp - 1) / 3;
            info.get_main.get().layout_show.text_number_set(
                    "play_hp",
                    1 );
            info.get_main.get().layout_show.text_number_add(
                    "play_technique",
                    num);
        }));
    }
    public static void crat_sk_2_3_4(Kilss_Button info) {
//    <string name="view_2_3_4">mp+ 当前Combo数值\n可使用三次  使用后进入1cd</string>
        set_namempuse(info, R.string.sk_2_3_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_technique",
                    info.get_main.get().layout_show.combo);
            info.cd_add(1);
        }));
    }
    public static void crat_sk_3_1_1(Kilss_Button info){ // 交易
//    <string name="view_3_1_1">选择一张手牌加入弃牌区，抽取一张牌,若数值大于等于6,则额外抽取一张\n可使用三次</string>
        set_namempuse(info, R.string.sk_3_1_1);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
            int num = View0.get_number();
            if(num >= 6) { // 包含 != -1
                info.get_main.get().tagContainer.addNewcard();
            }
        }));
    }
    public static void crat_sk_3_1_2(Kilss_Button info){ // 单色
//    <string name="view_3_1_2">选择一张手牌，将所有手牌颜色变为与它相同\n可使用三次</string>
        set_namempuse(info, R.string.sk_3_1_2);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            if(View0.is_number()) {
                Game main = info.get_main.get();
                int num = main.tagContainer.getChildCount();
                for(int i=0;i<num;i++){ // 手牌
                    View item = main.tagContainer.getChildAt(i);
                    if(item instanceof Card){
                        Card View1 = (Card) item;
                        int num0 = View1.get_number();
                        if(num0 != -1)
                            main.tagContainer.card_settext(View1, String.valueOf(num0) + View0.type_action());
                    }
                }
            }
        }));
    }
    public static void crat_sk_3_1_3(Kilss_Button info){ // 创造
//    <string name="view_3_1_3">随机生成一张卡牌并加入手牌\n可使用九次</string>
        set_namempuse(info, R.string.sk_3_1_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard(Hardcard_LinearLayout.getRandomListstring());
            // 随机生成一张牌，不从牌库生成
        }));
    }
    public static void crat_sk_3_1_4(Kilss_Button info) { // 规模
//    <string name="view_3_1_4">获得四回合buff:回合开始时额外抽取两张牌</string>
        set_namempuse(info, R.string.sk_3_1_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                int[] arr = (int[]) data.get("arr");
                if (arr != null) {
                    arr[0] += 2;
                }
            });
            main.card_begin_num_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(4, main.card_begin_num_sign, slot)
            ); // 绑定buff时间
        }));
    }
    public static void crat_sk_3_2_1(Kilss_Button info) { // 抽卡5
//    <string name="view_3_2_1">抽5张牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_3_2_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
            info.get_main.get().tagContainer.addNewcard();
        }));
    }
    public static void crat_sk_3_2_2(Kilss_Button info) { // 回流
//    <string name="view_3_2_2">将全部手牌放回牌库，然后随机抽取相同数量的手牌\n可使用三次</string>
        set_namempuse(info, R.string.sk_3_2_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++){ // 清空手牌
                View item = main.tagContainer.getChildAt(0);
                if(item instanceof Card){
                    Card View0 = (Card) item;
                    if(View0.abandon){
                        main.tagContainer.list.add(View0.gettext());
                    }
                }
                main.tagContainer.removecard(0);
            }
            for(int i=0;i<num;i++)
                info.get_main.get().tagContainer.addNewcard();
        }));
    }
    public static void crat_sk_3_2_3(Kilss_Button info) { // 回收
//    <string name="view_3_2_3">使场地技能使用次数回满\n可使用三次</string>
        set_namempuse(info, R.string.sk_3_2_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Kilss_Button sk = main.btn_skill.get(0);
            sk.set_use_fre_add(sk.use_fre_max - sk.use_fre);
        }));
    }
    public static void crat_sk_3_2_4(Kilss_Button info) { // 延迟
//    <string name="view_3_2_4">使对手mp-5\n可使用九次</string>
        set_namempuse(info, R.string.sk_3_2_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "enemy_technique",
                    -5 );
        }));
    }
    public static void crat_sk_3_3_1(Kilss_Button info) { // 计数降低
//    <string name="view_3_3_1">选择一张手牌，使其数值减1\n可使用九次</string>
        set_namempuse(info, R.string.sk_3_3_1);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            if(num > 1){
                num--;
                info.get_main.get().tagContainer.card_settext(View0, String.valueOf(num) + View0.type_action());
            }
            else
                info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
        }));
    }
    public static void crat_sk_3_3_2(Kilss_Button info) { // 计数增加
//    <string name="view_3_3_2">选择一张手牌，使其数值加1\n可使用九次</string>
        set_namempuse(info, R.string.sk_3_3_2);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            if(num != -1){
                num++;
                info.get_main.get().tagContainer.card_settext(View0, num + String.valueOf(View0.type_action()));
            }
            else
                info.get_main.get().tagContainer.addNewcard(View0.gettext());
        }));
    }
    public static void crat_sk_3_3_3(Kilss_Button info) { // 金字塔
//    <string name="view_3_3_3">获得四回合buff:回合结束时，保留手牌(不会触发扣除手牌回5mp)</string>
        set_namempuse(info, R.string.sk_3_3_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                boolean[] arr = (boolean[]) data.get("arr_bool");
                if (arr != null) {
                    arr[0] = false;
                }
            });
            main.click_trunover_sign.connect(slot); // 金字塔
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(4, main.click_trunover_sign, slot)
            ); // 绑定buff时间
        }));
    }
    public static void crat_sk_3_3_4(Kilss_Button info) { // 复苏
//    <string name="view_3_3_4">使其他技能的使用次数加1(不可超过上限)</string>
        set_namempuse(info, R.string.sk_3_3_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            for(int i=1;i<5;i++)
                if(i != info.order)
                    info.get_main.get().btn_skill.get(i).set_use_fre_add(1);
        }));
    }
    public static void crat_sk_4_1_1(Kilss_Button info){ // 再生
//    <string name="view_4_1_1">并获得五回合buff:回合开始时，hp+10</string>
        set_namempuse(info, R.string.sk_4_1_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                info.get_main.get().layout_show.text_number_add(
                        "play_hp",
                        10 );
            });
            main.trun_begin_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.trun_begin_sign, slot)
            );
        }));
    }
    public static void crat_sk_4_1_2(Kilss_Button info){ // 治愈
//    <string name="view_4_1_2">hp+50</string>
        set_namempuse(info, R.string.sk_4_1_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_hp",
                    50 );
        }));
    }
    public static void crat_sk_4_1_3(Kilss_Button info){ // 恢复
//    <string name="view_4_1_3">将hp恢复至上限</string>
        set_namempuse(info, R.string.sk_4_1_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            if(info.get_main.get().layout_show.play_hp < info.get_main.get().layout_show.play_hp_max)
                info.get_main.get().layout_show.text_number_add(
                        "play_hp",
                        info.get_main.get().layout_show.play_hp_max - info.get_main.get().layout_show.play_hp );
        }));
    }
    public static void crat_sk_4_1_4(Kilss_Button info){ // 过载
//    <string name="view_4_1_4">hp+200，hpmax+200，mp-99(可超过下限)</string>
        set_namempuse(info, R.string.sk_4_1_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_setmax(
                    "play_hp",
                    info.get_main.get().layout_show.play_hp_max + 200);
            info.get_main.get().layout_show.text_number_add(
                    "play_hp",
                    200);
            info.get_main.get().layout_show.text_number_set(
                    "play_technique",
                    info.get_main.get().layout_show.play_technique - 99);
        }));
    }
    public static void crat_sk_4_2_1(Kilss_Button info){ //
//    <string name="view_4_2_1">消耗所有mp，并恢复等量hp\n可使用三次</string>
        set_namempuse(info, R.string.sk_4_2_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            if(info.get_main.get().layout_show.play_technique > 0){
                info.get_main.get().layout_show.text_number_add(
                        "play_hp",
                        info.get_main.get().layout_show.play_technique);
                info.get_main.get().layout_show.text_number_add(
                        "play_technique",
                        -1 * info.get_main.get().layout_show.play_technique);
            }
        }));
    }
    public static void crat_sk_4_2_2(Kilss_Button info){ //
//    <string name="view_4_2_2">将hp恢复至上限，获得buff:每回合开始时减少当前一半hp(向下取整)</string>
        set_namempuse(info, R.string.sk_4_2_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_hp",
                    info.get_main.get().layout_show.play_hp_max - info.get_main.get().layout_show.play_hp);

            Game main = info.get_main.get();
            Signal.Slot slot = new Signal.Slot((data)->{
                info.get_main.get().layout_show.text_number_add(
                        "play_hp",
                        -1 * info.get_main.get().layout_show.play_hp / 2 );
            });
            main.trun_begin_sign.connect(slot);
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(-1, main.trun_begin_sign, slot, false)
            );
        }));
    }
    public static void crat_sk_4_2_3(Kilss_Button info){ //
//    <string name="view_4_2_3">使自身和对方血量均恢复到上限</string>
        set_namempuse(info, R.string.sk_4_2_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_hp",
                    info.get_main.get().layout_show.play_hp_max - info.get_main.get().layout_show.play_hp);
            info.get_main.get().layout_show.text_number_add(
                    "enemy_hp",
                    info.get_main.get().layout_show.enemy_hp_max - info.get_main.get().layout_show.enemy_hp);
        }));
    }
    public static void crat_sk_4_2_4(Kilss_Button info){ //
//    <string name="view_4_2_4">将当前的累计攻击力转化为血量\n可使用三次</string>
        set_namempuse(info, R.string.sk_4_2_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_hp",
                    info.get_main.get().layout_show.play_att_sum);
            info.get_main.get().layout_show.text_number_add(
                    "play_att",
                    -1 * info.get_main.get().layout_show.play_att_sum);
        }));
    }
//
//    <string name="sk_4_2_2">mp  0\n技能422\n1</string>
//    <string name="sk_4_2_3">mp  10\n技能423\n1</string>

    public static void crat_sk_5_1_1(Kilss_Button info){ // 归零
//    <string name="view_5_1_1">将自身mp设置为0\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_1_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_set(
                    "play_technique",
                    0 );
        }));
    }

    public static void crat_sk_5_1_2(Kilss_Button info){ // 洗澡
//    <string name="view_5_1_2">清除自身所有buff,包括正负面</string>
        set_namempuse(info, R.string.sk_5_1_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            List<Signal.Slot> tempList = new ArrayList<>(info.get_main.get().buff_time_settle_sign.slotList);
            for(Signal.Slot slot: tempList){
                if(slot instanceof Game.buff_time_settle){
                    Game.buff_time_settle View0 = (Game.buff_time_settle) slot;
                    if(View0.clear)
                        View0.completion.run();
                }
            }
        }));
    }
    public static void crat_sk_5_1_3(Kilss_Button info){ // 净化
//    <string name="view_5_1_3">清除自身所有负面buff,每清除一个，恢复5hp\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_1_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            int sum = 0;
            List<Signal.Slot> tempList = new ArrayList<>(info.get_main.get().buff_time_settle_sign.slotList);
            for(Signal.Slot slot: tempList){
                if(slot instanceof Game.buff_time_settle){
                    Game.buff_time_settle View0 = (Game.buff_time_settle) slot;
                    if(View0.clear && !View0.front){
                        View0.completion.run();
                        sum++;
                    }
                }
            }
            if(sum > 0)
                info.get_main.get().layout_show.text_number_add(
                        "play_hp",
                        sum * 5 );
        }));
    }
    public static void crat_sk_5_1_4(Kilss_Button info){ // 共享
//    <string name="view_5_1_4">使自身和对方mp+5\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_1_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_technique",
                    5 );
            info.get_main.get().layout_show.text_number_add(
                    "enemy_technique",
                    5 );
        }));
    }
    public static void crat_sk_5_2_1(Kilss_Button info){ // 方块
//    <string name="view_5_2_1">(方块卡牌打出后，可继续打牌)\n向手牌添加一张方块\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_2_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard(Card.square);
        }));
    }
    public static void crat_sk_5_2_2(Kilss_Button info){ // 压平
//    <string name="view_5_2_2">使自身和对方的累计攻击力减少40(不可低于0)\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_2_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().layout_show.text_number_add(
                    "play_att",
                    -40 );
            info.get_main.get().layout_show.text_number_add(
                    "enemy_att",
                    -40 );
        }));
    }
    public static void crat_sk_5_2_3(Kilss_Button info){ // 刻印
//    <string name="view_5_2_3">生成一张文字和上一张展示卡牌一样的卡牌加入手牌\n可使用三次(若展示手牌为空则不生效)</string>
        set_namempuse(info, R.string.sk_5_2_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            if(!Card.show_last_card.isEmpty())
                info.get_main.get().tagContainer.addNewcard(Card.show_last_card);
        }));
    }
    public static void crat_sk_5_3_1(Kilss_Button info) { // 保留
//    <string name="view_5_3_1">(获得"保留"的卡牌回合结束不会进入弃牌库，该效果打出后消失)\n选择一张手牌获得"保留"\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_3_1);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            View0.set_giveup(true);
        }));
    }
    public static void crat_sk_5_3_2(Kilss_Button info) { // 撤离
//    <string name="view_5_3_2">(获得"保留"的卡牌回合结束不会进入弃牌库，该效果打出后消失)\n使当前所有手牌获得"保留"\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_3_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard();
            Game main = info.get_main.get();
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++) { // 手牌
                View item = main.tagContainer.getChildAt(i);
                if (item instanceof Card) {
                    Card View1 = (Card) item;
                    View1.set_giveup(true);
                }
            }
        }));
    }
    public static void crat_sk_5_3_3(Kilss_Button info){ // 单数
//    <string name="view_5_3_3">选择一张手牌，将所有手牌数值变为与它相同\n可使用三次</string>
        set_namempuse(info, R.string.sk_5_3_3);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            int card_num = View0.get_number();
            if(card_num != -1) {
                Game main = info.get_main.get();
                int num = main.tagContainer.getChildCount();
                for(int i=0;i<num;i++){ // 手牌
                    View item = main.tagContainer.getChildAt(i);
                    if(item instanceof Card){
                        Card View1 = (Card) item;
                        char type = View1.type_action();
                        if(type != '0')
                            main.tagContainer.card_settext(View1, String.valueOf(card_num) + type);
                    }
                }
            }
        }));
    }






    public static void crat_sk_10_1_1(Kilss_Button info){ // 切分
//    <string name="view_10_1_1">选择一张卡牌，按数值将其分成两张</string>
        set_namempuse_redcolor(info, R.string.sk_10_1_1);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            if(num != -1) {
                if(num <= 1){
                    info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
                }
                else {
                    Random random = new Random();
                    int num0 = random.nextInt(num - 1) + 1;
                    num -= num0;
                    info.get_main.get().tagContainer.card_settext(View0, num + "" + View0.type_action());
                    info.get_main.get().tagContainer.addNewcard(num0 + "" + View0.type_action()).copy_bute(View0);
                }
            }
            else {
                info.get_main.get().tagContainer.addNewcard(View0.gettext()).copy_bute(View0);
            }
        }));
    }
    public static void crat_sk_10_1_2(Kilss_Button info){ // 小型涡轮
//    <string name="view_10_1_2">抽一张牌，并将所有卡牌花色变为x(黄色)</string>
        set_namempuse_redcolor(info, R.string.sk_10_1_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard();
            Game main = info.get_main.get();
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
        }));
    }
    public static void crat_sk_10_1_3(Kilss_Button info){ // 小型冻结
//    <string name="view_10_1_3">根据本回合打出的花色种类个数，抽取等量的牌\n使用后本回合不能触发额外抽牌</string>
        set_namempuse_redcolor(info, R.string.sk_10_1_3);

        TextView view_card = new TextView(info.getContext());
        view_card.setId(View.generateViewId());
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(0,0);
        info.addView(view_card, lp);
        ConstraintSet constraintSet = new ConstraintSet();
        // 克隆当前布局已有的约束
        constraintSet.clone(info);

        // ========= 设置四边约束：子view的上下左右 绑定父布局的上下左右 =========
        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

        // 应用约束生效
        constraintSet.applyTo(info);

        view_card.setTextSize(8); // sp

        view_card.setSingleLine(true); // 不能换行
        view_card.setTextColor(Color.WHITE);// 设置文字颜色 白色

        view_card.setGravity(Gravity.CENTER); // 上下左右居中
//        view_card.setText(""); // 文字 和 颜色
        view_card.setBackgroundColor(ContextCompat.getColor(info.getContext(), R.color.transparent));

        List<Character> cardList = new ArrayList<>();
        char[] list_char = {'x', 's', 'h', 'd', 'c'};

        info.get_main.get().hit_card_succes_sign.connect(new Signal.Slot((data)->{ // 小型冻结
            String str = (String) data.get("text");
            if(Card.is_number(str)){
                if(!cardList.contains(Card.type_action(str))){
                    cardList.add(Card.type_action(str));
                    StringBuilder string = new StringBuilder();
                    for(char char_i: list_char){
                        if(cardList.contains(char_i)){
                            string.append(char_i).append(" ");
                        }
                    }
                    view_card.setText(string.toString());
                }
            }
        }));
        info.get_main.get().click_trunover_sign.connect(new Signal.Slot((data)->{ // 小型冻结
            cardList.clear();
            view_card.setText("");
        }));
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();

            for(char char_i: list_char){
                if(cardList.contains(char_i))
                    main.tagContainer.addNewcard();
            }
            cardList.clear();
            view_card.setText("");

            Signal.Slot slot = new Signal.Slot((data)->{
                int[] arr = (int[]) data.get("arr");
                if(arr != null)
                    arr[0] = 0;
            }, 10000);
            main.addcard_sign.connect(slot); // 小型冻结
            main.buff_time_settle_sign.connect(
                    main. new buff_time_settle(1, main.addcard_sign, slot, false, false)
            );
            // 这里吐槽一下，原作用此技能进入冻结后章节的技能打出x也不能抽牌了
            // 但是用章节的打出x进入冻结，用此技能还能触发此技能的抽牌
        }));
    }

    public static void crat_sk_10_1_4(Kilss_Button info){ // 填充方块
//    <string name="view_10_1_4">持续消耗8mp，每消耗一次则获得一张方块</string>
        set_namempuse_redcolor(info, R.string.sk_10_1_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            int num = main.layout_show.play_technique / 8;
            if(num <= 0) return;
            main.layout_show.text_number_add(
                    "play_technique",
                    -8 * num);
            for(int i=0;i<num;i++)
                info.get_main.get().tagContainer.addNewcard(Card.square);
        }));
    }

    public static void crat_sk_10_2_1(Kilss_Button info){ // 转化方块
//    <string name="view_10_2_1">选择一张手牌将其变为方块</string>
        set_namempuse_redcolor(info, R.string.sk_10_2_1);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            info.get_main.get().tagContainer.card_settext(View0, Card.square);
        }));
    }

    public static void crat_sk_10_2_2(Kilss_Button info){ // 重置
//    <string name="view_10_2_2">将弃牌库所有牌放回牌库，根据放回张数恢复等量hp，并刷新展示卡牌</string>
        set_namempuse_redcolor(info, R.string.sk_10_2_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            main.layout_show.text_number_add(
                    "play_hp",
                    main.tagContainer.abandon_card_string.size());
            main.tagContainer.list.addAll(main.tagContainer.abandon_card_string); // 将弃牌堆的牌加回牌库
            main.tagContainer.abandon_card_string.clear(); // 清空弃牌
            main.layout_show.text_number_refresh("library"); // 打印牌库
            main.layout_show.text_number_refresh("abandon"); // 打印弃牌
            main.tagContainer.showlast_card_refresh("");
        }));
    }
    public static void crat_sk_10_2_3(Kilss_Button info){ // 小型魔法
//    <string name="view_10_2_3">抽牌直到手牌变为两张，再抽一张牌，生成一张方块加入手牌，hp+5，mp+5，对方mp-5</string>
        set_namempuse_redcolor(info, R.string.sk_10_2_3);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            int num = 2 - info.get_main.get().tagContainer.getChildCount();
            if(num > 0)
                for(int i=0;i<num;i++)
                    info.get_main.get().tagContainer.addNewcard();
            main.tagContainer.addNewcard();

            info.get_main.get().tagContainer.addNewcard(Card.square);

            main.layout_show.text_number_add(
                    "enemy_hp",
                    5);
            main.layout_show.text_number_add(
                    "play_technique",
                    5);
            main.layout_show.text_number_add(
                    "enemy_technique",
                    -5);
        }));
    }

    public static void crat_sk_10_2_4(Kilss_Button info){ // 复活
//    <string name="view_10_2_4">使所有普通技能使用次数恢复至上限</string
        set_namempuse_redcolor(info, R.string.sk_10_2_4);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            for(int i=1;i<main.btn_skill.size();i++){
                Kilss_Button sk = main.btn_skill.get(i);
                if(!info.equals(sk) && sk.type != Kilss_Button.type_red){
                    sk.set_use_fre_add(sk.use_fre_max - sk.use_fre);
                }
            }
        }));
    }

    public static void crat_sk_10_3_1(Kilss_Button info){ // 星尘
//    <string name="view_10_3_1">选择一张手牌进入弃牌库，并根据其数值获得等量的方块</string>
        set_namempuse_redcolor(info, R.string.sk_10_3_1);
        info.setOnClickListener(v->setOn_choosecard_sta(v, info, (View0)->{
            int num = View0.get_number();
            info.get_main.get().tagContainer.removecard(View0); // 此时去除卡牌是否有风险呢
            for(int i=0;i<num;i++){
                Card newTag = info.get_main.get().tagContainer.addNewcard(Card.square);
                newTag.abandon = false;
            }
        }));
    }

    public static void crat_sk_10_3_2(Kilss_Button info){ // 拉条
//    <string name="view_10_3_2">使第一个技能的使用次数+1(不可超过上限,会跳过红色技能)</string>
        set_namempuse_redcolor(info, R.string.sk_10_3_2);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            for(int i=1;i<main.btn_skill.size();i++){
                Kilss_Button sk = main.btn_skill.get(i);
                if(sk.type != Kilss_Button.type_red){
                    sk.set_use_fre_add(1);
                    break;
                }
            }
        }));
    }

    public static void crat_sk_10_3_3(Kilss_Button info){ // 白魔法
//    <string name="view_10_3_3">mp+5，mpmax+5，hp+5，本场每使用一次额外使自身mp+2，hp+2</string>
        set_namempuse_redcolor(info, R.string.sk_10_3_3);
        int[] arr = {0};
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            main.layout_show.text_number_add(
                    "play_hp",
                    5 + 2*arr[0]);
            main.layout_show.text_number_add(
                    "play_technique",
                    5 + 2*arr[0]);
            main.layout_show.text_number_setmax(
                    "play_technique",
                    5 + main.layout_show.play_technique_max);
            main.layout_show.text_number_setmax(
                    "play_hp",
                    5 + main.layout_show.play_hp_max);
            arr[0]++;
        }));
    }

    public static void crat_sk_10_3_4(Kilss_Button info){ // 黑魔法
//    <string name="view_10_3_4">使对方mp-5，本场每使用一次额外使对方mp-2和此技能mp需求-2(不会低于0)</string>
        set_namempuse_redcolor(info, R.string.sk_10_3_4);
        int[] arr = {0};
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            main.layout_show.text_number_add(
                    "enemy_technique",
                    -5 - 2*arr[0]);
            if(info.need_mp > 1) info.set_need_mp(info.need_mp - 2);
            else if(info.need_mp > 0) info.set_need_mp(0);
            arr[0]++;
        }));
    }

    public static void crat_sk_11_1_1(Kilss_Button info){ // 心之转换
//    <string name="view_11_1_1">将手牌中所有特殊卡牌变为随机的普通卡牌</string>
        set_namempuse_redcolor(info, R.string.sk_11_1_1);
        info.setOnClickListener(v->setOnClick_sta(v, info, ()->{
            info.get_main.get().tagContainer.addNewcard();
            Game main = info.get_main.get();
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++) { // 手牌
                View item = main.tagContainer.getChildAt(i);
                if (item instanceof Card) {
                    Card View1 = (Card) item;
                    if(View1.is_special()){
                        info.get_main.get().tagContainer.card_settext(View1, Hardcard_LinearLayout.getRandomListstring());
                    }
                }
            }
        }));
    }
    public static void crat_sk_11_1_2(Kilss_Button info){ // 熔炉
//    <string name="view_11_1_1">将手牌中所有特殊卡牌变为随机的普通卡牌</string>
        set_namempuse_redcolor(info, R.string.sk_11_1_2);
        info.setOnClickListener(v->Choose_Skills.setOnClick_sta(v, info, ()->{
            Game main = info.get_main.get();
            int num = main.tagContainer.getChildCount();
            for(int i=0;i<num;i++) { // 手牌
                View item = main.tagContainer.getChildAt(i);
                if (item instanceof Card) {
                    Card View1 = (Card) item;
                    if(Card.triangle.equals(View1.gettext())){ // 是三角
                        main.tagContainer.card_settext(View1, Card.square);
                        main.layout_show.text_number_add(
                                "play_technique",
                                -5);
                    }
                }
            }
        }));
    }

}