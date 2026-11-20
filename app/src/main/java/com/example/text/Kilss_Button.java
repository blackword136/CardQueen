package com.example.text;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.graphics.Color;
import android.content.res.ColorStateList;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Kilss_Button extends ConstraintLayout { // MaterialButton
    public int type = type_blue;
    public static final int type_blue = 0;
    public static final int type_red = 1;
//    public Map<String,Object> memory = new HashMap<>();
    public String name = ""; // 技能名
    public void set_name(String str){
        name = str;
        text_view.setText("mp  " + need_mp + "\n\n" + name);
    }
    public int cd = 0;
    public void cd_add(int i){ // 修改cd
        if(cd + i <= 0)
            cd = 0;
        else
            cd = cd + i;
//        setText(String.valueOf(cd));
        use_show_refresh.run();
    }
    public int need_mp = 0; // 所需蓝量
    public void set_need_mp(int num){
        need_mp = num;
        text_view.setText("mp  " + need_mp + "\n\n" + name);
        use_show_refresh.run();
    }
    public int use_fre_max = 0; // 使用次数上限
    public int use_fre = 0; // 使用次数
    public void set_use_fre(int num){
        use_fre = num;
        if(use_fre >= 1) num_view.setText(String.valueOf(num));
        else num_view.setText("");
        use_show_refresh.run();
    }
    public void set_use_fre_add(int num){
        if(num + use_fre <= 0) use_fre = 0;
        else if(num + use_fre > use_fre_max) use_fre = use_fre_max;
        else use_fre += num;
        set_use_fre(use_fre);
    }
    public Supplier<Game> get_main;
    public Boolean use_true_default(){ // 默认是否能使用以蓝量判断
        if(!get_main.get().allow_roune_strat()) return false; // 没到回合
        else if(cd > 0 || use_fre <= 0) return false; // 处于cd或没有使用次数
        else if(need_mp <= 0) return true; // 蓝量需求小于0
        else if(get_main.get().layout_show.play_technique >= need_mp)
            return true;
        return false;
    }
    public Supplier<Boolean> use_true = this::use_true_default;
    private boolean cd_ing = false;
//    private boolean use_ing = false;
    public int color_background_cantouch = R.color.skills_background_cantouch;
    public int color_background_notcan = R.color.skills_background_notcan;
    public int color_stroke_cantouch = R.color.skills_stroke_cantouch;
    public int color_stroke_notcan = R.color.skills_stroke_notcan;
    public void use_show_refresh_default(){ // 根据是否能点击刷新显示状态
        if((cd == 0) == cd_ing){
            cd_ing = !(cd == 0);
            if(cd == 0){
                cd_view.setText("");
                cd_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            }
            else {
                cd_view.setText("冷却中 ： " + cd);
                cd_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.skills_cd));
            }
        };

//        if(bool == use_ing) return;
        this.set_background(use_true.get());

//        if(use_true.get())
//            text_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.skills_background_cantouch));
//        else
//            text_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.skills_background_notcan));

        // 读取colors.xml里的颜色
