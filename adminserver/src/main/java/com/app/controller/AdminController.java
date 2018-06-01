package com.app.controller;

import com.app.model.Admin;
import com.jfinal.plugin.activerecord.Db;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;

public class AdminController extends BaseController
{
    public void login()
    {
        String name = getPara("name", "");
		String pwd = getPara("pwd", "");

		if (StringUtils.isBlank(name) || StringUtils.isBlank(pwd)) {
			errorInvalid();
			return;
		}

		Admin admin = Admin.dao.findByName(name);
		if (admin == null) {
			error("用户不存在");
			return;
		}

		if (!StringUtils.equals(pwd, admin.getStr(Admin.PWD))) {
			error("密码错误");
			return;
		}

        setUid(admin.getLong(Admin.ID));
        setLevel(admin.getInt(Admin.LEVEL));
		success(admin);
	}
	
	public void islogin()
	{
		Long uid = getUid();
		if (uid == null || uid == 0)
		{
			success(0);
		}
		success(1);
	}

	public void atnl()
	{
		if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
		}
		
		success(Db.findFirst("SELECT SUM(atnl) AS atnl,SUM(lockatnl) AS lockatnl,SUM(needatnl) AS needatnl,SUM(allatnl) AS allatnl FROM user"));
	}
}