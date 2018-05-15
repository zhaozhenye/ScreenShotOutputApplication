package mytool.yixin.navinfo.com.navigation.log;

/**
 * 运行时日志
 */
public class RuntimeLog {

    private static Relation relation = new Relation() {
        @Override
        public int getRuntimeLogMaxSize() {
            return 0;
        }
    };

    /**
     * 设置关系
     *
     * @param relation
     */
    public static void setRelation(Relation relation) {
        RuntimeLog.relation = relation;
    }

    /**
     * 关系
     */
    public interface Relation {
        /**
         * @return 运行时日志最大缓存字符数
         */
        int getRuntimeLogMaxSize();
    }

    /**
     * 单例持有器
     */
    public static final class InstanceHolder {
        public static final MemoryCacheLog RUNTIME_LOG = new MemoryCacheLog(relation.getRuntimeLogMaxSize());
    }

}
