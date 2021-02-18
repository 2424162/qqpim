package com.example.qqpim;
import android.util.Log;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.ArrayList;
import java.util.List;

public class CzfHook implements IXposedHookLoadPackage {
    public String tag = "baiduinput";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        //com.baidu.input
        if(!loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);



        XposedHelpers.findAndHookMethod("czf", loadPackageParam.classLoader, "bnE", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Object object = (Object)param.thisObject;
                Field[] fields = object.getClass().getDeclaredFields();
                for(int i=0;i<fields.length;i++){
                    fields[i].setAccessible(true);
                    Log.d(tag,fields[i].getName()+" = "+fields[i].get(object).toString());
                }
                Log.d(tag,""+param.getResult().toString());
                Log.d(tag,"=========");


            }
        });
        XposedHelpers.findAndHookMethod("com.sogou.encryptwall.SogouUrlEncrypt", loadPackageParam.classLoader, "a", String.class, String.class, byte[].class, int.class, int.class, byte[].class, byte[].class, byte[].class, byte[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                byte[] arg = (byte[]) param.args[2];
                String string = new String(arg);
                Log.d(tag,"上传内容 = "+string);
                Log.d(tag,"结果 = "+param.getResult().toString());
            }
        });
    }

}
