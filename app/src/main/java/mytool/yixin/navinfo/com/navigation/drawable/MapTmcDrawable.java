package mytool.yixin.navinfo.com.navigation.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import mytool.yixin.navinfo.com.navigation.R;
import mytool.yixin.navinfo.com.navigation.bean.NaviDataChangeEventInfo;
import mytool.yixin.navinfo.com.navigation.bean.TmcSections;
import mytool.yixin.navinfo.com.navigation.controller.NaviController;
import mytool.yixin.navinfo.com.navigation.log.Log;
import mytool.yixin.navinfo.com.navigation.log.LogTag;
import mytool.yixin.navinfo.com.navigation.utils.GlobalUtil;
import mytool.yixin.navinfo.com.navigation.utils.LayoutUtils;

/**
 * INFO: SUB_VIEWER/路况条信息
 */
public class MapTmcDrawable extends Drawable {

    private static final SparseIntArray TMC_COLOR = new SparseIntArray();

    static {
        TMC_COLOR.put(TmcSections.State.light, Color.parseColor("#ff00b700"));
        TMC_COLOR.put(TmcSections.State.medium, Color.parseColor("#ffffbd00"));
        TMC_COLOR.put(TmcSections.State.heavy, Color.parseColor("#ffff4d4d"));
        TMC_COLOR.put(TmcSections.State.blocked, Color.parseColor("#ffaaaaaa"));
        TMC_COLOR.put(TmcSections.State.none, Color.parseColor("#ff3c78ff"));
        TMC_COLOR.put(TmcSections.State.unknown, Color.parseColor("#ff3c78ff"));
    }

    /**
     * 获取车标icon的宽
     */
    int iconWidth = LayoutUtils.getPxByDimens(GlobalUtil.getResources(), R.dimen.map_tmc_drawable_car_icon_width);
    int iconHeight = LayoutUtils.getPxByDimens(GlobalUtil.getResources(), R.dimen.map_tmc_drawable_car_icon_height);
    int mHorizontalIconHeight = LayoutUtils.getPxByDimens(GlobalUtil.getResources(), R.dimen.IS3);


    /**
     * 路况信息TmcSections
     */
    private TmcSections mTmcsection;
    /**
     * 路况信息设置值
     */

    private TmcSections tmcSections;
    /**
     * 是否显示tmc信息状态
     */
    private boolean isTmcStatus;
    /**
     * 行驶进度
     */
    private float drivingProgress = 0;
    /**
     * 小车图标
     */
    private Drawable carDrawable;
    private Drawable vCarDrawable;
    /**
     * 小车图标是否显示
     */
    private boolean isCarShow = false;
    /**
     * 默认设置横向状态
     */
    private boolean isHorizontal = true;
    private Rect mBounds;
    /**
     * 横屏相关
     */
    private Paint mRoadPaint;
    private Drawable mBackGround;
    private int mLeftOffset;
    private int mRightOffset;
    private int mStartOffset;
    private int mEndOffset;

    private Path mRoadClipPath;
    private RectF mRoadClipRect;

    private RectF mRoadRectF;
    private RectF mFinishRect;
    /**
     * 跑道左侧位置
     */
    private float mRoadLeft;
    /**
     * 跑道右侧位置
     */
    private float mRoadRight;
    /**
     * 跑道高度
     */
    private int mRoadHeight;
    /**
     * 上下圆弧半径
     */
    private int mRadius;
    /**
     * 跑道宽度
     */
    private int mRoadWidth;

    private Paint mFinisPaint;
    NaviController naviController = NaviController.getInstance();
    /**/

    /**
     * @param isCarShow 是否显示小车标
     */
    public MapTmcDrawable(@NonNull TmcSections tmcSections, boolean isCarShow) {
        this.tmcSections = tmcSections;
        this.isCarShow = isCarShow;

        mLeftOffset = LayoutUtils.getPxByDimens(R.dimen.map_tmc_drawable_land_left_margin);
        mRightOffset = LayoutUtils.getPxByDimens(R.dimen.map_tmc_drawable_land_right_margin);

        mStartOffset = LayoutUtils.getPxByDimens(R.dimen.map_tmc_drawable_land_start_margin);
        mEndOffset = LayoutUtils.getPxByDimens(R.dimen.map_tmc_drawable_land_end_margin);

        mBackGround = ContextCompat.getDrawable(GlobalUtil.getContext(), R.drawable.bg_road_line);
        carDrawable = ContextCompat.getDrawable(GlobalUtil.getContext(), R.drawable.ic_navi_map_tmc_car);
        vCarDrawable = ContextCompat.getDrawable(GlobalUtil.getContext(), R.drawable.navi_car);
        //添加tmc数据发生边的监听事件
//        NaviRouteManager.getInstance().addTMCUpateListener(tmpUpdateListener, tmcSections);
        update(tmcSections);
    }


