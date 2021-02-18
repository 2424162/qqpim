package com.example.qqpim;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class UrlHook implements IXposedHookLoadPackage{
    public String tag = "qqsave";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        Log.i(tag, "Package name: " + loadPackageParam.packageName);

        if(!loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")){
            Log.d(tag,""+loadPackageParam.packageName);
            return ;
        }

        //new Throwable().printStackTrace();
        // 过防止调用loadClass加载 de.robv.android.xposed.
//        XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                //Log.d(tag,""+param.args[0].toString());
//                if (param.args != null && param.args[0] != null && param.args[0].toString().startsWith("de.robv.android.xposed.")) {
//                    // 改成一个不存在的类
//                    param.args[0] = "de.crobv.android.xposed.ThTest";
//                    // 调整了一下，听说这样改更好，直接改部分手机有未知影响。
//                    //Log.d(tag,"找到loadclass里的xopsed");
//                    param.setThrowable(new ClassNotFoundException("not found"));
//                }
//                super.beforeHookedMethod(param);
//            }
//        });
//
//        XposedHelpers.findAndHookMethod(StackTraceElement.class, "getClassName", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                String result = (String) param.getResult();
//                //Log.d(tag,"检测堆栈"+result);
//                if (result != null) {
//                    if (result.contains("de.robv.android.xposed.")) {
//                        // Log.d(tag,"getClassname里的异常");
//                        param.setResult("");
//                    } else if (result.contains("com.android.internal.os.ZygoteInit")) {
//                        param.setResult("");
//                    }
//                }
//                super.afterHookedMethod(param);
//            }
//        });
//
//        XposedHelpers.findAndHookMethod(Modifier.class, "isNative", int.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                //Log.d(tag,"判断是否Native");
//                String modify = "ffj";
//                //Log.d(tag,"对错："+param.args[0].toString());
//                param.args[0] = modify;
//                super.beforeHookedMethod(param);
//            }
//        });
//        XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                Class<?> cls =(Class<?>)param.getResult();
//                if(cls.getName().contains("HttpURLConnectionImpl")){
//                    Log.i(tag,cls.getName());
//                }
//            }
//        });

        // com.android.okhttp.internal.huc.HttpURLConnectionImpl
        // com.android.okhttp.internal.http.HttpURLConnectionImpl
//        XposedHelpers.findAndHookConstructor("java.net.URL", loadPackageParam.classLoader, String.class, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                Log.d(tag,""+param.args[0]);
//                new Throwable().printStackTrace();
//                param.args[0] ="www.bilibili.com";
//                Log.d(tag,"改完======");
//             }
//        });
        XposedHelpers.findAndHookConstructor("java.net.URL", loadPackageParam.classLoader, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,""+param.thisObject.toString());

            }
        });

        XposedHelpers.findAndHookConstructor("ghi", loadPackageParam.classLoader, String.class, String.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"==========4");
                param.args[0] = "text/xml";
                Log.d(tag,param.args[0].toString());
                Log.d(tag,param.args[1].toString());
                Log.d(tag,param.args[2].toString());
                Log.d(tag,param.args[3].toString());
                Log.d(tag,"==========4");


            }
        });

        XposedHelpers.findAndHookMethod("com.sogou.networking.callback.CommonRecord$Builder", loadPackageParam.classLoader, "getRealUrl", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag, "_________________");
                Log.d(tag,""+param.getResult().toString());

            }
        });

        XposedHelpers.findAndHookMethod("com.sogou.networking.callback.CommonRecord$Builder", loadPackageParam.classLoader, "getHttpClient", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag, "_________________");
                Log.d(tag,param.thisObject.toString());
                Log.d(tag,""+param.getResult().toString());
            }
        });

        XposedHelpers.findAndHookMethod("java.net.URLConnection", loadPackageParam.classLoader, "setRequestProperty", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.thisObject.toString());
                String string = (String)param.args[1];
                if(string.contains("text/p")){
                    Log.d(tag,param.thisObject.toString());
                    new Throwable().printStackTrace();
                }
            }
        });
        XposedHelpers.findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", loadPackageParam.classLoader, "getOutputStream", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object  obj = param.thisObject;
                Log.d(tag,"对象："+obj.toString());
                Log.d(tag,"========start");
                new Throwable().printStackTrace();
                Log.d(tag,"========end");
            }
        });
    }
}
