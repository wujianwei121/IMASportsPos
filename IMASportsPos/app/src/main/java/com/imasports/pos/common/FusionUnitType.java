package com.imasports.pos.common;

/**
 * Created by lenovo on 2017/8/9.
 */

public class FusionUnitType {
    public interface DeviceType {
        /**
         * 拉卡拉机子
         */
        String DEVICETYPE_LAKALA = "lakala";
        /**
         * p990银联机子
         */
        String DEVICETYPE_UNIPAY_P990 = "unipay_p990";
        /**
         * 银联a8 2.0机子
         */
        String DEVICETYPE_UNIPAY_A82 = "unipay_a8_2.0";
        /**
         * 银联a8 3.0机子
         */
        String DEVICETYPE_UNIPAY_A83 = "unipay_a8_3.0";
        /**
         * 沃银a8
         */
        String DEVICETYPE_WO_A8 = "wo_a8";
        /**
         * 汇宜pos
         */
        String DEVICETYPE_NYPOS_A8 = "nypos_a8";
    }

    /**
     * 主界面系统功能
     */
    public interface FuncType {
        int FUNC_MEMBER_ENTER = 1001;//会员登记
        int FUNC_COURSE_SALE = 1002;//课程销售
        int FUNC_CARD_CHANNEL = 1003;//票卡核销
        int FUNC_MEMBER_RECHARGE = 1004;//会员充值
        int FUNC_QEURYORDER = 1005;//订单查询
        int FUNC_MEMBER_SERACH = 1006;//会员查询
    }

    public interface StoreStatus {
        int OPEN = 1;//经营中
        int CLOSE = 2;//闭店
        int STOP = 3;//停业
    }


    /**
     * 服务接口地址
     */
    public enum ServiceEndPointMethod {

        /*******************************以下是2.0版本接口*********************************/
        /**
         * 获取商品子类别+第一个分类的商品列表（商品体系接口1号）
         */
        GetTypeAndFirstItems("product/typenew", 101),
        /**
         * 获取商品列表（商品体系接口2号）
         */
        GetProducts("product/list", 102),
        /**
         * 根据条码获取商品（商品体系接口3号）
         */
        GetProductForBarcode("product/barcode", 103),
        /**
         * 获取套餐下的子项目（商品体系接口4号）
         */
        GetPackageItems("product/package", 104),
        /**
         * 下单接口（订单体系接口1号）
         */
        ConfirmOrder("order/confirm", 105),
        /**
         * 支付接口（订单体系接口2号）
         */
        PayOrder("order/pay", 106),
        /**
         * 订单状态查询接口（订单体系接口3号）
         */
        QueryOrderStatus("order/status", 107),
        /**
         * 订单退款查询（订单体系接口5号）
         */
        OrderReturnQuery("order/rquery", 108),
        /**
         * 退款下单（订单体系接口6号）
         */
        ConfirmReturnOrder("order/rorder", 109),
        /**
         * 退款支付（订单体系接口7号）
         */
        PayReturnOrder("order/rpay", 110),
        /**
         * 计次卡或周期卡可用消费项目(会员体系接口4号)
         */
        GetItemsOfTimeOrCycle("member/items", 111),
        /**
         * 注册或编辑会员(会员体系接口1号)
         */
        RegisterOrUpdateMember("member/register", 112),
        /**
         * 会员挂失与解挂接口(会员体系接口2号)
         */
        UpdateMemberCardStatus("member/status", 113),
        /**
         * 会员消费票卡查询接口(会员体系接口3号)
         */
        GetConsumptionCards("member/cards", 114),
        /**
         * 计次项目核销接口(会员体系接口5号)
         */
        ConsumeTimeCard("member/ctime", 115),
        /**
         * 计次卡退次(会员体系接口6号)
         */
        ReturnFrequency("member/frequency", 116),
        /**
         * 卡的消费明细记录（带分页）(会员体系接口7号)
         */
        GetRecordsForConsume("member/crecords", 117),
        /**
         * 卡的充值记录（带分页）(会员体系接口8号)
         */
        GetRecordsForRecharge("member/rrecords", 118),
        /**
         * 周期项目核销接口(会员体系接口10号)
         */
        ConsumeCycleCard("member/ccycle", 119),
        /**
         * 会员可充值票卡查询接口(会员体系接口11号)
         */
        GetCardsForRecharge("member/rlist", 120),
        /**
         * 会员可升级票卡查询接口(会员体系接口12号)
         */
        GetCardsForUpgrade("member/ulist", 121),
        /**
         * 某票卡可升级套餐项目查询接口(会员体系接口13号)
         */
        GetAvaliableUpgradeItems("member/uitems", 122),
        /**
         * 会员基础信息查询接口（更多详情）(会员体系接口15号)
         */
        SearchMemberInfo("member/baseinfo", 123),
        /**
         * 周期卡暂停接口(会员体系接口16号)
         */
        CardDelay("member/delay", 124),
        /**
         * 周期卡取消暂停接口(会员体系接口17号)
         */
        CardDelayCancel("member/cdelay", 125),
        /**
         * 版本检查接口
         */
        CheckVersion("version/check", 126),
        /**
         * 交接班详情统计接口
         */
        SearchHandoverSummary("summary/hs", 127),
        /**
         * 交接班统计保存接口
         */
        SaveHandoverSummary("summary/savehs", 128),
        /**
         * 退卡下单（订单体系接口8号）
         */
        ConfirmReturnCardOrder("order/rcpay", 129),
        /**
         * 登录接口
         */
        LoginIn("oauth/token", 130),
        /**
         * 密码修改
         */
        ChangePassword("user/changepassword", 131),

