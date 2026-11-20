package com.example.text;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

import android.util.Log;
public class BgmService extends Service {
    public static float volume_bgm = 0.5f;
    private MediaPlayer bgmPlayer;

    //在BgmService里面增加SoundPool
    public static float volume_sound = 0.75f;
    private SoundPool soundPool;
    private int ok2; // 按钮点击 链接完成
    private int close; // 返回  取消技能选择
    private int equip; // 装技能
    private int unequip; // 卸技能
    private int touch; //(原meun) 选择 选牌的左移右移
    private int game_start; //游戏开始点击按钮音效  第1，3章打出场地卡牌的音效
    private int turnEnd;   // 回合结束
    private int turnStart; // 回合开始
    private int combo;     // 打牌
    private int draw;      // 额外抽牌
    private int drawSpecial; // 特殊额外抽牌
    private int ok;        // 技能选择
    private int skill;     // 技能完成
    private int lose;     // 游戏败北
    private int skill_activation;     // 对方使用技能
    private int skill_available;     // (对方技能准备就绪)
    private int achievement;     // (原本是成就音效)
    private int critical; // 可继续打牌
    private int element;     // 第一章的交叉刻印  进入冻结 mode_10获取元素
    private int tension;     // 有电
    private int no_battery;     // 没电
    private int rule_alert;     // 违反规则
    private int cyan_normal;     // 进入正面
    private int cyan_reverse;     // 进入反面
    private int thunder;     // 雷击
    private int interrupt;     // 第三方下牌
    private int purify;        // 同调
    private int full_synchro;     // 完全同调
    private int charm;        // 生成爱心
    private int send_charm;     // 打出爱心
    private int firewall;     // 元素爆发
    // 服务第一次创建，只执行一次
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化背景音乐
        bgmPlayer = MediaPlayer.create(this, R.raw.menu);
        if(bgmPlayer != null){
            // 【准备完成监听】配合prepareAsync使用
            bgmPlayer.setOnPreparedListener(mp -> {
                // 准备就绪，开始播放
                if(img)
                    bgmPlayer.start();
            });
            bgmPlayer.setVolume(volume_bgm,volume_bgm); //设置音量0~1
            bgmPlayer.setLooping(true);
            bgmPlayer.setOnErrorListener((m, what, extra) -> {
                Log.e("MP","error what="+what+" extra="+extra);
                return true;
            });
        }

        AudioAttributes attr = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(8) //最多8个音效同时播放
                .setAudioAttributes(attr)
                .build();
        //预加载点击音效，只加载一次
        ok2 = soundPool.load(this, R.raw.ok2,1);
        close = soundPool.load(this, R.raw.close,1);
        equip = soundPool.load(this, R.raw.equip,1);
        unequip = soundPool.load(this, R.raw.unequip,1);
        touch = soundPool.load(this, R.raw.touch,1);
        game_start = soundPool.load(this, R.raw.game_start,1);

        turnEnd = soundPool.load(this, R.raw.turn_end,1);
        turnStart = soundPool.load(this, R.raw.turn_start,1);
        combo = soundPool.load(this, R.raw.combo,1);
        draw = soundPool.load(this, R.raw.draw,1);
        drawSpecial = soundPool.load(this, R.raw.draw_special,1);
        ok = soundPool.load(this, R.raw.ok,1);
        skill = soundPool.load(this, R.raw.skill,1);
        lose = soundPool.load(this, R.raw.lose,1);
        skill_activation = soundPool.load(this, R.raw.skill_activation,1);
        skill_available = soundPool.load(this, R.raw.skill_available,1);
        achievement = soundPool.load(this, R.raw.achievement,1);
        critical = soundPool.load(this, R.raw.critical,1);

