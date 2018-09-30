package com.app.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;

import com.app.model.Goods;

import n.fw.base.BaseController;

public class GoodsController extends BaseController
{
  
    public void get()
    {
        String gid = getPara("gid", "");
        if (gid.equals(""))
        {
            error("商品id不能为空");
            return;
        }

        success(Goods.dao.findById(gid));
    }
    
   public void add()
   {
	   Long orderid = getParaToLong("orderid",0l);
	   Long sheepid = getParaToLong("sheepid",0l);
	   String blockurl = getPara("blockurl","");
	   String gicon = getPara("gicon","");
	   String killtime = getPara("killtime","");
	   String workphoto = getPara("workphoto","");
	   String packtime = getPara("packtime","");
	   String packphoto = getPara("packphoto","");
	   
	   success(Goods.dao.create(orderid, sheepid, blockurl, gicon, killtime, workphoto, packtime, packphoto));
   }
   
   public void list()
   {
	   int page = getParaToInt("page", 0);
       success(Goods.dao.list(page));
       
   }
   
   public void updatePic()
   {
	   try {
		   
		   DigestUtils.md5Hex(new FileInputStream(""));
		   
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
}