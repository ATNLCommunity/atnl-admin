package com.app.model;


import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import n.fw.utils.DateUtils;

public class AtnlAddRecord extends Model<AtnlAddRecord> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6749423764933930663L;
	public static final AtnlAddRecord dao = new AtnlAddRecord();
	public static final String ID = "id";
    public static final String SHEEPID = "sheepid";
    public static final String RECORDTIME = "recordtime";
    public static final String ATNLADD = "atnladd";
	
    public AtnlAddRecord getLeastRecord(Long sheepid)
    {
    	return findFirst("SELECT * FROM atnladdrecord WHERE sheepid=? ORDER BY id DESC limit 1", sheepid);
    }

    public float getAtnlSum(Long sheepid)
    {
    	return Db.queryFloat("SELECT sum(atnladd) FROM atnladdrecord WHERE sheepid=?", sheepid);
    }
    
    public List<AtnlAddRecord> getAll(Long sheepid)
    {
    	return find("SELECT * FROM atnladdrecord WHERE sheepid=?", sheepid);
    }
    
    public AtnlAddRecord create(Long sheepid, int atnladd)
    {
    	AtnlAddRecord atnlAddRecord = new AtnlAddRecord();

    	atnlAddRecord.set(SHEEPID, sheepid);
    	atnlAddRecord.set(ATNLADD, atnladd);
        String recordtime = DateUtils.getDateTime();
        atnlAddRecord.set(RECORDTIME, recordtime);

        if (atnlAddRecord.save())
        {
            return atnlAddRecord;
        }
        return null;
    }
}
