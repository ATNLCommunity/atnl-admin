package com.app.controller;

import java.util.HashMap;
import java.util.List;

import com.app.model.Log;
import com.app.model.Sheep;

import n.fw.base.BaseController;

public class SheepController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        String sid = getPara("sid", "");
        Integer page = getParaToInt("page", 0);
        
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<Sheep> arr = Sheep.dao.list(sid, page);
        map.put("arr", arr);
        map.put("count", Sheep.dao.count(sid));
        success(map);
    }

    public void add()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        String sid = getPara("sid", "");
        Sheep sheep = Sheep.dao.create(sid);
        if (sheep != null)
        {
            Log.dao.add(getUid(), "sheep/create", "create shepp - id=" + sheep.getLong(Sheep.ID) + " sid=" + sid);
        }
        success(sheep);
    }

    public void delete()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long id = getParaToLong("id", 0L);
        if (id != 0)
        {
            Sheep.dao.delete(id);
            Log.dao.add(getUid(), "sheep/delete", "delete sheep - id=" + id);
        }
        success();
    }
}