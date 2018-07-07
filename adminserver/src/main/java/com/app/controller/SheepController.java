package com.app.controller;

import java.util.HashMap;
import java.util.List;

import com.app.model.BasicRecord;
import com.app.model.DistanceRecord;
import com.app.model.EventRecord;
import com.app.model.GpsRecord;
import com.app.model.Log;
import com.app.model.Sheep;
import com.app.model.StepRecord;

import n.fw.base.BaseController;

public class SheepController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        String sid = getPara("sid", "");
        Integer page = getParaToInt("page", 0);
        
        HashMap<String, Object> map = new HashMap<String,Object>();
        List<Sheep> arr = Sheep.dao.list(sid, page);
        map.put("arr", arr);
        map.put("count", Sheep.dao.count(sid));
        success(map);
    }

    public void add()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
        String sid = getPara("sid", "");
        if(sid.length() == 0)
        {
        	error("档案编号不能为空");
        	return;
        }
        float price = Float.parseFloat(getPara("price",""));
        if(price == 0f)
        {
        	error("售卖价格不能为空");
        	return;
        }
        float weight = Float.parseFloat(getPara("weight",""));
        float height = Float.parseFloat(getPara("height",""));
        if(weight == 0f || height == 0f)
        {
        	error("体重身长不能为空");
        	return;
        }
        String birthday = getPara("birthday", "");
        String prekilltime = getPara("prekilltime", "");
        if(birthday.length() == 0 || prekilltime.length() == 0)
        {
        	error("出生日期和预收获日期不能为空");
        	return;
        }
        Sheep sheep = Sheep.dao.create(sid,price,birthday,weight,height,prekilltime);
        if (sheep != null)
        {
            Log.dao.add(getUid(), "sheep/create", "create shepp - id=" + sheep.getLong(Sheep.ID) + " sid=" + sid);
        }
        success(sheep);
    }
    
    /**
     * 添加羊
     */
    public void addSheep()
    {
    	if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
    	
    	
    }
    /**
     * 绑定设备
     */
    public void bindDevice()
    {
    	if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
    	Long sheepid = getParaToLong("sheepid", 0L);
    	if(sheepid == 0L)
    	{
    		error("不存在的羊羔ID");
    		return;
    	}
    	Long did = getParaToLong("did", 0L);
    	if(did == 0L)
    	{
    		error("不存在的设备ID");
    		return;
    	}
    	Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
    	float weight = Float.parseFloat(getPara("weight",""));
    	if(weight <= 0f || sheep.getFloat(Sheep.WEIGHT) > weight)
    	{
    		error("错误的体重信息");
    		return;
    	}
        float height = Float.parseFloat(getPara("height",""));
        if(height <= 0f ||sheep.getFloat(Sheep.HEIGHT) > height)
    	{
    		error("错误的身长信息");
    		return;
    	}
    	sheep.set(Sheep.DID, did);
    	sheep.update();
    	BasicRecord.dao.create(sheepid, weight, height, weight - sheep.getFloat(Sheep.WEIGHT), height - sheep.getFloat(Sheep.HEIGHT));
        
    	success(sheep);
    }
    /**
     * 修改羊的信息
     */
    public void updateSheep()
    {
    	if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }
    	String sid = getPara("sid", "");
        float price = Float.parseFloat(getPara("price",""));
        float weight = Float.parseFloat(getPara("weight",""));
        float height = Float.parseFloat(getPara("height",""));
        String birthday = getPara("birthday", "");
        String prekilltime = getPara("prekilltime", "");
        
        Long sheepid = getParaToLong("sheepid", 0L);
    	if(sheepid == 0L)
    	{
    		error("不存在的羊羔ID");
    		return;
    	}
    	
    	Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        
        sheep.set(Sheep.SID, sid);
        sheep.set(Sheep.BIRTHDAY, birthday);
        sheep.set(Sheep.PRICE, price);
        sheep.set(Sheep.WEIGHT, weight);
        sheep.set(Sheep.HEIGHT, height);
        sheep.set(Sheep.PREKILLTIME, prekilltime);
        sheep.update();
        success(sheep);
        
        
    }
    
    public void delete()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long id = getParaToLong("id", 0L);
        if (id != 0)
        {
            Sheep.dao.delete(id);
            Log.dao.add(getUid(), "sheep/delete", "delete sheep - id=" + id);
        }
        success();
    }
    
    public void addBasicRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        float weight = Float.parseFloat(getPara("weight"));
        if(weight <= 0)
        {
        	error("错误的体重数据");
            return;
        }
        float height = Float.parseFloat(getPara("height"));
        if(height <= 0)
        {
        	error("错误的身长数据");
            return;
        }
        float diffw = 0f;
        float diffh = 0f;
        BasicRecord leastRecord = BasicRecord.dao.getLeastRecord(sheepid);
        if(null == leastRecord)
        {
        	diffw = weight - sheep.getFloat(Sheep.WEIGHT);
        	diffh = height - sheep.getFloat(Sheep.HEIGHT);
        }
        else
        {
        	diffw = weight - leastRecord.getFloat(Sheep.WEIGHT);
        	diffh = height - leastRecord.getFloat(Sheep.HEIGHT);
        }
        
        BasicRecord basicRecord = BasicRecord.dao.create(sheepid, weight, height, diffw, diffh);
        success(basicRecord);
    }
    
    public void addDistanceRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        float distance = Float.parseFloat(getPara("distance"));
        if(distance <= 0)
        {
        	error("错误的距离数据");
            return;
        }
        DistanceRecord distanceRecord = DistanceRecord.dao.create(sheepid, distance);
        success(distanceRecord);
    }
    
    public void addEventRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        String event = getPara("event","");
        if(event == "")
        {
        	error("事件消息不能为空");
            return;
        }
        EventRecord eventRecord = EventRecord.dao.create(sheepid, event);
        success(eventRecord);
    }
    
    
    public void addStepRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        int steps = getParaToInt("steps",0);
        if(steps == 0)
        {
        	error("错误的计步数据");
            return;
        }
        StepRecord stepRecord = StepRecord.dao.create(sheepid, steps);
        success(stepRecord);
    }
    
    public void addGpsRecord()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long sheepid = getParaToLong("sheepid", 0l);
		if (sheepid <= 0)
        {
            error("该羊羔已经不存在了");
            return;
        }
        Sheep sheep = Sheep.dao.findById(sheepid);
        
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        float lon = Float.parseFloat(getPara("lon"));

        float lat = Float.parseFloat(getPara("lat"));

        float high = Float.parseFloat(getPara("high"));

        GpsRecord gpsRecord = GpsRecord.dao.create(sheepid, lon, lat, high);
        success(gpsRecord);
    }
}