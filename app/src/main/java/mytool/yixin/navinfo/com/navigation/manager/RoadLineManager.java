package mytool.yixin.navinfo.com.navigation.manager;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import mytool.yixin.navinfo.com.navigation.R;
import mytool.yixin.navinfo.com.navigation.bean.LaneModel;
import mytool.yixin.navinfo.com.navigation.bean.LanePainter;
import mytool.yixin.navinfo.com.navigation.bean.LaneUsage;
import mytool.yixin.navinfo.com.navigation.listener.BaseEventInfo;
import mytool.yixin.navinfo.com.navigation.listener.Listener;
import mytool.yixin.navinfo.com.navigation.listener.WeakGenericListeners;
import mytool.yixin.navinfo.com.navigation.log.Log;
import mytool.yixin.navinfo.com.navigation.log.LogTag;
import mytool.yixin.navinfo.com.navigation.log.LogUtil;

/**
 * 车道线管理器
 * Created by liangxin on 2017/6/21.
 */

public class RoadLineManager {

    /**
     * 车道线监听器
     */
    private LanePainter lanePainter;

    /**
     * 车道线监听器集合
     */
    private WeakGenericListeners<LaneInfo> listeners = new WeakGenericListeners<>();


    private int mEvent = -1;

    private LaneModel mLaneModel;


    private static final LaneType[] TYPE_EMPTY = new LaneType[0];

    private LaneInfo mlaneInfo = new LaneInfo();

    public void addLandListener(LanePainter.EventHandler eventHandler){
        lanePainter.addListener(eventHandler);
    }

    /**
     * 初始化车道线监听器
     */
    public void init() {
        if (lanePainter != null) {
            return;
        }
        lanePainter = new LanePainter();


//        lanePainter.addListener(new LanePainter.EventHandler() {
//            @Override
//            public void onLanePainterEvent(int event, LaneModel laneModel) {
//                // 日志
//                if (Log.isLoggable(LogTag.LANE, Log.DEBUG)) {
//                    String msg = " -->> onLanePainterEvent: event = " + event + ",laneModel = " + laneModel;
//                    Log.d(LogTag.LANE, msg);
//                }
//                boolean isNotify = event != mEvent || (event == LanePainter.Event.show && !isLaneModelEqual(mLaneModel, laneModel));
//                mEvent = event;
//                mLaneModel = laneModel;
//                if (isNotify) {
//                    switch (event) {
//                        case LanePainter.Event.hide:
//                            mlaneInfo.setLaneTypes(TYPE_EMPTY);
//                            mlaneInfo.setEvent(LaneVisibility.GONE);
//                            break;
//                        case LanePainter.Event.show:
//                            mlaneInfo.setLaneTypes(parser(laneModel));
//                            mlaneInfo.setEvent(LaneVisibility.VISIBILITY);
//                            break;
//                        default:
//                            break;
//                    }
//                    listeners.conveyEvent(mlaneInfo);
//                }
//
//
//            }
//        });
    }

    public LaneType[] parser(LaneModel laneModel) {
        // 日志
        if (Log.isLoggable(LogTag.LANE, Log.DEBUG)) {
            String msg = " -->> parser old : laneModel = " + laneModel;
            Log.d(LogTag.LANE, msg);
            LogUtil.printConsole(msg);
        }

        //获取当前车辆需要前进的方向
        LaneDirection laneDirection = parserLaneDirection(laneModel.driveArrow);

        //获取当前道路车道线信息
        LaneUsage[] lanes = laneModel.lanes;
        LaneType[] types = new LaneType[lanes.length];

        for (int i = 0; i < lanes.length; i++) {
            LaneDirection ld = LaneDirection.NONE;
            LaneStyle ls;

            //如果shouldUse为false，表示当前道路不可通行，使用LaneDirection.NONE。否则使用当前方向
            if (lanes[i].shouldUse) {
                ld = laneDirection;
            }
            ls = parserLaneStyle(lanes[i].arrow);
            Map<LaneDirection, LaneType> m = laneTypeCache.get(ls);
            if (m == null) {
                types[i] = LaneType.NONE;
            } else {
                LaneType laneType = m.get(ld);
                types[i] = (laneType == null ? LaneType.NONE : laneType);
            }
        }
        // 日志
        if (Log.isLoggable(LogTag.LANE, Log.DEBUG)) {
            String msg = " -->> parser new : types = " + Arrays.toString(types);
            Log.d(LogTag.LANE, msg);

        }
        return types;
    }

