package mytool.yixin.navinfo.com.navigation.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mytool.yixin.navinfo.com.navigation.R;
import mytool.yixin.navinfo.com.navigation.bean.MNaviElements;
import mytool.yixin.navinfo.com.navigation.bean.NaviDataChangeEventInfo;
import mytool.yixin.navinfo.com.navigation.bean.NaviType;
import mytool.yixin.navinfo.com.navigation.controller.NaviController;
import mytool.yixin.navinfo.com.navigation.drawable.ShadowDrawable;
import mytool.yixin.navinfo.com.navigation.log.Log;
import mytool.yixin.navinfo.com.navigation.log.LogTag;
import mytool.yixin.navinfo.com.navigation.utils.GlobalUtil;
import mytool.yixin.navinfo.com.navigation.utils.LayoutUtils;
import mytool.yixin.navinfo.com.navigation.utils.Utils;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class NaviTitleView extends BaseView {


    /**
     * 资源对象缓存
     */
    private Resources res = null;


    private static final String YAN = "沿";
    private static final String AFTER = "后";
    /**
     * 以100km为分割线，少于这个标准展示“进入XX路”，超过则显示“沿XX路行驶”
     */
    private static final int MAX = 100000;
    private String mRoadName;

    private int mTurnIconResource;
    private NaviType currentType;
    NaviController naviController = NaviController.getInstance();
    private NaviDataChangeEventInfo mNavigateData;
    private NavigateTitleDrawable mNavigateTitleDrawable;

    public NaviTitleView(Context context) {
        super(context);

    }

    public NaviTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NaviTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void update() {
        initRouteData();
        invalidateSelf();
    }
    private void invalidateSelf() {
        getBackground().invalidateSelf();
    }


    private void initView() {
        initRouteData();
        mNavigateTitleDrawable = new NavigateTitleDrawable();
        boolean ismExpandShow = naviController.ismExpandShow();
        Utils.setViewBackGroundDrawable(this, mNavigateTitleDrawable);
        ShadowDrawable.update(this, !ismExpandShow ? ShadowDrawable.DIRECTION_RIGHT : ShadowDrawable.DIRECTION_BOTTOM);

    }

    //    public void updateMode() {
//        //需要在引擎显示路口放大图，且是跟踪模式的情况下才调整标题栏高度为放大图模式
//        ViewGroup.LayoutParams lp          = getLayoutParameter();
//        View                   contentView = getContentView();
//        contentView.setLayoutParams(lp);
//       invalidate();
//    }

    private void initRouteData() {
        mNavigateData = naviController.getNaviDataInfo();
        if (mNavigateData == null) {
            return;
        }
        updateCarRoadInfo();

    }

    /**
     * 更新驾车模式相关的路线信息
     */
    private void updateCarRoadInfo() {
//        String distance = mNavigateData.getDistanceToCurrPoint().replace("米", "");
//        int distanceToCurrPoint = Integer.valueOf(distance);
//        boolean b = distanceToCurrPoint > MAX;
//        this.mRoadName = b ? mNavigateData.getName() : mNavigateData.getNextName();
        this.mRoadName = mNavigateData.getNextName();
        NaviType result = naviController.getType();
        smallAndNoDistanceText(result);
        mTurnIconResource = MNaviElements.engineIconId2ActionBigIconResourceId(mNavigateData.getIcon());

    }


    private void smallAndNoDistanceText(NaviType type) {
        switch (type) {
            case NO_NEXT_ROAD:
                currentType = NaviType.NO_NEXT_ROAD;
                break;
            case NEXT_HAS_DIRECTION:
                currentType = NaviType.NEXT_HAS_DIRECTION;
                mRoadName += "方向";
                break;
            case NEXT_EQUALS_CURRENT:
                currentType = NaviType.NEXT_EQUALS_CURRENT;
                break;
            case NEXT_EQUALS_END:
                currentType = NaviType.NEXT_EQUALS_END;
                break;
            case NEXT_HAS_EXIT:
                currentType = NaviType.NEXT_HAS_EXIT;
                mRoadName += "方向";
                break;
            case FORMAL_NEXT:
                currentType = NaviType.FORMAL_NEXT;
                break;
            default:
                break;
        }
    }


    private class NavigateTitleDrawable extends Drawable {

        private final Pattern NUMBER_PATTERN = Pattern.compile("\\d+(\\.\\d+)?");
        private Rect mBound;
        private TextPaint mDistancePaint;
        private TextPaint mDistanceTextPaint;
        private TextPaint mRoadPaint;



        private TextPaint mRetrainTextPaint;
        private TextPaint mRetrainPaint;
        private TextPaint mTimePaint;
        private TextPaint mDividerPaint;
        private int mDividerWidth;


        private int mTurnIconLandWidth;
        private int mTurnIconLandHeight;
        private int mTurnLandTopOffset;
        private int mTextLandTopOffset;
        private int mTurnLandDrawableLeft;
        private int mTurnLandDrawableTop;
        private int mTurnLandDrawableRight;
        private int mTurnLandDrawableBottom;


        private int mTurnIconPortWidth;
        private int mTurnIconPortHeight;
        private int mTurnPortLeftOffset;
        private int mPortTextLeftOffset;
        private int mPortTextTopOffset;
        private int mPortraitTurnLeft;
        private int mPortraitTurnTop;
        private int mPortraitTurnRight;
        private int mPortraitTurnBottom;

        private int mLandLeftMargin;

        private int mPortraitSmallTurnLeft;
        private int mPortraitSmallTurnTop;
        private int mPortraitSmallTurnRight;
        private int mPortraitSmallTurnBottom;
        private int mTurnSmallIconPortWidth;
        private int mTurnSmallIconPortHeight;
        private int mTurnSmallPortLeftOffset;
        private int portBgColor = LayoutUtils.getColorById(R.color.navigate_title_portrait_bg);
        private int landBgColor = LayoutUtils.getColorById(R.color.navigate_title_land_bg);
        private String mEllipsizeText = "...";

        @Override
        public void draw(Canvas canvas) {
            // 日志
            if (Log.isLoggable(LogTag.NAVI_TITLE, Log.INFO)) {
                Log.i(LogTag.NAVI_TITLE, " -->> 重绘了 ");
            }
            if (mBound == null || mBound.width() == 0) {
                mBound = getBounds();
            }
            initData();
            initPaint();
            drawBackGround(canvas);

            if (mNavigateData == null) {
                return;
            }
            drawRouteInformation(canvas);

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
            mBound = bounds;

        }

        private void drawBackGround(Canvas canvas) {
            int color = portBgColor;
            if (isPortMode) {
                color = landBgColor;
            }
            canvas.drawColor(color);
        }


        /**
         * 获取资源对象
         */
        private Resources getResource() {
            if (res == null) {
                res = GlobalUtil.getResources();
            }
            return res;
        }


        private void initData() {
            if (isPortMode) {
                mTurnIconLandWidth = LayoutUtils.getPxByDimens(R.dimen.navigate_title_turn_width_land);
                mTurnIconLandHeight = LayoutUtils.getPxByDimens(R.dimen.navigate_title_turn_height_land);
                mTurnLandTopOffset = LayoutUtils.getPxByDimens(R.dimen.navigate_title_turn_top_margin_land);
                mTextLandTopOffset = LayoutUtils.getPxByDimens(R.dimen.space_30);
                mLandLeftMargin = LayoutUtils.getPxByDimens(R.dimen.space_15);
            } else {
                mTurnIconPortWidth = LayoutUtils.getPxByDimens(R.dimen.navigate_title_turn_width_portrait);
                mTurnIconPortHeight = LayoutUtils.getPxByDimens(R.dimen.navigate_title_turn_height_portrait);
                mTurnSmallIconPortWidth = LayoutUtils.getPxByDimens(R.dimen.navigate_title_turn_small_width_portrait);
                mTurnSmallIconPortHeight = LayoutUtils.getPxByDimens(R.dimen.navigate_title_turn_small_height_portrait);
                mTurnPortLeftOffset = LayoutUtils.getPxByDimens(R.dimen.space_20);
                mPortTextLeftOffset = LayoutUtils.getPxByDimens(R.dimen.space_10);
                mPortTextTopOffset = LayoutUtils.getPxByDimens(R.dimen.space_9);

                mTurnSmallPortLeftOffset = LayoutUtils.getPxByDimens(R.dimen.space_15);
            }


        }

        private void initDividerPaint() {
            if (mDividerPaint == null) {
                mDividerPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                mDividerPaint.setColor(LayoutUtils.getColorById(R.color.navigate_title_divider));
                mDividerWidth = LayoutUtils.dp2px(1);
                mDividerPaint.setStrokeWidth(mDividerWidth);
            }
        }

        private void initDistancePaint() {
            if (mDistancePaint == null) {
                mDistancePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                mDistancePaint.setColor(Color.WHITE);
                mDistancePaint.setFakeBoldText(true);
            }

        }

        private void initDistanceTextPaint() {
            if (mDistanceTextPaint == null) {
                mDistanceTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                mDistanceTextPaint.setColor(LayoutUtils.getColorById(R.color.FC31));
                mDistanceTextPaint.setFakeBoldText(true);
            }

        }

        private void initRoadPaint() {
            if (mRoadPaint == null) {
                mRoadPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                mRoadPaint.setFakeBoldText(true);
                mRoadPaint.setColor(Color.WHITE);

            }
            mRoadPaint.setTextAlign(Paint.Align.LEFT);
        }

        private void initPaint() {

            initDividerPaint();
            initDistancePaint();
            initDistanceTextPaint();
            initRoadPaint();
            if (mRetrainTextPaint == null) {
                mRetrainTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                mRetrainTextPaint.setColor(LayoutUtils.getColorById(R.color.FC7));
                mRetrainTextPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F3));
                mRetrainTextPaint.setFakeBoldText(true);
            }
            if (mRetrainPaint == null) {
                mRetrainPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                mRetrainPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F3));
                mRetrainPaint.setColor(Color.WHITE);
                mRetrainPaint.setFakeBoldText(true);
            }
            if (mTimePaint == null) {
                mTimePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
                mTimePaint.setColor(Color.WHITE);
                mTimePaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F3));
                mTimePaint.setFakeBoldText(true);
            }


        }


        private void drawRouteInformation(Canvas canvas) {
            updateRoutePaint();

            switch (naviController.getType()) {
                case NO_NEXT_ROAD:
                    if (isPortMode) {
                        drawRouteLandWithNoNextRoad(canvas);
                    } else {
                        drawRoutePortraitWithNoNextRoad(canvas);
                    }

                    break;
                case NEXT_EQUALS_CURRENT:
                    if (isPortMode) {
                        drawRouteLandWithNextEqualsCurrent(canvas);
                    } else {
                        drawRoutePortraitWithNextEqualsCurrent(canvas);
                    }


                    break;
                case NEXT_HAS_DIRECTION:
                case NEXT_EQUALS_END:
                case NEXT_HAS_EXIT:
                case FORMAL_NEXT:
                default:
                    if (isPortMode) {
                        drawLandRouteInformation(canvas);
                    } else {
                        drawPortraitRouteInformation(canvas);
                    }
                    break;

            }
        }

        private void updateLandRoutePaint() {
            if (isPortMode) {
                mDistancePaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F11));
                mDistanceTextPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F4));
                mRoadPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F13));
            } else {
                mDistancePaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F44));
                mDistanceTextPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F13));
                mRoadPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F20));

            }
        }

        private void drawRouteLandWithNextEqualsCurrent(Canvas canvas) {
            //沿XXX行驶
            //200米
            String distanceText = mNavigateData.getDistanceToCurrPoint();


            String key = naviController.getType().getKey();
            int x, y;
            Matcher matcher = NUMBER_PATTERN.matcher(distanceText);
            int start = 0;
            int end = 0;
            if (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
            }
            String numberDistance = distanceText.substring(start, end);

            String unite = distanceText.substring(end, distanceText.length());


//            if (isPortMode && mExpandViewShow) {
//
//                int numberWidth = getTextWidth(mDistancePaint, numberDistance);
//                int yanWidth = getTextWidth(mDistanceTextPaint, YAN);
//                int roadWidth = getTextWidth(mRoadPaint, mRoadName);
//                int roadHeight = LayoutUtils.textHeight(mRoadPaint);
//                int numberHeight = LayoutUtils.textHeight(mDistancePaint);
//
//                int padding = (mBound.height() - roadHeight - numberHeight) / 2;
//
//                x = mLandLeftMargin;
//                y = padding + LayoutUtils.distanceOfBaselineAndTop(mRoadPaint);
//                //沿
//                canvas.drawText(YAN, x, y, mDistanceTextPaint);
//                //xx
//                canvas.drawText(mRoadName, x + yanWidth, y, mRoadPaint);
//                //行驶
//                canvas.drawText(key, x + yanWidth + roadWidth, y, mDistanceTextPaint);
//
//                //200米
//                y = padding + roadHeight + LayoutUtils.distanceOfBaselineAndTop(mDistancePaint);
//                canvas.drawText(numberDistance, x, y, mDistancePaint);
//                canvas.drawText(unite, x + numberWidth, y, mDistanceTextPaint);
//
//
//            } else {
//
//            }

            int numberWidth = getTextWidth(mDistancePaint, numberDistance);
            int uniteWidth = getTextWidth(mDistanceTextPaint, unite);
            int yanWidth = getTextWidth(mDistanceTextPaint, YAN);
            int roadWidth = getTextWidth(mRoadPaint, mRoadName);
            int keyWidth = getTextWidth(mDistanceTextPaint, key);

            int roadHeight = LayoutUtils.textHeight(mRoadPaint);


            drawLandTurn(canvas);
            x = (mBound.width() - yanWidth - roadWidth - keyWidth) / 2;
            y = mTurnLandDrawableBottom + mTextLandTopOffset + LayoutUtils.distanceOfBaselineAndTop(mRoadPaint);

            //沿xx行驶
            //沿
            canvas.drawText(YAN, x, y, mDistanceTextPaint);
            //xx

            canvas.drawText(mRoadName, x + yanWidth, y, mRoadPaint);
            //行驶
            canvas.drawText(key, x + yanWidth + roadWidth, y, mDistanceTextPaint);

            int total = numberWidth + uniteWidth;
            x = (mBound.width() - total) / 2;
            y = mTurnLandDrawableBottom + mTextLandTopOffset + roadHeight + LayoutUtils.distanceOfBaselineAndTop(mDistancePaint);
            //200米
            canvas.drawText(numberDistance, x, y, mDistancePaint);
            canvas.drawText(unite, x + numberWidth, y, mDistanceTextPaint);


        }

        private void drawRoutePortraitWithNextEqualsCurrent(Canvas canvas) {

            //沿XXX行驶
//            int distance = mNavigateData.getCurrentToNextTurnDistance();
            //200米
            String distanceText = mNavigateData.getDistanceToCurrPoint();

            String key = naviController.getType().getKey();
            int x, y;
            Matcher matcher = NUMBER_PATTERN.matcher(distanceText);
            int start = 0;
            int end = 0;
            if (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
            }
            String numberDistance = distanceText.substring(start, end);

            String unite = distanceText.substring(end, distanceText.length());

//            if (mExpandViewShow) {
//
//                int numberWidth = getTextWidth(mDistancePaint, numberDistance);
//                int roadWidth = getTextWidth(mRoadPaint, mRoadName);
//                int yanWidth = getTextWidth(mDistanceTextPaint, YAN);
//                int keyWidth = getTextWidth(mDistanceTextPaint, key);
//
//                int numberHeight = LayoutUtils.textHeight(mDistancePaint);
//
//
//                drawPortraitSmallTurn(canvas);
//
//                int padding = (mBound.height() - numberHeight) / 2;
//                x = mPortraitSmallTurnRight + mTurnSmallPortLeftOffset;
//
//                y = LayoutUtils.distanceOfBaselineAndTop(mRoadPaint) + padding;
//                //yan
//                canvas.drawText(YAN, x, y, mDistanceTextPaint);
//                //road
//                x = x + yanWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
//                canvas.drawText(mRoadName, x, y, mRoadPaint);
//                //行驶
//                x = x + roadWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
//                canvas.drawText(key, x, y, mDistanceTextPaint);
//                //200
//                x = x + keyWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
//                canvas.drawText(numberDistance, x, y, mDistancePaint);
//                //米
//                x = x + numberWidth;
//                canvas.drawText(unite, x, y, mDistanceTextPaint);
//
//
//            } else {
//            }
            int numberWidth = getTextWidth(mDistancePaint, numberDistance);
            int roadWidth = getTextWidth(mRoadPaint, mRoadName);
            int roadHeight = LayoutUtils.textHeight(mRoadPaint);
            int yanWidth = getTextWidth(mDistanceTextPaint, YAN);
            int numberHeight = LayoutUtils.textHeight(mDistancePaint);

            drawPortraitTurn(canvas);

            //LayoutUtils.dp2px(8)
            int padding = (mBound.height() - roadHeight - numberHeight) / 2;

            x = mPortraitTurnRight + mPortTextLeftOffset;
            y = LayoutUtils.distanceOfBaselineAndTop(mRoadPaint) + padding;

            //yan
            canvas.drawText(YAN, x, y, mDistanceTextPaint);
            //road
            x = x + yanWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
            canvas.drawText(mRoadName, x, y, mRoadPaint);
            //行驶
            x = x + roadWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
            canvas.drawText(key, x, y, mDistanceTextPaint);

            x = mPortraitTurnRight + mPortTextLeftOffset;
            y = padding + roadHeight + LayoutUtils.distanceOfBaselineAndTop(mDistancePaint);
            //200
            canvas.drawText(numberDistance, x, y, mDistancePaint);
            //米
            x = x + numberWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
            canvas.drawText(unite, x, y, mDistanceTextPaint);
        }

        private void drawRouteLandWithNoNextRoad(Canvas canvas) {

            //沿路行驶
            String text = naviController.getType().getKey();
            int x, y;
//            if (isPortMode && mExpandViewShow) {
//                x = mLandLeftMargin;
//                y = mBound.height() / 2 + LayoutUtils.distanceOfBaselineAndCenterY(mDistanceTextPaint);
//                canvas.drawText(text, x, y, mDistanceTextPaint);
//
//            } else {
//
//            }
            drawLandTurn(canvas);
            int distanceTextWidth = getTextWidth(mDistanceTextPaint, text);
            x = (mBound.width() - distanceTextWidth) / 2;
            y = mTurnLandDrawableBottom + mTextLandTopOffset + LayoutUtils.distanceOfBaselineAndTop(mDistancePaint);
            canvas.drawText(text, x, y, mDistanceTextPaint);
        }

        private void drawRoutePortraitWithNoNextRoad(Canvas canvas) {
            //沿路行驶
            String key = naviController.getType().getKey();
            int x, y;
//            if (mExpandViewShow) {
//                drawPortraitSmallTurn(canvas);
//                x = mPortraitSmallTurnRight + mPortTextLeftOffset;
//                y = mBound.height() / 2 + LayoutUtils.distanceOfBaselineAndCenterY(mDistancePaint);
//                canvas.drawText(key, x, y, mDistanceTextPaint);
//            } else {
//
//            }
            drawPortraitTurn(canvas);
            x = mPortraitTurnRight + mPortTextLeftOffset;
            y = mBound.height() / 2 + LayoutUtils.distanceOfBaselineAndCenterY(mDistancePaint);
            canvas.drawText(key, x, y, mDistanceTextPaint);
        }

        private void drawPortraitRouteInformation(Canvas canvas) {

            //200
//            int distance = mNavigateData.getCurrentToNextTurnDistance();
            //200米
            String distanceText = mNavigateData.getDistanceToCurrPoint();


            Matcher matcher = NUMBER_PATTERN.matcher(distanceText);
            int start = 0;
            int end = 0;
            if (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
            }
            String numberDistance = distanceText.substring(start, end);
            String unite = distanceText.substring(end, distanceText.length());

            String text = unite + "后" + currentType.getKey();

            int x, y;

//            if (mExpandViewShow) {
//
//                int numberWidth = getTextWidth(mDistancePaint, numberDistance);
//                int textWidth = getTextWidth(mDistanceTextPaint, text);
//
//                drawPortraitSmallTurn(canvas);
//
//
//                x = mPortraitSmallTurnRight + mTurnSmallPortLeftOffset;
//                y = LayoutUtils.distanceOfBaselineAndTop(mDistancePaint) + mTurnSmallPortLeftOffset;
//                //200米后前往road
//                //200
//                canvas.drawText(numberDistance, x, y, mDistancePaint);
//                //米后前往
//                x = x + numberWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
//                canvas.drawText(text, x, y, mDistanceTextPaint);
//                //road
//                x = x + textWidth + LayoutUtils.getPxByDimens(R.dimen.space_3);
//
//                //canvas.drawText(mRoadName, x, y, mRoadPaint);
//                int totalWidth = mBound.width() - x;
//                StaticLayout layout = createMultiplyLayout(mRoadName, mRoadPaint, 1, totalWidth, Layout.Alignment.ALIGN_NORMAL, mEllipsizeText);
//                canvas.save();
//                canvas.translate(x, mTurnSmallPortLeftOffset);
//                layout.draw(canvas);
//                canvas.restore();
//            } else {
//            }
            int numberWidth = getTextWidth(mDistancePaint, numberDistance);
            int numberHeight = LayoutUtils.textHeight(mDistancePaint);


            drawPortraitTurn(canvas);
            x = mPortraitTurnRight + mPortTextLeftOffset;
            y = LayoutUtils.distanceOfBaselineAndTop(mDistancePaint) + mPortTextTopOffset;
            //200米后前往road
            //200
            canvas.drawText(numberDistance, x, y, mDistancePaint);
            //米后前往
            x = x + numberWidth + LayoutUtils.getPxByDimens(R.dimen.space_2);
            canvas.drawText(text, x, y, mDistanceTextPaint);
            //road
            x = mPortraitTurnRight + mPortTextLeftOffset;
            y = mPortTextTopOffset + numberHeight;

            int totalWidth = getBounds().width() - x;
            StaticLayout layout = createMultiplyLayout(mRoadName, mRoadPaint, 1, totalWidth, Layout.Alignment.ALIGN_NORMAL, mEllipsizeText);
            canvas.save();
            canvas.translate(x, y);
            layout.draw(canvas);
            canvas.restore();
        }

        private void drawLandRouteInformation(Canvas canvas) {
            //200米后前往road
            //200米
            String distanceText = mNavigateData.getDistanceToCurrPoint();
            //
            NaviType currentType = naviController.getType();

            int x, y;
            Matcher matcher = NUMBER_PATTERN.matcher(distanceText);
            int start = 0;
            int end = 0;
            if (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
            }
            String numberDistance = distanceText.substring(start, end);
            String unite = distanceText.substring(end, distanceText.length());
            String text = unite + "后" + currentType.getKey();


//            if (isPortMode && mExpandViewShow) {
//
//                int numberWidth = getTextWidth(mDistancePaint, numberDistance);
//                int numberHeight = LayoutUtils.textHeight(mDistancePaint);
//
//                int roadHeight = LayoutUtils.textHeight(mRoadPaint);
//                int padding = (mBound.height() - numberHeight - roadHeight) / 2;
//
//                x = mLandLeftMargin;
//                y = padding + LayoutUtils.distanceOfBaselineAndTop(mDistancePaint);
//                //200
//                canvas.drawText(numberDistance, x, y, mDistancePaint);
//                //米后前往
//                x = x + numberWidth + LayoutUtils.getPxByDimens(R.dimen.space_1);
//                canvas.drawText(text, x, y, mDistanceTextPaint);
//                //road
//                y = padding + numberHeight;
//                canvas.save();
//                canvas.translate(mLandLeftMargin, y);
//                int totalWidth = mBound.width() - mLandLeftMargin;
//                StaticLayout layout = createMultiplyLayout(mRoadName, mRoadPaint, 1, totalWidth, Layout.Alignment.ALIGN_NORMAL, mEllipsizeText);
//                layout.draw(canvas);
//                canvas.restore();
//            } else {
//                //200米后前往road
//
//            }
            int numberWidth = getTextWidth(mDistancePaint, numberDistance);
            int numberHeight = LayoutUtils.textHeight(mDistancePaint);
            int distanceTextWidth = getTextWidth(mDistanceTextPaint, text);


            drawLandTurn(canvas);
            int space = LayoutUtils.getPxByDimens(R.dimen.space_1);
            x = (mBound.width() - numberWidth - distanceTextWidth - space) / 2;
            y = mTurnLandDrawableBottom + mTextLandTopOffset + LayoutUtils.distanceOfBaselineAndTop(mDistancePaint);
            //200
            canvas.drawText(numberDistance, x, y, mDistancePaint);
            //米后前往
            x = x + numberWidth + space;
            canvas.drawText(text, x, y, mDistanceTextPaint);
            //road
            y = mTurnLandDrawableBottom + mTextLandTopOffset + numberHeight;

            //多行的情况
            int totalWidth = getBounds().width() - 2 * mLandLeftMargin;
            canvas.save();
            StaticLayout layout = createMultiplyLayout(mRoadName, mRoadPaint, 2, totalWidth, Layout.Alignment.ALIGN_CENTER, mEllipsizeText);
            canvas.translate(mLandLeftMargin, y);
            layout.draw(canvas);
            canvas.restore();
        }

        private void drawPortraitTurn(Canvas canvas) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), mTurnIconResource);
            mPortraitTurnLeft = mTurnPortLeftOffset;
            mPortraitTurnTop = (mBound.height() - mTurnIconPortHeight) / 2;
            mPortraitTurnRight = mPortraitTurnLeft + mTurnIconPortWidth;
            mPortraitTurnBottom = mPortraitTurnTop + mTurnIconPortHeight;
            drawable.setBounds(mPortraitTurnLeft, mPortraitTurnTop, mPortraitTurnRight, mPortraitTurnBottom);
            drawable.draw(canvas);
        }

        private void drawPortraitSmallTurn(Canvas canvas) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), mTurnIconResource);
            mPortraitSmallTurnLeft = mTurnSmallPortLeftOffset;
            mPortraitSmallTurnTop = (mBound.height() - mTurnSmallIconPortWidth) / 2;
            mPortraitSmallTurnRight = mPortraitSmallTurnLeft + mTurnSmallIconPortWidth;
            mPortraitSmallTurnBottom = mPortraitSmallTurnTop + mTurnSmallIconPortHeight;
            drawable.setBounds(mPortraitSmallTurnLeft, mPortraitSmallTurnTop, mPortraitSmallTurnRight, mPortraitSmallTurnBottom);
            drawable.draw(canvas);
        }

        private void drawLandTurn(Canvas canvas) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), mTurnIconResource);
            mTurnLandDrawableLeft = (mBound.width() - mTurnIconLandWidth) / 2;
            mTurnLandDrawableTop = mTurnLandTopOffset;
            mTurnLandDrawableRight = mTurnLandDrawableLeft + mTurnIconLandWidth;
            mTurnLandDrawableBottom = mTurnLandDrawableTop + mTurnIconLandHeight;
            drawable.setBounds(mTurnLandDrawableLeft, mTurnLandDrawableTop, mTurnLandDrawableRight, mTurnLandDrawableBottom);
            drawable.draw(canvas);
        }

        private void updateRoutePaint() {
            if (isPortMode) {
                updateLandRoutePaint();
            } else {
                updatePortraitRoutePaint();
            }

        }

        private void updatePortraitRoutePaint() {
//            if (mExpandViewShow) {
//                mDistanceTextPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F2));
//                mDistancePaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F20));
//                mRoadPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F4));
//            } else {
//            }
            mDistanceTextPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F13));
            mDistancePaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F44));
            mRoadPaint.setTextSize(LayoutUtils.getPxByDimens(R.dimen.F13));
        }

        private int getTextWidth(Paint textPaint, String text) {
            return (int) textPaint.measureText(text);
        }


        private StaticLayout createMultiplyLayout(String source, TextPaint paint, int limitLine, int limitWidth, Layout.Alignment align, String ellipsizeText) {
            StaticLayout layout = new StaticLayout(source, 0, source.length(), paint, limitWidth, align, 1.0F, 1.0F, true);
            float dotWidth = mRoadPaint.measureText(ellipsizeText);
            int lineCount = layout.getLineCount();
            int maxLine = limitLine;
            if (lineCount > maxLine) {
                int startIndex = layout.getLineStart(maxLine - 1);
                int endIndex = layout.getLineEnd(maxLine - 1);
                String beforeText = source.substring(0, startIndex);
                String lineText = source.substring(startIndex, endIndex);
                // 将第 showLineCount 行最后的文字替换为 mEllipsizeText
                endIndex = 0;
                for (int i = lineText.length() - 1; i >= 0; i--) {
                    String str = lineText.substring(i, lineText.length());
                    // 找出文字宽度大于 mEllipsizeText 的字符
                    if (paint.measureText(str) >= dotWidth) {
                        endIndex = i;
                        break;
                    }
                }
                String newEndLineText = lineText.substring(0, endIndex) + ellipsizeText;
                newEndLineText = beforeText + newEndLineText;
                layout = new StaticLayout(newEndLineText, 0, newEndLineText.length(), mRoadPaint, limitWidth, Layout.Alignment.ALIGN_CENTER, 1.0F, 1.0F, true);
            }


            return layout;


        }


    }
}
