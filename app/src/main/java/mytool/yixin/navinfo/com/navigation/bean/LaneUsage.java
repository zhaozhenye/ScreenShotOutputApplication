package mytool.yixin.navinfo.com.navigation.bean;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class LaneUsage {
    public int arrow;
    public boolean shouldUse;

    private LaneUsage(int var1, boolean var2) {
        this.arrow = var1;
        this.shouldUse = var2;
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("LaneUsage [arrow=").append(this.arrow).append(", shouldUse=").append(this.shouldUse).append("]");
        return var1.toString();
    }

    public class LaneArrow {
        public static final int bus = 1;
        public static final int up = 2;
        public static final int down = 3;
        public static final int left = 4;
        public static final int right = 5;
        public static final int leftUp = 6;
        public static final int rightUp = 7;
        public static final int leftDown = 8;
        public static final int rightDown = 9;
        public static final int upDown = 10;
        public static final int leftRight = 11;
        public static final int leftUpRight = 12;
        public static final int downReversed = 13;
        public static final int leftDownReversed = 14;
        public static final int rightDownReversed = 15;
        public static final int upDownReversed = 16;

        public LaneArrow() {
        }
    }
}
