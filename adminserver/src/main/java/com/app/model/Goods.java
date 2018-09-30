package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;


public class Goods extends Model<Goods>
{

	private static final long serialVersionUID = 8612875850781959317L;

	public static final Goods dao = new Goods();

	
	
    public static final String ID = "id";  //商品id
    public static final String ORDERID = "orderid";//对应订单id
    public static final String SHEEPID = "sheepid";//源羊id
    public static final String BLOCKURL = "blockurl";//溯源链接
    public static final String GICON = "gicon";//显示图片
    public static final String KILLTIME = "killtime";//加工时间
    public static final String WORKPHOTO = "workphoto";//加工图片
    public static final String PACKTIME = "packtime";//打包时间
    public static final String PACKPHOTO = "packphoto";  //打包图片
    
    
    public Goods findById(Long gid)
    {
        return findFirst("SELECT * FROM goods WHERE id=? LIMIT 1", gid);
    }

    public Goods findByOid(long oid)
    {
        return findFirst("SELECT * FROM goods WHERE orderid=? LIMIT 1", oid);
    }
    
    public Goods create(Long orderid,Long sheepid, String blockurl,String gicon,String killtime,String workphoto,String packtime,String packphoto)
    {
    	Goods goods = new Goods();

    	goods.set(Goods.ORDERID,orderid);
    	goods.set(Goods.SHEEPID, sheepid);
    	goods.set(Goods.BLOCKURL, blockurl);
    	goods.set(Goods.GICON, gicon);
    	goods.set(Goods.KILLTIME, killtime);
    	goods.set(Goods.WORKPHOTO, workphoto);
    	goods.set(Goods.PACKTIME, packtime);
    	goods.set(Goods.PACKPHOTO, packphoto);
    	if (goods.save())
        {
            return goods;
        }
        return null;
    }
    
    public List<Goods> list(int page)
    {
    	return find("SELECT * FROM `goods` ORDER BY id DESC LIMIT ?,20", page * 20);
    }
}