    /**
     * 通知更新，根据路况信息发生更改
     */
    private void update(TmcSections tmcSections) {
        NaviDataChangeEventInfo dataInfo = naviController.getNaviDataInfo();
        // TODO: zhaozy 2018/5/16 MapTmcDrawable update getCurrentRoadTotalDistance:暂时写死，需要从导航获取数据
        if (dataInfo == null) {
            return;
        }
        int currentRoadTotalDistance = dataInfo.getTotalDistance();
        if (currentRoadTotalDistance == 0) {
            return;
        }
        // 已经行驶的距离/ 除以总距离
//        drivingProgress = (currentRoadTotalDistance - dataInfo.getDistanceToEnd()) * 1.0F / currentRoadTotalDistance;
        // TODO: zhaozy 2018/5/16 MapTmcDrawable update 暂时写死 dataInfo.getDistanceToEnd，需要从导航获取数据
        drivingProgress = (currentRoadTotalDistance - dataInfo.getDistanceToEnd()) * 1.0F / currentRoadTotalDistance;
        setDrivingProgress(drivingProgress);

        queryData(tmcSections);
        // 日志
        if (Log.isLoggable(LogTag.TMC, Log.INFO)) {
            StringBuilder sb = new StringBuilder().append(" -->> ") //
                    .append(", mTmcsection = ").append(mTmcsection) //
                    ;
            Log.i(LogTag.TMC, sb.toString());
        }
        if (isVisible()) {
            invalidateSelf();
        }
    }

    /**
     * 路况数据准备
     */
    private void queryData(TmcSections tmcSections) {
        if (null == tmcSections) {
            // 日志
            if (Log.isLoggable(LogTag.TMC, Log.INFO)) {
                Log.d(LogTag.TMC, " -->> ");
            }
            mTmcsection = null;
            isTmcStatus = false;
        } else {
            int distance = 100;

            if (mBounds != null) {
                if (isHorizontal) {
                    distance = mBounds.width();
                } else {
                    distance = mBounds.height() - mStartOffset - mEndOffset;
                }
            }
//            TmcSections ts = routeBase.getTmcBar(distance);
            TmcSections ts = tmcSections;


            // 没有路况数据
            if (null == ts || ts.length <= 0 || ts.states.length <= 0 || ts.pixels.length <= 0 || isAllDataInvalid(ts)) {
                mTmcsection = null;
                isTmcStatus = false;
            } else {
                mTmcsection = ts;
                isTmcStatus = true;
            }
        }

    }


    /**
     * 是否整条路线都没路况
     *
     * @param ts
     * @return
     */
    private boolean isAllDataInvalid(TmcSections ts) {
        for (int i = 0; i < ts.states.length; i++) {
            switch (ts.states[i]) {
                case TmcSections.State.light:
                case TmcSections.State.medium:
                case TmcSections.State.heavy:
                    return false;
                default:
                    continue;

            }
        }
        return true;
    }


    /**
     * 更新路况进度条所对应的RouteBase
     */
    public void updateRouteBase(TmcSections tmcSections) {
        this.tmcSections = tmcSections;
        update(tmcSections);
    }



    public void setIsHorizontal(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
    }

    /**
     * true表示有，false表示没有
     *
     * @return 返回TMC的状态
     */
    public boolean hasTmcStatus() {
        return isTmcStatus;
    }

    /**
     * 设置车行驶的进度
     */
    private void setDrivingProgress(float drivingProgress) {
        this.drivingProgress = drivingProgress;
        if (isVisible()) {
            invalidateSelf();
        }
    }

    private Paint mClipPaint;

    @Override
    public void draw(Canvas canvas) {
        // 日志
        if (Log.isLoggable(LogTag.TMC, Log.INFO)) {
            Log.i(LogTag.TMC, " -->> draw");
        }
        //fix
        if (mBounds == null) {
            mBounds = getBounds();
            queryData(tmcSections);
        }
        if (mTmcsection == null) {
            return;
        }
        initClipPaint();
        initPaint();
        initPath();
        initRect();
        initData();
        drawBackground(canvas);

        canvas.save();
        clipRoadLine();
        canvas.saveLayer(new RectF(0, 0, canvas.getWidth(), canvas.getHeight()), null, Canvas.ALL_SAVE_FLAG);
        drawRoadLine(canvas);
        drawFinishLine(canvas);

        canvas.drawPath(mRoadClipPath, mClipPaint);
        canvas.restore();
        drawCar(canvas);


    }

    @NonNull
    private void initClipPaint() {
        if (mClipPaint == null) {
            mClipPaint = new Paint();
            mClipPaint.setColor(Color.WHITE);
            mClipPaint.setAntiAlias(true);
            // 绘制模式为填充
            mClipPaint.setStyle(Paint.Style.FILL);
            // 混合模式为 DST_IN, 即仅显示当前绘制区域和背景区域交集的部分，并仅显示背景内容。
            mClipPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        }
    }

    private void initPaint() {

        if (mRoadPaint == null) {
            mRoadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mRoadPaint.setStyle(Paint.Style.FILL);
        }
        if (mFinisPaint == null) {
            mFinisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mFinisPaint.setStyle(Paint.Style.FILL);
            mFinisPaint.setColor(Color.parseColor("#ffaaaaaa"));
        }

    }

