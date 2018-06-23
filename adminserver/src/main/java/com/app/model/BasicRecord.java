package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

import n.fw.utils.DateUtils;

public class BasicRecord extends Model<BasicRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  
	public static final String ID = "id";
    public static final String SHEEPID = "sheepid";
    public static final String RECORDTIME = "recordtime";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String DIFFW = "diffw";
    public static final String DIFFH = "diffh";
    
    
    public BasicRecord create(Long sheepid, float weight,float height,float diffw,float diffh)
    {
    	BasicRecord basicRecord = new BasicRecord();

    	basicRecord.set(SHEEPID, sheepid);
    	basicRecord.set(WEIGHT, weight);
    	basicRecord.set(HEIGHT, height);
    	basicRecord.set(DIFFW, diffw);
    	basicRecord.set(DIFFH, diffh);
        String recordtime = DateUtils.getDateTime();
        basicRecord.set(RECORDTIME, recordtime);
        

        if (basicRecord.save())
        {
            return basicRecord;
        }
        return null;
    }
    
    public BasicRecord getLeastRecord(Long sheepid)
    {
    	return findFirst("SELECT * FROM basicrecord WHERE sheepid=? ORDER BY id DESC limit 1", sheepid);
    }

    public List<BasicRecord> getAll(Long sheepid)
    {
    	return find("SELECT * FROM basicrecord WHERE sheepid=?", sheepid);
    }
}
