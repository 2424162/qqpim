package com.example.qqpim;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONObject;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
public class WuganZhuabao implements IXposedHookLoadPackage{
    public String tag = "wuganzhuabao";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {

        Log.i(tag, "Package name: " + loadPackageParam.packageName);

        if(!loadPackageParam.packageName.equals("com.example.yyds")){
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
        Log.d(tag, "_________________");
        // com.android.okhttp.internal.huc.HttpURLConnectionImpl
        // com.android.okhttp.internal.http.HttpURLConnectionImpl
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

        Log.d(tag,"file");
        XposedHelpers.findAndHookMethod("java.io.File", loadPackageParam.classLoader, "createNewFile", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"创建file"+param.getResult().toString());
            }
        });
        //
        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.iwmh", loadPackageParam.classLoader, "a", JSONObject.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                Log.d(tag,"*******进入函数");
                Log.d(tag,"结果"+param.getResult().toString());
                Log.d(tag,"参数"+param.args[0].toString());
                Log.d(tag,"********");


            }
        });

        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.iwmh", loadPackageParam.classLoader, "a", JSONObject.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Log.d(tag,"*******进入函数");
                Log.d(tag,"结果"+param.getResult().toString());
                Log.d(tag,"参数"+param.args[0].toString());
                Log.d(tag,"********");


            }
        });


        //de.a方法
        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.ed", loadPackageParam.classLoader, "a", JSONObject.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                Log.d(tag,"********ed.a方法");

                Log.d(tag,"arg1 ="+param.args[0].toString());
                Log.d(tag, "arg2 = " + param.args[1].toString());
                Log.d(tag,"ed result = "+param.getResult().toString());
                Log.d(tag,"********");

                new Throwable().printStackTrace();
            }
        });

//        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.iwmh", loadPackageParam.classLoader, "a", Context.class, String.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//
//                Log.d(tag,"********");
//
//                Log.d(tag,"参数1"+param.args[0].toString());
//                Log.d(tag,"参数2"+param.args[1].toString());
//                Log.d(tag,"返回值"+param.getResult().toString());
//                Log.d(tag,"********");
//            }
//        });
        //qqq刚刚进入函数


        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.yzxk.yzxk", loadPackageParam.classLoader, "b", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.getResult().toString());
            }
        });
        Log.d(tag,"jsonobject");
        XposedHelpers.findAndHookMethod("org.json.JSONObject", loadPackageParam.classLoader, "put", String.class,Object.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.args[0].toString());
                Log.d(tag,param.getResult().toString());
            }
        });
        JSONObject obj = new JSONObject();
        JSONArray obj2 = new JSONArray();
        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.yzxk.w", loadPackageParam.classLoader, "b", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"hahaha");
                Log.d(tag,param.args[0].toString());
            }
        });

        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.sbuq", loadPackageParam.classLoader, "b", String.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"url==============");
                Log.d(tag,param.args[0].toString());
                Log.d(tag,param.args[1].toString());
                Log.d(tag,param.args[2].toString());
                Log.d(tag,param.getResult().toString());
                Log.d(tag,"++++++++");
            }
        });
        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.yzxk.w", loadPackageParam.classLoader, "a", String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"hahaha");
                Log.d(tag,param.args[0].toString());
            }
        });
        XposedHelpers.findAndHookConstructor("dpsx.ojy.rmdws.yzxk.w", loadPackageParam.classLoader, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"w类初始化====");
                Log.d(tag,param.thisObject.toString());
            }
        });

        XposedHelpers.findAndHookMethod("org.json.JSONArray", loadPackageParam.classLoader, "put",Object.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.args[0].toString());
                Log.d(tag,param.getResult().toString());

            }
        });

        XposedHelpers.findAndHookMethod("org.json.JSONObject", loadPackageParam.classLoader, "putOpt", String.class,Object.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.args[0].toString());
                Log.d(tag,param.getResult().toString());
            }
        });

        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.eigi.ykf", loadPackageParam.classLoader, "a", InputStream.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"网络接收到的信息=================");
                Log.d(tag,param.args[0].toString());
                Log.d(tag,"返回:"+param.getResult().toString());
                Log.d(tag,"=========");
            }
        });
        XposedHelpers.findAndHookMethod("java.io.File", loadPackageParam.classLoader, "renameTo", File.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                param.setResult(false);
                Log.d(tag,param.thisObject.toString());
                new Throwable().printStackTrace();
                Log.d(tag,param.args[0].toString());
                Log.d(tag,"修改返回");
            }
        });

        Class<?> w = XposedHelpers.findClass("dpsx.ojy.rmdws.yzxk.w",loadPackageParam.classLoader);
        Log.d(tag,"w加载");
        Class<?> i = XposedHelpers.findClass("dpsx.ojy.rmdws.w.i",loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod("dpsx.ojy.rmdws.vqnq.eigi", loadPackageParam.classLoader, "b", Context.class, w, int.class, i, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"url字符串");
            }
        });
        Class<?> eigi = XposedHelpers.findClass("dpsx.ojy.rmdws.vqnq.eigi",loadPackageParam.classLoader);
       // eigi.getMethod("")

    }
}
