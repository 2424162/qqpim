package com.example.qqpim;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import org.apache.commons.io.HexDump;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public String filename = "log.txt";
    public String tag = "hexdump";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String string = "hello";
        byte[] bytes = string.getBytes();
        Log.d(tag,bytes.toString());

        OutputStream ops = new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };
        long offset = 9999;

        try {
            HexDump.dump(bytes,offset,ops,0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.d(tag,ops.toString());


    }

    private void initEvent() {


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d(tag, "没有直接权限");

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        } else {
            Log.d(tag, "有权限");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }


    public void createFolder(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.d(tag,"创建文件夹"+file.getName());
    }

    private void createFile(String path) {
        File mFile = new File(path+"/"+filename);
        if (mFile.exists()) {
            mFile.delete();
        }
        try {
            mFile.createNewFile();
            Log.d(tag,"文件创建完成=====");
            Log.d(tag,mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FileWriteData(String url){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String t=format.format(new Date());


            FileWriter writer = new FileWriter("/sdcard/DouYinFile/log.txt",true);

            writer.write(t +" "+url);
            writer.write(t +" "+"\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}