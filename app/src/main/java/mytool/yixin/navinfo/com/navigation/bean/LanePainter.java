package mytool.yixin.navinfo.com.navigation.bean;

/**
 * $desc$
 *
 * @author zhaozy
 * @date 2018/5/15
 */


public class LanePainter {
    EventHandler eventHandler;

    public interface EventHandler {
        void onLanePainterEvent(int var1, LaneModel var2);
    }

    public static class Event {
        public static final int show = 0;
        public static final int hide = 1;

        public Event() {
        }
    }
    public void addListener(LanePainter.EventHandler eventHandler) {
        this.eventHandler =eventHandler;

    }
}