    private void initPath() {

        if (mRoadClipPath == null) {
            mRoadClipPath = new Path();
        }
        if (mRoadClipRect == null) {
            mRoadClipRect = new RectF();
        }


    }

    private void initRect() {
        if (mRoadRectF == null) {
            mRoadRectF = new RectF();
        }
        if (mFinishRect == null) {
            mFinishRect = new RectF();
        }
    }

    private void initData() {
        if (isHorizontal) {
            mRoadLeft = mBounds.left;
            mRadius = mBounds.height() / 2;
            mRoadRight = mRoadLeft + mBounds.width();
            mRoadWidth = mBounds.width();
            mRoadHeight = mBounds.height();
        } else {
            mRoadLeft = mBounds.left + mLeftOffset;
            mRoadRight = mBounds.right - mRightOffset;
            mRoadHeight = mBounds.height() - mStartOffset - mEndOffset;
            mRoadWidth = mBounds.width() - mLeftOffset - mRightOffset;
            mRadius = mRoadWidth / 2;

        }
    }

    private void drawBackground(Canvas canvas) {
        if (!isHorizontal) {
            mBackGround.setBounds(mBounds);
            mBackGround.draw(canvas);
        }
    }

    private void clipRoadLine() {
        mRoadClipPath.reset();
        float[] radii = new float[]{mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius, mRadius};
        if (isHorizontal) {
            mRoadClipRect.set(mRoadLeft, mBounds.top, mRoadRight, mBounds.bottom);
            mRoadClipPath.addRect(mRoadClipRect, Path.Direction.CW);
        } else {
            float top = mEndOffset;
            float bottom = mRoadHeight + mStartOffset;
            mRoadClipRect.set(mRoadLeft, top, mRoadRight, bottom);
            mRoadClipPath.addRoundRect(mRoadClipRect, radii, Path.Direction.CW);
        }
//        canvas.clipPath(mRoadClipPath);

    }

    private void drawRoadLine(Canvas canvas) {
        float lastEnd = 0;
        float top;
        float bottom;
        int length = mTmcsection.length;
        if (isHorizontal) {
            top = mBounds.top;
            bottom = mBounds.top + mRoadHeight;
        } else {
            top = mEndOffset;
            bottom = mRoadHeight + mStartOffset;
        }


        for (int i = 0; i < length; i++) {
            int color = getTMCColor(mTmcsection.states[i]);
            mRoadPaint.setColor(color);
            if (isHorizontal) {
                mRoadRectF.set(lastEnd, top, mTmcsection.pixels[i], bottom);
                canvas.drawRect(mRoadRectF, mRoadPaint);
                lastEnd = mTmcsection.pixels[i];
            } else {
                int height = mTmcsection.pixels[i];
                top = mRoadHeight - height + mEndOffset;
                mRoadRectF.set(mRoadLeft, top, mRoadRight, bottom);
                bottom = top;
                canvas.drawRect(mRoadRectF, mRoadPaint);
            }
        }


    }

    private void drawFinishLine(Canvas canvas) {
        if (!isHorizontal) {
            mFinishRect.set(mRoadLeft, mEndOffset + (mRoadHeight - (mRoadHeight * drivingProgress)), mRoadRight, mRoadHeight + mEndOffset);
            canvas.drawRect(mFinishRect, mFinisPaint);
        }


    }

    private void drawCar(Canvas canvas) {
        if (isCarShow) {
            if (isHorizontal) {
                int padding = LayoutUtils.getPxByDimens(R.dimen.space_1);
                //画出小车行驶时的坐标
                //car 在行驶过程中左下角的位置
                int carBottom = mRoadHeight + (iconHeight - mBounds.height()) / 2;
                //car 在行驶过程中左边的位置
                int carLeft = (int) (mBounds.width() * drivingProgress);
                // 画行驶中的小车
                Rect rect = new Rect(carLeft, carBottom - iconHeight + padding, carLeft + iconWidth, carBottom);
                carDrawable.setBounds(rect);
                carDrawable.draw(canvas);
            } else {

                int top = (int) (mRoadHeight * (1 - drivingProgress)) + mStartOffset;
                int bottom = top + mHorizontalIconHeight;

                if (bottom > mBounds.bottom) {
                    bottom = mBounds.bottom;
                    top = mBounds.bottom - mHorizontalIconHeight;
                }
                Rect vRect = new Rect(mBounds.left, top, mBounds.right, bottom);
                vCarDrawable.setBounds(vRect);
                vCarDrawable.draw(canvas);
            }


        }
    }


    private int getTMCColor(int status) {
        return TMC_COLOR.get(status, Color.parseColor("#ff3c78ff"));
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds = bounds;
        queryData(tmcSections);
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TmcSections.State.blocked, TmcSections.State.heavy, TmcSections.State.light, TmcSections.State.medium, TmcSections.State.none})
    public @interface TMCState {
    }


}