        /**
         * 会员持票卡查询接口(会员体系接口28号)
         */
        MemberCardInfo("member/cardinfo", 133),
        /**
         * 注销登录
         */
        LoginOut("oauth/logout", 134),
        /**
         * 订单查询（带分页）（订单体系接口9号）
         */
        QueryOrder("order/query", 135),
        /**
         * 订单明细查询（订单体系接口10号）
         */
        QueryOrderDetail("order/detail", 136),
        /**
         * 模糊查询会员（会员体系30号）
         */
        SearchTopTenMembers("member/searchten", 137),
        /**
         * 会员订单查询（带分页）  （订单体系11号）
         */
        QueryMemberOrder("order/mquery", 138),
        /**
         * 会员核销记录（带分页）（会员体系30号）
         */
        GetRecordsForMemberConsume("member/mrecords", 139),
        /**
         * 售卖商品个数（带分页）
         */
        QuerySaleCounts("order/counts", 140),
        /**
         * 卡的延期/暂停记录（带分页）会员体系33
         */
        GetExtendOrDelayRecords("member/orecords", 141),
        /**
         * 获取会员卡号
         */
        GetCardNo("member/cardno", 142),
        /**
         * 微信撤单接口(订单体系13)
         */
        CancelOrder("order/cancel", 143),
        /**
         * 会员会员信息查询(订单体系14)
         */
        QueryMemberInfo("order/memberinfo", 144),
        /**
         * 获取订单促销活动接口
         */
        OrderActivity("order/orderactivity", 145),
        /**
         * 获取门店用户列表
         */
        GetUsers("/user/getusers", 146),
        /**
         * 每日营收接口
         */
        DayRevenue("summary/day", 200),

        /**
         * 刷新TOKEN
         */
        RefreshToken("oauth/token", 201);

        /**
         * 类型名称
         */
        private String serviceName;
        /**
         * 类型ID
         */
        private int serviceID;

        ServiceEndPointMethod(String name, int id) {
            this.serviceName = name;
            this.serviceID = id;
        }

        @Override
        public String toString() {
            return this.serviceID + "";
        }

        /**
         * 获取接口ID
         */
        public int getMethodID() {
            return this.serviceID;
        }

        /**
         * 获取接口名称
         */
        public String getMethodName() {
            return this.serviceName;
        }

    }
}
