package com.example.qqpim;

import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static java.lang.Class.forName;

public class HttpUrlHook extends XC_LoadPackage {
    public String tag = "HttpHook";
    public String path = "/sdcard/DouyinFile/";
    public String filename = "log.txt";
    public Class<?> mRequest = null;


    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.ss.android.ugc.aweme")) {
            Log.d(tag, loadPackageParam.packageName);
            return;
        }
        Log.d(tag, loadPackageParam.packageName);


//        XposedHelpers.findAndHookConstructor("okhttp3.Request", loadPackageParam.classLoader, builder,new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                String url = (String)param.args[0];
//                Log.d(tag,"url = "+url);
//
//            }
//        });
//        XposedHelpers.findAndHookMethod("okhttp3.OkHttpClient", loadPackageParam.classLoader, "newCall", Request.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                Log.d(tag,param.args[0].toString());
//                Request object = (Request)param.getResult();
//                RequestBody requestBody = object.body();
//                Log.d(tag,"contentype = "+requestBody.contentType().toString());
//                Log.d(tag,requestBody.toString());
//
//                //Log.d(tag,param.getResult().toString());
//            }
//        });

        Class<?> builder = XposedHelpers.findClass("okhttp3.Request$Builder", loadPackageParam.classLoader);
//
//        XposedHelpers.findAndHookMethod(builder, "build", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//
//                Request request = (Request)(param.getResult());
//
//                Log.d(tag,request.toString());
//            }
//        });

//        XposedBridge.hookAllMethods(builder, "post", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                if (param.args.length > 0) {
//                    Log.d(tag, "post内容 = " + param.args[0].toString());
//                }
//            }
//        });


       // XposedHelpers.findClass("com.bytedance.frameworks.baselib.network.http.c.a.e$a")





//        Class<?> requestBody = XposedHelpers.findClass("okhttp3.RequestBody",loadPackageParam.classLoader);
//        XposedBridge.hookAllMethods(requestBody, "create", new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                String arg1 = param.args[0].toString();
//                //String arg2 = new String((byte[]) param.args[1]);
//                Log.d(tag,arg1 +" = ");
//            }
//        });
        Class<?> class1 = XposedHelpers.findClass("okhttp3.OkHttpClient", loadPackageParam.classLoader);
        XposedBridge.hookAllMethods(class1, "newCall", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (param.args.length > 0) {
                    Log.d(tag, "Request =  " + param.args[0].toString());
                    FileWriteData(param.args[0].toString());

                }
            }
        });


//        Method[] methods = class1.getDeclaredMethods();
//        for(Method m:methods){
//            Log.d(tag,"函数名 = "+m.getName());
//            if(m.getName().equals("newCall")){
//                Class<?>[] classes1 = m.getParameterTypes();
//                for (Class<?> class2:classes1){
//                    Log.d(tag,"参数 = "+class2.getName());
//                }
//            }
//        }
        XposedHelpers.findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", loadPackageParam.classLoader, "getOutputStream", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object obj = param.thisObject;
                Log.d(tag + "url", "Request = " + obj.toString() + param.args[0].toString());
                FileWriteData(obj.toString());

            }
        });


    }


    private void FileWriteData(String url) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String t = format.format(new Date());

            FileWriter writer = new FileWriter(path + filename, true);

            writer.write(t + " " + url);
            writer.write(t + " " + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}