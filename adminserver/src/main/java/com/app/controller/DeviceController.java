package com.app.controller;

import java.util.HashMap;
import java.util.List;

import com.app.model.Device;
import com.app.model.Log;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;

public class DeviceController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        String name = getPara("name");
        String bindcode = getPara("bindcode");
        String bindname = getPara("bindname");
        Integer page = getParaToInt("page", 0);
        
        String where = Device.dao.buildWhere(name, bindcode, bindname);
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<Device> arr = Device.dao.list(where, page);
        map.put("arr", arr);
        map.put("count", Device.dao.count(where));
        success(map);
    }

    public void add()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        String name = getPara("name");
        if (StringUtils.isBlank(name))
        {
            errorInvalidOper();
            return;
        }

        Device device = Device.dao.create(name);
        if (device == null)
        {
            error("创建失败");
            return;
        }

        Log.dao.add(getUid(), "device/add", "add device - id=" + device.getLong(Device.ID) + " name=" + name);
        success(device);
    }

    public void bind()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
        Long id = getParaToLong("id", 0L);
        String code = getPara("bindcode", "");
        String name = getPara("bindname", "");
        String detail = getPara("detail", "");
        Device.dao.bind(id, code, name, detail);
        
        Log.dao.add(getUid(), "device/bind", "bind device - code=" + code + " name=" + name + " detail=" + detail);
        success();
    }

    public void delete()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
        Long id = getParaToLong("id", 0L);
        Device.dao.delete(id);
        Log.dao.add(getUid(), "device/delete", "bind device - id=" + id);
        success();
    }
}