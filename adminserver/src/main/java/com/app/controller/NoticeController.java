package com.app.controller;

import com.app.model.Notice;


import n.fw.base.BaseController;

public class NoticeController extends BaseController
{
    /**
     * 分页获取公告信息
     */
	public void getNotice()
	{
		int page = getParaToInt("page",0);
		if(page <= 0)
		{
			page = 0;
		}
		else
		{
			page = page -1;
		}
		success(Notice.dao.list(page));
	}
	
	/**
	 * 增加公告信息
	 */
	public void addNotice()
	{
		if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }
		Long aid = getUid();
		if(aid == 0)
		{
			error("请重新登陆");
			return;
		}
		String title = getPara("title","");
		String content = getPara("content","");
		if(title.trim().length() == 0 || content.trim().length() == 0)
		{
			error("标题和内容都不能为空");
			return;
		}
		success(Notice.dao.create(aid, title, content));	
	}
	/**
	 * 修改公告信息
	 */
	
	public void updateNotice()
	{
		if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }
		Long aid = getUid();
		if(aid == 0)
		{
			error("请重新登陆");
			return;
		}
		Long id = getParaToLong("id",0L);
		if(id == 0L)
		{
			error("不存在的公告id");
			return;
		}
		Notice notice = Notice.dao.findById(id);
		if(null == notice)
		{
			error("不存在的公告id");
			return;
		}
		String title = getPara("title","");
		String content = getPara("content","");
		if(title.trim().length() == 0 || content.trim().length() == 0)
		{
			error("公告标题和公告内容不能为空");
			return;
		}
		notice.set(Notice.AID, aid);
		notice.set(Notice.TIILE, title);
		notice.set(Notice.CONTENT, content);
		notice.update();
		
		success(notice);
		
	}
	
	/*
	 * 删除公告
	 */
	public void deleteById()
	{
		if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }
		Long aid = getUid();
		if(aid == 0)
		{
			error("请重新登陆");
			return;
		}
		Long id = getParaToLong("id",0L);
		if(id == 0L)
		{
			error("不存在的公告id");
			return;
		}
		Notice notice = Notice.dao.findById(id);
		if(null == notice)
		{
			error("不存在的公告id");
			return;
		}
		success(notice.delete());
	}
	/**
	 * 显示单条详情
	 */
	public void getNoticeContent()
	{
		Long nid = getParaToLong("nid", 0l);
		
		success(Notice.dao.getByNid(nid));
	}
}
