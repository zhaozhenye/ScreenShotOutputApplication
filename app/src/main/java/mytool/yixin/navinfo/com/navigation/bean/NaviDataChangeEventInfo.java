package mytool.yixin.navinfo.com.navigation.bean;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class NaviDataChangeEventInfo {


    /**
     * name : 当前道路名称
     * nextName : 下条道路名称
     * distanceToCurrPoint : 500m
     * direction : 正北
     * icon : iconDirection
     * iconImage : byte[]->string
     * remainDistance : 1.5 千米
     * remainTime : 20 分钟
     * limitSpeed : 60
     * cameraType : -1
     * cameraDistance : 300m
     * currSpeed : 80km/h
     * percentToCurrPoint : 18
     * totalDistance
     * distanceToEnd
     */

    private String name;
    private String nextName;
    private String distanceToCurrPoint;
    private String direction;
    private int icon;
    private String iconImage;
    private String remainDistance;
    private String remainTime;
    private int limitSpeed;
    private int cameraType;
    private String cameraDistance;
    private String currSpeed;
    private double percentToCurrPoint;
    private int totalDistance;
    private int distanceToEnd;

    public int getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(int totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getDistanceToEnd() {
        return distanceToEnd;
    }

    public void setDistanceToEnd(int distanceToEnd) {
        this.distanceToEnd = distanceToEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNextName() {
        return nextName;
    }

    public void setNextName(String nextName) {
        this.nextName = nextName;
    }

    public String getDistanceToCurrPoint() {
        return distanceToCurrPoint;
    }

    public void setDistanceToCurrPoint(String distanceToCurrPoint) {
        this.distanceToCurrPoint = distanceToCurrPoint;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getRemainDistance() {
        return remainDistance;
    }

    public void setRemainDistance(String remainDistance) {
        this.remainDistance = remainDistance;
    }

    public String getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(String remainTime) {
        this.remainTime = remainTime;
    }

    public int getLimitSpeed() {
        return limitSpeed;
    }

    public void setLimitSpeed(int limitSpeed) {
        this.limitSpeed = limitSpeed;
    }

    public int getCameraType() {
        return cameraType;
    }

    public void setCameraType(int cameraType) {
        this.cameraType = cameraType;
    }

    public String getCameraDistance() {
        return cameraDistance;
    }

    public void setCameraDistance(String cameraDistance) {
        this.cameraDistance = cameraDistance;
    }

    public String getCurrSpeed() {
        return currSpeed;
    }

    public void setCurrSpeed(String currSpeed) {
        this.currSpeed = currSpeed;
    }

    public double getPercentToCurrPoint() {
        return percentToCurrPoint;
    }

    public void setPercentToCurrPoint(double percentToCurrPoint) {
        this.percentToCurrPoint = percentToCurrPoint;
    }
}