        element = soundPool.load(this, R.raw.element,1);
        tension = soundPool.load(this, R.raw.tension,1);
        no_battery = soundPool.load(this, R.raw.no_battery,1);
        rule_alert = soundPool.load(this, R.raw.rule_alert,1);
        cyan_normal = soundPool.load(this, R.raw.cyan_normal,1);
        cyan_reverse = soundPool.load(this, R.raw.cyan_reverse,1);
        thunder = soundPool.load(this, R.raw.thunder,1);
        interrupt = soundPool.load(this, R.raw.interrupt,1);
        purify = soundPool.load(this, R.raw.purify,1);
        full_synchro = soundPool.load(this, R.raw.full_synchro,1);
        charm = soundPool.load(this, R.raw.charm,1);
        send_charm = soundPool.load(this, R.raw.send_charm,1);
        firewall = soundPool.load(this, R.raw.firewall,1);

    }


    //播放音效方法
//    6. **第 6 个 rate 播放速率 `1f`**
//    范围 **0.5 ~ 2.0**
//            - `1.0f`：原速播放（你当前配置）
//            - <1：变慢；>1：加速，同时音调变化。
    private void playClickSound(String str){ // SOUND 界面音效
        if(soundPool == null) return;
        if("ok2".equals(str)){
            soundPool.play(ok2,volume_sound,volume_sound,1,0,1f);
        }
        else if("close".equals(str)){
            soundPool.play(close,volume_sound,volume_sound,1,0,1f);
        }
        else if("equip".equals(str)){
            soundPool.play(equip,volume_sound,volume_sound,1,0,1f);
        }
        else if("unequip".equals(str)){
            soundPool.play(unequip,volume_sound,volume_sound,1,0,1f);
        }
        else if("touch".equals(str)){
            soundPool.play(touch,volume_sound,volume_sound,1,0,1f);
        }
        else if("game_start".equals(str)){
            soundPool.play(game_start,volume_sound,volume_sound,1,0,1f);
        }

    }
    private void gameClickSound(String str){ // NORMAL 通用游戏音效
        if(soundPool == null) return;
        if("turnEnd".equals(str)){
            soundPool.play(turnEnd,volume_sound,volume_sound,1,0,1f);
        }
        else if("turnStart".equals(str)){
            soundPool.play(turnStart,volume_sound,volume_sound,1,0,1f);
        }
        else if("combo".equals(str)){
            soundPool.play(combo,volume_sound,volume_sound,1,0,1f);
        }
        else if("draw".equals(str)){
            soundPool.play(draw,volume_sound,volume_sound,1,0,1f);
        }
        else if("drawSpecial".equals(str)){
            soundPool.play(drawSpecial,volume_sound,volume_sound,1,0,1f);
        }
        else if("ok".equals(str)){
            soundPool.play(ok,volume_sound,volume_sound,1,0,1f);
        }
        else if("skill".equals(str)){
            soundPool.play(skill,volume_sound,volume_sound,1,0,1f);
        }
        else if("lose".equals(str)){
            soundPool.play(lose,volume_sound,volume_sound,1,0,1f);
        }
        else if("skill_activation".equals(str)){
            soundPool.play(skill_activation,volume_sound,volume_sound,1,0,1f);
        }
        else if("skill_available".equals(str)){
            soundPool.play(skill_available,volume_sound,volume_sound,1,0,1f);
        }
        else if("achievement".equals(str)){
            soundPool.play(achievement,volume_sound,volume_sound,1,0,1f);
        }
        else if("critical".equals(str)){
            soundPool.play(critical,volume_sound,volume_sound,1,0,1f);
        }

    }
    private void specialClickSound(String str){ // SPECIAL 不通用游戏音效
        if(soundPool == null) return;
        if("element".equals(str)){
            soundPool.play(element,volume_sound,volume_sound,1,0,1f);
        }
        else if("tension".equals(str)){
            soundPool.play(tension,volume_sound,volume_sound,1,0,1f);
        }
        else if("no_battery".equals(str)){
            soundPool.play(no_battery,volume_sound,volume_sound,1,0,1f);
        }
        else if("rule_alert".equals(str)){ // 音量太小，额外提升音量
            soundPool.play(rule_alert,0.9f + volume_sound * 0.1f,0.9f + volume_sound * 0.1f,1,0,1f);
        }
        else if("cyan_normal".equals(str)){
            soundPool.play(cyan_normal,volume_sound,volume_sound,1,0,1f);
        }
        else if("cyan_reverse".equals(str)){
            soundPool.play(cyan_reverse,volume_sound,volume_sound,1,0,1f);
        }
        else if("thunder".equals(str)){
            soundPool.play(thunder,volume_sound,volume_sound,1,0,1f);
        }
        else if("interrupt".equals(str)){
            soundPool.play(interrupt,volume_sound,volume_sound,1,0,1f);
        }
        else if("purify".equals(str)){
            soundPool.play(purify,volume_sound,volume_sound,1,0,1f);
        }
        else if("full_synchro".equals(str)){
            soundPool.play(full_synchro,volume_sound,volume_sound,1,0,1f);
        }
        else if("charm".equals(str)){
            soundPool.play(charm,volume_sound,volume_sound,1,0,1f);
        }
        else if("send_charm".equals(str)){
            soundPool.play(send_charm,volume_sound,volume_sound,1,0,1f);
        }
        else if("firewall".equals(str)){
            soundPool.play(firewall,volume_sound,volume_sound,1,0,1f);
        }

    }


    private boolean img = true;
    private boolean Second = false;
    // 核心：每次startService都会触发onStartCommand，在这里接收Intent指令
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent == null){
            return START_NOT_STICKY;
        }
        //取出指令标记
        String cmd = intent.getStringExtra("CMD");
        if("CHANGE".equals(cmd)){
            int musicId = intent.getIntExtra("MUSIC_ID",0);
            int second_musicId = intent.getIntExtra("SECOND_MUSIC_ID",0);
//            Log.d("BGM_scid", String.valueOf(second_musicId));
            changeMusic(musicId, second_musicId);
        }else if("SOUND".equals(cmd)){
            playClickSound(intent.getStringExtra("SE"));
        }else if("NORMAL".equals(cmd)){
            gameClickSound(intent.getStringExtra("SE"));
        }else if("SPECIAL".equals(cmd)){
            specialClickSound(intent.getStringExtra("SE"));
        }else if("SWITCH".equals(cmd)){
            img = !img;
            if(img)
                playBgm();
            else
                pauseBgm();
        }else if("NEXT".equals(cmd)){
//            Log.d("BGM", "Second");
            Second = true;
        }else if("PLAY".equals(cmd)){
            playBgm();
        }else if("PAUSE".equals(cmd)){
            pauseBgm();
        }else if("STOP".equals(cmd)){
            stopBgm();
            //停止服务，触发onDestroy释放资源
            stopSelf();
        }
        // START_NOT_STICKY：系统杀掉服务后，不会自动重启
        return START_NOT_STICKY;
    }

    private void changeMusic(int musicId, int second_musicId) {
        Second = false;
        if(musicId == 0){
            return;
        }
        if(bgmPlayer == null){
            bgmPlayer = MediaPlayer.create(this, R.raw.menu);
            // 【准备完成监听】配合prepareAsync使用
            bgmPlayer.setOnPreparedListener(mp -> { // 由 prepareAsync 触发
                // 准备就绪，开始播放
                if(img)
                    bgmPlayer.start();
            });
            bgmPlayer.setVolume(volume_bgm,volume_bgm); //设置音量0~1
            bgmPlayer.setOnErrorListener((m, what, extra) -> {
                Log.e("MP","error what="+what+" extra="+extra);
                return true;
            });
        }

        try {
            bgmPlayer.reset();
            //初始化音频
            bgmPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+musicId));
            if(second_musicId != 0){
                bgmPlayer.setLooping(false); // false=播放一次自动结束
                bgmPlayer.setOnCompletionListener(mp -> {
                    if(bgmPlayer == null)
                        return;
                    if(!Second){
                        bgmPlayer.seekTo(0);
                        bgmPlayer.start();
                        return;
                    }
                    bgmPlayer.reset();
                    try {
                        //初始化音频
                        bgmPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+second_musicId));
                        bgmPlayer.setLooping(true); // false=播放一次自动结束
                        bgmPlayer.prepareAsync(); // 仅限刚reset才能使用
//                        bgmPlayer.prepare();
//                        bgmPlayer.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                        bgmPlayer.release();
                        bgmPlayer = null;
                    }
                    bgmPlayer.setLooping(true);
                });
            }
            else
                bgmPlayer.setLooping(true); // false=播放一次自动结束

            bgmPlayer.prepareAsync();
