//package mytool.yixin.navinfo.com.navigation.manager;
//
//import android.graphics.Rect;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
//import mytool.yixin.navinfo.com.navigation.bean.TmcSections;
//import mytool.yixin.navinfo.com.navigation.listener.BaseEventInfo;
//import mytool.yixin.navinfo.com.navigation.listener.Listener;
//import mytool.yixin.navinfo.com.navigation.listener.TMCUpdateEventInfo;
//import mytool.yixin.navinfo.com.navigation.listener.TMCWarpSuccinctListener;
//import mytool.yixin.navinfo.com.navigation.listener.WeakGenericListeners;
//import mytool.yixin.navinfo.com.navigation.log.Log;
//import mytool.yixin.navinfo.com.navigation.log.LogTag;
//
//
//
///**
// * // INFO 路线管路器（算路、路线管理）
// * 回调时机：不存在监听器，全局回调
// * 存在监听器只需回回调该监听器。
// */
//public class NaviRouteManager {
//
//    /**
//     * 算路监听器（底层）
//     */
//    private final RouteEventInfoListener routeListener = new RouteEventInfoListener();
//    /**
//     * 偏航重算路监听器（底层）
//     */
//    private final RerouteListener rerouteListener = new RerouteListener();
//
//    /**
//     * 导航路线监听器
//     */
//    private WeakGenericListeners<NaviRouteEventInfo> listeners = new WeakGenericListeners<>();
//
//    private TMCUpdateListener tmcUpdateListener = new TMCUpdateListener();
//
//    /**
//     * 路况信息更新监听器
//     */
//    private WeakGenericListeners<TMCUpdateEventInfo> tmcUpdateChangeListeners = new WeakGenericListeners<TMCUpdateEventInfo>();
//
//    /**
//     * 路线TMC更新监听器包装类集合，防止监听器内存回收
//     */
//    private ArrayList<TMCWarpSuccinctListener> tmpListeners = new ArrayList<>();
//
//    private NaviManager naviManager = NaviManager.getInstance();
//
//
//    private CityManager cityManager = CityManager.getInstance();
//
//    private MapBarLocationManager mapBarLocationManager = MapBarLocationManager.getInstance();
//
//    private GpsTracker gpsTracker = GpsTracker.getInstance();
//
//    /**
//     * 起、途、终点信息
//     */
//    private RoutePoisInfo routePoisInfo;
//
//    private RoutePlan routePlan = new RoutePlan();
//
//    //保存行车的路线和选中路线的下标
//    private RouteInfo[] carRouteInfos;
//    private int carSelectedRouteIndex = 0;
//    private Listener.GenericListener<BaseEventInfo> walkStatusChangedListener;
//    /**
//     * 步行算路的路线
//     */
//    private RouteInfo[] walkRouteInfos;
//    private RouteRequest request;
//
//    private NaviRouteManager() {
//        naviManager.addRouteTMCUpdateListener(tmcUpdateListener);
//        naviManager.addRerouteListener(rerouteListener);
//
//    }
//
//    public static NaviRouteManager getInstance() {
//        return InstanceHolder.INSTANCE;
//    }
//
//    public boolean isWalkInit() {
//        return !(walkRouteInfos == null || walkRouteInfos == carRouteInfos);
//    }
//
//    public RouteInfo[] getRouteInfos() {
//        RouteInfo[] routeInfos = NaviStatus.NAVI_WALK.isActive() ? walkRouteInfos : carRouteInfos;
//        // 日志
//        if (Log.isLoggable(LogTag.NAVI, Log.DEBUG)) {
//            Log.d(LogTag.NAVI, " -->> NaviStatus.naviwalk = " + NaviStatus.NAVI_WALK.isActive());
//        }
//        RUNTIME_LOG.println("NaviRoute -->> routeInfos = " + routeInfos);
//        RUNTIME_LOG.println("NaviRoute -->> isWalk" + NaviStatus.NAVI_WALK.isActive());
//        RUNTIME_LOG.println("NaviRoute -->> isNavi" + NaviStatus.NAVI_RELATED.isActive());
//        RUNTIME_LOG.println("NaviRoute -->> carInfo" + carRouteInfos);
//        RUNTIME_LOG.println("NaviRoute -->> walkInfo" + walkRouteInfos);
//
//        return routeInfos;
//    }
//
//    public int getSelectedRouteIndex() {
//        int i = NaviStatus.NAVI_WALK.isActive() ? 0 : carSelectedRouteIndex;
//        // 日志
//        if (Log.isLoggable(LogTag.NAVI, Log.DEBUG)) {
//            Log.d(LogTag.NAVI, " -->> index = " + i);
//        }
//
//        return i;
//    }
//
//    public void setCarSelectedRouteIndex(int index) {
//        carSelectedRouteIndex = index;
//    }
//
//    public RouteInfo getRouteInfo() {
//        int index = getSelectedRouteIndex();
//        RouteInfo[] routeInfos = getRouteInfos();
//        if (routeInfos == null || index >= routeInfos.length) {
//            return null;
//        }
//        return routeInfos[index];
//    }
//
//    /**
//     * @param listener
//     */
//    public void addListener(Listener.GenericListener<NaviRouteEventInfo> listener) {
//        listeners.add(listener);
//    }
//
//    /**
//     * 添加路况信息更新监听器
//     */
//    public void addTMCUpateListener(Listener.SuccinctListener listener, TmcSections tmcSections) {
//        TMCWarpSuccinctListener tmpUpdateListener = new TMCWarpSuccinctListener(listener,  tmcSections);
//        tmpListeners.add(tmpUpdateListener);
//        tmcUpdateChangeListeners.add(tmpUpdateListener);
//    }
//
//    public void removeTMCUpdateInfo() {
//        tmpListeners.clear();
//    }
//
//    @NonNull
//    public RoutePoisInfo getRoutePoisInfo() {
//        if (routePoisInfo == null) {
//            routePoisInfo = new RoutePoisInfo();
//        }
//        return routePoisInfo;
//    }
//
//    /**
//     * 清除相关信息
//     */
//    public void clear() {
//        // 日志
//        if (Log.isLoggable(LogTag.ROUTE, Log.INFO)) {
//            StringBuilder sb = new StringBuilder().append(" -->> ") //
//                    ;
//            Log.is(LogTag.ROUTE, sb.toString());
//        }
//        routePoisInfo = null;
//        carRouteInfos = null;
//        walkRouteInfos = null;
//        carSelectedRouteIndex = 0;
//        request = null;
//
//        if (AppOnlineSharedPreferences.RUNTIME_LOG_SIZE.get() > 0) {
//            RUNTIME_LOG.println("NaviRoute-->> clear" + getLogStack("clear"));
//        }
//    }
//
//
//    private String getLogStack(String msg) {
//
//        Thread currentThread = Thread.currentThread();
//
//        StackTraceElement[] stackTrace = currentThread.getStackTrace();
//
//        int i = 4;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(currentThread.getId())//
//                .append("|")//
//                .append(getCodeLocation(Log.CodeLocationStyle.FIRST, null, stackTrace[i]))//
//                .append("|")//
//                .append(msg);
//        i++;
//
//        for (; (i < stackTrace.length); i++) {
//            String s = getCodeLocation(Log.CodeLocationStyle.SUBSEQUENT, currentThread, stackTrace[i]).toString();
//            sb.append("\n").append(s);
//        }
//        return sb.toString();
//    }
//
//    private static StringBuilder getCodeLocation(Log.CodeLocationStyle style, Thread currentThread, StackTraceElement stackTraceElement) {
//        String className = stackTraceElement.getClassName();
//        int lineNumber = stackTraceElement.getLineNumber();
//        String methodName = stackTraceElement.getMethodName();
//        String fileName = stackTraceElement.getFileName();
//        StringBuilder sb = new StringBuilder();
//        if (style.isAt()) {
//            sb.append("	at ");
//        }
//        if (style.isSimpleClassName()) {
//            sb.append(getSimpleName(className));
//        } else {
//            sb.append(className);
//        }
//        sb.append(".").append(methodName).append("(").append(fileName).append(":").append(lineNumber).append(")");
//        return sb;
//    }
//
//    private static String getSimpleName(String className) {
//        String[] split = className.split("\\.");
//        return split[split.length - 1];
//    }
////    /**
////     * 导航算路
////     * @deprecated 建议使用 {@link #navigationRoute(NaviConfig)}
////     */
////    void route() {
////        // 通知
//////        NaviRouteEventInfo eventInfo = new NaviRouteEventInfo();
//////        eventInfo.setEvent(NaviRouteEventType.START);
//////        listeners.conveyEvent(eventInfo);
//////
//////        // 统计
//////        UMengAnalysis.sendEvent(AnalysisConfigs.ROUTE_EVENT, AnalysisConfigs.ROUTE_VIAS_COUNT + getRoutePoisInfo().getViaPois().size());
//////        // 日志
//////        if (Log.isLoggable(LogTag.NAVI, Log.DEBUG)) {
//////            Log.d(LogTag.NAVI, " -->> rult = " + getRoutePoisInfo().getRoutePlan().getRule());
//////        }
//////        // 执行
//////        request = new RouteRequest(getRoutePoisInfo(), routeListener);
//////        RouteManager.getInstance().execute(request);
////
////        navigationRoute(null);
////    }
//
//    /**
//     * 导航算路
//     *
//     * @param naviConfig 如果和现有的导航跳转相同则 naviConfig 设为 null 即可；否则可以使用{@link NaviConfig#getConfigInstance(BasePage)}来配置“导航算路”相关
//     */
//    void navigationRoute(@Nullable NaviConfig naviConfig) {
//// 通知
//        NaviRouteEventInfo eventInfo = new NaviRouteEventInfo();
//        eventInfo.setEvent(NaviRouteEventType.START);
//        listeners.conveyEvent(eventInfo);
//
//
//        // 统计
//        UMengAnalysis.sendEvent(AnalysisConfigs.ROUTE_EVENT, AnalysisConfigs.ROUTE_VIAS_COUNT + getRoutePoisInfo().getViaPois().size());
//        // 执行
//        request = new RouteRequest(getRoutePoisInfo(), routeListener, naviConfig);
//        RouteManager.getInstance().execute(request);
//    }
//
//    /**
//     * @return
//     */
//    public boolean cancel() {
//        if (request != null) {
//            return request.cancel();
//        }
//        return false;
//    }
//
//    /**
//     * @return
//     */
//    public boolean isRouteing() {
//        if (request != null) {
//            return request.isRouteing();
//        }
//        return false;
//    }
//
//    /**
//     * 应用当前路线进行导航
//     *
//     * @param apply 应用或取消应用
//     */
//    public void naviRoute(boolean apply) {
//        if (apply) {
//            RouteBase routeBase = getRouteInfos()[getSelectedRouteIndex()].getRouteBase();
//            // 日志
//            if (Log.isLoggable(LogTag.ENGINE_NAVI, Log.INFO)) {
//                StringBuilder sb = new StringBuilder().append(" -->> ") //
//                        .append(", routeBase = ").append(routeBase) //
//                        .append(", routeBase.getDescription() = ").append(routeBase.getDescription()) //
//                        ;
//                Log.i(LogTag.ENGINE_NAVI, sb.toString());
//            }
//            naviManager.startNavi(routeBase, NaviStatus.SIMULATING.isActive());
//        } else {
//            naviManager.getNaviSession().removeRoute();
//            //
//        }
//    }
//
//    public void naviRoute() {
//        RouteBase routeBase = getRouteInfos()[getSelectedRouteIndex()].getRouteBase();
//        naviManager.getNaviSession().takeRouteQuietly(routeBase);
//    }
//
//    /**
//     * 获取展示所有路线Rect
//     *
//     * @return
//     */
//    public Rect getFitRectForShowRoutes() {
//        Rect rect = null;
//        for (RouteInfo ri : getRouteInfos()) {
//            if (null == rect) {
//                rect = new Rect(ri.getRect());
//            } else {
//                rect.union(ri.getRect());
//            }
//        }
//        return rect;
//    }
//
//    public void AnalysisRouteFailEvent(String state, String errorCode, RoutePoisInfo innerInfo) {
//        RoutePoisInfo routePoisInfo;
//        if (innerInfo == null) {
//            routePoisInfo = getRoutePoisInfo();
//        } else {
//            routePoisInfo = innerInfo;
//        }
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("state", state);
//            PoiFavorite startFavorite = routePoisInfo.getStartPoi().toPoiFavorite();
//            int startVersion = getVersion(startFavorite);
//            jsonObject.put("begin", getJsonResult(startFavorite.toString(), startVersion));
//            if (routePoisInfo.getViaPois() != null) {
//                for (int i = 0; i < routePoisInfo.getViaPois().size(); i++) {
//                    PoiFavorite viaFavorite = routePoisInfo.getViaPois().get(i).toPoiFavorite();
//                    int viaVersion = getVersion(viaFavorite);
//                    jsonObject.put("via" + i, getJsonResult(viaFavorite.toString(), viaVersion));
//                }
//            }
//            PoiFavorite endFavorite = routePoisInfo.getEndPoi().toPoiFavorite();
//            int endVersion = getVersion(endFavorite);
//            jsonObject.put("end", getJsonResult(endFavorite.toString(), endVersion));
//            JSONObject avoidJson = new JSONObject();
//            avoidJson.put("rule", routePlan.getRule());
//            avoidJson.put("aovidRuleType", routePlan.getAvoidRoadType());
//            avoidJson.put("useTmc", routePlan.getUseTmc());
//            jsonObject.put("aovid", avoidJson);
//            jsonObject.put("errorCode", errorCode);
//            StringBuilder builder = new StringBuilder();
////            Poi poi = mapBarLocationManager.getLastPoi();
////            builder.append("lon=").append(poi.getLon()).append(",lat=").append(poi.getLat()).append(",locationType=").append(poi.getLocationType());
//            //此处从引擎拿到的为02坐标，所有从引擎拿到的坐标都为02
//            jsonObject.put("location", gpsTracker.getGpsInfo().toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 日志
//        if (Log.isLoggable(LogTag.ROUTE, Log.DEBUG)) {
//            Log.d(LogTag.ROUTE, " -->> " + jsonObject.toString());
//        }
//        UMengAnalysis.sendEvent(AnalysisConfigs.ROUTE_FAILED, jsonObject.toString());
//    }
//
//    private String getJsonResult(String favorite, int version) {
//        int num = favorite.lastIndexOf("]");
//        favorite = favorite.substring(0, num);
//        StringBuilder builder = new StringBuilder();
//        builder.append(favorite).append(", ").append("version=").append(version).append("]");
//        return builder.toString();
//    }
//
//    private int getVersion(PoiFavorite poiFavorite) {
//        int startVersion = -1;
//        String startProvince = cityManager.getProvinceName(poiFavorite.regionName);
//        if (!TextUtils.isEmpty(startProvince)) {
//            startVersion = NAmityDatastoreManager.getInstance().getNaviVersion(startProvince);
//        }
//        return startVersion;
//    }
//
//    public static class InstanceHolder {
//        private static final NaviRouteManager INSTANCE = new NaviRouteManager();
//    }
//
//    /**
//     * 偏航重算路监听器
//     */
//    private class RerouteListener implements Listener.GenericListener<RouteEventInfo> {
//        @Override
//        public void onEvent(RouteEventInfo eventInfo) {
//            // 提示用户
//            String tip = eventInfo.getTip();
//            if (!StringUtil.isEmpty(tip)) {
//                ToastUtil.showToast(tip);
//            }
//            NaviRouteEventType nrEventType = null;
//            switch (eventInfo.getEvent()) {
//                case STARTED:
//                    // 采集
//                    CollectionManager.getInstance().rerouteStarted();
//                    break;
//                case CANCELLED:
//                    // 采集
//                    CollectionManager.getInstance().rerouteCancelled();
//                    break;
//                case FAILED:
//                    AnalysisRouteFailEvent("AgainRoute", eventInfo.getRouterErrorCode() + "", null);
//                    // 采集
//                    CollectionManager.getInstance().rerouteFailed();
//                    break;
//                case COMPLETE:
//                    // 采集
//                    CollectionManager.getInstance().rerouteComplete();
//                    // 重算路成功，更新起点信息
//                    getRoutePoisInfo().setStartPoi(MapBarLocationManager.getInstance().getLastPoi());
//                    if (NaviStatus.NAVI_WALK.isActive()) {
//                        //产品需求，步行算路只保留一条路线
//                        walkRouteInfos = new RouteInfo[]{eventInfo.getRoutes()[0]};
//                    } else {
//                        carRouteInfos = eventInfo.getRoutes();
//                    }
//                    carSelectedRouteIndex = 0;
//                    naviManager.getNaviSession().takeRouteQuietly(getRouteInfos()[0].getRouteBase());
////                    //步行导航重新规划路线后 再次初始化车标的位置与方向
////                    if(NaviStatus.NAVI_WALK.isActive()){
////                        MyPositionHelper.getInstance().initPositionForNavi(getRouteInfo());
////                    }
//                    nrEventType = NaviRouteEventType.RE_COMPLETE;
//                    break;
//            }
//
//            // 通知
//            if (nrEventType != null) {
//                NaviRouteEventInfo nrEventInfo = new NaviRouteEventInfo();
//                nrEventInfo.setEvent(nrEventType);
//                listeners.conveyEvent(nrEventInfo);
//            }
//        }
//    }
//
//    private class TMCUpdateListener implements Listener.GenericListener<TMCUpdateEventInfo> {
//        @Override
//        public void onEvent(TMCUpdateEventInfo eventInfo) {
//            // 日志
//            if (Log.isLoggable(LogTag.TMC, Log.INFO)) {
//                StringBuilder sb = new StringBuilder().append(" -->> ") //
//                        .append(", eventInfo.getRouteBase() = ").append(eventInfo.getRouteBase()) //
//                        ;
//                Log.i(LogTag.TMC, sb.toString());
//            }
//            tmcUpdateChangeListeners.conveyEvent(new TMCUpdateEventInfo(eventInfo.getRouteBase()));
//        }
//    }
//
//    /**
//     * 算路监听器
//     */
//    private class RouteEventInfoListener implements Listener.GenericListener<RouteEventInfo> {
//        @Override
//        public void onEvent(RouteEventInfo eventInfo) {
//            // 提示用户
//            String tip = eventInfo.getTip();
//            if (!StringUtil.isEmpty(tip)) {
//                ToastUtil.showToast(tip);
//            }
//
//            NaviConfig naviConfig = eventInfo.getNaviConfig();
//
//            NaviRouteEventType nrEventType = null;
//            switch (eventInfo.getEvent()) {
//                case STARTED:
//                    // 采集
//                    CollectionManager.getInstance().routeStarted();
//                    break;
//                case CANCELLED:
//                    // 采集
//                    CollectionManager.getInstance().routeCancelled();
//                    nrEventType = NaviRouteEventType.CANCEL;
//                    //防止出现继续导航算路取消后，再重新算路后，直接开始导航的问题
//                    naviManager.setAutoNavi(false);
//                    resetRoutePoisInfo();
//
//                    // 如果取消算路, 需要重置群组导航相关状态
//                    if (NaviStatus.GROUP_NAVI.isActive()){
//                        NaviStatusController.InstanceHolder.naviStatusController.setGroupMode(false);
//                    }
//
//                    break;
//                case FAILED:
//                    // 采集
//                    CollectionManager.getInstance().routeFailed();
//                    nrEventType = NaviRouteEventType.FAIL;
//                    //防止出现继续导航算路失败后，再重新算路后，直接开始导航的问题
//                    naviManager.setAutoNavi(false);
//                    resetRoutePoisInfo();
//                    break;
//                case COMPLETE:
//                    // 采集
//                    CollectionManager.getInstance().routeComplete();
//                    if (NaviStatus.NAVI_WALK.isActive()) {
//                        walkRouteInfos = new RouteInfo[]{eventInfo.getRoutes()[0]};
//                    } else {
//                        carRouteInfos = eventInfo.getRoutes();
//                        if (walkRouteInfos == null) {
//                            walkRouteInfos = carRouteInfos;
//                        }
//                    }
//                    carSelectedRouteIndex = 0;
//                    nrEventType = NaviRouteEventType.COMPLETE;
////                    if(NaviStatus.NAVI_WALK.isActive()){
////                        MyPositionHelper.getInstance().initPositionForNavi(getRouteInfo());
////                    }
//                    break;
//            }
//
//            // 通知
//            if (nrEventType != null) {
//                NaviRouteEventInfo nrEventInfo = new NaviRouteEventInfo();
//                nrEventInfo.setEvent(nrEventType);
//                nrEventInfo.setNaviConfig(naviConfig);
//                listeners.conveyEvent(nrEventInfo);
//            }
//        }
//
//        private void resetRoutePoisInfo() {
//            // 算路失败/取消算路时，避免途径点影响下次算路
//            if (carRouteInfos != null) {
//                routePoisInfo.set(carRouteInfos[0].getRoutePoisInfo());
//            } else if (walkRouteInfos != null) {
//                routePoisInfo.set(walkRouteInfos[0].getRoutePoisInfo());
//            }
//
//            if (NaviStatus.NAVI_WALK.isActive() && NaviStatus.NAVI_RELATED.isActive()) {
//                NaviStatusController.InstanceHolder.naviStatusController.setWalkMode(false);
//            }
//        }
//    }
//
//    public void addWalkStatusChangedListener(Listener.GenericListener<BaseEventInfo> walkStatusChangedListener) {
//        this.walkStatusChangedListener = walkStatusChangedListener;
//    }
//
//    public void sendWalkStatusChangedEvent() {
//        if (walkStatusChangedListener != null) {
//            walkStatusChangedListener.onEvent(null);
//        }
//    }
//
//    public void removeWalkStatusChangedListener() {
//        this.walkStatusChangedListener = null;
//    }
//}