    private LaneStyle parserLaneStyle(int laneUsageArray) {
        switch (laneUsageArray) {
            case LaneUsage.LaneArrow.bus:
                return LaneStyle.BUS;
            case LaneUsage.LaneArrow.up:
                return LaneStyle.UP;
            case LaneUsage.LaneArrow.down:
                return LaneStyle.DOWN;
            case LaneUsage.LaneArrow.left:
                return LaneStyle.LEFT;
            case LaneUsage.LaneArrow.right:
                return LaneStyle.RIGHT;
            case LaneUsage.LaneArrow.leftUp:
                return LaneStyle.LEFT_UP;
            case LaneUsage.LaneArrow.rightUp:
                return LaneStyle.RIGHT_UP;
            case LaneUsage.LaneArrow.leftDown:
                return LaneStyle.LEFT_DOWN;
            case LaneUsage.LaneArrow.rightDown:
                return LaneStyle.RIGHT_DOWN;
            case LaneUsage.LaneArrow.upDown:
                return LaneStyle.UP_DOWN;
            case LaneUsage.LaneArrow.leftRight:
                return LaneStyle.LEFT_RIGHT;
            case LaneUsage.LaneArrow.leftUpRight:
                return LaneStyle.LEFT_UP_RIGHT;
            case LaneUsage.LaneArrow.downReversed:
                return LaneStyle.DOWN_REVERSED;
            case LaneUsage.LaneArrow.leftDownReversed:
                return LaneStyle.LEFT_DOWN_REVERSED;
            case LaneUsage.LaneArrow.rightDownReversed:
                return LaneStyle.RIGHT_DOWN_REVERSED;
            case LaneUsage.LaneArrow.upDownReversed:
                return LaneStyle.UP_DOWN_REVERSED;
            default:
                return null;
        }
    }

    private LaneDirection parserLaneDirection(int driveArrow) {
        switch (driveArrow) {
            case LaneUsage.LaneArrow.bus:
                return LaneDirection.BUS;
            case LaneUsage.LaneArrow.up:
                return LaneDirection.UP;
            case LaneUsage.LaneArrow.down:
                return LaneDirection.DOWN;
            case LaneUsage.LaneArrow.left:
                return LaneDirection.LEFT;
            case LaneUsage.LaneArrow.right:
                return LaneDirection.RIGHT;
            default:
                return LaneDirection.NONE;
        }
    }

    private RoadLineManager() {
        initLaneTypeCache();
    }

    /**
     * 将laneTypeCache中的数据保存到map里，方便查找
     */
    private void initLaneTypeCache() {
        int count = 0;

        for (LaneType lt : LaneType.values()) {
            if (laneTypeCache.containsKey(lt.getLaneStyle())) {
                LaneType put = laneTypeCache.get(lt.getLaneStyle()).put(lt.getLaneDirection(), lt);
                // 日志
                if (put != null && Log.isLoggable(LogTag.LANE, Log.DEBUG)) {
                    String msg = " -->> initLanetype：key(" + put + ") is exists";
                    Log.d(LogTag.LANE, msg);
                    LogUtil.printConsole(msg);
                }
                if (put == null) {
                    count++;
                }
            } else {
                Map<LaneDirection, LaneType> m = new HashMap<>();
                m.put(lt.getLaneDirection(), lt);
                laneTypeCache.put(lt.getLaneStyle(), m);
                count++;
            }
        }

        // 日志
        if (Log.isLoggable(LogTag.LANE, Log.DEBUG)) {
            String msg = " -->> count = " + count + ", enum.size = " + LaneType.values().length;
            Log.d(LogTag.LANE, msg);
            LogUtil.printConsole(msg);
        }


    }

