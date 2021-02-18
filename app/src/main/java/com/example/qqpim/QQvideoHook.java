package com.example.qqpim;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class QQvideoHook implements IXposedHookLoadPackage {
    public String tag = "qqvideo";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //com.baidu.input
        if(!loadPackageParam.packageName.equals("com.tencent.qqlive")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod("com.tencent.tmdownloader.sdkdownload.logreport.AppInstallReportReceiver", loadPackageParam.classLoader, "onReceive", Context.class, Intent.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.args[1].toString());
                new Throwable().printStackTrace();
            }
        });
    }

}
