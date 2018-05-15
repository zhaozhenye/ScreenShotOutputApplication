package mytool.yixin.navinfo.com.navigation.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * 先进先出的指定缓存字数的内存级别日志缓存器
 */
public class MemoryCacheLog {

    /**
     * 换行符
     */
    private static final char LINE_BREAK_CHAR = 10;

    /**
     * 最大缓存字符数
     */
    private final int maxSize;

    /**
     * 缓存
     */
    private char[] cache;

    /**
     * 当前位置
     */
    private int index = 0;

    /**
     * 是否发生越界循环
     */
    private boolean exceed = false;

    /**
     * @param maxSize 最大缓存字符数（0：不运作）
     */
    public MemoryCacheLog(int maxSize) {
        this.maxSize = maxSize;
        if (maxSize <= 0) {
            return;
        }
        this.cache = new char[this.maxSize];
    }

    @Nullable
    public synchronized String toString() {
        if (maxSize <= 0) {
            return null;
        }
        int length = index - 1;
        int startOffset = 1;
        if (exceed) {
            length = cache.length;
            startOffset = index;
        }
        if (length <= 0) {
            return null;
        }
        char[] result = new char[length];
        for (int i = 0; i < length; i++) {
            result[i] = cache[(i + startOffset) % cache.length];
        }
        return new String(result);
    }

    /**
     * 放入内容
     *
     * @param str
     */
    public synchronized void println(@NonNull String str) {
        if (maxSize <= 0 || TextUtils.isEmpty(str)) {
            return;
        }
        addChar(LINE_BREAK_CHAR);
        for (int i = 0; i < str.length(); i++) {
            addChar(str.charAt(i));
        }
    }

    /**
     * 检查纠正标志位
     */
    private void check() {
        if (index >= cache.length) {
            index = 0;
            exceed = true;
        }
    }

    /**
     * 添加字符
     *
     * @param c
     */
    private void addChar(char c) {
        check();
        cache[index++] = c;
    }

}
