package com.app.controller;

import java.util.HashMap;
import java.util.List;

import com.app.model.Quan;

import n.fw.base.BaseController;

public class QuanController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        Long uid = getParaToLong("uid", 0l);
        Integer page = getParaToInt("page", 0);
        
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<Quan> arr = Quan.dao.list(uid, page);
        map.put("arr", arr);
        map.put("count", Quan.dao.count(uid));
        success(map);
    }
}