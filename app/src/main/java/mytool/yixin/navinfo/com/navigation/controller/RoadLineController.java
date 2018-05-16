package mytool.yixin.navinfo.com.navigation.controller;

import mytool.yixin.navinfo.com.navigation.R;
import mytool.yixin.navinfo.com.navigation.manager.RoadLineManager;
import mytool.yixin.navinfo.com.navigation.utils.LayoutUtils;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class RoadLineController {






    private  boolean mExpandShow = false;

    private int mItemWidth;
    private int mLineWidth;
    /**
     * 横屏相关
     */
    private boolean mShowCompress = true;
    private boolean mShowTime = true;
    private boolean mShowRoadLine;
    private int mRoadLineRealWidth;
    public double mScale = 1.0;
    private int mPortraitInitWidth;


    /**
     * 横屏相关
     */
    private double mLandScale = 1.0;
    private boolean mLandShowCompress = true;
    private boolean mLandShowTime = true;
    private boolean mLandShowRoadLine;

    private int mLandInitWidth;
    private int mLandExpandInitWidth;
    private int mPortraitMaxWidth;
    private int mLandRoadLineRealWidth;
    private int mLandMaxWidth;
    private int mLandExpandMaxWidth;


    /**
     * 方屏相关的
     */
    private int mSquareInitWidth;
    private int mSquareRoadLineRealWidth;
    private int mSquareMaxWidth;
    private double  mSquareScale        = 1.0;
    private boolean mSquareShowCompress = true;
    private boolean mSquareShowTime     = true;
    private boolean mSquareShowRoadLine;

    public void setLaneTypes(RoadLineManager.LaneType[] laneTypes) {
        this.laneTypes = laneTypes;
    }

    /**
     * 获取最新的车道线信息
     */
    public RoadLineManager.LaneType[] getLaneTypes() {
        return laneTypes;
    }

    private RoadLineManager.LaneType[] laneTypes;

    private int mTotalDividerWidth;


    private RoadLineController() {
        mItemWidth = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_width);
        mLineWidth = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_line_width);

        int[] screenWH = LayoutUtils.getScreenWH();
        int compassWith = LayoutUtils.getPxByDimens(R.dimen.map_compass_navigate_width);
        int leftOffset = LayoutUtils.getPxByDimens(R.dimen.margin_slid_map_common_land);
        int rightOffset = LayoutUtils.getPxByDimens(R.dimen.margin_slid_map_common_land);
        int leftPanel = LayoutUtils.getPxByDimens(R.dimen.LAND_COMMON_PANEL_SPACE);
        int leftExpandPanel = LayoutUtils.getPxByDimens(R.dimen.expand_view_width_land);
        int timePanel = LayoutUtils.getPxByDimens(R.dimen.space_50)+LayoutUtils.getPxByDimens(R.dimen.space_15)*2;
        int squareLeftPanel = LayoutUtils.getPxByDimens(R.dimen.navigate_title_square_width);
        //宽总是短的，高总是长的
        mPortraitMaxWidth = screenWH[0] - leftOffset - rightOffset;
        mLandMaxWidth = screenWH[1] - leftPanel - leftOffset - rightOffset;
        mLandExpandMaxWidth = screenWH[1] - leftExpandPanel - leftOffset - rightOffset;
        mSquareMaxWidth = screenWH[0]- squareLeftPanel- leftOffset - rightOffset;

        mPortraitInitWidth = mPortraitMaxWidth - compassWith - leftOffset;

        mLandInitWidth = mLandMaxWidth - compassWith - leftOffset - timePanel - rightOffset;
        mLandExpandInitWidth = mLandExpandMaxWidth - compassWith - leftOffset - timePanel - rightOffset;
        mSquareInitWidth = mSquareMaxWidth  - compassWith - leftOffset - timePanel - rightOffset;
