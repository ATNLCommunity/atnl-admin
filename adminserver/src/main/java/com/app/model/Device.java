package com.app.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

// 物联网设备表
public class Device extends Model<Device> 
{
    private static final long serialVersionUID = 3554755307155224109L;

    public static final Device dao = new Device();

    public static final String ID = "id";               // 设备ID
    public static final String NAME = "name";           // 设备名称
    public static final String BINDCODE = "bindcode";   // 绑定物体ID,如羊的id -- 可重复绑定
    public static final String BINDNAME = "bindname";   // 绑定物体的名称，如羊的名称
    public static final String BINDTIME = "bindtime";   // 绑定的时间
    public static final String DETAIL = "detail";       // 设备详情描述
    public static final String POWER = "power";         // 电量
    public static final String UPDATE_TIME = "update_time"; // 最后更新时间
    public static final String BINDSTATE = "bindstate";

    public Device create(String name) 
    {
        Device device = new Device();
        device.set(NAME, name);
        device.set(BINDSTATE, 0);

        if (device.save())
        {
            return device;
        }
        return null;
    }

    public Device findByName(String name)
    {
        return findFirst("SELECT * FROM device WHERE name=? LIMIT 1", name);
    }
    
    public List<Device> list()
    {
        return find("SELECT * FROM device where bindstate = 0 limit 10");
    }


    public String buildWhere(String name, String bindcode, String bindname)
    {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotBlank(name))
        {
            sb.append("name LIKE '%" + name + "%'");
        }
        if (StringUtils.isNotBlank(bindcode))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND bindcode LIKE '%" + bindcode + "%'");
            }
            else
            {
                sb.append("bindcode LIKE '%" + bindcode + "%'");
            }
        }
        if (StringUtils.isNotBlank(bindname))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND bindname LIKE '%" + bindname + "%'");
            }
            else
            {
                sb.append("bindname LIKE '%" + bindcode + "%'");
            }
        }
        
        return sb.toString();
    }

    public List<Device> list(String where, int page)
    {
        if (StringUtils.isBlank(where))
        {
            return find("SELECT * FROM device ORDER BY `id` ASC LIMIT ?,20", page * 20);
        }
        else
        {
            return find("SELECT * FROM device WHERE " + where + " ORDER BY `id` ASC LIMIT ?,20", page * 20);
        }
    }

    public Long count(String where)
    {
        if (StringUtils.isBlank(where))
        {
            return Db.queryLong("SELECT COUNT(1) FROM device");
        }
        else
        {
            return Db.queryLong("SELECT COUNT(1) FROM device WHERE " + where);
        }
    }

    public void bind(Long id, String code, String name, String detal)
    {
        Db.update("UPDATE device SET bindcode=?,bindname=?,detail=?,bindtime=NOW() WHERE id=?", code, name, detal, id);
    }

    public void delete(Long id)
    {
        Db.update("DELETE FROM device WHERE id=?", id);
    }
}