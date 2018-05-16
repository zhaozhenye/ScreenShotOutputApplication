package mytool.yixin.navinfo.com.navigation.listener;


import mytool.yixin.navinfo.com.navigation.bean.TmcSections;

public class TMCUpdateEventInfo extends BaseEventInfo {

    private TmcSections tmcSections;

    public TMCUpdateEventInfo(TmcSections tmcSections) {
        this.tmcSections = tmcSections;
    }

    public TmcSections getRouteBase() {
        return tmcSections;
    }
}