    //单例
    public static RoadLineManager getInstance() {
        return Holder.instance;
    }

    private static final class Holder {
        private static final RoadLineManager instance = new RoadLineManager();
    }

    /**
     * 注册监听车道线的更新监听器
     */
    public void addLaneListener(Listener.GenericListener<LaneInfo> listener) {
        listeners.add(listener);
    }

    /**
     * 车道线监听器返回的数据对象
     */
    public static class LaneInfo extends BaseEventInfo<LaneVisibility> {

        /**
         * 路线样式数组。其中数组长度为车道个数，数组元素封装了车道线要展示的图片
         */
        private LaneType[] laneTypes;

        /**
         * 获取路线样式数组
         */
        public LaneType[] getLaneTypes() {
            return laneTypes;
        }

        /**
         * 设置路线样式数组
         */
        void setLaneTypes(LaneType[] laneTypes) {
            this.laneTypes = laneTypes;
        }
    }

    /**
     * 车道线的显式状态
     */
    public enum LaneVisibility {
        /**
         * 隐藏车道线
         */
        GONE, /**
         * 展示车道线
         */
        VISIBILITY
    }

    /**
     * 缓存LaneType索引，方便查找
     */
    private Map<LaneStyle, Map<LaneDirection, LaneType>> laneTypeCache = new HashMap<>();

    /**
     * 图片状态
     */
    public enum LaneType {
        //直行车道状态
        /*直行车道前行状态*/           UP_UP(LaneStyle.UP, LaneDirection.UP, RoadLinePicFinder.Up.up), /*直行道但不允许通行状态*/      UP_NONE(LaneStyle.UP, LaneDirection.NONE, RoadLinePicFinder.Up.none), /*直行道但不允许通行状态*/      UP_DOWN(LaneStyle.UP, LaneDirection.DOWN, RoadLinePicFinder.Up.none), /*直行道但不允许通行状态*/      UP_LEFT(LaneStyle.UP, LaneDirection.LEFT, RoadLinePicFinder.Up.none), /*直行道但不允许通行状态*/      UP_RIGHT(LaneStyle.UP, LaneDirection.RIGHT, RoadLinePicFinder.Up.none), /*直行道但不允许通行状态*/      UP_BUS(LaneStyle.UP, LaneDirection.BUS, RoadLinePicFinder.Up.none),

        //调头车道状态
        /*调头车道调头状态*/           DOWN_DOWN(LaneStyle.DOWN, LaneDirection.DOWN, RoadLinePicFinder.Down.down), /*调头车道不允许通行状态*/      DOWN_NONE(LaneStyle.DOWN, LaneDirection.NONE, RoadLinePicFinder.Down.none), /*调头车道不允许通行状态*/      DOWN_UP(LaneStyle.DOWN, LaneDirection.UP, RoadLinePicFinder.Down.none), /*调头车道不允许通行状态*/      DOWN_LEFT(LaneStyle.DOWN, LaneDirection.LEFT, RoadLinePicFinder.Down.none), /*调头车道不允许通行状态*/      DOWN_RIGHT(LaneStyle.DOWN, LaneDirection.RIGHT, RoadLinePicFinder.Down.none), /*调头车道不允许通行状态*/      DOWN_BUS(LaneStyle.DOWN, LaneDirection.BUS, RoadLinePicFinder.Down.none),

        //右调头车道状态
        /*右调头车道右调头状态*/        DOWN_REVERSED_DOWN_REVERSED(LaneStyle.DOWN_REVERSED, LaneDirection.DOWN, RoadLinePicFinder.DownReversed.down), /*右调头车道不允许通行状态*/     DOWN_REVERSED_NONE(LaneStyle.DOWN_REVERSED, LaneDirection.NONE, RoadLinePicFinder.DownReversed.none), /*右调头车道不允许通行状态*/     DOWN_REVERSED_UP(LaneStyle.DOWN_REVERSED, LaneDirection.UP, RoadLinePicFinder.DownReversed.none), /*右调头车道不允许通行状态*/     DOWN_REVERSED_LEFT(LaneStyle.DOWN_REVERSED, LaneDirection.LEFT, RoadLinePicFinder.DownReversed.none), /*右调头车道不允许通行状态*/     DOWN_REVERSED_RIGHT(LaneStyle.DOWN_REVERSED, LaneDirection.RIGHT, RoadLinePicFinder.DownReversed.none), /*右调头车道不允许通行状态*/     DOWN_REVERSED_BUS(LaneStyle.DOWN_REVERSED, LaneDirection.BUS, RoadLinePicFinder.DownReversed.none),