//            bgmPlayer.prepare();
//            if(img)
//                bgmPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            bgmPlayer.release();
            bgmPlayer = null;
        }
//        if(musicId == 0){
//            return;
//        }
//        if(bgmPlayer == null){
//            bgmPlayer = MediaPlayer.create(this, R.raw.menu);
//            bgmPlayer.setVolume(volume_bgm,volume_bgm); //设置音量0~1
//            bgmPlayer.setLooping(true);
//        }
//        else {
//            bgmPlayer.reset();
//        }
//        try {
//            //初始化音频
//            bgmPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+musicId));
//            bgmPlayer.prepare();
//            if(second_musicId != 0){
//                bgmPlayer.setLooping(false); // false=播放一次自动结束
//                bgmPlayer.setOnCompletionListener(mp -> {
//                    bgmPlayer.reset();
//                    try {
//                        //初始化音频
//                        bgmPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+second_musicId));
//                        bgmPlayer.prepare();
//                        bgmPlayer.setLooping(true); // false=播放一次自动结束
//                        bgmPlayer.start();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                        bgmPlayer.release();
//                        bgmPlayer = null;
//                    }
//                    bgmPlayer.setLooping(true);
//                });
//            }
//            else
//                bgmPlayer.setLooping(true); // false=播放一次自动结束
//            if(img)
//                bgmPlayer.start();
////            main_Player.setOnCompletionListener(m -> mp.release());
//        } catch (IOException e) {
//            e.printStackTrace();
//            bgmPlayer.release();
//            bgmPlayer = null;
//        }
    }

