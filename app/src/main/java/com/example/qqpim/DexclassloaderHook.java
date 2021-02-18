package com.example.qqpim;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class DexclassloaderHook implements IXposedHookLoadPackage {
    public String tag = "baiduinput";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //com.baidu.input
        if(!loadPackageParam.packageName.equals("cn.lily.phone.cleaner")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        XposedHelpers.findAndHookConstructor("dalvik.system.DexClassLoader", loadPackageParam.classLoader, String.class, String.class, String.class, ClassLoader.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Log.d(tag+"dex","dexpath = "+param.args[0].toString());
                Log.d(tag,"optimized"+param.args[1].toString());
                Log.d(tag,"librarySearch"+param.args[2].toString());
            }
        });

        XposedHelpers.findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", loadPackageParam.classLoader, "getOutputStream", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object  obj = param.thisObject;
                Log.d(tag+"url","对象："+obj.toString());
                Log.d(tag,"========start");
                new Throwable().printStackTrace();
                Log.d(tag,"========end");
            }
        });
    }

}
