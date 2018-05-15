package mytool.yixin.navinfo.com.navigation.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.System.gc;

/**
 * 用于操作Bitmap相关的工具类
 * 比如：绘制圆形图片
 */

public class BitmapUtil {
    /**
     * 创建圆形图片
     *
     * @param bitmap
     * @param size
     * @return
     */
    public static Bitmap createCircleBitmap(Bitmap bitmap, int size) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Bitmap target = Bitmap.createBitmap(size, size, bitmap.getConfig());
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, size, size), paint);
        return target;
    }

    /**
     * 创建圆形图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createCircleBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return createCircleBitmap(bitmap, Math.min(bitmap.getHeight(), bitmap.getWidth()));
    }

    /**
     * 根据原Bitmap返回指定大小的Bitmap
     *
     * @param sourceBitmap
     * @param newWidth
     * @param newHeight
     * @param filter
     * @return
     */
    public static Bitmap createScaleBitmap(Bitmap sourceBitmap, int newWidth, int newHeight, boolean filter) {
        try {
            return Bitmap.createScaledBitmap(sourceBitmap, newWidth, newHeight, filter);
        } catch (OutOfMemoryError error) {
            gc();
            return Bitmap.createScaledBitmap(sourceBitmap, newWidth, newHeight, filter);
        }
    }

    public static Bitmap decodeStreamWithHP(InputStream ins) {
        return decodeStreamWithHP(ins, null);
    }

    public static Bitmap decodeResourceWithHP(Resources res, int id) {
        return decodeResourceWithHP(res, id, null);
    }

    /**
     * @NOTE 目前Resource资源buff 已暂时去除
     * @param res
     * @param id
     * @param opts
     * @return
     */
    public static Bitmap decodeResourceWithHP(Resources res, int id, BitmapFactory.Options opts) {
        return BitmapFactory.decodeResource(res, id, opts);
//        Bitmap bm = null;
//        InputStream is = null;
//        try {
//            final TypedValue value = new TypedValue();
//            is = res.openRawResource(id, value);
//            BufferedInputStream bins = new BufferedInputStream(is);
//
//            bm = BitmapFactory.decodeResourceStream(res, value, bins, null, opts);
//        } catch (Exception e) {
//            /*  do nothing.
//                If the exception happened on open, bm will be null.
//                If it happened on close, bm is still valid.
//            */
//        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException e) {
//                // Ignore
//            }
//        }
//
//        if (bm == null && opts != null && opts.inBitmap != null) {
//            throw new IllegalArgumentException("Problem decoding into existing bitmap");
//        }
//
//        return bm;
    }

    /**
     * 为 decodeFile 增加Buff,降低磁盘IO频次
     *
     * @param filePath
     * @param opts
     * @return
     */
    public static Bitmap decodeFileWithHP(String filePath, BitmapFactory.Options opts) {
        if (filePath == null) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        if (opts.inJustDecodeBounds) { //进解析图片头数据
            return BitmapFactory.decodeFile(filePath, opts);
        } else { //解析图片
            try {
                return decodeStreamWithHP(new FileInputStream(file), opts);
            } catch (FileNotFoundException e) {
//                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 为 decodeStreamWithHP 增加Buff，优化速度.
     * @Note 仅优化FileInputStream.
     * @param ins
     * @param opts
     * @return
     */
    private static Bitmap decodeStreamWithHP(InputStream ins, BitmapFactory.Options opts) {
        if (ins == null) {
            return null;
        }
        InputStream bins = null;
        if ((ins instanceof FileInputStream)) {
            bins = new BufferedInputStream(ins);
        } else {
            bins = ins;
        }
        try {
            return BitmapFactory.decodeStream(bins, null, opts);
        } finally {
            try {
                bins.close();
            } catch (IOException e) {
//                e.printStackTrace();
            }
            try {
                if (ins != bins) { //避免重复关闭
                    ins.close();
                }
            } catch (IOException e) {
//                e.printStackTrace();
            }

        }
    }


}
