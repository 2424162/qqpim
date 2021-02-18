package com.example.qqpim;

import android.content.Context;
import android.util.Log;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class StringtoBytes extends XC_LoadPackage {
    public String tag = "baiduinfo";
    @Override
    public void handleLoadPackage(LoadPackageParam loadPackageParam) throws Throwable {
        if (!loadPackageParam.packageName.equals("com.sohu.inputmethod.sogou")) {
            return;
        }
        Log.d(tag, loadPackageParam.packageName);
        XposedHelpers.findAndHookMethod("java.lang.String", loadPackageParam.classLoader, "getBytes", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"1---"+param.thisObject.toString());
                new Throwable().printStackTrace();
            }
        });
        XposedHelpers.findAndHookMethod("java.lang.String", loadPackageParam.classLoader, "getBytes",String.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"2---"+param.thisObject.toString());
                new Throwable().printStackTrace();
            }
        });
        XposedHelpers.findAndHookMethod("java.lang.String", loadPackageParam.classLoader, "getBytes",Character.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.d(tag,"3---"+param.thisObject.toString());
            }
        });

        XC_MethodHook ResponseHook = new XC_MethodHook() {

            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                HttpURLConnection urlConn = (HttpURLConnection) param.thisObject;

                if (urlConn != null) {
                    StringBuilder sb = new StringBuilder();
                    int code = urlConn.getResponseCode();
                    if (code == 200) {

                        Map<String, List<String>> properties = urlConn.getHeaderFields();
                        if (properties != null && properties.size() > 0) {

                            for (Map.Entry<String, List<String>> entry : properties.entrySet()) {
                                sb.append(entry.getKey() + ": " + entry.getValue() + ", ");
                            }
                        }
                    }
                    Log.d(tag, "RESPONSE: method=" + urlConn.getRequestMethod() + " " +
                            "URL=" + urlConn.getURL().toString() + " " +
                            "Params=" + sb.toString());
                }
            }
        };


    }
}
