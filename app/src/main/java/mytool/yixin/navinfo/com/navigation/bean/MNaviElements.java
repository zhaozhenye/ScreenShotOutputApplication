package mytool.yixin.navinfo.com.navigation.bean;

import mytool.yixin.navinfo.com.navigation.R;

/**
 * Created by zhangtj on 2015/8/11.
 */
public class MNaviElements {



    //转向标常量值com.mapbar.navi.RouteDescriptionItem.TurnIconID
    public static int engineIconId2ActionBigIconResourceId(int engineIconId) {
        switch (engineIconId) {
            case 1:
                return R.drawable.navi_big_end;// 终点 1
            case 2:
                return R.drawable.navi_big_turn_icons2; // 掉头 2
            case 3:
                return R.drawable.navi_big_turn_icons3; // 左前 3
            case 4:
                return R.drawable.navi_big_turn_icons4; // 右前 4
            case 5:
                return R.drawable.navi_big_turn_icons5; // 直行 5
            case 6:
                return R.drawable.navi_big_turn_icons6; // 右前 6
            case 7:
                return R.drawable.navi_big_turn_icons7; // 左前 7
            case 8:
                return R.drawable.navi_big_turn_icons8; // 左转 8
            case 9:
                return R.drawable.navi_big_turn_icons9; // 右转
            case 10:
                return R.drawable.navi_big_turn_icons10; // 左前
            case 11:
                return R.drawable.navi_big_turn_icons11; // 右前
            case 12:
                return R.drawable.navi_big_turn_icons12; // 环岛第一个出口
            case 13:
                return R.drawable.navi_big_turn_icons13; // 环岛第2个出口
            case 14:
                return R.drawable.navi_big_turn_icons14; // 环岛第3个出口
            case 15:
                return R.drawable.navi_big_turn_icons15; // 环岛第4个出口
            case 16:
                return R.drawable.navi_big_turn_icons16; // 环岛第5个出口
            case 17:
                return R.drawable.navi_big_turn_icons17; // 环岛第6个出口
            case 18:
                return R.drawable.navi_big_turn_icons18; // 环岛第7个出口
            case 19:
                return R.drawable.navi_big_turn_icons19; // 环岛第8个出口
            case 20:
                return R.drawable.navi_big_turn_icons20; // 环岛第9个出口
            case 21:
                return R.drawable.navi_big_turn_icons21; // 保持左侧行驶
            case 22:
                return R.drawable.navi_big_turn_icons22; // 保持右侧行驶
            case 23:
                return R.drawable.navi_big_turn_icons23; // 向左急转弯
            case 24:
                return R.drawable.navi_big_turn_icons24; // 向右急转弯
            case 25:
                return R.drawable.navi_big_turn_icons25; // 向左转并保持左侧行驶
            case 26:
                return R.drawable.navi_big_turn_icons26; // 向左转并保持右侧侧行驶
            case 27:
                return R.drawable.navi_big_turn_icons27; // 向右转并保持左侧行驶
            case 28:
                return R.drawable.navi_big_turn_icons28; // 向右转并保持右侧行驶
            case 29:
                return R.drawable.navi_big_turn_icons29; // 进入隧道29
            case 30:
                return R.drawable.navi_big_turn_icons30; // 坐船30
            case 31:
                return R.drawable.navi_big_start;// 起点 31 ic_maps_indicator_startpoint_list_big
            case 32:
                return R.drawable.navi_big_turn_icons32; // 途经点 1
            case 33:
                return R.drawable.navi_big_turn_icons33; // 途经点 2
            case 34:
                return R.drawable.navi_big_turn_icons34; // 途经点 3 34
            case 35:
                return R.drawable.navi_big_turn_icons35; // 35 出口指示
            case 36:
                return R.drawable.navi_big_turn_icons36;// 36 方向指示
            case 37:
                return R.drawable.navi_big_turn_icons37;// 37 非盘桥立交桥
            case 38:
                return R.drawable.navi_big_turn_icons38;// 38盘桥立交桥
            case 39:
                return R.drawable.navi_big_turn_icons39; // 39 靠左直行
            case 40:
                return R.drawable.navi_big_turn_icons40; // 40 靠右直行
            case 41:
                return R.drawable.navi_big_start; // 41 无效值 原值为 ic_bound
            case 43:
                return R.drawable.navi_big_turn_icons43;
            case 44:
                return R.drawable.navi_big_turn_icons44;
            case 45:
                return R.drawable.navi_big_turn_icons45;
            case 46:
                return R.drawable.navi_big_turn_icons46;
            case 47:
                return R.drawable.navi_big_turn_icons47;
            case 48:
                return R.drawable.navi_big_turn_icons48;
            //引擎新增4个转向标
            //https://jira.mapbar.com/browse/NAVICORE-1956
            case 49:
                return R.drawable.navi_big_turn_icons49;
            case 50:
                return R.drawable.navi_big_turn_icons50;
            case 51:
                return R.drawable.navi_big_turn_icons51;
            case 52:
                return R.drawable.navi_big_turn_icons52;

            // 下面是左车道行驶路段（香港）的图标
            case 1002:
                return R.drawable.navi_big_turn_icons1002; // 1002 掉头
            case 1004:
                return R.drawable.navi_big_turn_icons1004; // 1004 进入环岛
            case 1007:
                return R.drawable.navi_big_turn_icons1007; // 1007 离开环岛
            case 1012:
                return R.drawable.navi_big_turn_icons1012; // 1012 环岛第1个出口
            case 1013:
                return R.drawable.navi_big_turn_icons1013; // 1013 环岛第2个出口
            case 1014:
                return R.drawable.navi_big_turn_icons1014; // 1014 环岛第3个出口
            case 1015:
                return R.drawable.navi_big_turn_icons1015; // 1015 环岛第4个出口
            case 1016:
                return R.drawable.navi_big_turn_icons1016; // 1016 环岛第5个出口
            case 1017:
                return R.drawable.navi_big_turn_icons1017; // 1017 环岛第6个出口
            case 1018:
                return R.drawable.navi_big_turn_icons1018; // 1018 环岛第7个出口
            case 1019:
                return R.drawable.navi_big_turn_icons1019; // 1019 环岛第8个出口
            case 1020:
                return R.drawable.navi_big_turn_icons1020; // 1020 环岛第9个出口

            default:
                return R.drawable.navi_big_start; // 41 无效值 原值为 ic_bound
        }
    }





}
