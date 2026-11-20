package com.example.text;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;

public class Card extends ConstraintLayout {
    // 虽然使用了很多static，但最好不要用static指向组件哦

//    public class MyConstraintLayout extends ConstraintLayout{
//        public MyConstraintLayout(Context context) {
//            super(context);
//        }
//
//    }
    public final static String square = "方块"; // 方块
    public final static String triangle = "三角"; // 三角
    public static String show_last_card = ""; // 上一张打的牌
    // 每次新增手牌，需要对该新增的单独对上一张进行 打出 额外抽牌 检测（在这个类）
    // 每次更新上一张手牌，需要对所有手牌进行 打出 额外抽牌 检测（在手牌的类进行循环）
    public boolean abandon = true; // 决定这张牌被打出或者回合结束时去哪，true表示进入弃牌堆，否则直接消失
    public boolean giveup = false; // 回合结束是否保留卡牌, 该效果在打出后消失
    public void copy_bute(Card card){
        // 复制属性，主要是上面两个
        this.abandon = card.abandon;
        set_giveup(card.giveup);
    }
    public void set_giveup(boolean bool){
        giveup = bool;
        if(giveup)
            view_giveup.setVisibility(View.VISIBLE);
        else
            view_giveup.setVisibility(View.INVISIBLE);
    }

