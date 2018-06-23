package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

import n.fw.utils.DateUtils;

public class EventRecord extends Model<EventRecord> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  
	public static final String ID = "id";
    public static final String SHEEPID = "sheepid";
    public static final String RECORDTIME = "recordtime";
    public static final String EVENT = "event";

    public EventRecord create(Long sheepid, String event)
    {
    	EventRecord eventRecord = new EventRecord();

    	eventRecord.set(SHEEPID, sheepid);
    	eventRecord.set(EVENT, event);
        String recordtime = DateUtils.getDateTime();
        eventRecord.set(RECORDTIME, recordtime);

        if (eventRecord.save())
        {
            return eventRecord;
        }
        return null;
    }
    
    public EventRecord getLeastRecord(Long sheepid)
    {
    	return findFirst("SELECT * FROM eventrecord WHERE sheepid=? ORDER BY id DESC limit 1", sheepid);
    }

    public List<EventRecord> getAll(Long sheepid)
    {
    	return find("SELECT * FROM eventrecord WHERE sheepid=?", sheepid);
    }
}