        //左转车道状态
        /*左转车道左转状态*/           LEFT_LEFT(LaneStyle.LEFT, LaneDirection.LEFT, RoadLinePicFinder.Left.left), /*左转车道不允许通行状态*/      LEFT_NONE(LaneStyle.LEFT, LaneDirection.NONE, RoadLinePicFinder.Left.none), /*左转车道不允许通行状态*/      LEFT_RIGHT(LaneStyle.LEFT, LaneDirection.RIGHT, RoadLinePicFinder.Left.none), /*左转车道不允许通行状态*/      LEFT_DOWN(LaneStyle.LEFT, LaneDirection.DOWN, RoadLinePicFinder.Left.none), /*左转车道不允许通行状态*/      LEFT_UP(LaneStyle.LEFT, LaneDirection.UP, RoadLinePicFinder.Left.none), /*左转车道不允许通行状态*/      LEFT_BUS(LaneStyle.LEFT, LaneDirection.BUS, RoadLinePicFinder.Left.none),

        //右转车道状态
        /*右转车道右转状态*/           RIGHT_RIGHT(LaneStyle.RIGHT, LaneDirection.RIGHT, RoadLinePicFinder.Right.right), /*右转车道不允许通行状态*/      RIGHT_NONE(LaneStyle.RIGHT, LaneDirection.NONE, RoadLinePicFinder.Right.none), /*右转车道不允许通行状态*/      RIGHT_LEFT(LaneStyle.RIGHT, LaneDirection.LEFT, RoadLinePicFinder.Right.none), /*右转车道不允许通行状态*/      RIGHT_DOWN(LaneStyle.RIGHT, LaneDirection.DOWN, RoadLinePicFinder.Right.none), /*右转车道不允许通行状态*/      RIGHT_UP(LaneStyle.RIGHT, LaneDirection.UP, RoadLinePicFinder.Right.none), /*右转车道不允许通行状态*/      RIGHT_BUS(LaneStyle.RIGHT, LaneDirection.BUS, RoadLinePicFinder.Right.none),

        //左转调头车道状态
        /*左转调头车道左转状态*/        LEFT_DOWN_LEFT(LaneStyle.LEFT_DOWN, LaneDirection.LEFT, RoadLinePicFinder.LeftDown.left), /*左转调头车道调头状态*/        LEFT_DOWN_DOWN(LaneStyle.LEFT_DOWN, LaneDirection.DOWN, RoadLinePicFinder.LeftDown.down), /*左转调头车道不允许通行状态*/   LEFT_DOWN_NONE(LaneStyle.LEFT_DOWN, LaneDirection.NONE, RoadLinePicFinder.LeftDown.none), /*左转调头车道不允许通行状态*/   LEFT_DOWN_RIGHT(LaneStyle.LEFT_DOWN, LaneDirection.RIGHT, RoadLinePicFinder.LeftDown.none), /*左转调头车道不允许通行状态*/   LEFT_DOWN_UP(LaneStyle.LEFT_DOWN, LaneDirection.UP, RoadLinePicFinder.LeftDown.none), /*左转调头车道不允许通行状态*/   LEFT_DOWN_BUS(LaneStyle.LEFT_DOWN, LaneDirection.BUS, RoadLinePicFinder.LeftDown.none),

