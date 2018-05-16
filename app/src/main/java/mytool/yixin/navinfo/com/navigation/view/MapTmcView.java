package mytool.yixin.navinfo.com.navigation.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import mytool.yixin.navinfo.com.navigation.R;
import mytool.yixin.navinfo.com.navigation.bean.TmcSections;
import mytool.yixin.navinfo.com.navigation.controller.NaviController;
import mytool.yixin.navinfo.com.navigation.drawable.MapTmcDrawable;
import mytool.yixin.navinfo.com.navigation.utils.LayoutUtils;
import mytool.yixin.navinfo.com.navigation.utils.Utils;

/**
 * 导航页的路况条
 *
 */

public class MapTmcView extends BaseView {

    private static final int MAX_SQUARE_HEIGHT = LayoutUtils.getPxByDimens(R.dimen.map_road_traffic_square_height);
    private final int MAX_LAND_HEIGHT = LayoutUtils.getPxByDimens(R.dimen.map_road_traffic_land_height);
    private final int MAX_PORT_HEIGHT = LayoutUtils.getPxByDimens(R.dimen.map_road_traffic_portrait_height);
    private MapTmcDrawable mMapTmcDrawable;
    private MapTmcDrawable mMapLandTmcDrawable;

    public MapTmcView(Context context) {
        super(context);
    }

    public MapTmcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MapTmcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    NaviController naviController = NaviController.getInstance();

    public void initView() {

        TmcSections tmcSections = naviController.getTmcSections();
        mMapLandTmcDrawable = new MapTmcDrawable(tmcSections, true);
        mMapLandTmcDrawable.setIsHorizontal(false);
        Utils.setViewBackGroundDrawable(this, mMapLandTmcDrawable);
        updateUI();
    }


    public void updateUI() {

        //跟踪导航且非步行导航时显示路况条
        boolean isLineShow = true;

//        //set LayoutParameter
//        ConstraintLayout.LayoutParams lp;
//        lp = (ConstraintLayout.LayoutParams) contentView.getLayoutParams();
//        ViewGroup parentView = (ViewGroup) getParent();
//
//
//        int[] screenWH = LayoutUtils.getScreenWH();
//        if (!isExpandWidet) {
//            isLineShow = true;
//            int viewHeight = screenWH[1] - GlobalUtil.getStatusBarHeight();
//            int rootHeight = parentView.getHeight();
//            if (rootHeight > 0) {
//                viewHeight = rootHeight;
//            }
//            int height = viewHeight - getUsedHeight();
//            lp.height = Math.min(height, MAX_PORT_HEIGHT);
//            lp.topMargin = 0;
//            lp.bottomMargin = LayoutUtils.getPxByDimens(R.dimen.margin_slid_map_common);
//        } else {
//            isLineShow = false;
//
//        }

//        lp.validate();
//        contentView.setLayoutParams(lp);


        setVisibility(isLineShow ? View.VISIBLE : View.GONE);
        if (isLineShow) {
            TmcSections tmcSections = naviController.getTmcSections();
            mMapLandTmcDrawable.updateRouteBase(tmcSections);
        }
    }

    private int getUsedHeight() {
        int height = 0;
        int highWayHeight = 0;
        if (!isExpandWidet) {
            int titleHeight = LayoutUtils.getPxByDimens(R.dimen.navigate_title_height_portrait);
            int highTop = LayoutUtils.getPxByDimens(R.dimen.high_way_guide_padding);
            int highBottom = LayoutUtils.getPxByDimens(R.dimen.space_13);

            int bottomTop = LayoutUtils.getPxByDimens(R.dimen.margin_slid_map_common);
            int bottomHeight = LayoutUtils.getPxByDimens(R.dimen.navigate_bottom_height);


            height = titleHeight + highTop + highWayHeight + highBottom + bottomHeight + bottomTop;
        }

        return height;
    }


}
