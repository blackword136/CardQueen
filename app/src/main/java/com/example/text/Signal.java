package com.example.text;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

// 采用类似py的写法
public class Signal { // 信号触发器
    public static class Slot {// 槽函数/信号
        public Consumer<Map<String,Object>> invoke = null;
        public int sequence = 1000;
        public Slot(Consumer<Map<String,Object>> consumer){
            invoke = consumer;
        }
        public Slot(Consumer<Map<String,Object>> consumer, int sequence){
            invoke = consumer;
            this.sequence = sequence;
        }
//        int i = 0;
    }
    public final List<Slot> slotList = new ArrayList<>(); // 存储匿名方法


    public void connect(Slot slot) {
        for(int i=0;i<slotList.size();i++){
            if(slotList.get(i).sequence >= slot.sequence){ // 还是只能转链表吗
                slotList.add(i , slot);
                return;
            }
        }
        slotList.add(slot);
    }

    public void disconnect(Slot slot) {
        slotList.remove(slot);
    }

    public void disconnectAll() {
        slotList.clear();
    }

    // 直接调用（DirectConnection，当前线程执行）
    public void emitDirect(Map<String,Object> data) {
        List<Slot> tempList = new ArrayList<>(slotList); // 复制新引用，若原指向在其中消失时，可以不影响指向
        for (Slot slot : tempList) {
            if(slot.invoke != null)
                slot.invoke.accept(data);
        }
    }

//    private final Handler mainHandler = new Handler(Looper.getMainLooper());
//    // 抛到主线程执行（QueuedConnection，子线程emit，UI更新用）
//    public void emitUi(Map<String,Object> data) {
//        List<Slot> tempList = new ArrayList<>(slotList);
//        mainHandler.post(() -> {
//            for (Slot slot : tempList) {
//                slot.invoke(data);
//            }
//        });
//    }
}
