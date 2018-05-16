package mytool.yixin.navinfo.com.navigation.listener;

import mytool.yixin.navinfo.com.navigation.bean.TmcSections;

/**
 * // INFO 路线TMC更新监听器包装
 */
public class TMCWarpSuccinctListener implements Listener.GenericListener<TMCUpdateEventInfo> {

    private TmcSections tmcSections;
    private SuccinctListener succinctListener;

    public TMCWarpSuccinctListener(SuccinctListener succinctListener, TmcSections tmcSections) {
        this.tmcSections = tmcSections;
        this.succinctListener = succinctListener;
    }

    @Override
    public void onEvent(TMCUpdateEventInfo eventInfo) {
        if (eventInfo.getRouteBase().equals(tmcSections)) {
            succinctListener.onEvent();
        }
    }
}
