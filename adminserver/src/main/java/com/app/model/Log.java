package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

import n.fw.utils.DateUtils;

// 操作日志表
public class Log extends Model<Log>
{
    private static final long serialVersionUID = 3554755307155224117L;

    public static final Log dao = new Log();

    public static final String ID = "id";   // 主键
    public static final String UID = "uid"; // 操作者ID
    public static final String OPT = "opt"; // 操作
    public static final String MSG = "msg"; // 操作信息
    public static final String TIME = "time";   // 时间

    public Log add(Long uid, String opt, String msg)
    {
        Log log = new Log();
        log.set(UID, uid);
        log.set(OPT, opt);
        log.set(MSG, msg);
        log.set(TIME, DateUtils.getDateTime());
        if (log.save())
        {
            return log;
        }
        return null;
    }

    public String buildWhere(Long uid, String opt, String msg, String fromTime, String toTime)
    {
        StringBuffer sb = new StringBuffer();
        if (uid != null && uid != 0)
        {
            sb.append("uid=" + uid);
        }
        if (StringUtils.isNotBlank(opt))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("opt LIKE '%" + opt + "%'");
        }
        if (StringUtils.isNotBlank(msg))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("msg LIKE '%" + msg + "%'");
        }
        if (StringUtils.isNotBlank(fromTime))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("time>='" + fromTime + "'");
        }

        if (StringUtils.isNotBlank(toTime))
        {
            if (sb.length() > 0)
            {
                sb.append(" AND ");
            }
            sb.append("time<='" + toTime + "'");
        }
        return sb.toString();
    }

    public List<Log> list(String where, int page)
    {
        if (StringUtils.isBlank(where))
        {
            return find("SELECT * FROM log ORDER BY id DESC LIMIT ?,20", page * 20);
        }
        else
        {
            return find("SELECT * FROM log WHERE " + where + " ORDER BY id DESC LIMIT ?,20", page * 20);
        }
    }

    public Long count(String where)
    {
        if (StringUtils.isBlank(where))
        {
            return Db.queryLong("SELECT COUNT(1) FROM log");
        }
        else
        {
            return Db.queryLong("SELECT COUNT(1) FROM log WHERE " + where);
        }
    }
}