//        main_Player = MediaPlayer.create(this, R.raw.menu);
//
//        //播放完成监听：播放结束自动释放
//        main_Player.setOnCompletionListener(mp -> {
//            mp.release();
//        });
//        main_Player.start();
//        main_Player.pause();

//        //暂停
//        if(mPlayer.isPlaying()){
//            mPlayer.pause();
//        }
//        // 重头播放
//        mediaPlayer.seekTo(0);
//        mediaPlayer.start();

    //切换音频步骤
//        mediaPlayer.reset();
//        mediaPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/raw/music2"));
//        mediaPlayer.prepare();
//        mediaPlayer.start();
    private void playBgm(){
        if(bgmPlayer != null && !bgmPlayer.isPlaying()){
            img = true;
            bgmPlayer.start();
//            bgmPlayer.prepareAsync();
        }
    }

    private void pauseBgm(){
        if(bgmPlayer != null && bgmPlayer.isPlaying()){
            img = false;
            bgmPlayer.pause();
        }
    }

    private void stopBgm(){
        if(bgmPlayer != null){
            img = false;
            bgmPlayer.stop();
        }
    }
    //服务销毁，释放播放器
    //Service销毁时统一释放
    @Override
    public void onDestroy() {
        //释放音效池
        if(soundPool != null){
            soundPool.release();
            soundPool = null;
        }
        //释放背景音乐MediaPlayer
        if(bgmPlayer != null){
            bgmPlayer.setOnPreparedListener(null);
            bgmPlayer.release();
            bgmPlayer=null;
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}