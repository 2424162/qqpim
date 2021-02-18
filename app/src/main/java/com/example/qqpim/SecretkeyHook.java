package com.example.qqpim;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SecretkeyHook extends XC_LoadPackage {
    public String tag = "secretkeyhook";
    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals("com.baidu.input")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod("com.sijla.f.a", loadPackageParam.classLoader, "a", Context.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"===============================");
                Log.d(tag,"第一个string = "+param.args[1].toString());
                Log.d(tag,"第二个string = "+param.args[2].toString());
            }
        });

        XposedHelpers.findAndHookConstructor("javax.crypto.spec.SecretKeySpec", loadPackageParam.classLoader, byte[].class,String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                byte[] bytes = (byte[])param.args[0];
                String string = new String(bytes,"utf-8");
                Log.d(tag,string);

            }
        });
        XposedHelpers.findAndHookMethod("com.sijla.d.b", loadPackageParam.classLoader, "a", String.class, byte[].class, byte[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"------私钥");
                Log.d(tag,param.args[0].toString());
                byte[] bytes1 = (byte[])param.args[1];
                byte[] bytes2 = (byte[])param.args[2];
                String string1 = new String(bytes1,"utf-8");
                String string2= new String(bytes2,"utf-8");
                Log.d(tag,string1+"-------"+string2);

            }
        });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getDeviceId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"IMEI : "+param.getResult().toString());
                new Throwable().printStackTrace();
            }
        });

        XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", loadPackageParam.classLoader, "getSubscriberId", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"IMSI : "+param.getResult().toString());
                new Throwable().printStackTrace();
            }
        });
        XposedHelpers.findAndHookMethod("javax.crypto.Cipher", loadPackageParam.classLoader, "doFinal", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                byte[] bytes = (byte[])param.args[0];
                String sting = new String(bytes,"utf-8");
                Log.d(tag,"原文+   " +sting);
            }
        });
    }
}
