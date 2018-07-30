package com.app.controller;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;
import n.fw.utils.SignUtils;
import net.sf.json.JSONObject;

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

        JSONObject json = new JSONObject();
        json.put("addr", "test.atunala.com");
        json.put("interval", 60);
        success(json);
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
        String power = getPara("power", "0");
        String state = getPara("state", "0");

        String time = getPara("time", "0");

        String sign = getPara("sign", "");

        StringBuffer sb = new StringBuffer();
        sb.append("sid=" + sid);
        sb.append(",lon=" + lon);
        sb.append(",lat=" + lat);
        sb.append(",step=" + step);
        sb.append(",power=" + power);
        sb.append(",state=" + state);
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