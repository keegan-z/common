package com.keegan.commondemo.api;


public class ApiConstants {

    /**
     * 广告位
     */
    public static final String URL_INDEXAD = "api/index/indexad";


    /**
     * 用户
     */

    /**
     * 新闻
     */
    public static final String URL_NEWSLIST = "api/index/newslist";//首页
    public static final String URL_NEWSDETAIL = "api/index/newsdetail";//资讯详情

    /**
     * 行情
     */
    //行情列表
    public static final String URL_QUERY_QUOTATION_LIST = "api/common/product";
//    public static final String URL_QUERY_QUOTATION_LIST = "https://www.fx-eapp.com/api/market/index";

    /**
     * 汇率
     */
    public static final String URL_COMPUTE = "api/common/compute";//获取换算汇率
    public static final String URL_WAIHUI = "api/common/waihui";//外汇列表

    /**
     * 版本
     */
    public static final String URL_UPGRADE = "api/common/upgrade";//版本升级

    /**
     * 会员接口
     */
    public static final String URL_MEMBER = "api/user/index";//会员中心
    public static final String URL_LOGIN = "api/user/login";//会员登录
    public static final String URL_REGISTER = "api/user/register";//会员注册
    public static final String URL_LOGOUT = "api/user/logout";// 注销登录
    public static final String URL_UPDATE_PERSONAL_INFO = "api/user/profile";// 修改会员个人信息
    public static final String URL_UPDATA_PSW = "api/user/resetpwd";//重置密码
    public static final String URL_COLLECT = "api/user/collect";//收藏与取消
    public static final String URL_COLLECT_LIST = "api/user/collectlist";//收藏列表
    public static final String URL_FEEDBACK = "api/user/complain";//意见反馈

    /**
     * 上传
     */
    public static final String URL_UPLOAD = "/api/common/upload";//上传文件

    /**
     * 版本升级
     */
    public static final String URL_UPDATE = "api/common/upgrade";//重置密码


}
