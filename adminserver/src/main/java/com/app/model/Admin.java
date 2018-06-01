package com.app.model;

import com.jfinal.plugin.activerecord.Model;

public class Admin extends Model<Admin>
{
    private static final long serialVersionUID = 3554755307155224108L;

    public static final Admin dao = new Admin();

    public static final String ID = "id";
    public static final String LEVEL = "level";
    public static final String NAME = "name";
    public static final String PWD = "pwd";

    public Admin findByName(String name)
    {
        return findFirst("SELECT * FROM admin WHERE name=? LIMIT 1", name);
    }
}
