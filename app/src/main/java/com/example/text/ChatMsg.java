package com.example.text;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatMsg{
    // 回合数
    // 牌库 弃牌库
    // 上一张牌
    // 血量 mp （手牌数 连击数）
    // 累计伤害数 攻击力
    // （场地技能使用情况 场地信息）
    // （四个技能cd以及是否使用）
    // 使用了什么技能 / 刷新上一张牌
    // 是否是回合结束 / 是否双方都回合结束->打伤害->游戏是否结束->个人回合结束->个人回合开始->决定谁先开始

    // 每次打牌，使用技能，回合结束发一次信息
    // 每回合首次有一个人抽牌后，也要发一次信息给另一方抽牌
    // 若以上行为有触发场地效果，应在场地效果触发后再发信息

    public int enemy_att_sum; // 敌人累计伤害值
    public int enemy_att; // 敌人攻击力
    public int play_att_sum; // 玩家累计伤害值
    public int play_att; // 玩家攻击力
    public String show_last_card; // 上一张打的牌
    public int turn; // 轮次
//    public int combo;
    public ArrayList<String> abandon_card_string; //弃牌堆
    public ArrayList<String> list; // 牌库
    public int play_technique_max; // 玩家技力上限
    public int play_technique; // 玩家技力
    public int play_hp_max; // 玩家血量上限
    public int play_hp; // 玩家血量
    public int enemy_technique_max; // 玩家技力上限
    public int enemy_technique; // 玩家技力
    public int enemy_hp_max; // 敌人血量上限
    public int enemy_hp; // 敌人血量

    public int game_state; // 游戏状态
    public boolean ondownload = false;
    public int msgSeq = -1; // 0~2 -1表示例外不能用
    public Map<String,Object> memory = new HashMap<>();
    public ChatMsg(int i){
        ondownload = false;
        game_state = i;
    }
    public ChatMsg(Game main){
        this(main, main.game_state);
    }
    public ChatMsg(Game main, int i){
        show_last_card = Card.show_last_card;
        ondownload = true;

        abandon_card_string = new ArrayList<>(main.tagContainer.abandon_card_string);
        list = new ArrayList<>(main.tagContainer.list);
        // 浅拷贝，如果之后替换为自定义类修改修改初始赋值代码
        game_state = i;

        Print layout_show = main.layout_show;
        turn = layout_show.turn; // 轮次
//        combo = main.layout_show.combo; //
        play_technique_max = layout_show.play_technique_max; // 玩家技力上限
        play_technique = layout_show.play_technique; //   玩家技力
        play_hp_max = layout_show.play_hp_max; // 玩家血量上限
        play_hp = layout_show.play_hp; // 玩家血量
        enemy_technique_max = layout_show.enemy_technique_max; // 敌人技力上限
        enemy_technique = layout_show.enemy_technique; //   敌人技力
        enemy_hp_max = layout_show.enemy_hp_max; // 敌人血量上限
        enemy_hp = layout_show.enemy_hp; // 敌人血量

        enemy_att_sum =  layout_show.enemy_att_sum; // 敌人累计伤害值
        enemy_att =  layout_show.enemy_att; // 敌人攻击力
        play_att_sum =  layout_show.play_att_sum; // 玩家累计伤害值
        play_att =  layout_show.play_att; // 玩家攻击力


    }
    public String skills_show_text;
    public ChatMsg(Game main, int i, String text){
        this(main, i);
        skills_show_text = text;
    }
//    public String content;
//    public int msgType; // 0=普通文本消息
//    public long time;

//    public ChatMsg(String content, int msgType, long time) {
//        this.content = content;
//        this.msgType = msgType;
//        this.time = time;
//    }
}
