package mytool.yixin.navinfo.com.navigation.bean;

import java.util.Arrays;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class LaneModel {
    public int laneNumber;
    public int driveArrow;
    public LaneUsage[] lanes;

    public LaneModel(int var1, int var2, LaneUsage[] var3) {
        this.laneNumber = var1;
        this.driveArrow = var2;
        this.lanes = var3;
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        var1.append("LaneModel [laneNumber=").append(this.laneNumber).append(", driveArrow=").append(this.driveArrow).append(", lanes=").append(Arrays.toString(this.lanes)).append("]");
        return var1.toString();
    }
}
