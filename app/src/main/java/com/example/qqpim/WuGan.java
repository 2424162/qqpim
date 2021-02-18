package com.example.qqpim;
import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class WuGan implements IXposedHookLoadPackage {
    public String tag = "yydsyyhook";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        //Log.i(tag, "Package name: " + loadPackageParam.packageName);

        if(!loadPackageParam.packageName.equals("com.example.yyds")){
            //Log.d(tag,""+loadPackageParam.packageName);
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        Class<?> w = XposedHelpers.findClass("dpsx.ojy.rmdws.yzxk.w",loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.yua", loadPackageParam.classLoader, "a", Context.class, String.class,w, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.getResult().toString());
            }
        });
        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.yua", loadPackageParam.classLoader, "a", Context.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.getResult().toString());
            }
        });
        Log.d(tag,"file");
        XposedHelpers.findAndHookMethod("java.io.File", loadPackageParam.classLoader, "createNewFile", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"创建file"+param.getResult().toString());
            }
        });
        XposedHelpers.findAndHookMethod("java.io.File", loadPackageParam.classLoader, "renameTo", boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(false);
                Log.d(tag,"修改返回");
            }
        });
    }

}
