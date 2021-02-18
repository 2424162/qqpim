package com.example.qqpim;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class FileHook implements IXposedHookLoadPackage {
    public String tag = "baiduinput";
    @Override
//    FileInputStream fileInputStream;
//
//    {
//        try {
//            fileInputStream = new FileInputStream("haha");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //com.baidu.input
        if(!loadPackageParam.packageName.equals("com.baidu.input")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        //File file = new File("");

        XposedHelpers.findAndHookConstructor("java.io.File", loadPackageParam.classLoader, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if(param.args[0].toString().contains("com.baidu.input")){
                    Log.d(tag,param.args[0].toString());
                    if (param.args[0].toString().contains("ted/0/Android/data/com.baidu.input")){
                        new Throwable().printStackTrace();
                    }
                }
                else {
                Log.d(tag,"File对象"+param.args[0].toString());
                }
            }
        });
        XposedHelpers.findAndHookMethod("java.io.File", loadPackageParam.classLoader, "delete", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"删除文件"+param.getResult().toString()+" "+param.args[0].toString());
            }
        });
        XposedHelpers.findAndHookMethod("java.io.FileInputStream", loadPackageParam.classLoader, "read", byte[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"arg1 = "+param.args[0].toString());
            }
        });

        XposedHelpers.findAndHookConstructor("dalvik.system.DexClassLoader", loadPackageParam.classLoader, String.class, String.class, String.class, ClassLoader.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Log.d(tag,"dexpath = "+param.args[0].toString());
                Log.d(tag,"optimized"+param.args[1].toString());
                Log.d(tag,"librarySearch"+param.args[2].toString());
            }
        });
    }

}
