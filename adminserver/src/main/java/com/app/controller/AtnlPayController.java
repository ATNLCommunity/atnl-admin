package com.app.controller;


import com.app.model.AtnlPayRecord;


import n.fw.base.BaseController;
import n.fw.utils.DateUtils;

public class AtnlPayController extends BaseController
{
  
	public void getByMonth()
    {
    	Long uid = getParaToLong("uid",0l);
        
        int page = getParaToInt("page",0);
    	String datestr = getPara("month",DateUtils.getDate());
    	success(AtnlPayRecord.dao.getByMonth(uid, datestr,page));
    }
    
    public void getAll()
    {
    	Long uid = getParaToLong("uid",0l);
    	int page = getParaToInt("page",0);
    	success(AtnlPayRecord.dao.getAll(uid,page));
    }
   
}