        //左转右调头车道状态
        /*左转右调头车道左转状态*/     LEFT_DOWN_REVERSED_LEFT(LaneStyle.LEFT_DOWN_REVERSED, LaneDirection.LEFT, RoadLinePicFinder.LeftDownReversed.left), /*左转右调头车道的右调头状态*/  LEFT_DOWN_REVERSED_DOWN(LaneStyle.LEFT_DOWN_REVERSED, LaneDirection.DOWN, RoadLinePicFinder.LeftDownReversed.down), /*左转右调头车道的不允许通行状态*/LEFT_DOWN_REVERSED_NONE(LaneStyle.LEFT_DOWN_REVERSED, LaneDirection.NONE, RoadLinePicFinder.LeftDownReversed.none), /*左转右调头车道的不允许通行状态*/LEFT_DOWN_REVERSED_RIGHT(LaneStyle.LEFT_DOWN_REVERSED, LaneDirection.RIGHT, RoadLinePicFinder.LeftDownReversed.none), /*左转右调头车道的不允许通行状态*/LEFT_DOWN_REVERSED_UP(LaneStyle.LEFT_DOWN_REVERSED, LaneDirection.UP, RoadLinePicFinder.LeftDownReversed.none), /*左转右调头车道的不允许通行状态*/LEFT_DOWN_REVERSED_BUS(LaneStyle.LEFT_DOWN_REVERSED, LaneDirection.BUS, RoadLinePicFinder.LeftDownReversed.none),

        //左右转弯车道状态
        /*左右转弯左转弯状态*/         LEFT_RIGHT_LEFT(LaneStyle.LEFT_RIGHT, LaneDirection.LEFT, RoadLinePicFinder.LeftRight.left), /*左右转弯右转弯状态*/         LEFT_RIGHT_RIGHT(LaneStyle.LEFT_RIGHT, LaneDirection.RIGHT, RoadLinePicFinder.LeftRight.right), /*左右转弯不允许通行状态*/      LEFT_RIGHT_NONE(LaneStyle.LEFT_RIGHT, LaneDirection.NONE, RoadLinePicFinder.LeftRight.none), /*左右转弯不允许通行状态*/      LEFT_RIGHT_UP(LaneStyle.LEFT_RIGHT, LaneDirection.UP, RoadLinePicFinder.LeftRight.none), /*左右转弯不允许通行状态*/      LEFT_RIGHT_DOWN(LaneStyle.LEFT_RIGHT, LaneDirection.DOWN, RoadLinePicFinder.LeftRight.none), /*左右转弯不允许通行状态*/      LEFT_RIGHT_BUS(LaneStyle.LEFT_RIGHT, LaneDirection.BUS, RoadLinePicFinder.LeftRight.none),

        //左前车道状态
        /*左前车道不允许通行状态*/      LEFT_UP_NONE(LaneStyle.LEFT_UP, LaneDirection.NONE, RoadLinePicFinder.LeftUp.none), /*左前车道左转*/              LEFT_UP_LEFT(LaneStyle.LEFT_UP, LaneDirection.LEFT, RoadLinePicFinder.LeftUp.left), /*左前车道直行*/              LEFT_UP_UP(LaneStyle.LEFT_UP, LaneDirection.UP, RoadLinePicFinder.LeftUp.up), /*左前车道直行*/              LEFT_UP_DOWN(LaneStyle.LEFT_UP, LaneDirection.DOWN, RoadLinePicFinder.LeftUp.none), /*左前车道直行*/              LEFT_UP_RIGHT(LaneStyle.LEFT_UP, LaneDirection.RIGHT, RoadLinePicFinder.LeftUp.none), /*左前车道直行*/              LEFT_UP_BUS(LaneStyle.LEFT_UP, LaneDirection.BUS, RoadLinePicFinder.LeftUp.none),

        //右前车道状态
        /*右前车道右转状态*/           RIGHT_UP_RIGHT(LaneStyle.RIGHT_UP, LaneDirection.RIGHT, RoadLinePicFinder.RightUp.right), /*右前车道前行状态*/           RIGHT_UP_UP(LaneStyle.RIGHT_UP, LaneDirection.UP, RoadLinePicFinder.RightUp.up), /*右前车道不允许通行状态*/      RIGHT_UP_NONE(LaneStyle.RIGHT_UP, LaneDirection.NONE, RoadLinePicFinder.RightUp.none), /*右前车道不允许通行状态*/      RIGHT_UP_LEFT(LaneStyle.RIGHT_UP, LaneDirection.LEFT, RoadLinePicFinder.RightUp.none), /*右前车道不允许通行状态*/      RIGHT_UP_DOWN(LaneStyle.RIGHT_UP, LaneDirection.DOWN, RoadLinePicFinder.RightUp.none), /*右前车道不允许通行状态*/      RIGHT_UP_BUS(LaneStyle.RIGHT_UP, LaneDirection.BUS, RoadLinePicFinder.RightUp.none),

