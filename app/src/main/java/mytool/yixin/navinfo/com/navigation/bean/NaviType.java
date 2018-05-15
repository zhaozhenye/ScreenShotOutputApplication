package mytool.yixin.navinfo.com.navigation.bean;

/**
 * Created by zych on 2018/1/19.
 * 导航名称类别
 */
public enum NaviType {
    /**
     * 引擎不返回前方路名
     */
    NO_NEXT_ROAD("沿路行驶"), /**
     * 前方路名里有“，”或“方向”
     */
    NEXT_HAS_DIRECTION("前往"), /**
     * 前方路名和当前路名相同
     */
    NEXT_EQUALS_CURRENT("行驶"), /**
     * 前方路名和路线终点同名
     */
    NEXT_EQUALS_END("到达"), /**
     * 前方路名里有“出口”
     */
    NEXT_HAS_EXIT("前往"), /**
     * 正常前方路名
     */
    FORMAL_NEXT("进入");
    private String key;

    NaviType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
