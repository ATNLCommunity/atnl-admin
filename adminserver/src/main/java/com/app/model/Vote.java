package com.app.model;

import com.jfinal.plugin.activerecord.Model;

// 投票表
public class Vote extends Model<Vote>
{
    private static final long serialVersionUID = 3554755307155224124L;

    public static final Vote dao = new Vote();

    public static final String UID = "uid";     // 用户id
    public static final String IDX = "idx";     // 投票id
    public static final String UPDATE_TIME = "update_time"; // 投票时间

    public Vote findByUid(Long uid)
    {
        return findFirst("SELECT * FROM VOTE WHERE uid=?", uid);
    }
}