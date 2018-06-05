package com.app.controller;

import java.util.List;

import com.app.model.VoteSeed;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import org.apache.commons.lang3.StringUtils;

import n.fw.base.BaseController;

public class VoteController extends BaseController
{
    public void list()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        List<Record> arr = Db.query("SELECT idx,COUNT(1) FROM vote GROUP BY idx");
        success(arr);
    }

    public void seed()
    {
        if (!checkLevel(1))
        {
            errorInvalidOper();
            return;
        }

        List<VoteSeed> arr = VoteSeed.dao.getAll();
        success(arr);
    }

    public void add()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        String name = getPara("name", "");
        String logo = getPara("logo", "");
        float rate = Float.valueOf(getPara("rate", "0"));
        if (StringUtils.isBlank(name) || StringUtils.isBlank(logo))
        {
            errorInvalid();
            return;
        }

        VoteSeed seed = new VoteSeed();
        seed.set(VoteSeed.NAME, name);
        seed.set(VoteSeed.LOGO, logo);
        seed.set(VoteSeed.RATE, rate);

        seed.save();
        success(seed);
    }

    public void edit()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long id = getParaToLong("id", 0L);
        String name = getPara("name", "");
        String logo = getPara("logo", "");
        float rate = Float.valueOf(getPara("rate", "0"));
        if (StringUtils.isBlank(name) || StringUtils.isBlank(logo))
        {
            errorInvalid();
            return;
        }

        VoteSeed seed = VoteSeed.dao.findBySid(id);
        if (seed == null)
        {
            error("找不到改选项");
            return;
        }

        seed.set(VoteSeed.NAME, name);
        seed.set(VoteSeed.LOGO, logo);
        seed.set(VoteSeed.RATE, rate);
        seed.update();

        success(seed);
    }

    public void del()
    {
        if (!checkLevel(2))
        {
            errorInvalidOper();
            return;
        }

        Long id = getParaToLong("id", 0L);
        Db.update("DELETE FROM voteseed WHERE id=?", id);
        success("成功");
    }
}