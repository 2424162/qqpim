package com.example.qqpim;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class UploadFile extends XC_LoadPackage {
    public String tag = "baiduinput";

    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.baidu.input")) {
            return;
        }
        Log.d(tag, loadPackageParam.packageName);


        XposedHelpers.findAndHookMethod("com.sijla.f.a", loadPackageParam.classLoader, "a", Context.class, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"===============================");
                Log.d(tag,"第一个string = "+param.args[1].toString());
                Log.d(tag,"第二个string = "+param.args[2].toString());
            }
        });

        XposedHelpers.findAndHookMethod("java.io.OutputStream", loadPackageParam.classLoader, "write", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                File file = (File)param.thisObject;
                Log.d(tag,"文件数据写入========");
                Log.d(tag,param.args[1].toString());
                Log.d(tag,file.getAbsolutePath());
                new Throwable().printStackTrace();

            }
        });

        XposedHelpers.findAndHookMethod("java.io.File", loadPackageParam.classLoader, "createNewFile", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                File file = (File)param.thisObject;
                String name  = file.getName();
                String name2 = file.getAbsolutePath();
                Log.d(tag,"---------"+file.toString());
                if(name.contains("qt.csv.")){
                    Log.d(tag,name.toString());
                }
                if(name2.contains("qt.csv.")){
                    Log.d(tag,name2.toString());
                }

            }
        });


        XposedHelpers.findAndHookMethod("com.android.okhttp.internal.http.HttpURLConnectionImpl", loadPackageParam.classLoader, "setRequestProperty", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if (param.args[0] == "Content-Type" && param.args[1].toString().contains("multipart/form-data")) {
                    String object = param.thisObject.toString();
                    Log.d(tag, object);
                    Log.d(tag, "调用栈------------");
                    XposedHelpers.findAndHookMethod("java.io.DataOutputStream", loadPackageParam.classLoader, "writeBytes", String.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Log.d(tag, "写入的arr1 = " + param.args[0].toString());
                            Log.d(tag, "长度1：" + param.args[1].toString() + param.args[2].toString());
                        }
                    });
                    XposedHelpers.findAndHookMethod("java.io.DataOutputStream", loadPackageParam.classLoader, "write", byte[].class, int.class, int.class, new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            byte[] bytes = (byte[]) param.args[0];
                            Log.d(tag, "写入的arr = " + param.args[0].toString());
                            String result = new String(bytes, "UTF-8");
                            Log.d(tag, "string00" + result);
                            new Throwable().printStackTrace();
                            Log.d(tag, "长度：" + param.args[1].toString() + param.args[2].toString());
                            if (bytes.length > 200) {
                                String path = "/mnt/sdcard/chenxh22/mytestApp22/test2.txt";

                                writeFile(path, bytes);

                            }
                        }
                    });

                    new Throwable().printStackTrace();
                    try {
                        Log.i(tag,
                                "--------------------------------NullPointerException-----------1");
                        throw new NullPointerException();
                    } catch (NullPointerException e1) {
                        // TODO: handle exception
                        Log.i(tag, "--------------------------------NullPointerException");
                        Log.e(tag, Log.getStackTraceString(e1));
                        // e1.printStackTrace();
                    }
                    Log.i(tag,
                            "--------------------------------NullPointerException-----------end");
                }
            }
        });

    }


    private void writeFile(String path, byte[] bytes) {
        File file = new File(path);
        Log.d(tag,file.getAbsolutePath());

        if(!file.exists()){
            try {
                File file1 = new File("/mnt/sdcard/chenxh22/mytestApp22/test2.txt");
                file1.createNewFile();
                Log.d(tag,"创建成功");
                Log.d(tag,file.getAbsolutePath());


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {

        }

        try {
            FileOutputStream out = new FileOutputStream(path);//指定写到哪个路径中
            FileChannel fileChannel = out.getChannel();
            fileChannel.write(ByteBuffer.wrap(bytes)); //将字节流写入文件中
            fileChannel.force(true);//强制刷新
            fileChannel.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






        }