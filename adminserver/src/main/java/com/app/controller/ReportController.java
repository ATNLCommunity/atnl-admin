package com.app.controller;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;
import n.fw.utils.SignUtils;

public class ReportController extends BaseController
{
    private static final String NB_REPORT_KEY = "nb_shian&atnl@1340";

    public void server()
    {
        String sid = getPara("sid");
        if (StringUtils.isBlank(sid))
        {
            errorInvalid();
            return;
        }

        String addr = "test.atunala.com";
        success(addr);
    }

    public void report()
    {
        String sid = getPara("sid");
        if (StringUtils.isBlank(sid))
        {
            errorInvalid();
            return;
        }

        String lon = getPara("lon", "0");
        String lat = getPara("lat", "0");

        String step = getPara("step", "0");
        String time = getPara("time", "0");

        String sign = getPara("sign", "");

        StringBuffer sb = new StringBuffer();
        sb.append("sid=" + sid);
        sb.append(",lon=" + lon);
        sb.append(",lat=" + lat);
        sb.append(",step=" + step);
        sb.append(",time=" + time);
        System.out.println(sb.toString());

        String mySign = SignUtils.sign(sb.toString(), NB_REPORT_KEY);
        if (!StringUtils.equals(sign, mySign))
        {
            System.out.println("sign=" + sign + ",mysign=" + mySign);
            errorInvalid();
            return;
        }

        success();
    }
}