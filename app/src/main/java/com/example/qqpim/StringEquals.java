package com.example.qqpim;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;


import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class StringEquals extends XC_LoadPackage {
    public String tag = "StringEquals";
    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
//        if(!loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")){
//
//        }
        Log.d(tag,loadPackageParam.packageName);

        XposedHelpers.findAndHookMethod("java.lang.String", loadPackageParam.classLoader, "equals", Object.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(tag,"equals===========");
                Log.d(tag,param.args[0].toString());
                if(param.args[0].equals("com.tencent.mobileqq")){
                    new Throwable().printStackTrace();
                }

            }
        });


    }
}
