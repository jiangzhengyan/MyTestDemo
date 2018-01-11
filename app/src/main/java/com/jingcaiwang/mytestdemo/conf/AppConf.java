package com.jingcaiwang.mytestdemo.conf;

/**
 * Created by jiang_yan on 2017/9/28.
 * 接口
 */

public class AppConf {

    public static final String APPDIR = "workerApp";

    //    public static final String BASE_URL = "http://192.168.30.115:8181";192.168.30.38
    public static final String BASE_URL = "http://testjcgj.jingcaiwang.cn:8881";
    public static final String BASE_URL_1 = "http://testjcgj.jingcaiwang.cn:8886";

    public static final String PREFIX = "";


    public static final String TOAS_TLIST = BASE_URL + PREFIX + "/toast/toastList.do";
    public static final String GET_BANNER = BASE_URL + PREFIX + "/banner/getBanner.do";
    public static final String ADD_TOAST = BASE_URL + PREFIX + "/toast/addToast.do";
    public static final String GET_MY_APP_FUNCTIONS = BASE_URL + PREFIX + "/toast/addToast.do";




    public static final int CONNECTION_TIME_OUT = 10000;
    public static final int READ_TIME_OUT = 10000;
    public static final int WRITE_TIME_OUT = 10000;
    public static final String NET_FAIL_MSG = "网络请求失败";
    public static final String NET_SERVICE_ERR_MSG = "服务器忙,请稍后再试";
    public static final String NET_DAATA_PARSE_ERR_MSG = "数据解析失败";
    public static final String DOWNLOAD_ERR_MSG = "数据异常,下载失败";

}
