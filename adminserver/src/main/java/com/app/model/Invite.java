package com.app.model;

import com.jfinal.plugin.activerecord.Model;

// 邀请设置表
public class Invite extends Model<Invite>
{
    private static final long serialVersionUID = 3554755307155224123L;

    public static final Invite dao = new Invite();

    public static final String ID = "id";       // 主键
    public static final String REG = "reg";     // 注册赠送
    public static final String INVITE = "invite";   // 邀请M1的赠送
    public static final String COUNT = "count"; // 开放注册人数

    public Invite get()
    {
        return findFirst("SELECT * FROM invite ORDER BY id DESC LIMIT 1");
    }
}