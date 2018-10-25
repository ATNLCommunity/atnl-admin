package com.app.controller;

import java.util.HashMap;

import com.app.model.BankInfo;
import com.app.model.Withdrawal;

import n.fw.base.BaseController;
import n.fw.utils.DateUtils;


public class WithdrawalController extends BaseController
{

	  public void update()
	  {
		  Long uid = getUid();
	      if (uid == null || uid == 0)
	      {
	          error("请先登录");
	          return;
	      }
	      Long wid = getParaToLong("wid", 0l);
	      if(0l == wid)
	      {
	    	  error("记录id不能为空");
	          return;
	      }
	      Withdrawal wd = Withdrawal.dao.findById(wid);
	      if(null == wd)
	      {
	    	  error("提现记录不存在");
	          return;
	      }
	      wd.set(Withdrawal.CHECKID, uid);
		  wd.set(Withdrawal.CHECKTIME, DateUtils.getDateTime());
		  wd.set(Withdrawal.STATE, 2);  
	      if(!wd.update())
		  {
			  error("数据异常，请联系客服");
	          return;
		  }
		  wd = Withdrawal.dao.findById(wid);
		  success(wd);
	  }
  
	  public void list()
	  {
		  Long uid = getUid();
	      if (uid == null || uid == 0)
	      {
	          error("请先登录");
	          return;
	      }
	      int page = getParaToInt("page",0);
	      if(page < 0)
	      {
	    	  page = 0;
	      }
	      success(Withdrawal.dao.getAll(page));
	  }
	  
	  /**
	   * 已体现记录
	   */
	  public void records()
	  {
		  Long uid = getUid();
	      if (uid == null || uid == 0)
	      {
	          error("请先登录");
	          return;
	      }
	      int page = getParaToInt("page",0);
	      if(page < 0)
	      {
	    	  page = 0;
	      }
	      success(Withdrawal.dao.getRecords(page));
	  }
	  
	  public void getSingle()
	  {
		  Long wid = getParaToLong("wid", 0l);
			if (wid == 0l) {
				error("提现月份不能为空");
				return;
			}
			Withdrawal wd = Withdrawal.dao.findById(wid);
			if (null == wd) {
				error("提现申请月份不存在");
				return;
			}
			BankInfo bankinfo = BankInfo.dao.findById(wd.getLong(Withdrawal.BANKID));
			HashMap<String, Object> map = new HashMap<String,Object>();
			map.put("wd", wd);
			map.put("bank", bankinfo);
			success(map);
			
	  }
}