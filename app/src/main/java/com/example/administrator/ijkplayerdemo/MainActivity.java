package com.example.administrator.ijkplayerdemo;

import android.Manifest;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dou361.ijkplayer.widget.AndroidMediaController;
import com.dou361.ijkplayer.widget.IRenderView;
import com.dou361.ijkplayer.widget.IjkVideoView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class MainActivity extends AppCompatActivity {

    private IjkVideoView ijk_video_view;
    private ArrayList<MediaStore.Video> list;

    {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ijk_video_view = (IjkVideoView) findViewById(R.id.ijk_video_view);
        ijk_video_view.setAspectRatio(IRenderView.AR_MATCH_PARENT);
        AndroidMediaController mMediaController = new AndroidMediaController(MainActivity.this, false);
        ijk_video_view.setMediaController(mMediaController);

        requestRxPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        String name = "splash_video.mp4";
//        File file = new File(path + "/" + name);
//        if (file.exists()) {
//            String localPath = Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + "/foreo/skincare"+"/splash_video.mp4";
//            ijk_video_view.setVideoPath(localPath);
//            ijk_video_view.start();
//        } else {
//            CopyZipFileToSD copyZipFileToSD = new CopyZipFileToSD(this, name, path);
//            copyZipFileToSD.copy();
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                String number = cursor.getString(0); // 视频编号
                String path = cursor.getString(1); // 视频文件路径
                String size = cursor.getString(2); // 视频大小
                String name = cursor.getString(3); // 视频文件名
                Log.e("-----", "number=" + number);
                Log.e("-----", "v_path=" + path);
                Log.e("-----", "v_size=" + size);
                Log.e("-----", "v_name=" + name);

            }
        }
    }


    public List<MediaStore.Video> getList() {
        list = null;
        Cursor cursor = getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,
                null, null);
        if (cursor != null) {
            list = new ArrayList<MediaStore.Video>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                Log.d("MainActivity", "id:" + id);
                String title = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                Log.d("MainActivity", title);
                String album = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));
                String artist = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));
                String displayName = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                String mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                String path = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                Log.d("MainActivity", path);
                Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MINI_KIND);

                long duration = cursor
                        .getInt(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                long size = cursor
                        .getLong(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
            }
            cursor.close();
            /*测试dev*/
        }
        return list;
    }

    //请求权限
    private void requestRxPermissions(String... permissions) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(permissions).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean granted) throws Exception {
                if (granted) {
//                    Toast.makeText(MainActivity.this, "已获取权限", Toast.LENGTH_SHORT).show();
//                    getList();

                    Toast.makeText(MainActivity.this, "权限获取到了", Toast.LENGTH_SHORT).show();
                    ijk_video_view.setVideoPath(Environment.getExternalStorageDirectory() + "/4k1.mp4");
////                    ijk_video_view.setVideoPath("http://mp4.vjshi.com/2017-09-19/e1920013741c5c66d408c7cdb3d9eddf.mp4");
                    ijk_video_view.start();
//                    ijk_video_view.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
//                        @Override
//                        public void onCompletion(IMediaPlayer iMediaPlayer) {
//                            ijk_video_view.start();
//                        }
//                    });
//                    startActivity(new Intent(MainActivity.this, DecodeActivity.class));
                } else {
                    Toast.makeText(MainActivity.this, "已拒绝一个或以上权限", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
