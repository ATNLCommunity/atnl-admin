package com.app.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

import org.apache.commons.lang3.StringUtils;

import n.fw.utils.DateUtils;

// ��ı�
public class Sheep extends Model<Sheep>
{
    private static final long serialVersionUID = 3554755307155224116L;

    public static final Sheep dao = new Sheep();

    public static final String ID = "id";
    public static final String STATE = "state";
    public static final String SID = "sid";
    public static final String UPDATE_TIME = "update_time";
    public static final String PRICE = "price";
    public static final String BIRTHDAY = "birthday";
    public static final String WEIGHT = "weight";
    public static final String HEIGHT = "height";
    public static final String PREKILLTIME = "prekilltime";
    public static final String DID = "did";
    

    public Sheep create(String sid,float price,String birthday,float weight,float height,String prekilltime)
    {
        Sheep sheep = new Sheep();
        sheep.set(STATE, 0);
        sheep.set(SID, sid);
        sheep.set(UPDATE_TIME, DateUtils.getDateTime());
        sheep.set(PRICE, price);
        sheep.set(BIRTHDAY, birthday);
        sheep.set(WEIGHT, weight);
        sheep.set(HEIGHT, height);
        sheep.set(PREKILLTIME, prekilltime);
        if (sheep.save())
        {
            return sheep;
        }
        return null;
    }

    public void delete(Long id)
    {
        Db.update("DELETE FROM sheep WHERE id=?", id);
    }

    public List<Sheep> list(String sid, int page)
    {
        if (StringUtils.isBlank(sid))
        {
            return find("SELECT * FROM sheep ORDER BY id DESC LIMIT ?,20", page * 20);
        }
        else
        {
            return find("SELECT * FROM sheep WHERE sid LIKE '%" + sid + "%' ORDER BY id DESC LIMIT ?,20", page * 20);
        }
    }

    public Long count(String sid)
    {
        if (StringUtils.isBlank(sid))
        {
            return Db.queryLong("SELECT COUNT(1) FROM sheep");
        }
        else
        {
            return Db.queryLong("SELECT COUNT(1) FROM sheep WHERE sid LIKE '%" + sid + "%'");
        }
    }

    public List<Sheep> getSheep(Integer count)
    {
        return find("SELECT * FROM sheep WHERE state=0 LIMIT ?", count);
    }
    
    public Sheep getBySheepId(Long sheepid)
    {
    	return findFirst("select * from sheep where id = ?",sheepid);
    }
}