    public Card(Context context, String itemstr, int childNum, int width){ // 生成指定手牌
        super(context);
        init(context, itemstr, childNum, width);
    }
    private TextView view_card;
    private TextView view_add;
    private TextView view_giveup;
    private void init(Context context, String itemstr, int childNum, int width){
//        add_effects_bool = false;
//        abandon = true;
        view_card = new TextView(context);
        view_card.setId(View.generateViewId());
//        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(0,0);
        this.addView(view_card, new ConstraintLayout.LayoutParams(0,0));

        view_add = new TextView(context);
        view_add.setId(View.generateViewId());
        view_add.setTextSize(12);
        this.addView(view_add, new ConstraintLayout.LayoutParams(0, 0));

        view_giveup = new TextView(context);
        view_giveup.setId(View.generateViewId());
        this.addView(view_giveup, new ConstraintLayout.LayoutParams(0, 0));
//        1. **View.VISIBLE** 👉 默认，**显示控件**，占布局位置
//        2. **View.INVISIBLE** 👉 看不见，**仍然占用原来位置**（空白占位）
//        3. **View.GONE** 👉 看不见，**不占布局空间**（直接从布局树移除占位）
        view_giveup.setVisibility(View.INVISIBLE);
        view_giveup.setBackgroundResource(R.drawable.retain);

        ConstraintSet constraintSet = new ConstraintSet();
        // 克隆当前布局已有的约束
        constraintSet.clone(this);

        constraintSet.constrainWidth(view_add.getId(), ConstraintSet.WRAP_CONTENT);
//        constraintSet.constrainHeight(view_add.getId(), ConstraintSet.WRAP_CONTENT);
        constraintSet.constrainPercentHeight(view_add.getId(), 0.2f);
        view_add.setSingleLine(true); // 不能换行
        view_add.setGravity(Gravity.CENTER); // 上下左右居中
        view_add.setTextColor(Color.WHITE);// 设置文字颜色 huang色

        constraintSet.constrainPercentHeight(view_giveup.getId(), 0.3f);
        constraintSet.constrainPercentWidth(view_giveup.getId(), 0.2f);

        // ========= 设置四边约束：子view的上下左右 绑定父布局的上下左右 =========
        constraintSet.connect(view_card.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_card.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(view_card.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(view_card.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);

//        constraintSet.connect(view_add.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_add.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        constraintSet.connect(view_add.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(view_add.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.setVerticalBias(view_add.getId(), 0.2f); // 置顶-0.2

        constraintSet.connect(view_giveup.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        constraintSet.connect(view_giveup.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        // 应用约束生效
        constraintSet.applyTo(this);

//        view_card.setPadding(30,0,30,0);
        view_card.setTextSize(36); // sp
//        setBackgroundResource(R.drawable.tag_bg); //你可以自己准备圆角drawable，也可以注释掉

        LinearLayout.LayoutParams params;
        if(childNum >= 4){
            // width=0dp ！weight=1 均分宽度；height包裹内容
            params = new LinearLayout.LayoutParams(
                    0,
                    ViewGroup.LayoutParams.MATCH_PARENT, // 高度拉满
                    1   //权重 weight=1
            );
        }
        else{
            params = new LinearLayout.LayoutParams(
                    width,  //showlast_card_width(5),
                    ViewGroup.LayoutParams.MATCH_PARENT // 高度拉满
            );
        }

        // 判断：不是第一个子元素，左边加间隙，实现标签中间缝隙
        if(childNum > 0){
            int gapDp = 5; //标签之间缝隙dp
            float density = getResources().getDisplayMetrics().density;
            //dp转px
            params.leftMargin = (int)(gapDp * density);
        }
        this.setLayoutParams(params);

        view_card.setSingleLine(true); // 不能换行
        view_card.setTextColor(Color.WHITE);// 设置文字颜色 白色

        view_card.setGravity(Gravity.CENTER); // 上下左右居中
//        this.settext(itemstr); // 文字 和 颜色  转移到手牌布局调用了

//        add_effects(increment()); // 额外抽牌检测
//        {
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setShape(GradientDrawable.RECTANGLE);
//            drawable.setColor(ContextCompat.getColor(context, R.color.transparent));
//            drawable.setStroke(
//                    3,
//                    ContextCompat.getColor(context, R.color.blue)
//            );
//            this.setBackground(drawable);
//        }
    }

    public boolean is_special(){ // 判断是否为特殊卡牌
        return is_special(this.gettext());
    }
    public static boolean is_special(String str){ // 判断是否为特殊卡牌
        if(square.equals(str)) return true;
        else if(triangle.equals(str)) return true;
        else return false;
    }
    public boolean is_number(){ // 判断是否为普通牌或数字卡牌
        return is_number(this.gettext());
//        try{
//            Integer.parseInt(this.gettext().substring(0, this.gettext().length() - 1));
//            return true;
//            // 是否能转数值
//        }catch (NumberFormatException e){
//            return false;
//        }
    }
    public static boolean is_number(String str){ // 判断是否为普通牌或数字卡牌
        if(str == null || str.isEmpty()) return false;
        try{
            Integer.parseInt(str.substring(0, str.length() - 1));
            return true;
            // 是否能转数值
        }catch (NumberFormatException e){
            return false;
        }
    }
    public int get_number(){ // 获取数值
        return get_number(this.gettext());
//        try{
//            return Integer.parseInt(this.gettext().substring(0, this.gettext().length() - 1));
//            // 是否能转数值
//        }catch (NumberFormatException e){
//            return -1;
//        }
    }
    public static int get_number(String str){ // 获取数值
        if(str == null || str.isEmpty()) return -1;
        try{
            return Integer.parseInt(str.substring(0, str.length() - 1));
            // 是否能转数值
        }catch (NumberFormatException e){
            return -1;
        }
    }
    public char type_action(){ // 获取花色，获取类型
//        String str = view_card.getText().toString();
        return type_action(gettext());
    }
    public static char type_action(String str){
        if(str == null || str.isEmpty() || Card.is_special(str)) return '0';
        return str.charAt(str.length() - 1);
    }

//    public boolean effects; // 特效
    public void special_effects(boolean bool){ // 偏移特效
        if(bool){
            this.setTranslationY(-20);
        }
        else{
            this.setTranslationY(0);
        }
    }
//    public boolean add_effects_bool;
    public void add_effects(int i){ // 额外抽牌特效
//        add_effects_bool = bool;
        if(i > 0){
            view_card.setTextColor(Color.BLACK);
            view_add.setText("+" + i);
        }
        else{
            view_card.setTextColor(Color.WHITE);
            view_add.setText("");
        }
    }


//    public boolean checkture_hit(){ // 检测这张牌否可以打出
//        return checkture_hit(this.gettext(), show_last_card);
//    }
//    public static boolean checkture_hit(String str_0, String str_1){ // 检测这张牌否可以打出
//        if(triangle.equals(str_0)) return false;
//        if(str_0.isEmpty()) return false;
//        if(str_1.isEmpty()) return true; // str.length() == 0
//
//        if(square.equals(str_0) || square.equals(str_1)) return true; // 为方块
//
//        int num = get_number(str_1), mun0 = get_number(str_0);
//        if(num == -1 || mun0 == -1) return false;
//
//        if(type_action(str_1) == type_action(str_0))
//            return true; // 类型相同
//        else if( mun0 >= num - 1 && mun0 <= num + 1)
//            return true; // 差值1
//
//        return false;
//    }

    public boolean increment(){ // 检测打出这张牌是否可以额外抽牌
        return increment(this.gettext(), show_last_card);
    }
    public static boolean increment(String str_0, String str_1){ // 检测打出这张牌是否可以额外抽牌
        if(str_1.isEmpty() || str_0.isEmpty()) return false; // str.length() == 0

        if(is_special(str_0) || is_special(str_1)) return false;

        int num = get_number(str_1), mun0 = get_number(str_0);
        if(num == -1 || mun0 == -1) return false;

        if(num == mun0) return true; // 数字相同
        else if(type_action(str_1) == type_action(str_0) && mun0 >= num - 1 && mun0 <= num + 1)
            return true; // 类型相同 且 差值1

        return false;
    }

    public String color; // 颜色（不包含透明度）
    public String alpha; // 透明度 分为可以打出，不可以打出，高亮提示
    public static final String alpha_can_hit = "dd";
    public static final String alpha_cant_hit = "44";
    public static final String alpha_prompt = "ff";
    public static final String color_x = "dbe604"; // 黄 FFFFFF00
    public static final String color_s = "2288FF"; // 蓝
    public static final String color_h = "FF0000"; // 红
    public static final String color_d = "9C27B0"; // 紫
    public static final String color_c = "00FF00"; // 绿
    public static final String color_gray = "CCCCCC"; // 灰  FF888888 FF444444
    public static String get_color(char type){
        switch(type){
            case 'x':
                return color_x;
            case 's':
                return color_s;
            case 'd':
                return color_d;
            case 'c':
                return color_c;
            case 'h':
                return color_h;
            default: return color_gray;
        }
    }
//    //方式3：设置GradientDrawable动态渐变背景
//    GradientDrawable gd = new GradientDrawable();
//gd.setColors(new int[]{0xFF111111,0xFF444444});
//gd.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
//rootLayout.setBackground(gd);
    public static void set_color(TextView view, String cl, String ap){ // 设置一张牌的背景颜色
        view.setBackgroundColor(Color.parseColor("#" + ap + cl));
    }
    public static void set_color(TextView view, String color){ // 设置一张牌的背景颜色
        view.setBackgroundColor(Color.parseColor("#" + color));
    }
    public static void set_color(TextView view, int color){ // 设置一张牌的背景颜色
        view.setBackgroundColor(ContextCompat.getColor(view.getContext(), color));
    }
    public void set_color_begin(boolean bool){ // 根据是否能点击自动设置一张牌的背景颜色透明 初始化
//        if(checkture_hit())
        if(bool)
            alpha = alpha_can_hit;
        else
            alpha = alpha_cant_hit;
        color = get_color(type_action());
        set_color(view_card, get_color(type_action()), alpha);
    }

    public String gettext(){
        return view_card.getText().toString();
    }
    public void settext(String str){ // 设置或者修改牌
        view_card.setText(str);
//        this.set_color();
    }

    // 每次新增手牌，需要对该新增的单独对上一张进行 打出 额外抽牌 检测（在这个类）
    // 每次更新上一张手牌，需要对所有手牌进行 打出 额外抽牌 检测（在手牌的类进行循环）
}
