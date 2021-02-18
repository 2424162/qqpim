package com.example.qqpim;

import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import static de.robv.android.xposed.XposedBridge.hookAllConstructors;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;

public class HttpurlHook1 implements IXposedHookLoadPackage{
    public String tag = "httpurl";
    public String stringtmp = "";
    public String path = "/sdcard/DouyinFile/";
    public String filename = "log.txt";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        Log.d(tag,loadPackageParam.packageName);
        if(!loadPackageParam.packageName.equals("com.ss.android.ugc.aweme")){
            return ;
        }
        final Class<?> httpurlconnection = findClass("java.net.HttpURLConnection",loadPackageParam.classLoader);
        hookAllConstructors(httpurlconnection, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if(param.args.length>0) {

                    //Log.d(tag, param.args[0].toString());
                }
            }
        });

        XC_MethodHook ResponseHook = new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                HttpURLConnection urlConn = (HttpURLConnection) param.thisObject;
                findAndHookMethod("java.io.OutputStream", loadPackageParam.classLoader, "write", byte[].class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        OutputStream os = (OutputStream)param.thisObject;
                        //Log.d(tag,os.toString());
                        if(!os.toString().contains("internal.http"))
                            return;
                        String print = new String((byte[]) param.args[0]);
                        stringtmp = print;



                        Pattern pt = Pattern.compile("(\\w+.*)");

                        Matcher match = pt.matcher(print);
                        if(match.matches())
                        {
                            Log.d(tag,"Array转String = "+print);
                        }
                    }
                });
                findAndHookMethod("java.io.OutputStream", loadPackageParam.classLoader, "write", byte[].class,int.class,int.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        OutputStream os = (OutputStream)param.thisObject;
                        Log.d(tag,param.getResult().toString());
                        if(!os.toString().contains("internal.http"))
                            return;
                        String print = new String((byte[]) param.args[0]);
                        //TEST.log("DATA"+print.toString());
                        Pattern pt = Pattern.compile("(\\w+=.*)");
                        Matcher match = pt.matcher(print);
                        if(match.matches())
                        {
                            Log.d(tag,"Array转String = "+print);
                        }
                    }
                });
            }
        };
//        try {
//            findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", loadPackageParam.classLoader, "getInputStream", ResponseHook);
//        }catch(Exception ex){}

        try {
            findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", loadPackageParam.classLoader, "getOutputStream", ResponseHook);
        }catch(Exception ex){

        }



    }
    private void FileWriteData(String url){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String t=format.format(new Date());

            FileWriter writer = new FileWriter(path+format.toPattern()+filename,true);

            writer.write(t +" "+url);
            writer.write(t +" "+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
