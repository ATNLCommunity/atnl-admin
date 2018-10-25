package com.app.model;
import java.util.List;

import com.jfinal.plugin.activerecord.Model;

import n.fw.utils.DateUtils;

public class Apply extends Model<Apply> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8968813007010578000L;

	public static final Apply dao = new Apply();

	public static final String ID = "id";
    public static final String USERID = "userid";
    public static final String CONTACT = "contact";
    public static final String TYPE = "type";
    public static final String CONTENT = "content";
    public static final String CREATETIME = "createtime";
    public static final String STATE = "state";
    public static final String CHECKID = "checkid";
    public static final String CHECKTIME = "checktime";
    public static final String NAME = "name";
	
    public Apply create(Long userid,String contact,int type)
    {
    	Apply apply = new Apply();
    	apply.set(USERID, userid);
    	apply.set(CONTACT, contact);
    	apply.set(TYPE, type);
    	apply.set(STATE, 0);
    	apply.set(CREATETIME, DateUtils.getDateTime());

        if (apply.save())
        {
            return apply;
        }
        return null;
    }
    
    public List<Apply> getList(int page)
    {
    	return find("select * from apply where state = 0 ORDER BY id DESC LIMIT ?,20", page * 20);
    }

    public List<Apply> getRecords(int page)
    {
    	return find("select * from apply where state = 1 ORDER BY id DESC LIMIT ?,20", page * 20);
    }
}
