package com.example.qqpim;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FileTest extends XC_LoadPackage {
        public String tag = "baidufile";
        @Override
        public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
            if(!loadPackageParam.packageName.equals("com.baidu.input")){
                return ;
            }
            Log.d(tag,loadPackageParam.packageName);

            XposedHelpers.findAndHookMethod("com.sijla.g.j", loadPackageParam.classLoader, "f", Context.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Log.d(tag,param.getResult().toString());
                }
            });

            XposedHelpers.findAndHookMethod("com.sijla.g.a", loadPackageParam.classLoader, "b", String.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.d(tag,"=="+param.args[0].toString());
                    Log.d(tag,"=="+param.getResult().toString());
                }
            });
        }
}