        //右转弯或调头车道状态
        /*右转弯或调头车道右转弯状态*/   RIGHT_DOWN_RIGHT(LaneStyle.RIGHT_DOWN, LaneDirection.RIGHT, RoadLinePicFinder.RightDown.right), /*右转弯或调头车道调头状态*/    RIGHT_DOWN_DOWN(LaneStyle.RIGHT_DOWN, LaneDirection.DOWN, RoadLinePicFinder.RightDown.down), /*右转弯或者调头车道不允许通行状态*/RIGHT_DOWN_NONE(LaneStyle.RIGHT_DOWN, LaneDirection.NONE, RoadLinePicFinder.RightDown.none), /*右转弯或者调头车道不允许通行状态*/RIGHT_DOWN_UP(LaneStyle.RIGHT_DOWN, LaneDirection.UP, RoadLinePicFinder.RightDown.none), /*右转弯或者调头车道不允许通行状态*/RIGHT_DOWN_LEFT(LaneStyle.RIGHT_DOWN, LaneDirection.LEFT, RoadLinePicFinder.RightDown.none), /*右转弯或者调头车道不允许通行状态*/RIGHT_DOWN_BUS(LaneStyle.RIGHT_DOWN, LaneDirection.BUS, RoadLinePicFinder.RightDown.none),

        //右转弯或右调头车道状态
        /*右转弯或右调头车道右转弯状态*/ RIGHT_DOWN_REVERSED_RIGHT(LaneStyle.RIGHT_DOWN_REVERSED, LaneDirection.RIGHT, RoadLinePicFinder.RightDownReversed.right), /*右转弯或者右调头车道右调头状态*/RIGHT_DOWN_REVERSED_DOWN(LaneStyle.RIGHT_DOWN_REVERSED, LaneDirection.DOWN, RoadLinePicFinder.RightDownReversed.down), /*右转弯或者右调头车道不允许通行状态*/RIGHT_DOWN_REVERSED_NONE(LaneStyle.RIGHT_DOWN_REVERSED, LaneDirection.NONE, RoadLinePicFinder.RightDownReversed.none), /*右转弯或者右调头车道不允许通行状态*/RIGHT_DOWN_REVERSED_LEFT(LaneStyle.RIGHT_DOWN_REVERSED, LaneDirection.LEFT, RoadLinePicFinder.RightDownReversed.none), /*右转弯或者右调头车道不允许通行状态*/RIGHT_DOWN_REVERSED_UP(LaneStyle.RIGHT_DOWN_REVERSED, LaneDirection.UP, RoadLinePicFinder.RightDownReversed.none), /*右转弯或者右调头车道不允许通行状态*/RIGHT_DOWN_REVERSED_BUS(LaneStyle.RIGHT_DOWN_REVERSED, LaneDirection.BUS, RoadLinePicFinder.RightDownReversed.none),

        //前行调头车道状态
        /*前行调头车道前行状态*/        UP_DOWN_UP(LaneStyle.UP_DOWN, LaneDirection.UP, RoadLinePicFinder.UpDown.up), /*前行调头车道调头方向状态*/    UP_DOWN_DOWN(LaneStyle.UP_DOWN, LaneDirection.DOWN, RoadLinePicFinder.UpDown.down), /*前行调头车道不允许通行状态*/   UP_DOWN_NONE(LaneStyle.UP_DOWN, LaneDirection.NONE, RoadLinePicFinder.UpDown.none), /*前行调头车道不允许通行状态*/   UP_DOWN_LEFT(LaneStyle.UP_DOWN, LaneDirection.LEFT, RoadLinePicFinder.UpDown.none), /*前行调头车道不允许通行状态*/   UP_DOWN_RIGHT(LaneStyle.UP_DOWN, LaneDirection.RIGHT, RoadLinePicFinder.UpDown.none), /*前行调头车道不允许通行状态*/   UP_DOWN_BUS(LaneStyle.UP_DOWN, LaneDirection.BUS, RoadLinePicFinder.UpDown.none),

