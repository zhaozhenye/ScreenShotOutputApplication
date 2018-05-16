package mytool.yixin.navinfo.com.navigation.controller;

import android.text.TextUtils;

import mytool.yixin.navinfo.com.navigation.bean.NaviDataChangeEventInfo;
import mytool.yixin.navinfo.com.navigation.bean.NaviType;
import mytool.yixin.navinfo.com.navigation.bean.TmcSections;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class NaviController {
    private  boolean mExpandShow = false;

    public boolean ismExpandShow() {
        return mExpandShow;
    }

    public void setmExpandShow(boolean mExpandShow) {
        this.mExpandShow = mExpandShow;
    }

    private NaviDataChangeEventInfo naviDataInfo;

    public NaviDataChangeEventInfo getNaviDataInfo() {
        return naviDataInfo;
    }

    public void setNaviDataInfo(NaviDataChangeEventInfo naviDataInfo) {
        this.naviDataInfo = naviDataInfo;
    }

    private TmcSections tmcSections;

    public TmcSections getTmcSections() {
        return tmcSections;
    }

    public void setTmcSections(TmcSections tmcSections) {
        this.tmcSections = tmcSections;
    }

    /**
     * 获得单例
     *
     * @return
     */
    public static NaviController getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 单例持有器
     */
    private static final class InstanceHolder {
        private static final NaviController INSTANCE = new NaviController();
    }

    /**
     * 禁止构造
     */
    private NaviController() {
    }



    /**
     * 判断导航信息类别
     *
     * @return
     */
    public NaviType getType() {
        NaviType result    = NaviType.NEXT_HAS_DIRECTION;
//        NaviDataChangeEventInfo mNavigateData=getNaviData();
//        RouteInfo routeInfo = getCurrentRouteInfo();
//        if (mNavigateData == null || routeInfo == null) {
//            return result;
//        }


//        String nextRoad = mNavigateData.getNextRoadName();
//        int signTurnType = mNavigateData.getNextTurnType();
//        switch (signTurnType) {
//            case NaviSessionData.SignInfo.Direction:
//                return NaviType.NEXT_HAS_DIRECTION;
//            case NaviSessionData.SignInfo.IC:
//                return NaviType.NEXT_HAS_EXIT;
//            default:
//                break;
//        }
//        String currentRoad = mNavigateData.getCurrentRoadName();
//        String endRoad     = routeInfo.getEndPoi().getFitName();
        NaviDataChangeEventInfo naviDataInfo = getNaviDataInfo();
        String nextRoad = naviDataInfo.getNextName();
        String currentRoad = naviDataInfo.getName();
        if (TextUtils.isEmpty(nextRoad)) {
            result = NaviType.NO_NEXT_ROAD;
        } else if (nextRoad.contains(",") || nextRoad.contains("方向")) {
            result = NaviType.NEXT_HAS_DIRECTION;
        } else if (nextRoad.equals(currentRoad)) {
            result = NaviType.NEXT_EQUALS_CURRENT;
        } else if (nextRoad.equals("")) {
            result = NaviType.NEXT_EQUALS_END;
        } else if (nextRoad.contains("出口")) {
            result = NaviType.NEXT_HAS_EXIT;
        } else {
            result = NaviType.FORMAL_NEXT;
        }
        return result;
    }
}
