package com.example.qqpim;

import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.apache.commons.io.HexDump;

import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class HexHook implements IXposedHookLoadPackage {
    public String tag = "youyinhook";
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if(!loadPackageParam.packageName.equals("com.ss.android.ugc.aweme")){
            return ;
        }
        Log.d(tag,loadPackageParam.packageName);
        initDouyin(loadPackageParam);


    }
    public void initDouyin(XC_LoadPackage.LoadPackageParam lpp){

        findAndHookMethod("java.io.OutputStream", lpp.classLoader, "write", byte[].class,int.class,int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                OutputStream os = (OutputStream)param.thisObject;
                Log.d(tag,param.getResult().toString());
                if(!os.toString().contains("internal.http"))
                    return;
                byte[] bytes = (byte[])param.args[0];
                OutputStream ops = new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {

                    }
                };
                long offset = 9999;

                HexDump.dump(bytes,offset,ops,0);

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