        //前行右调头车道状态
        /*前行右调头车道前行状态*/       UP_DOWN_REVERSED_UP(LaneStyle.UP_DOWN_REVERSED, LaneDirection.UP, RoadLinePicFinder.UpDownReversed.up), /*前行右调头车道右调头状态*/     UP_DOWN_REVERSED_DOWN(LaneStyle.UP_DOWN_REVERSED, LaneDirection.DOWN, RoadLinePicFinder.UpDownReversed.down), /*前行右调头车道不允许通行状态*/  UP_DOWN_REVERSED_NONE(LaneStyle.UP_DOWN_REVERSED, LaneDirection.NONE, RoadLinePicFinder.UpDownReversed.none), /*前行右调头车道不允许通行状态*/  UP_DOWN_REVERSED_LEFT(LaneStyle.UP_DOWN_REVERSED, LaneDirection.LEFT, RoadLinePicFinder.UpDownReversed.none), /*前行右调头车道不允许通行状态*/  UP_DOWN_REVERSED_RIGHT(LaneStyle.UP_DOWN_REVERSED, LaneDirection.RIGHT, RoadLinePicFinder.UpDownReversed.none), /*前行右调头车道不允许通行状态*/  UP_DOWN_REVERSED_BUS(LaneStyle.UP_DOWN_REVERSED, LaneDirection.BUS, RoadLinePicFinder.UpDownReversed.none),

        //左右前车道状态
        /*左右前车道时的前行状态*/      LEFT_UP_RIGHT_UP(LaneStyle.LEFT_UP_RIGHT, LaneDirection.UP, RoadLinePicFinder.LeftUpRight.up), /*左右前车道时的左转状态*/      LEFT_UP_RIGHT_LEFT(LaneStyle.LEFT_UP_RIGHT, LaneDirection.LEFT, RoadLinePicFinder.LeftUpRight.left), /*左右前车道时的右转状态*/      LEFT_UP_RIGHT_RIGHT(LaneStyle.LEFT_UP_RIGHT, LaneDirection.RIGHT, RoadLinePicFinder.LeftUpRight.right), /*左右前车道但不允许通行状态*/   LEFT_UP_RIGHT_NONE(LaneStyle.LEFT_UP_RIGHT, LaneDirection.NONE, RoadLinePicFinder.LeftUpRight.none), /*左右前车道但不允许通行状态*/   LEFT_UP_RIGHT_DOWN(LaneStyle.LEFT_UP_RIGHT, LaneDirection.DOWN, RoadLinePicFinder.LeftUpRight.none), /*左右前车道但不允许通行状态*/   LEFT_UP_RIGHT_BUS(LaneStyle.LEFT_UP_RIGHT, LaneDirection.BUS, RoadLinePicFinder.LeftUpRight.none),

        //公交专用车道状态(通过shouldUse表示来确定bus是否点亮)
        /*公交专用车道可以通行*/        BUS_BUS(LaneStyle.BUS, LaneDirection.BUS, RoadLinePicFinder.Bus.bus), /*公交专用车道不允许通行*/      BUS_NONE(LaneStyle.BUS, LaneDirection.NONE, RoadLinePicFinder.Bus.none), /*公交专用车道不允许通行*/      BUS_UP(LaneStyle.BUS, LaneDirection.UP, RoadLinePicFinder.Bus.bus), /*公交专用车道不允许通行*/      BUS_DOWN(LaneStyle.BUS, LaneDirection.DOWN, RoadLinePicFinder.Bus.bus), /*公交专用车道不允许通行*/      BUS_LEFT(LaneStyle.BUS, LaneDirection.LEFT, RoadLinePicFinder.Bus.bus), /*公交专用车道不允许通行*/      BUS_RIGHT(LaneStyle.BUS, LaneDirection.RIGHT, RoadLinePicFinder.Bus.bus),