//        int bgColor;
////        int strokeColor;
//        if(use_true.get()){
//            bgColor = ContextCompat.getColor(getContext(), R.color.skills_background_cantouch);
////            strokeColor = ContextCompat.getColor(getContext(), R.color.skills_stroke_cantouch);
//        }
//        else{
//            bgColor = ContextCompat.getColor(getContext(), R.color.skills_background_notcan);
////            strokeColor = ContextCompat.getColor(getContext(), R.color.skills_stroke_notcan);
//        }
//
//// 转成ColorStateList（backgroundTint、strokeColor必须用这个）
//        ColorStateList bgTintList = ColorStateList.valueOf(bgColor);
////        ColorStateList strokeColorList = ColorStateList.valueOf(strokeColor);
//
//        this.setBackgroundTintList(bgTintList);
//        this.setStrokeColor(strokeColorList);
//            this.setBackgroundTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#2196F3")));
    }
    public void set_background(boolean bool){
        if(bool){
            drawable.setColor(ContextCompat.getColor(getContext(), color_background_cantouch));
            drawable.setStroke(
                    2,
                    ContextCompat.getColor(getContext(), color_stroke_cantouch)
            );
        }
        else{
            drawable.setColor(ContextCompat.getColor(getContext(), color_background_notcan));
            drawable.setStroke(
                    2,
                    ContextCompat.getColor(getContext(), color_stroke_notcan)
            );
        }
        this.setBackground(drawable);
    }
    public Runnable use_show_refresh = this::use_show_refresh_default;

    public Kilss_Button(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    // XML加载 + 应用主题默认样式（你问的这个）
    public Kilss_Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TextView text_view; // 打印消耗mp和技能名的文字框
    private TextView num_view; // 右上角的使用次数文字框
    private TextView cd_view; // cd
    public int order;

    GradientDrawable drawable = new GradientDrawable(); // 这个属性是一对一的，多个组件要多个new哦

    public void init(int i, Supplier<Game> game_main){ // 后续在java的构造函数，技能无技能也要设置
        order = i;
        get_main = game_main;
        switch (i){
            case 0:{
                text_view = findViewById(R.id.btn_sk_0);
                num_view = findViewById(R.id.view_sk_0);
                cd_view = findViewById(R.id.cd_sk_0);
                break;
            }
            case 1:{
                text_view = findViewById(R.id.btn_sk_1);
                num_view = findViewById(R.id.view_sk_1);
                cd_view = findViewById(R.id.cd_sk_1);
                break;
            }
            case 2:{
                text_view = findViewById(R.id.btn_sk_2);
                num_view = findViewById(R.id.view_sk_2);
                cd_view = findViewById(R.id.cd_sk_2);
                break;
            }
            case 3:{
                text_view = findViewById(R.id.btn_sk_3);
                num_view = findViewById(R.id.view_sk_3);
                cd_view = findViewById(R.id.cd_sk_3);
                break;
            }
            case 4:{
                text_view = findViewById(R.id.btn_sk_4);
                num_view = findViewById(R.id.view_sk_4);
                cd_view = findViewById(R.id.cd_sk_4);
                break;
            }
        }

// dp转px
        float radiusDp = 8;
        float r = radiusDp * getResources().getDisplayMetrics().density;
//        app:strokeWidth="2dp"
//        app:strokeColor="#2196F3"
//        app:cornerRadius="8dp"
        float[] radii = {
                r, r,      // 左上角
                0f, 0f,    // 右上角
                r, r,      // 右下角
                0f, 0f     // 左下角
        };

        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(radii);
        this.set_background(false);

        if(order > 0 && !Choose_Skills.skills_detailde.get(order - 1).isEmpty())
            setonlongclick.accept(this, Choose_Skills.skills_detailde.get(order - 1));
//            this.setOnLongClickListener(v -> {
//                // 长按触发逻辑
//                View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_skills_show,null);
//                TextView tvContent = view.findViewById(R.id.tv_content);
////                String bigText = "这里放大量文字......";
//                tvContent.setText(Choose_Skills.skills_detailde.get(order - 1));
//
//                new AlertDialog.Builder(getContext())
//                        .setTitle(name)
//                        .setView(view)  // 设置自定义滚动布局
//                        .setPositiveButton("关闭",(dialog,which)->{
//                            dialog.dismiss();
//                        })
//                        .show();
//
//                // 返回 true：消费这个长按事件，不会再触发单击
//                // 返回 false：长按执行完，还会继续触发普通点击
//                return true;
//            });

//        text_view = new TextView(context);
//        text_view.setId(View.generateViewId());
//        num_view = new TextView(context);
//        num_view.setId(View.generateViewId());
//        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(0,0);
//        this.addView(text_view, lp);
//        this.addView(num_view, lp);
//        ConstraintSet constraintSet = new ConstraintSet();
//        // 克隆当前布局已有的约束
//        constraintSet.clone(this);
//
//        // ========= 设置四边约束：子view的上下左右 绑定父布局的上下左右 =========
//        constraintSet.connect(text_view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
//        constraintSet.connect(text_view.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
//        constraintSet.connect(text_view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
//        constraintSet.connect(text_view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
//
//
//        int viewId = num_view.getId();
//
//// ========== 设置百分比宽高（对应你的xml 0.18 和0.88） ==========
//        constraintSet.constrainPercentWidth(viewId, 0.2f);
//        constraintSet.constrainPercentHeight(viewId, 0.2f);
//
//// 同时设置宽高模式为 0dp(MATCH_CONSTRAINT)
//        constraintSet.constrainWidth(viewId, ConstraintSet.MATCH_CONSTRAINT);
//        constraintSet.constrainHeight(viewId, ConstraintSet.MATCH_CONSTRAINT);
//
//// ========== 设置四边约束（必须！百分比控件一般要锚定parent）==========
//// 左绑定父左，右绑定父右；上绑定父上，下绑定父下
////        constraintSet.connect(viewId, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT);
//        constraintSet.connect(viewId, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT);
//        constraintSet.connect(viewId, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
////        constraintSet.connect(viewId, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
//
//
//        // 应用约束生效
//        constraintSet.applyTo(this);
//
////        View childView = ...;
//// 应用约束，生效
////        set.applyTo(this);
//
////        view_card.setPadding(30,0,30,0);
////        view_card.setTextSize(36); // sp
////        setBackgroundResource(R.drawable.tag_bg); //你可以自己准备圆角drawable，也可以注释掉
//
////        view_card.setLayoutParams(params);
//
////        text_view.setSingleLine(true); // 不能换行
//        text_view.setTextColor(Color.WHITE);// 设置文字颜色 白色
//        text_view.setTextSize(8); // sp
//        text_view.setText("无技能"); // 文字
//        text_view.setGravity(Gravity.CENTER); // 上下左右居中
//        text_view.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.skills_background_cantouch));
//
//        num_view.setSingleLine(true); // 不能换行
//        num_view.setTextColor(Color.WHITE);// 设置文字颜色 白色
//        num_view.setTextSize(4); // sp
//        num_view.setText(""); // 文字
//        num_view.setGravity(Gravity.CENTER); // 上下左右居中
    }
    public static BiConsumer<Kilss_Button, String> setonlongclick = (bt, str)-> bt.setOnLongClickListener(v -> {
        // 长按触发逻辑
        View view = LayoutInflater.from(bt.getContext()).inflate(R.layout.dialog_skills_show,null);
        TextView tvContent = view.findViewById(R.id.tv_content);
//                String bigText = "这里放大量文字......";
        tvContent.setText(str);

        new AlertDialog.Builder(bt.getContext())
                .setTitle(bt.name)
                .setView(view)  // 设置自定义滚动布局
                .setPositiveButton("关闭",(dialog,which)->{
                    dialog.dismiss();
                })
                .show();

        // 返回 true：消费这个长按事件，不会再触发单击
        // 返回 false：长按执行完，还会继续触发普通点击
        return true;
    });
//    public void create(){
//
////        this.setOnClickListener(v -> {
////            // 点击布局触发的逻辑
////        });
//        // 实在不行，写个信号和槽吧
//        // 设置蓝量需求，点击效果(因为一些技能需要将一些信息展示出来)，使用次数，使用次数上限，技能名
////        this.setOnClickListener();
//
//    }
}
