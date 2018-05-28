//package mytool.yixin.navinfo.com.navigation;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import mytool.yixin.navinfo.com.navigation.bean.NaviDataChangeEventInfo;
//import mytool.yixin.navinfo.com.navigation.bean.TmcSections;
//import mytool.yixin.navinfo.com.navigation.controller.NaviController;
//import mytool.yixin.navinfo.com.navigation.manager.RoadLineManager;
//
///**
// * 手车互联广播接收者.
// */
//public class WeLinkMessageReceive extends BroadcastReceiver {
//    private static final String FIELD_COMMAND = "command";
//
//
//
//
//    private static final String ACTION_SEND_NAVI_DATA = "com.wedrive.action.NAVI_COMMAND_RESULT";
//    private static final String ACTION_SEND_TMC_DATA = "com.wedrive.action.TMC_COMMAND_RESULT";
//    private static final String ACTION_SEND_ROAD_LINE_DATA = "com.wedrive.action.ROAD_LINE_COMMAND_RESULT";
//    private static final String EXTRA_WELINK = "com.wedrive.extra.COMMAND_DATA";
//    private static final String EXTRA_SHOW_ROAD_LINE = "com.wedrive.extra.SHOW_ROAD_LINE";
//
//
//    NaviController naviController = NaviController.getInstance();
//    RoadLineManager roadLineManager = RoadLineManager.getInstance();
//
//
//    private static final String ACTION_QUERY_WELINK_STATE = "com.wedrive.action.COMMAND_SEND";
//
//    private static final String FIELD_MODULE_NAME = "moduleName";
//    private static final String DYNAMIC_LAUNCHER = "DynamicLauncher";
//    private static final String FIELD_VERSION = "version";
//    private static final String FIELD_METHOD = "method";
//    /**
//     * 询问互联状态
//     */
//    private static final String METHOD_QUERY_WELINK_STATE = "getWeLinkState";
//    /**
//     * 开始数据交互；目前是指 Launcher 进入前台
//     */
//    private static final String METHOD_START_INTERACTION = "startInteraction";
//    /**
//     * 结束数据交互；目前是指 Launcher 进入后台
//     */
//    private static final String METHOD_STOP_INTERACTION = "endInteraction";
//
//
//
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        String action = intent.getAction();
//        if (action.equalsIgnoreCase(ACTION_SEND_NAVI_DATA)){
//            Bundle extras = intent.getExtras();
//            String data = (String) extras.get(EXTRA_WELINK);
//            try {
//                System.out.println("naviDataReceiver :data:"+data);
//                JSONObject jsonObject = new JSONObject(data);
//                String moduleName = jsonObject.optString("moduleName");
//                String version = jsonObject.optString("version");
//                JSONObject commandJson = jsonObject.optJSONObject("command");
//                String method = commandJson.optString("method");
//
////                    if (METHOD_NAVI_STOPED.equalsIgnoreCase(method)){
////                        showNaviIng(false);
////                        return;
////                    }
//
//
//                JSONObject extDataJson = commandJson.optJSONObject("extData");
//                if (extDataJson != null) {
//                    showNaviIng(true);
//                    String routeBase = extDataJson.optString("routeBase");
//                    TmcSections tmcSections = new Gson().fromJson(routeBase, TmcSections.class);
//                    naviController.setTmcSections(tmcSections);
//
//                    NaviDataChangeEventInfo naviDataChangeEventInfo = wrapData(extDataJson);
//                    naviController.setNaviDataInfo(naviDataChangeEventInfo);
//                    System.out.println("naviData tmcSections 的值 ：" + naviController.getTmcSections());
//                    naviTitleView.update();
//                    mapTmcView.updateUI();
//                } else {
//                    showNaviIng(false);
//                    Toast.makeText(MainActivity.this, "恭喜到达目的地", Toast.LENGTH_SHORT).show();
//                }
//
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }
//
//
//    }
//}
