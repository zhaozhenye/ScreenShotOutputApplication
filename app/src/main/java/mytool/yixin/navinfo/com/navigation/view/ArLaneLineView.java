package mytool.yixin.navinfo.com.navigation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;

import mytool.yixin.navinfo.com.navigation.R;
import mytool.yixin.navinfo.com.navigation.controller.RoadLineController;
import mytool.yixin.navinfo.com.navigation.log.Log;
import mytool.yixin.navinfo.com.navigation.log.LogTag;
import mytool.yixin.navinfo.com.navigation.log.LogUtil;
import mytool.yixin.navinfo.com.navigation.manager.RoadLineManager;
import mytool.yixin.navinfo.com.navigation.utils.LayoutUtils;
import mytool.yixin.navinfo.com.navigation.utils.Utils;


/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class ArLaneLineView extends View {
    public ArLaneLineView(Context context) {
        super(context);
        init();
    }

    public ArLaneLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArLaneLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 车道线控制器
     */
    private RoadLineController roadLineController = RoadLineController.getInstance();

    /**
     * 设置的Drawable
     */
    private ArLaneLineDrawable mArLaneLineDrawable;
    private ArLaneLineDrawable mLandArLaneLineDrawable;
    /**
     * 判断车道线是否显示
     */
    private boolean isArLaneLineShow;

    /**
     * 是否是展开的widget
     */
    private boolean isExpandWidet;


    private void init() {
        if (isExpandWidet) {
            mArLaneLineDrawable = new ArLaneLineDrawable();
            Utils.setViewBackGroundDrawable(this, mArLaneLineDrawable);
        } else {
            mLandArLaneLineDrawable = new ArLaneLineDrawable();
            Utils.setViewBackGroundDrawable(this, mArLaneLineDrawable);

        }
    }

    /**
     * 获得车道线变化的通知
     */
    public void notifyLaneLineChange() {
        boolean isShow = roadLineController.isShowRoadLine();
        // 日志
        if (Log.isLoggable(LogTag.LANE, Log.INFO)) {
            StringBuilder sb = new StringBuilder().append(" -->> ").append(", isShow = ").append(isShow);
            Log.i(LogTag.LANE, sb.toString());
        }
//        if (isShow && isLazy()) {
//            use();
//        }
//        View contentView = getContentView();
//        if (contentView == null) {
//            return;
//        }
        if (isShow) {
            isArLaneLineShow = true;
            //获取车道线信息
            show(true);
            setArLaneLineList();
        } else {
            isArLaneLineShow = false;
            show(false);
        }

//        if (NaviStatus.NAVI_WALK.isActive() || !NaviStatus.TRACK_NAVI.isActive() || !isShow) {
//            isArLaneLineShow = false;
//            show(false);
//        } else {
//            isArLaneLineShow = true;
//            //获取车道线信息
//            show(true);
//            setArLaneLineList();
//
//        }
    }

    /**
     * 获得车道线变化及导航跟踪发生变化的通知
     */
    public void setArLaneLineList() {
        RoadLineManager.LaneType[] laneTypes = roadLineController.getLaneTypes();
        int roadLineRealWidth = roadLineController.getRoadLineRealWidth();
        if (laneTypes.length > 0) {
            if (isExpandWidet) {
                mArLaneLineDrawable.setLaneTypes(laneTypes, roadLineController.getScale());
            } else {
                mLandArLaneLineDrawable.setLaneTypes(laneTypes, roadLineController.getScale());

            }
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) getLayoutParams();
            lp.width = roadLineRealWidth;
            lp.validate();
            setLayoutParams(lp);
            getBackground().invalidateSelf();
        } else {
            setVisibility(View.GONE);
        }

    }

    private void show(boolean show) {
        setVisibility(show ? View.VISIBLE : View.GONE);
    }


    /**
     * 单个车道线图标的Drawable
     */
    private class ArLaneLineDrawable extends SimpleDrawable {

        /**
         * 进行个缓存操作，将之前得到的车道线图片缓存下来，方便下次去取
         */
        private HashMap<Integer, Drawable> arLaneLineDrawableHashMap;

        private final int ITEM_WIDTH = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_width);
        ;
        private final int ITEM_HEIGHT = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_height);
        private final int ICON_WIDTH = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_image_width);
        ;
        private final int ICON_HEIGHT = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_image_height);

        private int mItemWidth;
        private int mItemHeight;
        private int mIconWidth;
        private int mIconHeight;
        private int mLineWidth;
        private int mLineHeight;
        private Paint mLinePaint;


        private int mWidth;
        private int mHeight;
        private RectF mBackGroundBound = new RectF();
        private int mRadius;
        private Paint mBackgroundPaint;

        private RoadLineManager.LaneType[] mLaneTypes;

        public ArLaneLineDrawable() {
            arLaneLineDrawableHashMap = new HashMap<>(16);
            initData();
            initLinePaint();
            initBackGroundPaint();
        }

        /**
         * 初始化数据
         */
        private void initData() {
            mItemWidth = ITEM_WIDTH;
            mItemHeight = ITEM_HEIGHT;
            mIconWidth = ICON_WIDTH;
            mIconHeight = ICON_HEIGHT;
            mLineWidth = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_line_width);
            mLineHeight = LayoutUtils.getPxByDimens(R.dimen.arlane_line_viewer_item_line_height);
        }

        private void initLinePaint() {
            mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mLinePaint.setColor(LayoutUtils.getColorById(R.color.arlane_line_viewer_divider));
            mLinePaint.setStrokeWidth(mLineWidth);
        }

        private void initBackGroundPaint() {
            mRadius = LayoutUtils.getPxByDimens(R.dimen.space_6);
            mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBackgroundPaint.setColor(LayoutUtils.getColorById(R.color.BC2));
        }

        private void setLaneTypes(RoadLineManager.LaneType[] laneTypes, double scale) {
            mLaneTypes = laneTypes;
            mItemWidth = (int) (ITEM_WIDTH * scale);
            mIconWidth = (int) (ICON_WIDTH * scale);
            mIconHeight = (int) (ICON_HEIGHT * scale);

        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mWidth = bounds.width();
            mHeight = bounds.height();
            mBackGroundBound.set(0, 0, mWidth, mHeight);
        }

        @Override
        public int getIntrinsicHeight() {
            return ITEM_HEIGHT;
        }

        @Override
        public void draw(Canvas canvas) {
            if (isArLaneLineShow) {
                //drawBackGround
                canvas.drawRoundRect(mBackGroundBound, mRadius, mRadius, mBackgroundPaint);
                int length = mLaneTypes.length;
                int left, top, right, bottom;
                int leftOffset = (mItemWidth - mIconWidth) / 2;
                top = (mItemHeight - mIconHeight) / 2;
                int lineTop = (mItemHeight - mLineHeight) / 2;

                RoadLineManager.LaneType lane;
                //通过遍历车道线数据来进行绘制
                for (int i = 0; i < length; i++) {
                    lane = mLaneTypes[i];
                    Drawable drawable = getDrawable(lane);
                    left = i * (mItemWidth + mLineWidth) + leftOffset;
                    right = left + mIconWidth;
                    bottom = top + mIconHeight;
                    drawable.setBounds(left, top, right, bottom);
                    drawable.draw(canvas);
                    if (i != length - 1) {
                        //draw line
                        canvas.drawLine(right, lineTop, right, lineTop + mLineHeight, mLinePaint);
                    }
                }
            }
        }

        /**
         * 根据不同的路况信息，进行了设置drawable,同时进行缓存机制
         *
         * @param lane 获得的车道线数据
         * @return
         */
        private Drawable getDrawable(RoadLineManager.LaneType lane) {
            // 日志
            if (Log.isLoggable(LogTag.LANE, Log.INFO)) {
                StringBuilder sb = new StringBuilder().append(" -->> ").append(", lane = ").append(lane) //
                        ;
                Log.i(LogTag.LANE, sb.toString());
            }
            if (lane == null) {
                // 日志
                if (Log.isLoggable(LogTag.LANE, Log.DEBUG)) {
                    String msg = " -->> 没啥可显示的";
                    Log.d(LogTag.LANE, msg);
                    LogUtil.printConsole(msg);
                }
                return null;
            }
            int id = lane.getPicId();
            //获取车道的Drawable信息
            if (arLaneLineDrawableHashMap.get(id) == null) {
                Drawable drawable = getContext().getResources().getDrawable(id);
                // 日志
                if (Log.isLoggable(LogTag.LANE, Log.DEBUG)) {
                    Log.d(LogTag.LANE, "车道线的id -->> " + id);
                }
                arLaneLineDrawableHashMap.put(id, drawable);
                return drawable;
            } else {
                return arLaneLineDrawableHashMap.get(id);
            }
        }
    }


}
