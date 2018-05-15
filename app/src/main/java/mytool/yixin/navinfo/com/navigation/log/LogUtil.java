package mytool.yixin.navinfo.com.navigation.log;

/**
 * Created by liangxin on 2016/11/21.
 */
public class LogUtil {

    /**
     * 使用控制台打印日志
     * 在不能使用日志的情况下使用，正常情况下禁止使用
     */
    @Deprecated
    public static void printConsole(Object log) {
        System.out.println("导航犬日志：" + log);
    }
}
