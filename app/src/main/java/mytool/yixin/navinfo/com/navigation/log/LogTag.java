package mytool.yixin.navinfo.com.navigation.log;

/**
 *
 */
public enum LogTag implements LogTagInterface {
    /**
     * 支付
     */
    PAY,
    /**
     * 插件
     */
    PLUGIN,
    DEX_LOG,
    /**
     * 语音
     */
    VOICE,
    /**
     * 电子狗
     */
    ELECTRON,
    /**
     * 积分
     */
    INTEGRAL,
    /**
     * SD卡
     */
    SDCARD,
    /**
     * 所有（研发请勿使用）
     */
    /**
     * 临时
     */
//    TEMP,
    TOUCH,
    YYC,
    /**
     * 全局
     */
    GLOBAL,
    /**
     * 我的
     */
    MY_POSITION,

    /**
     * 系统：
     */
    SYSTEM_ANDROID,

    /**
     * 传感器：
     */
    SENSOR,

    /**
     * 引擎：
     */
    ENGINE,

    GATEWAY,
    /**
     * 滚动控件
     */
    SCROLL,

    /**
     * 引擎：地图
     */
    ENGINE_MAP,

    /**
     * 引擎：导航
     */
    ENGINE_NAVI,
    /**
     * 引擎路线
     */
    ENGINE_ROUTE,
    /**
     * 框架
     */
    FRAMEWORK,

    /**
     * 框架：页面
     */
    FRAMEWORK_PAGE,

    /**
     * 搜索数据相关
     */
    QUERY_DATA,

    /**
     * 框架：事件
     */
    FRAMEWORK_EVENT,

    /**
     * 界面
     */
    UI,

    /**
     * 界面：标题栏
     */
    UI_TITLE,

    /**
     * 界面：搜索
     */
    UI_SEARCH,
    /**
     * SUGGEST相关
     */
    SUGGEST,

    /**
     * 界面：地图打点
     */
    UI_POIS,

    /**
     * 跨页面跨布局对齐
     */
    UI_ALIGNMENT,

    /**
     * 渲染
     */
    DRAW,

    /**
     * 分页列表控件
     */
    PAGE_LIST,
    PAGE_LIST_TEMP,
    /**
     * 关于翻页控件相关的日志
     */
    PAGE_PULL,

    /**
     * 锁车琐图
     */
    LOCK_MAP,

    /**
     * 导航
     */
    NAVI,
    /**
     * 导航标题栏
     */
    NAVI_TITLE,
    /**
     * 路线
     */
    ROUTE,

    /**
     * 地图
     */
    MAP,

    /**
     * 地图上的控件
     */
    MAP_WIDGET,

    /**
     * 地图恢复
     */
    MAP_RESTORE,

    /**
     * 定位
     */
    LOCATION,

    /**
     * 下发活动配置
     */
    ACTIVITY_CONFIG,

    /**
     * 帷千
     */
    WEIQIAN,

    /**
     * 拥堵信息
     */
    TMC,

    /**
     * 用户中心
     */
    USER_CENTER,

    /**
     * 网络
     */
    HTTP_NET,

    /**
     * 脚本
     */
    SCRIPT,

    /**
     * 统计
     */
    ANALYSIS,

    /**
     * POI 详情
     */
    POI_DETAIL,

    /**
     * obd
     */
    OBD,

    /**
     * 启动
     */
    LAUNCH,

    /**
     * 数据
     */
    DATA,

    /**
     * 遮罩层
     */
    MASK,

    /**
     * 任务
     */
    TASK,

    /**
     * 搜索
     */
    QUERY,
    /**
     * 关于搜素模块的界面
     */
    QUERY_VIEWER,

    /**
     * 推送
     */
    PUSH,
    /**
     * 违章相关
     * （注意：之前违章模块都直接使用了push，原因未知。如果发现有类似的情况，请帮忙修改为这个标记）
     */
    VIOLATION,
    /**
     * 车道线
     */
    LANE,
    /**
     * 电子眼
     */
    ELECTRONIC,
    /**
     * 版本自更新
     */
    UPDATE,
    /**
     * 高速路牌
     */
    HIGHWAY,
    /**
     * 路口放大图
     */
    EXPANDVIEW,
    /**
     * 横竖屏
     */
    SCREEN,
    /**
     * 逆地理
     */
    INVERSE,
    /**
     * 游标
     */
    CURSOR,
    /**
     * 小地图
     */
    SMAP,
    /**
     * 外部调用
     */
    OUT_CALL,
    /**
     * 覆盖物
     */
    OVERLAY,
    /**
     * 气泡面板
     */
    ANNOTATIONPANEL,
    /**
     * 打印城市信息
     */
    CITY,
    /**
     * 打印城市模块调试信息（CITY太长，平常调试使用这个标签）
     */
    CITY_DEBUG,
    /**
     * 违章高发,违停,限行查询
     */
    ILLEGAL,
    /**
     * 星图
     */
    STAR_MAP,
    /**
     * 速度测试
     */
    SPEED,
    /**
     * 人工导航
     */
    ECAR,
    /**
     * 广告
     */
    ADVERTISE,

    /**
     * 路况订阅提醒
     */
    TMCRSS,

    /**
     * 存储设备
     */
    STORAGE_DEVICE,

    /**
     * Welink
     */
    WELINK,
    /**
     * loading 对话框
     */
    LOADING,
    /**
     *
     */
    TMCSURVEY,
    /**
     * 场景（车机，手机，后视镜)
     */
    SCENE,
    /**
     * 页面栈
     */
    PAGE_STACK,
    /**
     * 自定义toast使用
     */
    TOAST,
    /**
     * 在线参数相关
     */
    ONLINE_CONFIG,
    /**
     * 横屏分页翻页功能
     */
    PAGE,
    /**
     * 点击事件
     */
    CLICK,
    /**
     * 自定义对话框相关
     */
    DIALOG,

    /**
     * 步行导航相关
     */
    WALK_NAVI,

    /**
     * 偏航提示
     */
    YAW,

    /**
     * 语音播报相关
     */
    TTS_AUDIO,
    /**
     * 链接
     */
    LINK,
    /**
     * 数据传输
     */
    TRANSPORT_SERVER,
    /**
     * 数据传输客户端
     */
    TRANSPORT_CLIENT,
    TRANSPORT_CLIENT_ADB,
    TRANSPORT_DOWNLOAD,
    TRANSPORT_CLIENT_DOWNLOAD,

    /**
     * 音频相关(录制, 播放)
     */
    AUDIO,

    /**
     * 群组导航相关
     */
    GROUP_NAVI,

    /**
     * WebSocket, 长连接相关
     */
    WEB_SOCKET,

    /**
     * 网络
     */
    NETWORK,
    /**
     * 行程相关
     */
    TRIP,
    /**
     * webview相关
     */
    WEBVIEW,
    /**
     * 上滑托盘效果
     */
    SLIDING_UP_PANEL,
    /**
     * 气泡面板
     */
    BUBBLE,
    AFFECTED_TITLE,
    /**
     * 自定义组件
     */
    ASSEMBLE,
    WATER_DROP,
    /**
     * 收藏
     */
    FAVORITE,
    /**
     * 语音控制
     */
    VOICE_CONTROL,
    /**
     * real 3d
     */
    REAL_3D,
    /**
     * 数据商店
     */
    DATA_STORE,

    /**
     * 智能目的地
     */
    SMART_DESTINATION,
    /**
     * 所有（研发请勿使用）
     */
    @Deprecated
    ALL;

    @Override
    public String getTagName() {
        return name();
    }

}
