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

public class BaiduMutipart extends XC_LoadPackage {
    public String tag = "baidumuti";
    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals("com.baidu.input")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);

//        Class<?> a = XposedHelpers.findClass("com.baidu.fwr$a",loadPackageParam.classLoader);
//        XposedHelpers.findAndHookConstructor("com.baidu.fwr", loadPackageParam.classLoader, a, new XC_MethodHook() {
//            @Override
//            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                super.beforeHookedMethod(param);
//                Object object = param.thisObject;
//                Field[] fields = object.getClass().getDeclaredFields();
//                for (int j = 0;j<fields.length;j++){
//                    fields[j].setAccessible(true);
//                    if(fields[j].getName()=="b") {
//                        Log.d(tag, "公共的=========");
//                        Log.d(tag, fields[j].getName() + " =3 " + fields[j].get(object));
//                    }
//                }
//
//            }
//        });
        XposedHelpers.findAndHookMethod("com.baidu.eha", loadPackageParam.classLoader, "j", String.class, String.class, byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(tag,"j===========");
                Log.d(tag,param.args[0].toString());
                Log.d(tag,param.args[1].toString());
                XposedHelpers.findAndHookConstructor("java.io.FileInputStream", loadPackageParam.classLoader, File.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(tag,"arg1 = "+param.args[0].toString());
                        new Throwable().printStackTrace();
                    }
                });
            }
        });
        XposedHelpers.findAndHookMethod("com.baidu.aqp", loadPackageParam.classLoader,"a", InputStream.class, File.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"写数据+"+param.args[1].toString()+"-----"+param.getResult().toString()+"-----"+param.args[0].toString());
            }
        });


        XposedHelpers.findAndHookMethod("com.baidu.eha", loadPackageParam.classLoader, "i", String.class, String.class, byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.d(tag,"i===========");
                Log.d(tag,param.args[0].toString());
                Log.d(tag,param.args[1].toString());
                XposedHelpers.findAndHookConstructor("java.io.FileInputStream", loadPackageParam.classLoader, File.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Log.d(tag,"arg1 = "+param.args[0].toString());
                        new Throwable().printStackTrace();

                    }
                });


            }
        });
        Class<?> class1 = XposedHelpers.findClass("okhttp3.MediaType",loadPackageParam.classLoader);
        XposedHelpers.findAndHookMethod("okhttp3.RequestBody", loadPackageParam.classLoader, "create", class1, File.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"-----------");
                Log.d(tag,param.thisObject.toString());

            }
        });

        XposedHelpers.findAndHookMethod("com.baidu.a.a.a.a$a$a", loadPackageParam.classLoader, "hX", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"build=======");
                Object object  = (Object)param.getResult();

                Log.d(tag,param.getResult().toString());
            }
        });



        XposedHelpers.findAndHookMethod("java.io.File", loadPackageParam.classLoader,"createNewFile", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,param.thisObject.toString());

                if(param.thisObject.toString().contains("com.baidu.input/files/logcatrrcl")){
                    Log.d(tag,"创建文件栈==============");

                    new Throwable().printStackTrace();
                    XposedHelpers.findAndHookMethod("java.io.FileOutputStream", loadPackageParam.classLoader, "write", byte[].class,int.class,int.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Log.d(tag,"写入文件============");
                            new Throwable().printStackTrace();
                            FileOutputStream fileOutputStream = (FileOutputStream)param.getResult();
                        }
                    });


                }

            }
        });



        Class<?> fwr = XposedHelpers.findClass("com.baidu.fwr",loadPackageParam.classLoader);

        XposedHelpers.findAndHookMethod("com.baidu.fwl", loadPackageParam.classLoader, "f", Closeable.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"closeable = "+param.args[0].toString());

            }
        });


    }
}
