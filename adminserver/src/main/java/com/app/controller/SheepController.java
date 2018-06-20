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
        Sheep sheep = Sheep.dao.create(sid);
        if (sheep != null)
        {
            Log.dao.add(getUid(), "sheep/create", "create shepp - id=" + sheep.getLong(Sheep.ID) + " sid=" + sid);
        }
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
        BasicRecord basicRecord = new BasicRecord();
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
        float diffw = Float.parseFloat(getPara("diffw"));
        float diffh = Float.parseFloat(getPara("diffh"));
        basicRecord.create(sheepid, weight, height, diffw, diffh);
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
        DistanceRecord distanceRecord = new DistanceRecord();
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
        distanceRecord.create(sheepid, distance);
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
        EventRecord eventRecord = new EventRecord();
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
        eventRecord.create(sheepid, event);
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
        StepRecord stepRecord = new StepRecord();
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
        stepRecord.create(sheepid, steps);
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
        GpsRecord gpsRecord = new GpsRecord();
        if (sheep == null)
        {
        	error("该羊羔已经不存在了");
            return;
        }
        float lon = Float.parseFloat(getPara("lon"));

        float lat = Float.parseFloat(getPara("lat"));

        float high = Float.parseFloat(getPara("high"));

        gpsRecord.create(sheepid, lon, lat, high);
        success(gpsRecord);
    }
}