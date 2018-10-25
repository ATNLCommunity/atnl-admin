package com.app.controller;

import com.app.model.Admin;
import com.app.model.Apply;
import com.app.model.User;

import n.fw.base.BaseController;
import n.fw.utils.DateUtils;

public class ApplyController extends BaseController {
	public void update() {
		Long uid = getUid();
		if (uid == null || uid == 0) {
			error("请先登录");
			return;
		}
		Admin admin = Admin.dao.findById(uid);
		if (null == admin) {
			error("用户不存在");
			return;
		}
		Long aid = getParaToLong("id", 0l);
		if (0l == aid) {
			error("记录id不能为空");
			return;
		}
		Apply apply = Apply.dao.findById(aid);
		if (null == apply) {
			error("记录不存在");
			return;
		}
		apply.set(Apply.CHECKID, uid);
		apply.set(Apply.CHECKTIME, DateUtils.getDateTime());
		apply.set(Apply.STATE, 1);
		if (!apply.update()) {
			error("数据异常");
			return;
		}
		User user = User.dao.findById(apply.getLong(Apply.USERID));
		if(null == user)
		{
			error("用户不存在");
			return;
		}
		user.set(User.UTYPE, 3);// 直销类型为3
		user.update();
		success(Apply.dao.findById(aid));
	}

	public void list() {
		int page = getParaToInt("page", 0);
		if (page < 0) {
			page = 0;
		}
		success(Apply.dao.getList(page));
	}
	
	public void records() {
		int page = getParaToInt("page", 0);
		if (page < 0) {
			page = 0;
		}
		success(Apply.dao.getRecords(page));
	}
}