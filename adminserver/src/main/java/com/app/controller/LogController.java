package com.app.controller;

import java.util.HashMap;

import com.app.model.Log;

import n.fw.base.BaseController;

public class LogController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        Long uid = getParaToLong("uid", 0l);
        String opt = getPara("opt");
        String msg = getPara("msg");
        String fromTime = getPara("from");
        String toTime = getPara("to");
        Integer page = getParaToInt("page", 0);
        
        String where = Log.dao.buildWhere(uid, opt, msg, fromTime, toTime);
        HashMap<String, Object> map = new HashMap<String,Object>();
        map.put("arr", Log.dao.list(where, page));
        map.put("count", Log.dao.count(where));
        success(map);
    }
}