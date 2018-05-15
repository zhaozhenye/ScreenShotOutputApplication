package zzy.test.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/12
 */


public class Utils {
    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(bytes, Base64.DEFAULT);

    }

    public static String getNetFileSizeDescription(long size) {
        StringBuffer bytes = new StringBuffer();
        DecimalFormat format = new DecimalFormat("###.0");
        if (size >= 1024 * 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0 * 1024.0));
            bytes.append(format.format(i)).append("GB");
        } else if (size >= 1024 * 1024) {
            double i = (size / (1024.0 * 1024.0));
            bytes.append(format.format(i)).append("MB");
        } else if (size >= 1024) {
            double i = (size / (1024.0));
            bytes.append(format.format(i)).append("KB");
        } else if (size < 1024) {
            if (size <= 0) {
                bytes.append("0B");
            } else {
                bytes.append((int) size).append("B");
            }
        }
        return bytes.toString();
    }

    public static Bitmap compressMatrix(Bitmap bm) {
        Bitmap mSrcBitmap = null;
        Matrix matrix = new Matrix();
        matrix.setScale(0.5f, 0.5f);
        mSrcBitmap = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        return mSrcBitmap;
    }

    /**
     * 放缩法压缩 矩阵
     * @param bm
     * @return
     */
    public static Bitmap compressQuality(Bitmap bm) {
        Bitmap mSrcBitmap = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] bytes = bos.toByteArray();
        mSrcBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return mSrcBitmap;
    }

    /**
     * 采样率压缩
     * @param bm
     * @return
     */
    public static Bitmap compressSampling(Bitmap bm) {
        Bitmap mSrcBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bytes = bos.toByteArray();
        mSrcBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return mSrcBitmap;
    }
    /**
     * 采样率压缩
     * @param bm
     * @return
     */
    public static Bitmap compressRGB565(Bitmap bm) {
        Bitmap mSrcBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bytes = bos.toByteArray();
        mSrcBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        return mSrcBitmap;
    }


}
