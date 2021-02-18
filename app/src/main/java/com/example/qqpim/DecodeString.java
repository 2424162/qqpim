package com.example.qqpim;
import android.util.Log;

import java.lang.reflect.Field;
import android.util.Base64;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;



public class DecodeString implements IXposedHookLoadPackage {
    public String tag = "decodeString";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //com.baidu.input
        if(!loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);

//        XposedHelpers.findAndHookMethod("axq", loadPackageParam.classLoader, "f", byte[].class, int.class, int.class, new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                super.afterHookedMethod(param);
//                //Log.d(tag,"-------"+Arrays.toString((byte[])param.args[0]));
//                String string = new String((byte[])param.args[0]);
//                Log.d(tag,"压缩前的数据: "+string);
//                int int1 = (int)param.args[1];
//                int int2 = (int)param.args[2];
//                byte[] result = (byte[])param.getResult();
//                Inflater inflater = new Inflater(true);
//                inflater.setInput(result);
//                byte[] result1 = new byte[200];
//
//                inflater.inflate(result1);
//                //Log.d(tag,"终====="+Arrays.toString(result1));
//
//
//            }
//        });

        XposedHelpers.findAndHookMethod("java.util.zip.Deflater", loadPackageParam.classLoader, "setInput", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                byte[] bytes = (byte[])param.args[0];
                String string = new String(bytes);
                Log.d(tag,"数据1 = "+string);
            }
        });
        XposedHelpers.findAndHookMethod("java.util.zip.Deflater", loadPackageParam.classLoader, "setInput", byte[].class,  new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                byte[] bytes = (byte[])param.args[0];
                String string = new String(bytes);
                Log.d(tag,"数据2 = "+string);
            }
        });


        XposedHelpers.findAndHookMethod("axq", loadPackageParam.classLoader, "encodeAES",byte[].class,byte[].class,byte[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                byte[] bArr1= (byte[])param.args[0];
                byte[] bArr2 = (byte[])param.args[1];
                byte[] bArr3 = (byte[])param.args[2];



                Log.d(tag,"原2===="+Arrays.toString(bArr1));
                SecretKeySpec secretKeySpec = new SecretKeySpec(bArr2, "AES");
                Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
                instance.init(2, secretKeySpec, new IvParameterSpec(bArr3));
                //byte[] doFinal = instance.doFinal(bArr1);


                //Log.d(tag,"新2===="+Arrays.toString(doFinal));
                String  sk = new String(bArr2);
                Log.d(tag,Arrays.toString(bArr2));
                Log.d(tag,"key1 = "+sk);


                String  sk2 = new String(bArr3);
                Log.d(tag,Arrays.toString(bArr3));
                Log.d(tag,"key2 = "+sk2);




            }
        });
        XposedHelpers.findAndHookMethod("axq", loadPackageParam.classLoader, "encodeBase64", byte[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                byte[] bytes = (byte[])param.args[0];
                Log.d(tag,"原===="+Arrays.toString(bytes));
                String string = new String(bytes);
                String result = (String)param.getResult();
                byte[] bytes1 = Base64.decode(result,2);
                Log.d(tag,"新===="+Arrays.toString(bytes1));
                Log.d(tag,result);


            }
        });

    }

}
