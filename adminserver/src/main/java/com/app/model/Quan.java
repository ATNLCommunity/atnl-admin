package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class Quan extends Model<Quan>
{
    private static final long serialVersionUID = 3554755307155224115L;

    public static final Quan dao = new Quan();

    public static final String ID = "id";
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String MONEY = "money";
    public static final String STATE = "state";
    public static final String EXPIRE_TIME = "expire_time";
    public static final String CREATE_TIME = "create_time";

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