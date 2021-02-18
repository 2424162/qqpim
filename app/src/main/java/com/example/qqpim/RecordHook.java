package com.example.qqpim;

import android.media.MediaSyncEvent;
import android.text.format.Time;
import android.util.Log;



import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class RecordHook extends XC_LoadPackage {
    public String tag = "baiduinput";

    //&!oadPackageParam.packageName!="com.smile.gifmaker")&!oadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")&!oadPackageParam.packageName.equals("com.snda.wifilocating")&!oadPackageParam.packageName.equals("com.ss.android.ugc.aweme")
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals( "com.baidu.input")& (!loadPackageParam.packageName.equals("com.smile.gifmaker"))& !loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")&!loadPackageParam.packageName.equals("com.snda.wifilocating")&!loadPackageParam.packageName.equals("com.ss.android.ugc.aweme"))
        {
            Log.d(tag,loadPackageParam.packageName.equals("com.baidu.input")+"");
            Log.d(tag,"过滤"+loadPackageParam.packageName);
            return;
        }

        //XposedBridge.log(loadPackageParam.packageName);
        Log.d(tag, loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod("android.media.AudioRecord", loadPackageParam.classLoader, "startRecording", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Time t = new Time("GMT+8");
                t.setToNow();
                int hour = t.hour;
                int minute = t.minute;
                int second = t.second;
                Log.d(tag, "开始录音1 ---" + hour + ":" + minute + ":" + second);
                XposedBridge.log("开始录音1 ----" + hour + ":" + minute + ":" + second);
            }
        });
        XposedHelpers.findAndHookMethod("android.media.AudioRecord", loadPackageParam.classLoader, "startRecording", MediaSyncEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Time t = new Time("GMT+8");
                t.setToNow();
                int hour = t.hour;
                int minute = t.minute;
                int second = t.second;
                Log.d(tag, "开始录音11--- " + hour + ":" + minute + ":" + second);
                XposedBridge.log("开始录音11----" + hour + ":" + minute + ":" + second);
            }
        });
        XposedHelpers.findAndHookMethod("android.media.AudioRecord", loadPackageParam.classLoader, "stop", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Time t = new Time("GMT+8");
                t.setToNow();
                int hour = t.hour;
                int minute = t.minute;
                int second = t.second;
                Log.d(tag, "停止录音1---" + hour + ":" + minute + ":" + second);
                XposedBridge.log("停止录音1---" + hour + ":" + minute + ":" + second);
            }
        });
        XposedHelpers.findAndHookMethod("android.media.MediaRecorder", loadPackageParam.classLoader, "start", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Time t = new Time("GMT+8");
                t.setToNow();
                int hour = t.hour;
                int minute = t.minute;
                int second = t.second;
                Log.d(tag, "开始录音2---" + hour + ":" + minute + ":" + second);
                XposedBridge.log("开始录音2---" + hour + ":" + minute + ":" + second);
            }
        });
        XposedHelpers.findAndHookMethod("android.media.MediaRecorder", loadPackageParam.classLoader, "stop", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Time t = new Time("GMT+8");
                t.setToNow();
                int hour = t.hour;
                int minute = t.minute;
                int second = t.second;
                Log.d(tag, "结束录音2---" + hour + ":" + minute + ":" + second);
                XposedBridge.log("结束录音2---" + hour + ":" + minute + ":" + second);
            }
        });


    }
}
