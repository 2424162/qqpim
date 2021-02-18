package com.example.qqpim;

import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.MediaType;

import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class HttpHook implements IXposedHookLoadPackage {
    public static String tag = "HttpHook";
    public String stringtmp = "";
    public String bodytmp = "";
    public String path = "/sdcard/DouYinFile/";
    public String filename = "log.txt";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals("com.ss.android.ugc.aweme")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        for(int i = 0;i<10;i++){
            //FileWriteData(i+"----------------");
        }
        //createFolder("sdcard/DouYinFile2");
        //createFile("sdcard/DouYinFile2");

        initHooking(loadPackageParam);


    }
    public void createFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.d(tag,"创建文件夹"+file.getName());
    }
    private void createFile(String path) {
        File mFile = new File(path+"/"+filename);
        if (mFile.exists()) {
            mFile.delete();
        }
        try {
            mFile.createNewFile();
            Log.d(tag,"文件创建完成=====");
            Log.d(tag,mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public  void initHooking(XC_LoadPackage.LoadPackageParam lpparam) throws NoSuchMethodException {
        final Class<?> httpUrlConnection = findClass("java.net.HttpURLConnection", lpparam.classLoader);



        hookAllConstructors(httpUrlConnection, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                if (param.args.length != 1 || param.args[0].getClass() != URL.class)
                    return;

                Log.d(tag, "HttpURLConnection: " + param.args[0] + "");
                if(!stringtmp.equals( param.args[0].toString())){
                    XposedBridge.log("HttpURLConnection: " + param.args[0] + "");
//                    Log.d(tag,"httpURL写文件");
//                    FileWriteData("HttpURLConnection: " + param.args[0] + "");
                    stringtmp = param.args[0].toString();
                }

            }
        });



        findAndHookMethod("java.io.OutputStream", lpparam.classLoader, "write", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                OutputStream os = (OutputStream) param.thisObject;
                if (!os.toString().contains("internal.http"))
                    return;
                String print = new String((byte[]) param.args[0]);
                Log.d(tag, "DATA" + print.toString());
                XposedBridge.log("DATA: "+print);
                if (!bodytmp.equals(print)){
                    XposedBridge.log("DATA"+print);
                    //FileWriteData("DATA"+print);
                    bodytmp = print;
                }
                Pattern pt = Pattern.compile("(\\w+=.*)");
                Matcher match = pt.matcher(print);
                if (match.matches()) {
                    Log.d(tag, "POST DATA: " + print.toString());
                    XposedBridge.log("POST DATA"+print);

                }

            }
        });
        findAndHookMethod("java.io.OutputStream", lpparam.classLoader, "write", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                OutputStream os = (OutputStream) param.thisObject;
                if (!os.toString().contains("internal.http"))
                    return;
                String print = new String((byte[]) param.args[0]);
                Log.d(tag, "DATA: " + print.toString());
                XposedBridge.log("DATA: "+print);
                if (!bodytmp.equals(print)){
                  //  FileWriteData("DATA"+print);
                    bodytmp = print;
                }
                Pattern pt = Pattern.compile("(\\w+=.*)");
                Matcher match = pt.matcher(print);
                if (match.matches()) {
                    Log.d(tag, "POST DATA: " + print.toString());
                    XposedBridge.log("POST DATA"+print);
                }
            }
        });


        Class<?> class1 = XposedHelpers.findClass("okhttp3.OkHttpClient", lpparam.classLoader);
        XposedBridge.hookAllMethods(class1, "newCall", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                if (param.args.length > 0) {
                    Log.d(tag, "Request =  " + param.args[0].toString());
                    XposedBridge.log("Request =  " + param.args[0].toString());
                   // FileWriteData("Request =  " + param.args[0].toString());


                }
            }
        });
        Class<?> formurl = XposedHelpers.findClass("com.bytedance.retrofit2.mime.FormUrlEncodedTypedOutput",lpparam.classLoader);

        XposedBridge.hookAllMethods(formurl, "addField", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if(param.args.length>3) {
                    String string1 = param.args[0].toString();
                    String string2 = param.args[2].toString();

                    Log.d(tag, string1+"="+string2);

                }
            }
        });


    }

    public void FileWriteData(String url){
        Log.d(tag,"开始写");
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String t=format.format(new Date());


            FileWriter writer = new FileWriter("/sdcard/DouYinFile2/log.txt",true);
            Log.d(tag,"写文件"+writer.toString());

            writer.write(t +" "+url);
            writer.write(t +" "+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
