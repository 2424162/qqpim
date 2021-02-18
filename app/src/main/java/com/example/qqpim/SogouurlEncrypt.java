package com.example.qqpim;

import android.content.Context;
import android.util.Log;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SogouurlEncrypt extends XC_LoadPackage {
    public String tag = "sogouurl";
    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod("com.sogou.encryptwall.SogouUrlEncrypt", loadPackageParam.classLoader, "a", String.class,String.class,byte[].class,int.class,int.class,byte[].class,byte[].class,byte[].class,byte[].class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                byte[] arg3 = (byte[])param.args[2];
                String string1 = new String(arg3);

                Log.d(tag,"===============================");
                Log.d(tag,"byte类型转string"+string1);

                //Log.d(tag,param.getResult().toString());
            }
        });

    }
}
