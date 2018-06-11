package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

// 优惠券表
public class Quan extends Model<Quan>
{
    private static final long serialVersionUID = 3554755307155224115L;

    public static final Quan dao = new Quan();

    public static final String ID = "id";       // 优惠券id
    public static final String UID = "uid";     // 所有者id
    public static final String NAME = "name";   // 优惠券名称
    public static final String MONEY = "money"; // 可以兑换价格
    public static final String STATE = "state"; // 状态 1表示可用，0表示已用
    public static final String EXPIRE_TIME = "expire_time"; // 过期时间
    public static final String CREATE_TIME = "create_time"; // 创建时间

    public List<Quan> list(Long uid, int page)
    {
        if (uid == 0)
        {
            return find("SELECT * FROM quan ORDER BY id DESC LIMIT ?,20", page * 20);
        }
        else
        {
            return find("SELECT * FROM quan WHERE uid=? ORDER BY id DESC LIMIT ?,20", uid, page * 20);
        }
    }

    public Long count(Long uid)
    {
        if (uid == 0)
        {
            return Db.queryLong("SELECT count(1) FROM quan");
        }
        else
        {
            return Db.queryLong("SELECT count(1) FROM quan WHERE uid=?", uid);
        }
    }
}