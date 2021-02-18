package com.example.qqpim;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.HexDump;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.util.regex.*;

public class MainEntry implements IXposedHookLoadPackage {

    private static final String TAG = "MainEntry";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedBridge.log("Loading app: " + lpparam.packageName);
        Log.i(TAG, "Loading app: " + lpparam.packageName);

        if ("com.sohu.inputmethod.sogou".equals(lpparam.packageName)) {

            Class<?> clsSogouUrlEncrypt = XposedHelpers.findClass("com.sogou.encryptwall.SogouUrlEncrypt", lpparam.classLoader);
            XposedHelpers.findAndHookMethod(clsSogouUrlEncrypt, "g",
                    byte[].class, int.class, int.class, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            byte [] buf = (byte [])param.args[0];
                            if(buf.length==0){
                                return;
                            }

                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            HexDump.dump(buf, 0, os, 0);
                            Log.i(TAG, "SogouUrlEncrypt = " + os.toString());

                            if (buf[0] == 0x0A) {
                                Class<?> clsReportData = XposedHelpers.findClass("dgv$d", lpparam.classLoader);

                                try {
                                    // public static d_ReportData au_fromBuf(byte[] arg9) throws InvalidProtocolBufferNanoException {
                                    Object reportData = XposedHelpers.callStaticMethod(clsReportData, "au", buf);
                                    if (reportData != null) {
                                        ObjectMapper mapper = new ObjectMapper();
                                        Log.i(TAG, "[SogouUrlEncrypt] Report Data = " + mapper.writeValueAsString(reportData));

                                        //Log.d(TAG+"11",reportData.toString());
                                        JSONObject jsonObject = new JSONObject(mapper.writeValueAsString(reportData));
                                        Object object  = (Object)jsonObject.getJSONArray("hQE");
                                        Log.d(TAG+"input",object.toString());

                                        String pattern  =",\"hQE\":\\[\"(.*?)\",\"\"],";
                                        Pattern r = Pattern.compile(pattern);
                                        Matcher m = r.matcher(mapper.writeValueAsString(reportData));

                                        if(m.find()){
                                            Log.d(TAG+"Input",m.group(1));
                                        }

                                    } else
                                        Log.i(TAG, "[SogouUrlEncrypt] Report Data = null");
                                } catch (Exception e) {
                                    Log.e(TAG, "", e);
                                }
                            }

                            /*
                            byte [] aesKey = (byte[]) XposedHelpers.getObjectField(param.thisObject, "aesKey");
                            byte [] aesIv = (byte[]) XposedHelpers.getObjectField(param.thisObject, "aesIv");
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            os.write("aesKey = ".getBytes());
                            HexDump.dump(aesKey, 0, os, 0);
                            os.write('\n');
                            os.write("aesIv = ".getBytes());
                            HexDump.dump(aesIv, 0, os, 0);
                            Log.i(TAG, os.toString());

                            Log.i(TAG, "Calling from");
                            new Throwable().printStackTrace();
                             */

                            // 用这句来验证，即便屏蔽上传，也不影响云输入的正常使用
                            //param.setResult(Base64.encodeToString(new byte [] { 0 }, Base64.DEFAULT));
                        }
                    });

            Class<?> clsDhx = XposedHelpers.findClass("dhx", lpparam.classLoader);
            Class<?> clsDcr = XposedHelpers.findClass("dcr", lpparam.classLoader);
            // public int a_todo(String arg18, int arg19, SogouUrlEncrypt arg20, dhx arg21, dcr arg22) {
            XposedHelpers.findAndHookMethod("ear", lpparam.classLoader, "a",
                    String.class, int.class, clsSogouUrlEncrypt, clsDhx, clsDcr, new XC_MethodHook() {

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                            Object reportData = XposedHelpers.getObjectField(param.args[3], "iaV");
                            if (reportData != null) {
                                ObjectMapper mapper = new ObjectMapper();
                                Log.i(TAG, "Report Data = " + mapper.writeValueAsString(reportData));
                                mapper.writeValueAsString(reportData);

                                //mapper.writeValueAS
                                //Log.d(TAG+"1",reportData.toString());


                            } else
                                Log.i(TAG, "Report Data = null");

//                            Log.i(TAG, "Calling from");
//                            new Throwable().printStackTrace();
                        }
                    });

            /* 实测发现没啥用
            // public static boolean AH_isDailyBuild() {
            XposedHelpers.findAndHookMethod("ash", lpparam.classLoader, "AH", new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    param.setResult(true);
                }
            });
             */
        }
    }
}
