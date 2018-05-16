package mytool.yixin.navinfo.com.navigation.bean;

import java.util.Arrays;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class TmcSections {
    public int mode;
    public float[] ends;
    public int[] pixels;
    public int[] states;
    public int length;

    private TmcSections(float[] var1, int[] var2, int[] var3) {
        this.ends = var1;
        this.pixels = var2;
        this.states = var3;
        this.length = var3.length;
        if(var3.length == 0) {
            this.mode = 0;
        } else if(var2.length == 0) {
            this.mode = 1;
        } else {
            this.mode = 2;
        }

    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("TmcSections [mode=").append(this.mode).append(", ends=").append(Arrays.toString(this.ends)).append(", pixels=").append(Arrays.toString(this.pixels)).append(", states=").append(Arrays.toString(this.states)).append(", length=").append(this.length).append("]");
        return var1.toString();
    }

    public class Mode {
        public static final int none = 0;
        public static final int percent = 1;
        public static final int pixel = 2;

        public Mode() {
        }
    }

    public class State {
        public static final int unknown = 0;
        public static final int light = 1;
        public static final int medium = 2;
        public static final int heavy = 3;
        public static final int blocked = 4;
        public static final int none = 5;

        public State() {
        }
    }
}
