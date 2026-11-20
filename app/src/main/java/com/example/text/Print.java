package com.example.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.function.Supplier;

public class Print {
    private final Supplier<Game> get_main;
    public Print(Supplier<Game> game_main){
        get_main = game_main;
        Game main = get_main.get();

        this.btnendround = main.findViewById(R.id.btn_end_round);

        this.turn_view = main.findViewById(R.id.turn_view);

        this.head_card = main.findViewById(R.id.head_card);
        this.head_card.setText("手牌\n0");
        this.abandon_card = main.findViewById(R.id.abandon_card);
        this.abandon_card.setText("弃牌\n0");
        this.library_card = main.findViewById(R.id.library_card);
        this.library_card.setText("牌库\n" + main.tagContainer.list.size());
        this.text_combo = main.findViewById(R.id.text_combo);
        this.text_combo.setText("COMBO: 0");

        this.play_skill_number = main.findViewById(R.id.view_play_skill_number);
        this.progress_play_skill = main.findViewById(R.id.progress_play_skill);
        progress_play_skill.setMax(play_technique_max);
        this.text_number_set("play_technique" ,this.play_technique);

        this.enemy_skill_number = main.findViewById(R.id.view_enemy_skill_number);
        this.progress_enemy_skill = main.findViewById(R.id.progress_enemy_skill);
        progress_enemy_skill.setMax(enemy_technique_max);
        this.text_number_set("enemy_technique" ,this.enemy_technique);

        this.play_hp_number = main.findViewById(R.id.text_hp);
        this.progress_play_hp = main.findViewById(R.id.progress_bar);
        progress_play_hp.setMax(play_hp_max);
        this.text_number_set("play_hp" ,this.play_hp);

        this.enemy_hp_number = main.findViewById(R.id.enemy_text_hp);
        this.progress_enemy_hp = main.findViewById(R.id.enemy_progress_bar);
        progress_enemy_hp.setMax(enemy_hp_max);
        this.text_number_set("enemy_hp" ,this.enemy_hp);

        play_att_view = main.findViewById(R.id.text_att);
        enemy_att_view = main.findViewById(R.id.enemy_text_att);
    }
//    public Print(Runnable task){
//        task.run();
//    }

    private final TextView turn_view; // 轮次文字框
    private final TextView head_card; // 手牌
    private final TextView text_combo; // combo
    private final TextView abandon_card; //弃牌堆
    private final TextView library_card; // 牌库
    private final TextView enemy_att_view; // 敌人攻击力文字框 0 / 1
    private final TextView play_att_view; // 玩家攻击力文字框 0 / 1
    private final TextView play_skill_number; // 玩家技力文字框 50 / 0
    private final ProgressBar progress_play_skill;// 玩家技力条
    private final TextView enemy_skill_number; // 敌人技力文字框 50 / 0
    private final ProgressBar progress_enemy_skill;// 敌人技力条
    private final TextView play_hp_number; // 玩家血量文字框 50 / 50
    private final ProgressBar progress_play_hp; // 玩家血量条
    private final TextView enemy_hp_number; // 敌人血量文字框 50 / 50
    private final ProgressBar progress_enemy_hp; // 敌人血量条


    public int turn = 1; // 轮次
    public int combo = 0;
    public final Button btnendround; // 结束回合按钮

    public int enemy_att_sum = 0; // 敌人累计伤害值
    public int enemy_att = 5; // 敌人攻击力
    public int play_att_sum = 0; // 玩家累计伤害值
    public int play_att = 5; // 玩家攻击力

