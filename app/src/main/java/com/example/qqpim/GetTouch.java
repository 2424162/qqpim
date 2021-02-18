package com.example.qqpim;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class GetTouch implements IXposedHookLoadPackage {
    public String tag = "kuaishouhaha";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
//        if (!loadPackageParam.processName.equals("com.baidu.input")) {
//            Log.d(tag, "过滤程序：" + loadPackageParam.processName.toString());
//            //return;
//        }
        Log.d(tag,loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod("android.view.ViewGroup", loadPackageParam.classLoader, "onInterceptTouchEvent", MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                //Log.d(tag,"onInterceptTouch类："+param.getResult().toString()+param.thisObject.toString());
            }
        });
        XposedHelpers.findAndHookMethod("android.view.View.OnTouchListener", loadPackageParam.classLoader, "onTouch", View.class, MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (param.getResult().toString()=="true")

                Log.d(tag+"clickview",param.args[0].toString());
            }
        });

        XposedHelpers.findAndHookMethod("android.view.View", loadPackageParam.classLoader, "onTouchEvent", MotionEvent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (param.getResult().toString() =="true") {
                    Log.d(tag, "ontouch类：" + param.getResult().toString() + "--" + param.thisObject.toString());
                    View view = (View)param.thisObject;

                    Log.d(tag,view.getId()+"");
                }
            }
        });
        XposedHelpers.findAndHookConstructor("java.io.File", loadPackageParam.classLoader, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if(param.args[0].toString().contains("com.baidu.input")){
                    Log.d(tag,param.args[0].toString());
                    new Throwable().printStackTrace();
                }
                else {
                    Log.d(tag,"File对象"+param.args[0].toString());
                }
            }
        });
//,,,,,,,,,
        XposedHelpers.findAndHookMethod("android.media.AudioRecord", loadPackageParam.classLoader, "startRecording", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag+"rec","开始录音1");
            }
        });
        XposedHelpers.findAndHookMethod("android.media.MediaRecorder", loadPackageParam.classLoader,"start", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag+"rec","开始录音2");
            }
        });

    }
}