//        RoadLineManager.getInstance().addLaneListener(laneInfoGenericListener);

    }

    public void update() {
        laneTypes =getLaneTypes();
        //calculate
        calculateRoadLineWidth();
        //change flag
        changeFlagAndScale();
        // TODO: zhaozy 2018/5/15 RoadLineController update 这里需要更新view
//        EventManager.getInstance().sendToCycle(R.id.event_road_line_change);
    }
    private void calculateRoadLineWidth() {
        int length = 0;
        if(laneTypes != null) {
            length = laneTypes.length;
        }
        mRoadLineRealWidth = mLandRoadLineRealWidth = mSquareRoadLineRealWidth = 0;
        if (length > 0) {
            mTotalDividerWidth = mLineWidth * (length - 1);
            mRoadLineRealWidth = mLandRoadLineRealWidth= mSquareRoadLineRealWidth =mItemWidth * length + mTotalDividerWidth;
        }

    }

    private void changeFlagAndScale() {
        if(LayoutUtils.isSquare()){
            changeSquareFlagAndScale();
        }else {
            changeLandFlagAndScale();
            changePortraitFlagAndScale();
        }

    }

    private void changeSquareFlagAndScale() {
        mSquareScale = 1.0;
        int initMaxWith = mSquareInitWidth;
        int maxWidth = mSquareMaxWidth;

        if (mLandRoadLineRealWidth >= initMaxWith) {
            mSquareShowTime = false;
            mSquareShowCompress = false;
        } else {
            mSquareShowTime = true;
            mSquareShowCompress = true;
        }

        if (mLandRoadLineRealWidth >= maxWidth) {
            mSquareScale = (maxWidth - mTotalDividerWidth) * 1.0 / (mRoadLineRealWidth - mTotalDividerWidth);
            mSquareRoadLineRealWidth = maxWidth;
        }
    }

    private void changePortraitFlagAndScale() {
        int initMaxWith = mPortraitInitWidth;
        int maxWidth = mPortraitMaxWidth;
        mScale = 1.0;
        if (mRoadLineRealWidth >= initMaxWith) {
            mShowTime = false;
            mShowCompress = false;
        } else {
            mShowCompress = true;
            mShowTime = true;
        }

        if (mRoadLineRealWidth >= maxWidth) {
            mScale = (maxWidth - mTotalDividerWidth) * 1.0 / (mRoadLineRealWidth - mTotalDividerWidth);
            mRoadLineRealWidth = maxWidth;
        }

    }

    private void changeLandFlagAndScale() {
        mLandScale = 1.0;
        //横屏
        int initMaxWith = mLandInitWidth;
        int maxWidth = mLandMaxWidth;
        if(mExpandShow) {
            initMaxWith = mLandExpandInitWidth;
            maxWidth = mLandExpandMaxWidth ;
        }
        if (mLandRoadLineRealWidth >= initMaxWith) {
            mLandShowTime = false;
            mLandShowCompress = false;
        } else {
            mLandShowTime = true;
            mLandShowCompress = true;
        }

        if (mLandRoadLineRealWidth >= maxWidth) {
            mLandScale = (maxWidth - mTotalDividerWidth) * 1.0 / (mRoadLineRealWidth - mTotalDividerWidth);
            mLandRoadLineRealWidth = maxWidth;
        }


    }

    public boolean isShowCompress() {
        if(LayoutUtils.isSquare()){
            return mSquareShowCompress;
        }
        return LayoutUtils.isNotPortrait()? mLandShowCompress:mShowCompress;
    }

    public boolean isShowTime() {
        if(LayoutUtils.isSquare()){
            return mSquareShowTime;
        }
        return LayoutUtils.isNotPortrait()? mLandShowTime:mShowTime;

    }

    public boolean isShowRoadLine() {

        return mLandShowRoadLine;
    }

    public void setmShowRoadLine(boolean mShowRoadLine) {
        this.mShowRoadLine = mShowRoadLine;
    }

    public double getScale() {
        if(LayoutUtils.isSquare()){
            return mSquareScale;
        }
        return LayoutUtils.isNotPortrait()? mLandScale:mScale;
    }

    public static RoadLineController getInstance() {
        return Holder.instance;
    }

    /**
     * 得到车道线的真实宽度
     *
     * @return 得到车道线的真实宽度
     */
    public int getRoadLineRealWidth() {
        if(LayoutUtils.isSquare()){
            return mSquareRoadLineRealWidth;
        }
        return LayoutUtils.isNotPortrait()?mLandRoadLineRealWidth:mRoadLineRealWidth;
    }

    private static final class Holder {
        private static final RoadLineController instance = new RoadLineController();
    }
}
