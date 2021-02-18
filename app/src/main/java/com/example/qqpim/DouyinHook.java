package com.example.qqpim;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.OutputStream;
import java.lang.reflect.Modifier;

public class DouyinHook implements IXposedHookLoadPackage {
    public String tag = "douyinhook";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")) {
            Log.d(tag,loadPackageParam.packageName);
            return;

        }

        Log.d(tag,loadPackageParam.packageName);
        try {

            Class<?> formurl = XposedHelpers.findClass("com.bytedance.retrofit2.mime.FormUrlEncodedTypedOutput", loadPackageParam.classLoader);
            //XposedBridge.hookAllConstructors()

            XposedBridge.hookAllMethods(formurl, "addField", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if (param.args.length > 2) {
                        String string1 = param.args[0].toString();
                        String string2 = new String((byte[]) param.args[0]);
                        Log.d(tag, string1);
                        Log.d(tag, string2);
                    }
                }
            });
        }catch (Exception ex){}
    }
}
