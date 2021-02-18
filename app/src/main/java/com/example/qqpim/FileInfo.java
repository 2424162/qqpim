package com.example.qqpim;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FileInfo extends XC_LoadPackage {
    public String tag = "baiduinfo";
    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.baidu.input")) {
            return;
        }
        Log.d(tag, loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod("java.lang.StringBuffer", loadPackageParam.classLoader, "append", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"---"+param.args[0]);
            }
        });
        XposedHelpers.findAndHookMethod("com.sijla.f.a", loadPackageParam.classLoader, "b", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"=============i");
                Log.d(tag,param.getResult().toString());



            }
        });

    }
}