    public int play_technique_max = 50; // 玩家技力上限
    public int play_technique = 0; // 玩家技力
    public int enemy_technique_max = 50; // 敌人技力上限
    public int enemy_technique = 0; // 敌人技力
    public int play_hp_max = 200; // 玩家血量上限
    public int play_hp = play_hp_max; // 玩家血量
    public int enemy_hp_max = 200; // 敌人血量上限
    public int enemy_hp = enemy_hp_max; // 敌人血量

//<string name="num_text">数值：%d</string>
//            2. Java 代码调用
//    String content = getString(R.string.num_text, play_technique);
//    textView.setText(content);
    // 使用set不会顾虑上下限，只有使用add才会计算后限制上下限
    @SuppressLint("SetTextI18n")
    public void text_number_setmax(String str, int number){ // 设置最大值 敌我技力 玩家血量 敌我基础攻击力
        if ("play_technique".equals(str)) {
            play_technique_max = number;
            play_skill_number.setText(number + " / " + play_technique);
            progress_play_skill.setMax(number);
            get_main.get().sk_use_show_refresh_all(); // 每次修改技力时，刷新所有技能的显示
            progress_play_skill.setProgress(play_technique);
        } else if ("enemy_technique".equals(str)) {
            enemy_technique_max = number;
            enemy_skill_number.setText(number + " / " + enemy_technique);
            progress_enemy_skill.setMax(number);
            progress_enemy_skill.setProgress(enemy_technique);
        } else if ("play_hp".equals(str)) {
            play_hp_max = number;
//            if(play_hp > play_hp_max) play_hp = play_hp_max;
            play_hp_number.setText(number + " / " + play_hp);
            progress_play_hp.setMax(number);
            progress_play_hp.setProgress(number);
        } else if ("enemy_hp".equals(str)) {
            enemy_hp_max = number;
//            if(enemy_hp > enemy_hp_max) enemy_hp = enemy_hp_max;
            enemy_hp_number.setText("敌人 hp: " + number + " / " + enemy_hp);
            progress_enemy_hp.setMax(number);
            progress_enemy_hp.setProgress(number);
        } else if ("play_att".equals(str)) {
            play_att = number;
            play_att_view.setText(play_att_sum + " / " + play_att);
        } else if ("enemy_att".equals(str)) {
            enemy_att = number;
            enemy_att_view.setText(enemy_att_sum + " / " + enemy_att);
        }
    }
    @SuppressLint("SetTextI18n")
    public void text_number_set(String str, int number){ // 手牌 COMBO 弃牌 牌库 敌我技力 敌我血量 敌我累计伤害
//        String str;
        if ("head_card".equals(str)) {
            head_card.setText("手牌：" + number);
        } else if ("combo".equals(str)) {
            text_combo.setText("COMBO: " + number);
            combo = number;
        } else if ("abandon".equals(str)) {
            abandon_card.setText("弃牌：" + number);
        } else if ("library".equals(str)) {
            library_card.setText("牌库：" + number);
        } else if ("turn".equals(str)) {
            turn_view.setText("Turn: " + number);
            turn = number;
        } else if ("btnendround".equals(str)) {
            btnendround.setText("结束回合\nMP+" + number);
        } else if ("play_technique".equals(str)) {
            play_technique = number;
            play_skill_number.setText(play_technique_max + " / " + number);
            progress_play_skill.setProgress(number);
            get_main.get().sk_use_show_refresh_all(); // 每次修改技力时，刷新所有技能的显示
        } else if ("enemy_technique".equals(str)) {
            enemy_technique = number;
            enemy_skill_number.setText(enemy_technique_max + " / " + number);
            progress_enemy_skill.setProgress(number);
        } else if ("play_hp".equals(str)) {
            play_hp = number;
            play_hp_number.setText(play_hp_max + " / " + number);
            progress_play_hp.setProgress(number);
        } else if ("enemy_hp".equals(str)) {
            enemy_hp = number;
            enemy_hp_number.setText("敌人 hp: " + enemy_hp_max + " / " + number);
            progress_enemy_hp.setProgress(number);
        } else if ("play_att".equals(str)) {
            play_att_sum = number;
            play_att_view.setText(play_att_sum + " / " + play_att);
        } else if ("enemy_att".equals(str)) {
            enemy_att_sum = number;
            enemy_att_view.setText(enemy_att_sum + " / " + enemy_att);
        }
    }
    public void text_number_add(String str, int number){ // 手牌 COMBO 弃牌 牌库 敌我技力 敌我血量 敌我累计伤害
        int i = 0;
        TextView View0 = null;
        if ("head_card".equals(str)) {
            i = 3;
            View0 = head_card;
        } else if ("combo".equals(str)) {
            text_number_set(str, combo + number);
        } else if ("abandon".equals(str)) {
            i = 3;
            View0 = abandon_card;
        } else if ("library".equals(str)) {
            i = 3;
            View0 = library_card;
        } else if ("turn".equals(str)) {
            text_number_set(str, turn + number);
        } else if ("btnendround".equals(str)) {
            i = 8;
            View0 = btnendround;
        } else if ("play_technique".equals(str)) { // 增加减少技力不能超上下限
            if(number > 0){
                if(play_technique + number <= play_technique_max) number += play_technique;
                else if(play_technique < play_technique_max) number = play_technique_max;
                else return ;
            }
            else if(number < 0){
                if(play_technique + number >= 0) number += play_technique;
                else if(play_technique > 0) number = 0;
                else return ;
            }
            else number = play_technique;
            text_number_set(str, number);
        } else if ("enemy_technique".equals(str)) {// 增加减少技力不能超上下限
            if(number > 0){
                if(enemy_technique + number <= enemy_technique_max) number += enemy_technique;
                else if(enemy_technique < enemy_technique_max) number = enemy_technique_max;
                else return ;
            }
            else if(number < 0){
                if(enemy_technique + number >= 0) number += enemy_technique;
                else if(enemy_technique > 0) number = 0;
                else return ;
            }
            else number = enemy_technique;
            text_number_set(str, number);
        } else if ("play_hp".equals(str)) {
            if(number > 0){
                if(play_hp + number <= play_hp_max) number += play_hp;
                else if(play_hp < play_hp_max) number = play_hp_max;
                else return ;
            }
            else number += play_hp;
            text_number_set(str, number);
        } else if ("enemy_hp".equals(str)) {
            if(number > 0){
                if(enemy_hp + number <= enemy_hp_max) number += enemy_hp;
                else if(enemy_hp < enemy_hp_max) number = enemy_hp_max;
                else return ;
            }
            else number += enemy_hp;
            text_number_set(str, number);
        } else if ("play_att".equals(str)) {
            number += play_att_sum;
            if(number < 0) number = 0;
            text_number_set(str, number);
        } else if ("enemy_att".equals(str)) {
            number += enemy_att_sum;
            if(number < 0) number = 0;
            text_number_set(str, number);
        }
        if(View0 == null) return;

        try{
            String str1 = View0.getText().toString().substring(i);
            int num = Integer.parseInt(str1);
            num += number;
            text_number_set(str, num);
        }catch (NumberFormatException e){
//        e.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    public void text_number_refresh(String str){ // 手牌数 COMBO 弃牌 牌库 敌我技力 敌我血量 敌我累计伤害
        if ("head_card".equals(str)) {
            head_card.setText("手牌：" + get_main.get().tagContainer.getChildCount());
        } else if ("combo".equals(str)) {
            text_combo.setText("COMBO: " + combo);
        } else if ("abandon".equals(str)) {
            abandon_card.setText("弃牌：" + get_main.get().tagContainer.abandon_card_string.size());
        } else if ("library".equals(str)) {
            library_card.setText("牌库：" + get_main.get().tagContainer.list.size());
        } else if ("turn".equals(str)) {
            turn_view.setText("Turn: " + turn);
        } else if ("btnendround".equals(str)) {
            btnendround.setText("结束回合\nMP+" + get_main.get().tagContainer.getChildCount());
        } else if ("play_technique".equals(str)) {
            play_skill_number.setText(play_technique_max + " / " + play_technique);
            progress_play_skill.setProgress(play_technique);
        } else if ("enemy_technique".equals(str)) {
            enemy_skill_number.setText(enemy_technique_max + " / " + enemy_technique);
            progress_enemy_skill.setProgress(enemy_technique);
            get_main.get().sk_use_show_refresh_all(); // 每次修改技力时，刷新所有技能的显示
        } else if ("play_hp".equals(str)) {
            play_hp_number.setText(play_hp_max + " / " + play_hp);
            progress_play_hp.setProgress(play_hp);
        } else if ("enemy_hp".equals(str)) {
            enemy_hp_number.setText("敌人 hp: " + enemy_hp_max + " / " + enemy_hp);
            progress_enemy_hp.setProgress(enemy_hp);
        } else if ("play_att".equals(str)) {
            play_att_view.setText(play_att_sum + " / " + play_att);
        } else if ("enemy_att".equals(str)) {
            enemy_att_view.setText(enemy_att_sum + " / " + enemy_att);
        }
    }
}
