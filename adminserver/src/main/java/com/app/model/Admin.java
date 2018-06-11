package com.app.model;

import com.jfinal.plugin.activerecord.Model;

// 后台管理员账号表
public class Admin extends Model<Admin>
{
    private static final long serialVersionUID = 3554755307155224108L;

    public static final Admin dao = new Admin();

    public static final String ID = "id";       // 账号ID
    public static final String LEVEL = "level"; // 权限等级
    public static final String NAME = "name";   // 账号名
    public static final String PWD = "pwd";     // 密码

    public Admin findByName(String name)
    {
        return findFirst("SELECT * FROM admin WHERE name=? LIMIT 1", name);
    }
}