        //其他状态
        /*无效状态*/                  NONE(null, null, R.drawable.icon_lane_00);

        public LaneStyle getLaneStyle() {
            return laneStyle;
        }

        public LaneDirection getLaneDirection() {
            return laneDirection;
        }

        public int getPicId() {
            return picId;
        }

        /**
         * 路线样式状态
         */
        private LaneStyle laneStyle;
        /**
         * 路线方向图片
         */
        private LaneDirection laneDirection;

        /**
         * 图片id
         */
        private int picId;

        LaneType(LaneStyle laneStyle, LaneDirection laneDirection, int picId) {
            this.laneDirection = laneDirection;
            this.laneStyle = laneStyle;
            this.picId = picId;
        }

        @Override
        public String toString() {
            return "LaneType{" + "laneStyle=" + laneStyle + ", laneDirection=" + laneDirection + ", picId=" + picId + '}';
        }
    }

    /**
     * 表示路口状态，如道路允许前行或左转
     * 含义参考引擎 LaneUsage类中的arrow属性
     */
    public enum LaneStyle {
        /**
         * 公交专用道路
         */
        BUS, /**
         * 只允许调头的路段
         */
        DOWN, /**
         * 只允许右调头的路段
         */
        DOWN_REVERSED, /**
         * 只允许左转的路段
         */
        LEFT, /**
         * 允许左转或者调头
         */
        LEFT_DOWN, /**
         * 允许左转或者右调头
         */
        LEFT_DOWN_REVERSED, /**
         * 允许左转或者右转
         */
        LEFT_RIGHT, /**
         * 当前车道既可左转，又可直行
         */
        LEFT_UP, /**
         * 当前车道既可左转，又可直行，也可右转
         */
        LEFT_UP_RIGHT, /**
         * 右转
         */
        RIGHT, /**
         * 当前车道既可右转，又可掉头
         */
        RIGHT_DOWN, /**
         * 当前车道既可右转，又可右掉头
         */
        RIGHT_DOWN_REVERSED, /**
         * 当前车道既可右转，又可直行
         */
        RIGHT_UP, /**
         * 直行
         */
        UP, /**
         * 当前车道既可直行，又可掉头
         */
        UP_DOWN, /**
         * 当前车道既可直行，又可右掉头
         */
        UP_DOWN_REVERSED
    }

    /**
     * 表示路口处的需要的行进方向
     * 含义参考引擎LaneModel中的driveArrow属性
     */
    public enum LaneDirection {
        /**
         * 公交专用道，不具有方向
         */
        BUS, /**
         * 前行
         */
        UP, /**
         * 调头
         */
        DOWN, /**
         * 左转
         */
        LEFT, /**
         * 右转
         */
        RIGHT, /**
         * 车道不能行走
         */
        NONE
    }

    /**
     * 判断两个LaneModel是否相等
     */
    private boolean isLaneModelEqual(LaneModel lm1, LaneModel lm2) {
        if (lm1 == null && lm2 == null) {
            return true;
        }
        if (lm1 == null || lm2 == null) {
            return false;
        }
        if (lm1.driveArrow != lm2.driveArrow) {
            return false;
        }
        if (lm1.laneNumber != lm2.laneNumber) {
            return false;
        }
        LaneUsage[] lanes1 = lm1.lanes;
        LaneUsage[] lanes2 = lm2.lanes;
        if (lanes1 == null && lanes2 == null) {
            return true;
        }
        if (lanes1 == null || lanes2 == null) {
            return false;
        }
        if (lanes1.length != lanes2.length) {
            return false;
        }
        for (int i = 0; i < lanes1.length; i++) {
            if (lanes1[i].shouldUse != lanes2[i].shouldUse) {
                return false;
            }
            if (lanes1[i].arrow != lanes2[i].arrow) {
                return false;
            }
        }
        return true;
    }
}
