package com.example.administrator.ijkplayerdemo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

/**
 * 将assets文件夹下的文件复制到SD卡中
 */

public class CopyZipFileToSD {
    private Context context;
    String fileName;
    String filePath;

    /**
     * @param context
     * @param fileName fileName(assets文件夹下压缩文件):文件名称+后缀
     * @param filePath sd本地路径
     */
    public CopyZipFileToSD(Context context, String fileName, String filePath) {
        this.context = context;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public void copy() {
        InputStream inputStream;
        try {
            inputStream = context.getResources().getAssets().open(fileName);// assets文件夹下的文件
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath + "/" + fileName);// 保存到本地的文件夹下的文件
            byte[] buffer = new byte[1024];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            Log.d("CopyZipFileToSD", "拷贝成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CopyZipFileToSD", "拷贝失败");
        }
    }

}