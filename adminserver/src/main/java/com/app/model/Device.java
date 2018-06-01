package com.app.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Device extends Model<Device> 
{
    private static final long serialVersionUID = 3554755307155224109L;

    public static final Device dao = new Device();

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String BINDCODE = "bindcode";
    public static final String BINDNAME = "bindname";
    public static final String BINDTIME = "bindtime";
    public static final String DETAIL = "detail";
    public static final String POWER = "power";
    public static final String UPDATE_TIME = "update_time";

    public Device create(String name) 
    {
        Device device = new Device();
        device.set(NAME, name);

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