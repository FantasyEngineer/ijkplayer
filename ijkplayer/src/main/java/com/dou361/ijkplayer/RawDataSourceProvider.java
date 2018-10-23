package com.dou361.ijkplayer;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

/**
 * Created by Administrator on 2018/1/10.
 */

public class RawDataSourceProvider implements IMediaDataSource {
    private AssetFileDescriptor mDescriptor;
    long length;
    private byte[] mMediaBytes;
    long mPosition;

    public RawDataSourceProvider(Context context, Uri uri) throws IOException {
        mDescriptor = context.getResources().openRawResourceFd(R.raw.splash_video);
        length = mDescriptor.getLength();
        mMediaBytes = readBytes(mDescriptor.createInputStream());
        Log.d("RawDataSourceProvider", "length:" + length);
    }

    @Override
    public int readAt(long position, byte[] buffer, int offset, int size) throws IOException {
        if (position + 1 >= mMediaBytes.length) {
            return -1;
        }

        int length;
        if (position + size < mMediaBytes.length) {
            length = size;
        } else {
            length = (int) (mMediaBytes.length - position);
            if (length > buffer.length)
                length = buffer.length;

            length--;
        }

        System.arraycopy(mMediaBytes, (int) position, buffer, offset, length);
        mPosition = position;
        return length;

    }

    @Override
    public long getSize() throws IOException {
        return length;
    }

    @Override
    public void close() throws IOException {
        if (mDescriptor != null)
            mDescriptor.close();

        mDescriptor = null;
        mMediaBytes = null;
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

}
